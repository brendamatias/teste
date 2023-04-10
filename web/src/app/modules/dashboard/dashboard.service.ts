import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { AppConfig } from 'src/app/core/config/config';
import { Utils } from 'src/app/core/shared/utils';
import { DashboardAvailableInvoiceAmount } from 'src/app/model/dashboard_available_invoice_amount';
import { DashboardTotalInvoiceAmount } from 'src/app/model/dashboard_total_invoice_amount';
import { DashboardMonthlyBalance } from 'src/app/model/dashboard_monthly_balance';
import { DashboardExpensesCategory } from 'src/app/model/dashboard_expenses_category';

@Injectable({
  providedIn: 'root',
})
export class DashboardService {
  private apiUrl = AppConfig.hostUrl + AppConfig.path;

  constructor(private http: HttpClient, private utils: Utils) {
    if (window.location.protocol == 'http:') {
      this.apiUrl = AppConfig.hostUrl + ':8080' + AppConfig.path;
    }
  }

  availableInvoiceAmount(year: number): Observable<HttpResponse<DashboardAvailableInvoiceAmount>> {
    let endPoint = this.apiUrl + 'dashboard/available-invoice-amount/' + year;

    return this.http.get<DashboardAvailableInvoiceAmount>(endPoint, { observe: 'response' });
  }

  totalInvoiceAmounts(year: number): Observable<HttpResponse<DashboardTotalInvoiceAmount[]>> {
    let endPoint = this.apiUrl + 'dashboard/total-invoice-amounts/' + year;

    return this.http.get<DashboardTotalInvoiceAmount[]>(endPoint, { observe: 'response' });
  }

  totalExpenseAmounts(year: number): Observable<HttpResponse<DashboardTotalInvoiceAmount[]>> {
    let endPoint = this.apiUrl + 'dashboard/total-expense-amounts/' + year;

    return this.http.get<DashboardTotalInvoiceAmount[]>(endPoint, { observe: 'response' });
  }

  monthlyBalance(year: number): Observable<HttpResponse<DashboardMonthlyBalance[]>> {
    let endPoint = this.apiUrl + 'dashboard/monthly-balance/' + year;

    return this.http.get<DashboardMonthlyBalance[]>(endPoint, { observe: 'response' });
  }

  expensesCategory(year: number): Observable<HttpResponse<DashboardExpensesCategory[]>> {
    let endPoint = this.apiUrl + 'dashboard/expenses/category/' + year;

    return this.http.get<DashboardExpensesCategory[]>(endPoint, { observe: 'response' });
  }
}
