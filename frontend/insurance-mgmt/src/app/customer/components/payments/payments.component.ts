import { Component } from '@angular/core';
import { CustomerService } from '../../services/customer.service';
import { AuthService } from '../../../services/auth.service';
import { RazorpayOptions, TransactionResponseDTO } from '../../model';
declare var Razorpay: any;
@Component({
  selector: 'app-payments',
  standalone: false,
  templateUrl: './payments.component.html',
  styleUrl: './payments.component.css'
})
export class PaymentsComponent {
  policies: any[] = [];
  customerId!: number;
  transactions: TransactionResponseDTO[] = [];
  
  constructor(
    private customerService: CustomerService,
    private authService: AuthService
  ) {}
  
  ngOnInit(): void {
    this.customerId = this.authService.getUserId();
    this.loadPastPremiumTransactions();
    this.loadPolicies();
  }
  
  loadPolicies(): void {
    this.customerService.getPurchasedPolicies(this.customerId).subscribe({
      next: (res) => (this.policies = res),
      error: (err) => console.error('Failed to load policies', err)
    });
  }
  
  payPremium(policyId: number): void {
    this.customerService.createPremiumOrder(policyId, this.customerId).subscribe({
      next: (res) => {
        const orderId = res.orderId;
        this.launchRazorpay(orderId, policyId);
      },
      error: (err) => {
        alert(err?.error?.message || 'Could not create order');
      }
    });
  }
  
  launchRazorpay(orderId: string, policyId: number): void {
    const options: RazorpayOptions = {
      key: 'rzp_test_b3xq8mfWS0hkb4', 
      amount: 0, 
      currency: 'INR',
      name: 'Insurance Premium Payment',
      description: 'Pay your premium',
      order_id: orderId,
      handler: (response: any) => {
        this.customerService.verifyPremiumPayment(
          {
            customerPolicyId: policyId,
            razorpayOrderId: response.razorpayOrderId,
            razorpayPaymentId: response.razorpayPaymentId,
            razorpaySignature: response.razorpaySignature
          },
          this.customerId
        ).subscribe({
          next: () => alert('Payment successful and recorded!'),
          error: () => alert('Verification failed!')
        });
      },
      prefill: {
        name: 'Customer',
        email: 'customer@example.com',
        contact: '9999999999'
      },
      theme: {
        color: '#3399cc'
      }
    };
  
    const rzp = new Razorpay(options);
    rzp.open();
  }

  loadPastPremiumTransactions() {
    this.customerService.getPastPremiumTransactions(this.authService.getUserId()).subscribe({
      next: (transactions) => {
        this.transactions = transactions;
        console.log(this.transactions)
      },
      error: (err) => {
        console.error('Error fetching transactions:', err);
      }
    });
  }
  
}
