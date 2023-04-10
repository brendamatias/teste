import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { AppConfig } from 'src/app/core/config/config';
import { Preference } from 'src/app/model/preference';

@Injectable({
  providedIn: 'root',
})
export class PreferencesService {
  private apiUrl = AppConfig.hostUrl + AppConfig.path;

  constructor(private http: HttpClient) {
    if (window.location.protocol == 'http:') {
      this.apiUrl = AppConfig.hostUrl + ':8080' + AppConfig.path;
    }
  }

  find(): Observable<HttpResponse<Preference>> {
    let endPoint = this.apiUrl + 'preferences';

    return this.http.get<Preference>(endPoint, { observe: 'response' });
  }

  save(body: Preference): Observable<HttpResponse<Preference>> {
    let endPoint = this.apiUrl + 'preferences';
    return this.http.put<Preference>(endPoint, body ,{ observe: 'response' });
  }
  
}
