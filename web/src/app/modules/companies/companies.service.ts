import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { AppConfig } from 'src/app/core/config/config';
import { Utils } from 'src/app/core/shared/utils';
import { Company } from 'src/app/model/company';
import { CompaniesRequest } from './form/companies_request';

@Injectable({
  providedIn: 'root',
})
export class CompaniesService {
  private apiUrl = AppConfig.hostUrl + AppConfig.path;

  constructor(private http: HttpClient, private utils: Utils) {
    if (window.location.protocol == 'http:') {
      this.apiUrl = AppConfig.hostUrl + ':8080' + AppConfig.path;
    }
  }

  list(): Observable<HttpResponse<Company[]>> {
    let endPoint = this.apiUrl + 'companies';

    return this.http.get<Company[]>(endPoint, { observe: 'response' });
  }

  archiveOrActive(id: number): Observable<HttpResponse<Company>> {
    let endPoint = this.apiUrl + 'companies/' + id + '/status';

    return this.http.patch<Company>(endPoint, {}, { observe: 'response' });
  }

  find(id: number): Observable<HttpResponse<Company>> {
    let endPoint = this.apiUrl + 'companies/' + id;

    return this.http.get<Company>(endPoint, { observe: 'response' });
  }

  save(body: CompaniesRequest, id?: number): Observable<HttpResponse<Company>> {
    let endPoint = this.apiUrl + 'companies';

    if (id) {
      endPoint += '/' + id;
      return this.http.put<Company>(endPoint, body ,{ observe: 'response' });
    } else {
      return this.http.post<Company>(endPoint, body ,{ observe: 'response' });
    }
  }
  
}
