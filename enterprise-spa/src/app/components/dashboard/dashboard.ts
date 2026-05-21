import { Component, signal, WritableSignal } from '@angular/core';
import { Company, CompanyData } from '../../services/company';
import { Supplier, SupplierData } from '../../services/supplier';
import { NgClass } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { zip } from 'rxjs';
import { Cep } from '../../services/cep';


@Component({
  selector: 'app-dashboard',
  imports: [NgClass, ReactiveFormsModule],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss',
})
export class Dashboard {
  companies: WritableSignal<CompanyData[]> = signal([]);
  suppliers: WritableSignal<SupplierData[]> = signal([]);

  validCEP = signal(false);
  validCEPSupplier = signal(false);

  form: FormGroup = new FormGroup({});
  formSupplier: FormGroup = new FormGroup({});

  constructor(
    private companyService: Company,
    private supplierService: Supplier,
    private cepService: Cep,
    private formBuilder: FormBuilder
  ) {}

  ngOnInit() {
    this.getCompanies();
    this.getSuppliers();
    this.createForm();
  }

  get getSuppliersWithAge() {
    const currentDate = new Date();
    return this.suppliers().filter(supplier => {
      if (!supplier.birthDate) return false;
      const birthDate = new Date(supplier.birthDate);
      const age = currentDate.getFullYear() - birthDate.getFullYear();
      return age < 18;
    }).length;
  }

  createForm() {
    this.form = this.formBuilder.group({
      cnpj: [''],
      fantasyName: [''],
      zipCode: [''],
      city: [{ value: '', disabled: true }],
      state: [{ value: '', disabled: true }],
    });

    this.formSupplier = this.formBuilder.group({
      documentType: ['CPF'],
      document: [''],
      name: [''],
      email: [''],
      zipCode: [''],
      citySupplier: [{ value: '', disabled: true }],
      stateSupplier: [{ value: '', disabled: true }],
      rg: [''],
      birthDate: ['']
    });
  }

  saveSupplier() {
    if (!this.validCEPSupplier()) {
      this.cepService.findByZipCode(this.formSupplier.get('zipCode')?.value).subscribe({
        next: (data) => {
          this.validCEPSupplier.set(true);
          this.formSupplier.patchValue({
            citySupplier: data.city,
            stateSupplier: data.state
          });
          console.log('CEP válido:', data);
        },
        error: (error) => {
          this.validCEPSupplier.set(false);
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
        rg: this.formSupplier.get('rg')?.value,
        birthDate: this.formSupplier.get('birthDate')?.value
      }
    ).subscribe(res => {
      this.getSuppliers();
      this.formSupplier.reset();
      this.formSupplier.get('documentType')?.setValue('CPF');
      this.validCEPSupplier.set(false);
    });
  }

  saveCompany() {
    if (!this.validCEP()) {
      this.cepService.findByZipCode(this.form.get('zipCode')?.value).subscribe({
        next: (data) => {
          this.validCEP.set(true);
          this.form.patchValue({
            city: data.city,
            state: data.state
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

    this.companyService.createCompany(
      {
        cnpj: this.form.get('cnpj')?.value,
        fantasyName: this.form.get('fantasyName')?.value,
        zipCode: this.form.get('zipCode')?.value
      }
    ).subscribe(res => {
      this.getCompanies();
      this.form.reset();
      this.validCEP.set(false);
    })

  }

  updateStateZipCode(signal: WritableSignal<boolean>) {
    if (signal()) {
      signal.set(false);
    }
  }

  getCompanies() {
    return this.companyService.getCompanies().subscribe((data) => {
      this.companies.set(data);
    });
  }

  getSuppliers() {
    return this.supplierService.getSuppliers().subscribe((data) => {
      this.suppliers.set(data);
    });
  }

  deleteCompany(id: string) {
    return this.companyService.deleteCompany(id).subscribe(() => {
      this.getCompanies();
    });
  }

  deleteSupplier(id: string) {
    return this.supplierService.deleteSupplier(id).subscribe(() => {
      this.getSuppliers();
    });
  }
}
