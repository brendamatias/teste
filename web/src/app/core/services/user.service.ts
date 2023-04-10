import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { UserLoginDto } from '../dto/auth.dto';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { AppConfig } from '../config/config';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  constructor(private http: HttpClient) {
    if (window.location.protocol == 'http:') {
      this.apiUrl = AppConfig.hostUrl + ':8080' + AppConfig.path;
    }
  }

  private apiUrl = AppConfig.hostUrl + AppConfig.path;

  private userNameSubject = new BehaviorSubject<string | undefined>(undefined);
  userName$ = this.userNameSubject.asObservable();

  private userEmailSubject = new BehaviorSubject<string | undefined>(undefined);
  userEmail$ = this.userEmailSubject.asObservable();

  updateUserName(name: string): void {
    this.userNameSubject.next(name);
  }

  updateUserEmail(email: string): void {
    this.userEmailSubject.next(email);
  }

  auth(auth: UserLoginDto): Observable<HttpResponse<any>> {
    const endPoint = this.apiUrl + 'auth/login';
    return this.http.post<any>(endPoint, auth, { observe: 'response' });
  }

  clear() {
    this.userNameSubject.next(undefined);
    this.userEmailSubject.next(undefined);
  }
  
}
