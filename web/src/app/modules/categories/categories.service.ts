import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { AppConfig } from 'src/app/core/config/config';
import { Utils } from 'src/app/core/shared/utils';
import { Category } from 'src/app/model/category';
import { CategoriesRequest } from './form/categories_request';

@Injectable({
  providedIn: 'root',
})
export class CategoriesService {
  private apiUrl = AppConfig.hostUrl + AppConfig.path;

  constructor(private http: HttpClient, private utils: Utils) {
    if (window.location.protocol == 'http:') {
      this.apiUrl = AppConfig.hostUrl + ':8080' + AppConfig.path;
    }
  }

  list(): Observable<HttpResponse<Category[]>> {
    let endPoint = this.apiUrl + 'categories';

    return this.http.get<Category[]>(endPoint, { observe: 'response' });
  }
  
  archiveOrActive(id: number, status: string): Observable<HttpResponse<Category>> {
    let endPoint = this.apiUrl + 'categories/' + id + '/status?status=' + status;

    return this.http.patch<Category>(endPoint, {'status' : status}, { observe: 'response' });
  }

  find(id: number): Observable<HttpResponse<Category>> {
    let endPoint = this.apiUrl + 'categories/' + id;

    return this.http.get<Category>(endPoint, { observe: 'response' });
  }

  save(body: CategoriesRequest, id?: number): Observable<HttpResponse<Category>> {
    let endPoint = this.apiUrl + 'categories';

    if (id) {
      endPoint += '/' + id;
      return this.http.put<Category>(endPoint, body ,{ observe: 'response' });
    } else {
      return this.http.post<Category>(endPoint, body ,{ observe: 'response' });
    }
  }

  
}
