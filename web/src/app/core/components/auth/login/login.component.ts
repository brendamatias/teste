import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { LayoutService } from 'src/app/layout/service/app.layout.service';
import { Utils } from 'src/app/core/shared/utils';
import { UserService } from 'src/app/core/services/user.service';
import { UserLoginDto } from 'src/app/core/dto/auth.dto';
import { LocalService } from 'src/app/core/services/local.service';
import { Router } from '@angular/router';
import { DecodedToken } from 'src/app/model/decoded_token';
import jwt_decode from 'jwt-decode';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styles: [
    `
      :host ::ng-deep .pi-eye,
      :host ::ng-deep .pi-eye-slash {
        transform: scale(1.6);
        margin-right: 1rem;
        color: var(--primary-color) !important;
      }
    `,
  ],
})
export class LoginComponent implements OnInit {
  public loginForm: FormGroup = this.fb.group({
    email: ['', Validators.required],
    password: ['', Validators.required],
    check: ['remember'],
  });

  constructor(
    public layoutService: LayoutService,
    private fb: FormBuilder,
    private router: Router,
    private utils: Utils,
    private localStore: LocalService,
    private userService: UserService
  ) {}

  ngOnInit() {
    this.userService.clear();
    this.localStore.removeData('token');
  }

  onSubmit() {
    if (this.loginForm.valid) {
      let errorMessage = 'O usuário e senha são inválidos.';

      let auth: UserLoginDto = {
        email: this.loginForm.value.email,
        password: this.loginForm.value.password,
      };
      this.userService.auth(auth).subscribe(
        (response) => {
          try {
            const responseObject = response.body;
            if (responseObject && responseObject.token) {

              const decodedToken: DecodedToken = jwt_decode(responseObject.token);
              this.localStore.saveData('token', responseObject.token);
              this.userService.updateUserName(decodedToken.name);
              this.userService.updateUserEmail(decodedToken.sub);

              this.router.navigate(['/dashboard']);
            } else {
              this.utils.exibirErro(errorMessage);
            }
          } catch (parseError) {
            console.error('Error parsing error object:', parseError);
          }
        },
        (e) => {          
          if (e.status === 403) {
            this.utils.exibirErro(e.error.message ? e.error.message : errorMessage);
          } else {
            this.utils.exibirErro(
              'Ocorreu um erro ao se comunicar com o serviço de autenticação!'
            );
          }
        }
      );
    }
  }
}
