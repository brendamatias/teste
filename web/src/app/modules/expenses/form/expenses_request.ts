export interface ExpenseRequest {
    categoryId: number;
    amount: number;
    expenseName: string;
    competenceMonth: number;
    competenceYear: number;
    paymentDate: string;
    companyId?: number;
  }