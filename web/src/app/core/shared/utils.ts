import { ActivatedRoute, Params, Router } from '@angular/router';
import moment from 'moment';
import { Injectable } from '@angular/core';
import { LoadingDialog } from '../components/dialogs/loading/loading.dialog';
import { DialogService, DynamicDialogRef } from 'primeng/dynamicdialog';
import { MessageService } from 'primeng/api';

type SortDirection = 'asc' | 'desc';

//Classe com funcionalidades utilitárias que podem ser reaproveitadas em diversas telas
@Injectable()
export class Utils {
  ref: DynamicDialogRef | undefined;

  constructor(
    protected route: ActivatedRoute,
    protected router: Router,
    private messageService: MessageService,
    private dialogService: DialogService
  ) {}

  filterCompaniesFields: any[] = ['name', 'cnpj'];
  filterCategoriesFields: any[] = ['name', 'description'];

  //Formatada para mensagens de confirmação de operações bem sucedidas(ex.: Registro excluído com sucesso)
  exibirSucesso(msg: string) {
    this.messageService.add({
      severity: 'success',
      summary: 'Sucesso',
      detail: msg,
    });
  }
  //Formatada para mensagens de erro
  exibirErro(msg: string) {
    this.messageService.add({
      severity: 'error',
      summary: 'Erro',
      sticky: true,
      detail: msg,
    });
  }

  //Formatada para mensagens de erro
  exibirErroOperation(operation: string) {
    this.messageService.add({
      severity: 'error',
      summary: 'Erro',
      sticky: true,
      detail: `Ocorreu um erro ao executar a operação ${operation}. Por favor, entre em contato com o setor responsável ou tente novamente mais tarde.`,
    });
  }
  //Mensagens de alerta(ex.: sua senha expirará em 2 dias)
  exibirWarning(msg: string) {
    this.messageService.add({
      severity: 'warn',
      summary: 'Alerta',
      sticky: true,
      detail: msg,
    });
  }
  //Informações relevantes para o usuario(ex.: dois novos processos disponíveis)
  exibirInformacao(msg: string) {
    this.messageService.add({ severity: 'info', detail: msg });
  }

  //Método que obtém a resposta dos resolvers.
  get respResolvers() {
    return this.route.snapshot.data;
  }
  //Método que obtém a resposta dos resolvers.
  get parentRespResolvers() {
    return this.route.parent?.snapshot.data;
  }

  formatarData(pIsoDate: string) {
    if (pIsoDate != null) {
      return moment(pIsoDate, moment.ISO_8601).format('DD/MM/YYYY');
    } else {
      return null;
    }
  }

  formatarDataHora(pIsoDate: string) {
    if (pIsoDate != null) {
      return moment(pIsoDate, moment.ISO_8601).format('DD/MM/YYYY HH:mm');
    } else {
      return null;
    }
  }

  public formatarHora(pIsoDate: string) {
    if (pIsoDate != null) {
      return moment(pIsoDate, moment.ISO_8601).format('HH:mm');
    } else {
      return null;
    }
  }

  formatarDataEN(pIsoDate: string) {
    if (pIsoDate != null) {
      return moment(pIsoDate, moment.ISO_8601).format('YYYY-MM-DD');
    } else {
      return null;
    }
  }

  //Obtem o valor de um parâmetro recebido na rota corrente
  public obterParametro(pParametro: string) {
    let lParametro = this.route.snapshot.paramMap.get(pParametro);
    if (lParametro != null && lParametro != 'undefined') {
      return lParametro;
    } else {
      return null;
    }
  }

  //Atualiza a tela corrente
  reload() {
    window.location.reload();
  }

  //Ordena um array de objetos com base nos nomes dos atributos e direção()
  sort(
    records: Array<any>,
    atributos: string[],
    direction: SortDirection
  ): any {
    let directions: number[];
    if (direction == 'asc') {
      directions = [1];
    } else {
      if (direction == 'desc') {
        directions = [-1];
      }
    }
    return records.sort(function (a, b) {
      for (let i = 0; i < atributos.length; i++) {
        if (a[atributos[i]] < b[atributos[i]]) {
          return -1 * directions[i];
        } else if (a[atributos[i]] > b[atributos[i]]) {
          return 1 * directions[i];
        }
      }
      return 0;
    });
  }

  //retorna a rota atual, incluindo a URL e seus parametros
  obterRotaAtual() {
    let lParams = this.obterParamsRotaCorrente();

    return [this.obterUrlRotaAtual(), lParams];
  }

  obterUrlRotaAtual(): string {
    //Retorna a url da rota atual, sem os parametros
    let lIndex = this.router.url.indexOf(';');
    if (lIndex >= 0) {
      //A partir do ';' vem os parametros que devem ser desconsiderados
      return this.router.url.substring(0, this.router.url.indexOf(';'));
    } else {
      return this.router.url;
    }
  }

  obterParamsRotaCorrente(): Params {
    return this.route.snapshot.params;
  }

  //Provoa o refresh do browser na rota corrente
  atualizar() {
    this.router
      .navigate(this.obterRotaAtual(), { replaceUrl: true })
      .then((resp) => {});
  }

  abrirDialogAguarde() {
    this.ref = this.dialogService.open(LoadingDialog, {
      closable: false,
    });
  }

  fecharDialogAguarde() {
    if (this.ref) {
      this.ref.close();
    }
  }

  convertStringListToMap(list: String[]) {
    let result: { label: String; value: String }[] = [];
    list.forEach((e) => {
      if (e && e.trim() != '') {
        result.push({
          label: e,
          value: e,
        });
      }
    });
    return result;
  }

  formatDocument(value: number) {
    const cnpjCpf = value?.toString().replace(/\D/g, '');

    if (cnpjCpf?.length === 11) {
      return cnpjCpf
        ?.padStart(11, '0')
        .replace(/(\d{3})(\d{3})(\d{3})(\d{2})/g, '$1.$2.$3-$4');
    }

    return cnpjCpf
      ?.padEnd(14, '0')
      .replace(/(\d{2})(\d{3})(\d{3})(\d{4})(\d{2})/g, '$1.$2.$3/$4-$5');
  }

  getMonthBR(month: number) {
    const monthers = [
      'Janeiro',
      'Fevereiro',
      'Março',
      'Abril',
      'Maio',
      'Junho',
      'Julho',
      'Agosto',
      'Setembro',
      'Outubro',
      'Novembro',
      'Dezembro',
    ];
    return monthers[month - 1];
  }
}
