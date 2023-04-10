import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { AppConfig } from 'src/app/core/config/config';
import { Utils } from 'src/app/core/shared/utils';
import { Invoice } from 'src/app/model/invoice';
import { InvoiceRequest } from './form/invoice_request';

@Injectable({
  providedIn: 'root',
})
export class InvoicesService {
  private apiUrl = AppConfig.hostUrl + AppConfig.path;

  constructor(private http: HttpClient, private utils: Utils) {
    if (window.location.protocol == 'http:') {
      this.apiUrl = AppConfig.hostUrl + ':8080' + AppConfig.path;
    }
  }

  list(competence?: string, company?: number): Observable<HttpResponse<Invoice[]>> {
    let endPoint = this.apiUrl + 'invoices';

    if (competence && company) {
      endPoint += '?competence=' + competence + '&company=' + company;
    } else if (competence) {
      endPoint += '?competence=' + competence;
    } else if (company) {
      endPoint += '?company=' + company;
    }

    return this.http.get<Invoice[]>(endPoint, { observe: 'response' });
  }

  find(id: number): Observable<HttpResponse<Invoice>> {
    let endPoint = this.apiUrl + 'invoices/' + id;

    return this.http.get<Invoice>(endPoint, { observe: 'response' });
  }

  save(body: InvoiceRequest, id?: number): Observable<HttpResponse<Invoice>> {
    let endPoint = this.apiUrl + 'invoices';

    if (id) {
      endPoint += '/' + id;
      return this.http.put<Invoice>(endPoint, body ,{ observe: 'response' });
    } else {
      return this.http.post<Invoice>(endPoint, body ,{ observe: 'response' });
    }
  }

  delete(id: number): Observable<HttpResponse<any>> {
    let endPoint = this.apiUrl + 'invoices/' + id;
    return this.http.delete<any>(endPoint, { observe: 'response' });
  }
}
