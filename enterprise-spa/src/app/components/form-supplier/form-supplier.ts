import { Component, Inject, signal, WritableSignal } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { Cep } from '../../services/cep';
import { Supplier, SupplierData } from '../../services/supplier';
import { MaskUtils } from '../../utils/mask.utils';
import { ModalRef } from '../modal/modal-ref';
import { MODAL_DATA, MODAL_REF } from '../modal/modal';

interface SupplierFormModalData {
  mode: 'create' | 'edit';
  supplier?: SupplierData;
}

interface SupplierFormModalResult {
  success?: boolean;
  canceled?: boolean;
}

@Component({
  selector: 'app-form-supplier',
  imports: [ReactiveFormsModule],
  templateUrl: './form-supplier.html',
  styleUrl: './form-supplier.scss',
})
export class FormSupplier {

  formSupplier: FormGroup = new FormGroup({});
  validCEP = signal(false);

  constructor(
    @Inject(MODAL_DATA) public readonly data: SupplierFormModalData,
    @Inject(MODAL_REF) private readonly modalRef: ModalRef<SupplierFormModalResult>,
    private formBuilder: FormBuilder,
    private cepService: Cep,
    private supplierService: Supplier,
  ) {
    this.createForm();
  }

  createForm() {
    this.formSupplier = this.formBuilder.group({
      documentType: ['CPF'],
      document: [''],
      name: [''],
      email: [''],
      zipCode: [''],
      city: [{ value: '', disabled: true }],
      state: [{ value: '', disabled: true }],
      rg: [''],
      birthDate: ['']
    });

    if (this.data.mode === 'edit' && this.data.supplier?.id) {
      const typeDoc = this.data.supplier.documentType
      this.formSupplier.patchValue({
        documentType: typeDoc,
        document:  typeDoc === 'CPF' ? MaskUtils.cpf(this.data.supplier.document) : MaskUtils.cnpj(this.data.supplier.document),
        name: this.data.supplier.name,
        email: this.data.supplier.email,
        zipCode: MaskUtils.cep(this.data.supplier.zipCode),
        city: this.data.supplier.city,
        state: this.data.supplier.state,
        rg: MaskUtils.rg(this.data.supplier.rg),
        birthDate: this.data.supplier.birthDate
      });
      this.validCEP.set(true);
    }
  }

  saveSupplier() {
    if (this.data.mode === 'edit' && this.data.supplier?.id && this.formSupplier.valid && this.validCEP()) {
      this.supplierService.updateSupplier(this.data.supplier.id, this.formSupplier.value).subscribe({
        next: (updatedSupplier) => {
          this.modalRef.close({ success: true });
        },
        error: (error) => {
          console.error('Erro ao atualizar fornecedor:', error);
        }
      });
      return;
    }
    if (!this.validCEP()) {
      this.cepService.findByZipCode(this.formSupplier.get('zipCode')?.value).subscribe({
        next: (data) => {
          this.validCEP.set(true);
          this.formSupplier.patchValue({
            citySupplier: data.city,
            stateSupplier: data.state
          });
          console.log('CEP válido:', data);
        },
        error: (error) => {
          this.validCEP.set(false);
          console.error('Erro ao validar CEP:', error);
        }
      });

      return;
    }

    this.supplierService.createSupplier(
      {
        documentType: this.formSupplier.get('documentType')?.value,
        document: this.formSupplier.get('document')?.value,
        name: this.formSupplier.get('name')?.value,
        email: this.formSupplier.get('email')?.value,
        zipCode: this.formSupplier.get('zipCode')?.value,
        city: this.formSupplier.get('citySupplier')?.value,
        state: this.formSupplier.get('stateSupplier')?.value,
        rg: this.formSupplier.get('rg')?.value,
        birthDate: this.formSupplier.get('birthDate')?.value
      }
    ).subscribe(res => {
      this.modalRef.close({ success: true });
    });
  }

  updateStateZipCode(signal: WritableSignal<boolean>) {
    if (signal()) {
      signal.set(false);
    }
  }

  applyCpfOrCnpjMask(form: AbstractControl): void {
    if (this.formSupplier.get('documentType')?.value === 'CPF') {
      this.applyCpfMask(form);
    } else {
      this.applyCnpjMask(form);
    }
  }

  applyCpfMask(form: AbstractControl): void {
    form?.setValue(MaskUtils.cpf(form?.value), {
      emitEvent: false
    });
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

  applyRgMask(form: AbstractControl): void {
    form?.setValue(MaskUtils.rg(form?.value), {
      emitEvent: false
    });
  }

}
