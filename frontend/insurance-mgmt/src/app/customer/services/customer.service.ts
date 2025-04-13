import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { CustomerQueryRequest, CustomerQueryResponse } from '../model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CustomerService {
  private apiUrl = 'http://localhost:8080/app/customer/queries'; 

  constructor(private http: HttpClient) { }

  raiseQuery(queryData: any, customerId: number): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/${customerId}`, queryData);
  }

  getUserQueries(userId: number): Observable<CustomerQueryResponse[]> {
    return this.http.get<CustomerQueryResponse[]>(`${this.apiUrl}/${userId}`);
  }
  
}
