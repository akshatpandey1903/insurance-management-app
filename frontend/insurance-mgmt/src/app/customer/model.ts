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