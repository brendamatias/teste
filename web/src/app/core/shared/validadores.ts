
import { ValidationErrors, FormControl, ValidatorFn, AbstractControl, FormGroup } from '@angular/forms';

export class Validadores {

        static ValidaCpf(controle: AbstractControl) {
          const cpf = controle.value;
      
          let soma: number = 0;
          let resto: number;
          let valido: boolean;
      
          const regex = new RegExp('[0-9]{11}');
      
          if (
            cpf == '00000000000' ||
            cpf == '11111111111' ||
            cpf == '22222222222' ||
            cpf == '33333333333' ||
            cpf == '44444444444' ||
            cpf == '55555555555' ||
            cpf == '66666666666' ||
            cpf == '77777777777' ||
            cpf == '88888888888' ||
            cpf == '99999999999' ||
            !regex.test(cpf)
          )
            valido = false;
          else {
            for (let i = 1; i <= 9; i++)
              soma = soma + parseInt(cpf.substring(i - 1, i)) * (11 - i);
            resto = (soma * 10) % 11;
      
            if (resto == 10 || resto == 11) resto = 0;
            if (resto != parseInt(cpf.substring(9, 10))) valido = false;
      
            soma = 0;
            for (let i = 1; i <= 10; i++)
              soma = soma + parseInt(cpf.substring(i - 1, i)) * (12 - i);
            resto = (soma * 10) % 11;
      
            if (resto == 10 || resto == 11) resto = 0;
            if (resto != parseInt(cpf.substring(10, 11))) valido = false;
            valido = true;
          }
      
          if (valido) return null;
      
          return { cpfInvalido: true };
        }

    static cpf(pControl: FormControl): ValidationErrors | undefined {
        if (pControl.value != undefined && pControl.value != null && pControl.value.toString() != '') {

            if (!pControl.value.match("^[0-9]{3}\\.[0-9]{3}\\.[0-9]{3}\\-[0-9]{2}$")
                && !pControl.value.match("^[0-9]{11}$")) {
                return { cpf: true };
            }
        }
        return undefined;
    }

    static cnpj(pControl: FormControl): ValidationErrors| undefined {
        if (pControl.value != undefined && pControl.value != null && pControl.value.toString() != '') {
            if (!pControl.value.match("^[0-9]{2}\\.[0-9]{3}\\.[0-9]{3}\\/[0-9]{4}\\-[0-9]{2}$")
                && !pControl.value.match("^[0-9]{14}$")) {
                return { cnpj: true };
            }
        }
        return undefined;
    }



    static cep(pControl: FormControl): ValidationErrors| undefined {

        if (pControl.value != undefined && pControl.value != null && pControl.value.toString() != '') {
            if (!pControl.value.match("^[0-9]{2}\\.[0-9]{3}\\-[0-9]{3}$")
                && !pControl.value.match("^[0-9]{5}\\-[0-9]{3}$")
                && !pControl.value.match("^[0-9]{8}$")) {
                return { cep: true };
            }
        }
        return undefined;
    }

    static entidadeJPA(pControl: FormControl): ValidationErrors| undefined {
        if (pControl.value != undefined && pControl.value != null && pControl.value.toString() != '') {
            if (!pControl.value.id) {
                return { entidadeJPA: true }
            }
        }

        return undefined;

    }

    static telefone(pControl: FormControl): ValidationErrors| undefined {


        if (pControl.value != undefined && pControl.value != null && pControl.value.toString() != '') {
            if (!pControl.value.match("^\\([0-9]{2}\\)\\s?[0-9]{4,5}\\-[0-9]{4}$")) {
                return { telefone: true };
            }
        }

        return undefined;
    }

    static celular(pControl: FormControl): ValidationErrors| undefined {


        if (pControl.value != undefined && pControl.value != null && pControl.value.toString() != '') {
            if (!pControl.value.match("^\\([0-9]{2}\\)\\s?9[0-9]{4}\\-[0-9]{4}$")) {
                return { celular: true };
            }
        }

        return undefined;
    }

    static email(pControl: FormControl): ValidationErrors| undefined {
        if (pControl.value != undefined && pControl.value != null && pControl.value.toString() != '') {
            if (!pControl.value.match("^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$")) {
                return { email: true };
            }
        }
        return undefined;
    }

    static smsCodeLengthValidator(): ValidatorFn {
        return (control: AbstractControl): { [key: string]: any } | null => {
          const value = control.value;
          if (value && value.toString().length !== 6) {
            return { invalidSmsCodeLength: true };
          }
          return null;
        };
      }
      static passwordValidator(minLength: number, maxLength: number, allowedSpecialChars: string): ValidatorFn {
        return (control: AbstractControl): { [key: string]: any } | null => {
          const password = control.value;
      
          if (!password || password.length < minLength || password.length > maxLength || !new RegExp(`^[a-zA-Z0-9${allowedSpecialChars}]+$`).test(password)) {
            return { invalidPassword: true };
          }
      
          const lastCharacter = password[password.length - 1];
          if (allowedSpecialChars.includes(lastCharacter)) {
            return { invalidPassword: true };
          }
      
          return null;
        };
      }
}