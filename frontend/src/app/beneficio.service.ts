import { Inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Beneficio } from './types';

@Injectable({ providedIn: 'root' })
export class BeneficioService {
  constructor(private http: HttpClient, @Inject('API_BASE') private base: string) {}

  list(): Observable<Beneficio[]> {
    return this.http.get<Beneficio[]>(this.base);
    }

  get(id: number): Observable<Beneficio> {
    return this.http.get<Beneficio>(`${this.base}/${id}`);
  }

  create(b: Beneficio): Observable<Beneficio> {
    return this.http.post<Beneficio>(this.base, b);
  }

  update(id: number, b: Beneficio): Observable<Beneficio> {
    return this.http.put<Beneficio>(`${this.base}/${id}`, b);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}`);
  }

  transfer(fromId: number, toId: number, amount: number): Observable<void> {
    return this.http.post<void>(`${this.base}/transfer`, { fromId, toId, amount });
  }
}