import { Component, OnInit } from '@angular/core';
import { MessageService, ConfirmationService } from 'primeng/api';
import { Utils } from 'src/app/core/shared/utils';
import { Company } from 'src/app/model/company';
import { CompaniesService } from './companies.service';

@Component({
  templateUrl: './companies.component.html',
  providers: [MessageService, ConfirmationService],
})
export class CompaniesComponent implements OnInit {

  constructor(
    public utils: Utils,
    private companiesService: CompaniesService,
    private confirmationService: ConfirmationService,
  ) {}
  
  
  list!: Company[];
  currentDate: Date = new Date();
  lastYear!: Date;

  ngOnInit() {  
    this.lastYear = this.currentDate;
    this.findAll();
  }

  findAll() {
    this.companiesService.list().subscribe((res) => {
      this.list = res.body!;
      console.log('COMPANIES', this.list);
    });
  }

  archive(event: MouseEvent, company: Company) {
    var message = company.inactivatedAt ? 'ativar' : 'arquivar';
    this.confirmationService.confirm({
      target: event.target as EventTarget,
      message: 'Realmente deseja '+message+' a empresa: ' + company.name + '?',
      header: company.inactivatedAt ? 'Ativação' : 'Arquivamento',
      icon: company.inactivatedAt ? 'pi pi-eye' : 'pi pi-eye-slash',
      acceptIcon: 'none',
      rejectIcon: 'none',
      rejectButtonStyleClass: 'p-button-text',
      acceptLabel: 'Sim',
      rejectLabel: 'Não',
      accept: async () => {
        await this._exeArchive(company);
      },
      reject: () => {},
    });
  }

  async _exeArchive(company: Company) {
    var message = company.inactivatedAt ? 'ativada' : 'arquivada';
    this.companiesService.archiveOrActive(company.id).subscribe(
      (res) => {
        this.utils.exibirSucesso('Empresa ' + message + ' com sucesso!');
        this.findAll();
      },
      (erro) => {
        console.error('erro', erro);
        this.utils.exibirErro(
          'Erro ao se comunicar com o serviço, contate a equipe de TI.'
        );
      }
    );
  }
 
}
