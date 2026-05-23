import { Component, Inject, signal, WritableSignal } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Company, CompanyData } from '../../services/company';
import { Cep } from '../../services/cep';
import { MaskUtils } from '../../utils/mask.utils';
import { MODAL_DATA, MODAL_REF } from '../modal/modal';
import { ModalRef } from '../modal/modal-ref';
import { SupplierData } from '../../services/supplier';
import { disabled } from '@angular/forms/signals';

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

  constructor(
    @Inject(MODAL_DATA) public readonly data: CompanyFormModalData,
    @Inject(MODAL_REF) private readonly modalRef: ModalRef<CompanyFormModalResult>,
    private formBuilder: FormBuilder,
    private companyService: Company,
    private cepService: Cep,
  ) {
    this.createForm();
  }

  updateStateZipCode(signal: WritableSignal<boolean>) {
    if (signal()) {
      signal.set(false);
    }
  }

  saveCompany() {
    if (this.data.mode === 'edit' && this.data.company?.id && this.validCEP() && this.form.valid) {
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
