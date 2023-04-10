import { Injectable } from '@angular/core';
import {
  CanActivate,
  ActivatedRouteSnapshot,
  RouterStateSnapshot,
  Router,
} from '@angular/router';
import jwt_decode from 'jwt-decode';
import { Utils } from 'src/app/core/shared/utils';
import { environment } from 'src/environments/environment';
import { LocalService } from '../services/local.service';
import { UserService } from '../services/user.service';
import { DecodedToken } from 'src/app/model/decoded_token';

@Injectable({
  providedIn: 'root',
})
export class AuthTokenGuard implements CanActivate {
  constructor(private util: Utils, private router: Router, private localStore: LocalService,
    private userService: UserService) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    const token = this.localStore.getData('token');
    if (!token) {
      this.userService.clear();
      this.util.exibirErro('Usuário não autenticado!');
      this.router.navigate(['login']);
      return false;
    }
    
    const decodedToken: DecodedToken = jwt_decode(token);
    if (decodedToken.iss !== environment.iss) {
      this.userService.clear();
      this.util.exibirErro('Token gerado não possui permissão para a aplicação!');
      this.router.navigate(['error-page']);
      return false;
    }

    this.userService.updateUserName(decodedToken.name);
    this.userService.updateUserEmail(decodedToken.sub);

    const currentTimestamp = Math.floor(Date.now() / 1000);
    if (decodedToken.exp && decodedToken.exp < currentTimestamp) {
      this.util.exibirErro('Tempo expirado. Por favor, comece novamente.');
      this.router.navigate(['login']);
      return false;
    }
    return true;

  }
}