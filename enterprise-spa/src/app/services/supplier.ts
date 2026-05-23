import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

export interface SupplierData {
  id: string;
  document: string;
  documentType: string;
  name: string;
  email: string;
  zipCode: string;
  rg: string;
  birthDate: Date;
  city: string;
  state: string;
}

export interface CreateSupplierData {
  document: string;
  documentType: string;
  name: string;
  email: string;
  zipCode: string;
  rg: string;
  city: string;
  state: string;
  birthDate: Date;
}

@Injectable({
  providedIn: 'root',
})
export class Supplier {
  constructor(
    private http: HttpClient
  ) {}

  getSuppliers() {
    return this.http.get<SupplierData[]>('http://localhost:8080/suppliers');
  }

  createSupplier(supplier: CreateSupplierData) {
      return this.http.post<SupplierData>('http://localhost:8080/suppliers', supplier);
  }

  updateSupplier(id: string, supplier: CreateSupplierData) {
    return this.http.put<SupplierData>(`http://localhost:8080/suppliers/${id}`, supplier);
  }

  deleteSupplier(id: string) {
    return this.http.delete(`http://localhost:8080/suppliers/${id}`);
  }
}
