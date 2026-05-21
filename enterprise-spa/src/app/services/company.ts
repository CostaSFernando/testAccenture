import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

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

  createCompany(company: CreateCompanyData) {
    return this.http.post<CompanyData>('http://localhost:8080/companies', company);
  }

  deleteCompany(id: string) {
    return this.http.delete(`http://localhost:8080/companies/${id}`);
  }

}
