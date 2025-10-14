import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../environments/environment';

export interface CurrencyRequestCommand { currency: string; name: string; }
export interface CurrencyValueResponse { value: number; }
export interface CurrencyRequestView { currency: string; name: string; date: string; value: number; }
export interface CurrencyCodeView { code: string; name: string; }

@Injectable({ providedIn: 'root' })
export class CurrencyService {
  private base = environment.apiBase;
  constructor(private http: HttpClient) {}

  getCurrentValue(cmd: CurrencyRequestCommand): Observable<CurrencyValueResponse> {
    return this.http.post<CurrencyValueResponse>(
      `${this.base}/currencies/get-current-currency-value-command`, cmd);
  }

  listRequests(code?: string): Observable<CurrencyRequestView[]> {
    let params = new HttpParams();
    if (code && code.trim()) params = params.set('code', code.trim());
    return this.http.get<CurrencyRequestView[]>(`${this.base}/currencies/requests`, { params });
  }

  getCodes() {
  return this.http.get<CurrencyCodeView[]>(`${this.base}/nbp/codes`);
  }
}
