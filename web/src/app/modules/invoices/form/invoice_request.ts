export interface InvoiceRequest {
    number: number;
    amount: number;
    serviceDescription: string;
    competenceMonth: number;
    competenceYear: number;
    paymentDate: string;
    companyId: number;
  }