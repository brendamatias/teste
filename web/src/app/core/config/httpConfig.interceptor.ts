import { Injectable } from '@angular/core';
import {
  HttpInterceptor,
  HttpRequest,
  HttpResponse,
  HttpHandler,
  HttpEvent,
} from '@angular/common/http';

import { Observable, throwError } from 'rxjs';
import { map, catchError } from 'rxjs/operators';
import { Utils } from '../shared/utils';
import { LocalService } from '../services/local.service';

@Injectable()
export class HttpConfigInterceptor implements HttpInterceptor {
  //Contador de solicitações de abertura de dialog.
  //Para controle e evitar abertura e fechamento de várias modais de aguarde simultaneamente
  private qtdDialogsAguarde: number;
  constructor(private utils: Utils, private localService: LocalService) {
    this.qtdDialogsAguarde = 0;
  }

  intercept(
    pRequest: HttpRequest<any>,
    pNext: HttpHandler
  ): Observable<HttpEvent<any>> {
    //Feed back a depender da presenca do parâmetro "_feedback" na url
    let lFeedBack = pRequest.urlWithParams.indexOf('_feedback=false') <= 0;
    let isNotJson = pRequest.urlWithParams.indexOf('_json=false') <= 0;
    const token = this.localService.getData('token');
    
    if (token) {
      pRequest = pRequest.clone({
        headers: pRequest.headers.set(
            "Authorization", 'Bearer ' + token
        ),
      });
    }

    if (isNotJson) {
      pRequest = pRequest.clone({
        headers: pRequest.headers.set(
            "Content-Type", 'application/json'
        ),
      });
    }

    if (lFeedBack) {
      this.qtdDialogsAguarde++;
      if (this.qtdDialogsAguarde == 1) {
        this.utils.abrirDialogAguarde();
      }
    }

    return pNext.handle(pRequest).pipe(
      map((event: HttpEvent<any>) => {
        if (event instanceof HttpResponse) {
          let lResp: HttpResponse<any> = event;

          //DMN-51772 Verifica se a resposta foi uma exception de negocio exception json
          if (lResp.body?.negocioException) {
            let lErr: any = new Error(lResp.body.message);
            lErr.negocioException = lResp.body;
            throw lErr;
          }

          if (lFeedBack) {
            setTimeout(() => {
              this.qtdDialogsAguarde--;
              if (this.qtdDialogsAguarde == 0) {
                this.utils.fecharDialogAguarde();
              }
            }, 100);
          }
        }

        return event;
      }),
      catchError((error) => {
        if (lFeedBack && this.utils.ref) {
          setTimeout(() => {
            this.qtdDialogsAguarde = 0;
            this.utils.fecharDialogAguarde();
          }, 100);
        }
        return throwError(error);
      })
    );
  }
}
