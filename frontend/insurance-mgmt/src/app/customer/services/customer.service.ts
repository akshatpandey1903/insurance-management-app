import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { CustomerDocumentResponseDTO, CustomerKaPolicyResponseDTO, CustomerQueryRequest, CustomerQueryResponse, TransactionResponseDTO, VerifyPaymentRequestDTO } from '../model';
import { Observable } from 'rxjs';
import { CustomerPolicyResponseDTO } from '../../models/customer.model';
import { AuthService } from '../../services/auth.service';

@Injectable({
  providedIn: 'root'
})
export class CustomerService {
  private apiUrl = 'http://localhost:8080/app/customer/queries';
  private baseUrl = 'http://localhost:8080/app/customer/claims'

  constructor(private http: HttpClient, private authService: AuthService) { }

  raiseQuery(queryData: any, customerId: number): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/${customerId}`, queryData);
  }

  getUserQueries(userId: number): Observable<CustomerQueryResponse[]> {
    return this.http.get<CustomerQueryResponse[]>(`${this.apiUrl}/${userId}`);
  }

  registerPolicy(customerId: number, policyData: any): Observable<any> {
    return this.http.post(`http://localhost:8080/app/policy/register/${customerId}`, policyData);
  }

  getPurchasedPolicies(customerId: number): Observable<CustomerKaPolicyResponseDTO[]> {
    const headers = new HttpHeaders().set('Authorization', `Bearer ${this.authService.getToken()}`);
    return this.http.get<CustomerKaPolicyResponseDTO[]>(`http://localhost:8080/app/policy/customer/${customerId}/policies`, { headers });
  }

  submitClaim(data: any, customerId: number): Observable<any> {
    const headers = new HttpHeaders().set('Authorization', `Bearer ${this.authService.getToken()}`);
    return this.http.post(`${this.baseUrl}/${customerId}`, data, { headers });
  }

  getMyClaims(customerId: number): Observable<any[]> {
    const headers = new HttpHeaders().set('Authorization', `Bearer ${this.authService.getToken()}`);
    return this.http.get<any[]>(`${this.baseUrl}/${customerId}`, { headers });
  }

  cancelPolicy(id: number, policyId: number) {
    return this.http.put(`http://localhost:8080/app/policy/cancel/${id}/${policyId}`, {});
  }

  getAllPolicies(id: number): Observable<CustomerKaPolicyResponseDTO[]> {
    const headers = new HttpHeaders().set('Authorization', `Bearer ${this.authService.getToken()}`);
    return this.http.get<CustomerKaPolicyResponseDTO[]>(`http://localhost:8080/app/policy/customer/${id}/policies`, { headers });
  }

  createPremiumOrder(policyId: number, customerId: number) {
    return this.http.post<{ orderId: string }>(
      `http://localhost:8080/app/payments/premium/order/${policyId}?customerId=${customerId}`,
      {}
    );
  }

  verifyPremiumPayment(payload: VerifyPaymentRequestDTO, customerId: number) {
    return this.http.post(
      `http://localhost:8080/app/payments/premium/verify?customerId=${customerId}`,
      payload
    );
  }

  getPastPremiumTransactions(customerId: number): Observable<TransactionResponseDTO[]> {
    const headers = new HttpHeaders().set('Authorization', `Bearer ${this.authService.getToken()}`);
    return this.http.get<TransactionResponseDTO[]>(`http://localhost:8080/app/transactions/premium/transactions/user/${customerId}` , {headers});
  }

  getCustomerDocuments(customerId: number) {
    return this.http.get<CustomerDocumentResponseDTO[]>(
      `http://localhost:8080/app/documents/customer/${customerId}`
    );
  }

}
