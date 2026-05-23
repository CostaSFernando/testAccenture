import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { SupplierData } from './supplier';

export interface CompanyData {
  id: string
  cnpj: number
  fantasyName: string
  zipCode: string
  city: string
  state: string
}

interface CreateCompanyData {
  cnpj: number
  fantasyName: string
  zipCode: string
}

@Injectable({
  providedIn: 'root',
})
export class Company {
  constructor(
    private http: HttpClient
  ) {}

  getCompanies() {
    return this.http.get<CompanyData[]>('http://localhost:8080/companies');
  }

  getAssociatedSuppliers(companyId: string) {
    return this.http.get<SupplierData[]>(`http://localhost:8080/companies/${companyId}/suppliers`);
  }

  associateSuppliers(companyId: string, supplierIds: string[]) {
    return this.http.post(`http://localhost:8080/companies/${companyId}/suppliers`, { supplierIds });
  }

  createCompany(company: CreateCompanyData) {
    return this.http.post<CompanyData>('http://localhost:8080/companies', company);
  }

  updateCompany(id: string, company: CreateCompanyData) {
    return this.http.put<CompanyData>(`http://localhost:8080/companies/${id}`, company);
  }

  deleteCompany(id: string) {
    return this.http.delete(`http://localhost:8080/companies/${id}`);
  }

}
