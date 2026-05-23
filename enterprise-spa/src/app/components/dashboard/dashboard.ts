import { Component, signal, WritableSignal } from '@angular/core';
import { Company, CompanyData } from '../../services/company';
import { Supplier, SupplierData } from '../../services/supplier';
import { NgClass } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';

import { ModalService } from '../../services/modal.service';
import { FormCompany } from '../form-company/form-company';
import { FormSupplier } from '../form-supplier/form-supplier';

@Component({
  selector: 'app-dashboard',
  imports: [NgClass, ReactiveFormsModule],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss',
})
export class Dashboard {
  allData: {
    companies: CompanyData[],
    suppliers: SupplierData[]
  } = {
    companies: [],
    suppliers: []
  };
  companies: WritableSignal<CompanyData[]> = signal([]);
  suppliers: WritableSignal<SupplierData[]> = signal([]);

  constructor(
    private companyService: Company,
    private supplierService: Supplier,
    private modalService: ModalService
  ) {}

  ngOnInit() {
    this.getCompanies();
    this.getSuppliers();
  }

  filterCompanies(data: string) {
    this.companies.set(this.allData.companies.filter(company =>
      company.fantasyName.toLowerCase().includes(data.toLowerCase()) ||
      company.cnpj.toString().includes(data)
    ));
  }

  filterSuppliers(data: string) {
    this.suppliers.set(this.allData.suppliers.filter(supplier =>
      supplier.name.toLowerCase().includes(data.toLowerCase()) ||
      supplier.document.includes(data)
    ));
  }

  openCompanyModal() {
    const modalRef = this.modalService.open<
    { mode: 'create'},
    { success: boolean}
    >(FormCompany, { mode: 'create' });
    modalRef.afterClosed$.subscribe(result => {
      if (result?.success) {
        this.getCompanies();
      }
    });
  }

  openSupplierModal() {
    const modalRef = this.modalService.open<{mode: 'create'}, { success: boolean }>(FormSupplier, { mode: 'create' });
    modalRef.afterClosed$.subscribe(result => {
      if (result?.success) {
        this.getSuppliers();
      }
    });
  }

  editCompany(item: CompanyData) {
    const modalRef = this.modalService.open<
      { mode: 'edit'; company: CompanyData },
      { updated: boolean }
    >(FormCompany, {
      mode: 'edit',
      company: item,
    });

    modalRef.afterClosed$.subscribe((result) => {
      if (result?.updated) {
        this.getCompanies();
      }
    });
  }

  editSupplier(item: SupplierData) {
    const modalRef = this.modalService.open<
      { mode: 'edit'; supplier: SupplierData },
      { updated: boolean }
    >(FormSupplier, {
      mode: 'edit',
      supplier: item,
    });

    modalRef.afterClosed$.subscribe((result) => {
      if (result?.updated) {
        this.getSuppliers();
      }
    });
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

  getCompanies() {
    return this.companyService.getCompanies().subscribe((data) => {
      this.companies.set(data);
      this.allData.companies = data;
    });
  }

  getSuppliers() {
    return this.supplierService.getSuppliers().subscribe((data) => {
      this.suppliers.set(data);
      this.allData.suppliers = data;
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
