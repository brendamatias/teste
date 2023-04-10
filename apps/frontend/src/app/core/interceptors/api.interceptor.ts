import { HttpInterceptorFn } from '@angular/common/http';
import { environment } from '../../../environments/environment';

export const apiInterceptor: HttpInterceptorFn = (req, next) => {
  let requestUrl = req.url;

  if (!requestUrl.includes('http')) {
    requestUrl = `${environment.baseUrl}${requestUrl}`;
  }

  if (!requestUrl.includes('login')) {
    const token = localStorage.getItem('token');
    req = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
  }

  req = req.clone({
    url: requestUrl
  });

  return next(req);
};
