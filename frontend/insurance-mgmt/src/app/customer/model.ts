export interface CustomerQueryRequest {
    subject: string;
    message: string;
}

export interface CustomerQueryResponse {
    id: number;
    subject: string;
    message: string;
    status: string;
    adminResponse: string;
    createdAt: Date;
    updatedAt: Date;
    customerName: string;
}

export interface CustomerKaPolicyResponseDTO {
    policyId: number;
    planName: string;
    paymentFrequency: 'MONTHLY' | 'QUARTERLY' | 'HALF_YEARLY' | 'YEARLY';
    calculatedPremium: number;
    selectedCoverageAmount: number;
    selectedDurationYears: number;
    active: boolean;
    startDate: string;
    endDate: string;
    nextDueDate: string;
    approvedBy: string;
}

export interface VerifyPaymentRequestDTO {
    customerPolicyId: number;
    razorpayOrderId: string;
    razorpayPaymentId: string;
    razorpaySignature: string;
}

export interface RazorpayOptions {
    key: string;
    amount: number;
    currency: string;
    name: string;
    description: string;
    order_id: string;
    handler: (response: any) => void;
    prefill: {
        name: string;
        email: string;
        contact: string;
    };
    theme: {
        color: string;
    };
}

export interface TransactionResponseDTO {
    transactionId: number;
    amount: number;
    description: string;
    transactionType: 'PREMIUM_PAYMENT';
    transactionTime: string;
    userFullName: string;
    userRole: string;
    paymentReference: string;
}

export interface CustomerDocumentResponseDTO {
    documentId: number;
    customerId: number;
    customerName: string;
    documentType: 'AADHAR' | 'PANCARD' | 'VOTERID' | 'LICENSE' | 'PASSPORT' | 'BILL' | 'BIRTHCERTIFICATE';
    documentUrl: string;
    documentStatus: 'PENDING' | 'APPROVED' | 'REJECTED';
    uploadedAt: string; // ISO date string
}