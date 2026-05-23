import { Component, Inject, Signal, signal, WritableSignal } from '@angular/core';
import { AbstractControl, FormArray, FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Company, CompanyData } from '../../services/company';
import { Cep } from '../../services/cep';
import { MaskUtils } from '../../utils/mask.utils';
import { MODAL_DATA, MODAL_REF } from '../modal/modal';
import { ModalRef } from '../modal/modal-ref';
import { Supplier, SupplierData } from '../../services/supplier';
import { disabled } from '@angular/forms/signals';
import { map, of, switchMap } from 'rxjs';

interface CompanyFormModalData {
  mode: 'create' | 'edit';
  company?: CompanyData;
  supplier?: SupplierData;
}

interface CompanyFormModalResult {
  success?: boolean;
  canceled?: boolean;
}

@Component({
  selector: 'app-form-company',
  imports: [ReactiveFormsModule],
  templateUrl: './form-company.html',
  styleUrl: './form-company.scss',
})
export class FormCompany {

  form: FormGroup = new FormGroup({});
  validCEP = signal(false);
  suppliers: WritableSignal<SupplierData[]> = signal([]);
  selectedSuppliersIds: WritableSignal<string[]> = signal([]);

  constructor(
    @Inject(MODAL_DATA) public readonly data: CompanyFormModalData,
    @Inject(MODAL_REF) private readonly modalRef: ModalRef<CompanyFormModalResult>,
    private formBuilder: FormBuilder,
    private companyService: Company,
    private supplierService: Supplier,
    private cepService: Cep,
  ) {
    this.createForm();
    this.supplierService.getSuppliers().subscribe({
      next: (suppliers) => {
        this.suppliers.set(suppliers);
      },
      error: (error) => {
        console.error('Erro ao carregar fornecedores:', error);
      }
    });

    this.companyService.getAssociatedSuppliers(this.data.company?.id || '').subscribe(
      res => {
        this.selectedSuppliersIds.set(res.map(supplier => supplier.id));
        this.form.patchValue({
          suppliers: this.selectedSuppliersIds(),
        });
      }
    );
  }

  toggleSupplier(event: any, supplierId: string) {
    const isChecked = event.target.checked;

    if (isChecked) {
      this.selectedSuppliersIds.set([...this.selectedSuppliersIds(), supplierId]);
    } else {
      this.selectedSuppliersIds.set(this.selectedSuppliersIds().filter(id => id !== supplierId));
    }
    this.form.patchValue({
      suppliers: this.selectedSuppliersIds
    });
  }

  updateStateZipCode(signal: WritableSignal<boolean>) {
    if (signal()) {
      signal.set(false);
    }
  }

  saveCompany() {
    if (this.data.mode === 'edit' && this.data.company?.id && this.validCEP() && this.form.valid) {
      this.companyService.associateSuppliers(this.data.company.id, this.selectedSuppliersIds()).subscribe({
        next: (res) => {
          console.log('Fornecedores associados com sucesso');
        },
        error: (error) => {
          console.error('Erro ao associar fornecedores:', error);
        }
      });

      this.companyService.updateCompany(this.data.company.id, this.form.value).subscribe({
        next: (updatedCompany) => {
          this.modalRef.close({ success: true });
        },
        error: (error) => {
          console.error('Erro ao atualizar empresa:', error);
        }
      });
      return;
    }

    if (!this.validCEP()) {
      this.cepService.findByZipCode(this.form.get('zipCode')?.value).subscribe({
        next: (data) => {
          this.validCEP.set(true);
          this.form.patchValue({
            city: data.city,
            state: data.state
          });
        },
        error: (error) => {
          this.validCEP.set(false);
        }
      });

      return;
    }

    this.companyService.createCompany(
      {
        cnpj: this.form.get('cnpj')?.value,
        fantasyName: this.form.get('fantasyName')?.value,
        zipCode: this.form.get('zipCode')?.value
      }
    ).pipe(
      switchMap((createdCompany) => {
        if (this.selectedSuppliersIds().length > 0) {
          return this.companyService.associateSuppliers(createdCompany.id, this.selectedSuppliersIds()).pipe(
            map(() => createdCompany)
          );
        }
        return of(createdCompany);
      })
    ).subscribe(res => {
      this.modalRef.close({ success: true });
    })
  }

  cancel() {
    this.modalRef.close({ canceled: true });
  }

  createForm() {
    this.form = this.formBuilder.group({
      cnpj: ['', [Validators.required]],
      fantasyName: ['', [Validators.required]],
      zipCode: ['', [Validators.required]],
      city: [{ value: '', disabled: true }, [Validators.required]],
      state: [{ value: '', disabled: true }, [Validators.required]],
    });

    if (this.data.mode === 'edit' && this.data.company?.id) {
      this.form.patchValue({
        cnpj: MaskUtils.cnpj(String(this.data.company.cnpj)),
        fantasyName: this.data.company.fantasyName,
        zipCode: MaskUtils.cep(this.data.company.zipCode),
        city: this.data.company.city,
        state: this.data.company.state,
      });
      this.validCEP.set(true);
    }
  }

  applyCnpjMask(form: AbstractControl): void {
    form?.setValue(MaskUtils.cnpj(form?.value), {
      emitEvent: false
    });
  }

  applyCepMask(form: AbstractControl): void {
    form?.setValue(MaskUtils.cep(form?.value), {
      emitEvent: false
    });
  }

}
