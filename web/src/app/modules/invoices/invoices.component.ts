import { Component, OnInit, ViewChild } from '@angular/core';
import { Table, TableLazyLoadEvent, TablePageEvent } from 'primeng/table';
import { MessageService, ConfirmationService } from 'primeng/api';
import { InvoicesService } from './invoices.service';
import { Utils } from 'src/app/core/shared/utils';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UserService } from 'src/app/core/services/user.service';
import { environment } from 'src/environments/environment';
import { Invoice } from 'src/app/model/invoice';
import { Company } from 'src/app/model/company';
import { CompaniesService } from '../companies/companies.service';

@Component({
  templateUrl: './invoices.component.html',
  providers: [MessageService, ConfirmationService],
})
export class InvoicesComponent implements OnInit {
  @ViewChild('table') table!: Table;

  constructor(
    public utils: Utils,
    private invoicesService: InvoicesService,
    private confirmationService: ConfirmationService,
    private companiesService: CompaniesService,
    private fb: FormBuilder
  ) {}

  list!: Invoice[];
  companies!: Company[];
  currentDate: Date = new Date();
  lastYear!: Date;

  filter: FormGroup = this.fb.group({
    competence: undefined,
    company: undefined,
  });

  async ngOnInit() {
    this.lastYear = this.currentDate;
    await this._initCompanies();
    this.findAll();
  }

  async _initCompanies() {
    this.companiesService.list().subscribe((res) => {
      this.companies = res.body!;
    });
  }

  onFilter() {
    var company = this.filter.get('company')?.value;
    var competenceFilter = this.filter.get('competence')?.value;
    var competence = undefined;
    if (competenceFilter) {
      var month = competenceFilter.getMonth() + 1;
      month = month < 10 ? '0' + month : month;
      competence = competenceFilter.getFullYear() + '-' + month;
    }
    
    this.findAll(competence, company);
  }

  clean() {
    this.filter.reset({
      competence: undefined,
      company: undefined,
    });
    this.findAll();
  }

  findAll(competence?: string, company?: number) {
    this.invoicesService.list(competence, company).subscribe((res) => {
      this.list = res.body!;
    });
  }

  delete(event: MouseEvent, invoice: Invoice) {
    this.confirmationService.confirm({
      target: event.target as EventTarget,
      message:
        'Realmente deseja excluir nota fiscal de número ' +
        invoice.number +
        '?',
      header: 'Exclusão',
      icon: 'pi pi-trash',
      acceptIcon: 'none',
      rejectIcon: 'none',
      rejectButtonStyleClass: 'p-button-text',
      acceptLabel: 'Sim',
      rejectLabel: 'Não',
      accept: async () => {
        await this._exeDelete(invoice);
      },
      reject: () => {},
    });
  }

  async _exeDelete(invoice: Invoice) {
    this.invoicesService.delete(invoice.id).subscribe(
      (res) => {
        this.utils.exibirSucesso('Nota fiscal excluída com sucesso!');
        this.findAll();
      },
      (erro) => {
        console.error('erro', erro);
        this.utils.exibirErro(
          'Erro ao excluir nota fiscal, contate a equipe de TI.'
        );
      }
    );
  }
}
