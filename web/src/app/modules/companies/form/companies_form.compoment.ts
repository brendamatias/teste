import { Component, OnInit } from '@angular/core';
import { MessageService, ConfirmationService } from 'primeng/api';
import { Utils } from 'src/app/core/shared/utils';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CompaniesRequest } from './companies_request';
import { CompaniesService } from '../companies.service';
import { Company } from 'src/app/model/company';

@Component({
  templateUrl: './companies_form.component.html',
  providers: [MessageService, ConfirmationService],
})
export class CompaniesFormComponent implements OnInit {
  constructor(
    private router: Router,
    public route: ActivatedRoute,
    public utils: Utils,
    private companiesServices: CompaniesService,
    private fb: FormBuilder
  ) {}

  id!: number;
  company!: FormGroup;

  async ngOnInit() {

    this.route.params.subscribe((params) => {
      this.createFormClean();
      this.id = params['id'];
      if (this.id) {
        this._loadEntity(this.id);
      }
    });
  }

  createFormClean() {
    this.company = this.fb.group({
      name: [null, [Validators.minLength(3), Validators.required]],
      cnpj: [null, [Validators.pattern('^[0-9]*$'), Validators.required, Validators.minLength(14), Validators.maxLength(14)]],
      legalName: [null, [Validators.minLength(3), Validators.required]]
    });
  }

  _loadingFormGroup(companyDTO: Company) {
    this.company.controls['name'].setValue(companyDTO.name);
    this.company.controls['cnpj'].setValue(companyDTO.cnpj);
    this.company.controls['legalName'].setValue(companyDTO.legalName);
  }

  _loadEntity(id: number) {
    this.companiesServices.find(id).subscribe(
      (res) => {
        this._loadingFormGroup(res.body!);
      },
      (erro) => {
        console.error('erro', erro);
        this.utils.exibirErro('Erro ao buscar dados da empresa: ' + id);
      }
    );
  }

  onSave() {
    if (this.company.valid) {
      var request = this._formToEntity();

      this.companiesServices.save(request, this.id).subscribe(
        (res) => {
          this.router.navigate(['/dashboard/companies']);
          this.utils.exibirSucesso('Empresa salva com sucesso!');
        },
        (erro) => {
          console.error('erro', erro);
          this.utils.exibirErroOperation('Salvar Empresa!');
        }
      );
    } else {
      this.utils.exibirErro('Preencha todos os campos!');
    }
  }

  _formToEntity() {
    var request: CompaniesRequest = {
      name: this.company.get('name')?.value,
      cnpj: this.company.get('cnpj')?.value,
      legalName: this.company.get('legalName')?.value,
    };

    return request;
  }
}
