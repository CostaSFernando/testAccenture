import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map, Observable, throwError } from 'rxjs';

export interface ZipCodeInfo {
  zipCode: string;
  street: string | null;
  neighborhood: string | null;
  city: string;
  state: string;
}

@Injectable({
  providedIn: 'root',
})
export class Cep {

  private readonly baseUrl = 'http://localhost:8080/zip-codes/';

  constructor(private readonly http: HttpClient) {}

  findByZipCode(zipCode: string): Observable<ZipCodeInfo> {
    const normalizedZipCode = this.normalizeOnlyNumbers(zipCode);

    if (!normalizedZipCode || normalizedZipCode.length !== 8) {
      return throwError(() => new Error('CEP deve conter 8 dígitos'));
    }

    return this.http.get<ZipCodeInfo>(`${this.baseUrl}${normalizedZipCode}`);
  }

  private normalizeOnlyNumbers(value: string | null | undefined): string {
    return value ? value.replace(/\D/g, '') : '';
  }

}
