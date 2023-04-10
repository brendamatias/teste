import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { AppConfig } from 'src/app/core/config/config';
import { Utils } from 'src/app/core/shared/utils';
import { Expense } from 'src/app/model/expense';
import { ExpenseRequest } from './form/expenses_request';

@Injectable({
  providedIn: 'root',
})
export class ExpensesService {
  private apiUrl = AppConfig.hostUrl + AppConfig.path;

  constructor(private http: HttpClient, private utils: Utils) {
    if (window.location.protocol == 'http:') {
      this.apiUrl = AppConfig.hostUrl + ':8080' + AppConfig.path;
    }
  }

  list(competence?: string, category?: number): Observable<HttpResponse<Expense[]>> {
    let endPoint = this.apiUrl + 'expenses';

    if (competence && category) {
      endPoint += '?competence=' + competence + '&category=' + category;
    } else if (competence) {
      endPoint += '?competence=' + competence;
    } else if (category) {
      endPoint += '?category=' + category;
    }

    return this.http.get<Expense[]>(endPoint, { observe: 'response' });
  }

  find(id: number): Observable<HttpResponse<Expense>> {
    let endPoint = this.apiUrl + 'expenses/' + id;

    return this.http.get<Expense>(endPoint, { observe: 'response' });
  }

  save(body: ExpenseRequest, id?: number): Observable<HttpResponse<Expense>> {
    let endPoint = this.apiUrl + 'expenses';

    if (id) {
      endPoint += '/' + id;
      return this.http.put<Expense>(endPoint, body ,{ observe: 'response' });
    } else {
      return this.http.post<Expense>(endPoint, body ,{ observe: 'response' });
    }
  }

  delete(id: number): Observable<HttpResponse<any>> {
    let endPoint = this.apiUrl + 'expenses/' + id;
    return this.http.delete<any>(endPoint, { observe: 'response' });
  }
}
