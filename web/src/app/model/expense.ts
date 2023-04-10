import { Category } from "./category";
import { Company } from "./company";

export interface Expense {
  id: number;
  expenseName: string;
  amount: number;
  paymentDate: Date;
  competenceMonth: number;
  competenceYear: number;
  category: Category;
  company?: Company;
}