import { Company } from "./company";

export interface Invoice {
  id: number;
  number: number;
  amount: number;
  serviceDescription: string;
  competenceMonth: number;
  competenceYear: number;
  paymentDate: string;
  company: Company;
}