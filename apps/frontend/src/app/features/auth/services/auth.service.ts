import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { STORAGE_KEYS } from '../../../core/utils/storage-keys';

export type User = {
  userId: string;
  name: string;
  email: string;
}

type LoginResponse = User & {
  token: string;
};

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(
    private readonly http: HttpClient,
  ) { }

  public login(email: string, password: string): Observable<LoginResponse> {
    return this.http.post<LoginResponse>('/users/login', { email, password })
      .pipe(
        tap(({ token }) => localStorage.setItem(STORAGE_KEYS.TOKEN, token)),
        tap(response => {
          localStorage.setItem(STORAGE_KEYS.USER, JSON.stringify({
            userId: response.userId,
            name: response.name,
            email: response.email,
          }));
        }),
      );
  }

  public logout(): void {
    localStorage.removeItem(STORAGE_KEYS.TOKEN);
  }
}
