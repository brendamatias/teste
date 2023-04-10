import { Component, OnInit, ViewChild } from '@angular/core';
import { Table, TableLazyLoadEvent, TablePageEvent } from 'primeng/table';
import { MessageService, ConfirmationService } from 'primeng/api';
import { ExpensesService } from './expenses.service';
import { Utils } from 'src/app/core/shared/utils';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UserService } from 'src/app/core/services/user.service';
import { environment } from 'src/environments/environment';
import { Expense } from 'src/app/model/expense';
import { Category } from 'src/app/model/category';
import { CompaniesService } from '../companies/companies.service';
import { CategoriesService } from '../categories/categories.service';

@Component({
  templateUrl: './expenses.component.html',
  providers: [MessageService, ConfirmationService],
})
export class ExpensesComponent implements OnInit {
  @ViewChild('table') table!: Table;

  constructor(
    public utils: Utils,
    private expensesService: ExpensesService,
    private confirmationService: ConfirmationService,
    private categoriesService: CategoriesService,
    private fb: FormBuilder
  ) {}

  list!: Expense[];
  categories!: Category[];
  currentDate: Date = new Date();
  lastYear!: Date;

  filter: FormGroup = this.fb.group({
    competence: undefined,
    category: undefined,
  });

  async ngOnInit() {
    this.lastYear = this.currentDate;
    await this._initCompanies();
    this.findAll();
  }

  async _initCompanies() {
    this.categoriesService.list().subscribe((res) => {
      console.log('CATEOGIRAS', res.body);

      this.categories = res.body!;
    });
  }

  onFilter() {
    var category = this.filter.get('category')?.value;
    var competenceFilter = this.filter.get('competence')?.value;
    var competence = undefined;
    if (competenceFilter) {
      var month = competenceFilter.getMonth() + 1;
      month = month < 10 ? '0' + month : month;
      competence = competenceFilter.getFullYear() + '-' + month;
    }

    this.findAll(competence, category);
  }

  clean() {
    this.filter.reset({
      competence: undefined,
      category: undefined,
    });
    this.findAll();
  }

  findAll(competence?: string, category?: number) {
    this.expensesService.list(competence, category).subscribe((res) => {
      console.log('DESPESAS', res.body);
      this.list = res.body!;
    });
  }

  delete(event: MouseEvent, expense: Expense) {
    this.confirmationService.confirm({
      target: event.target as EventTarget,
      message:
        'Realmente deseja excluir a despesa: ' + expense.expenseName + '?',
      header: 'Exclusão',
      icon: 'pi pi-trash',
      acceptIcon: 'none',
      rejectIcon: 'none',
      rejectButtonStyleClass: 'p-button-text',
      acceptLabel: 'Sim',
      rejectLabel: 'Não',
      accept: async () => {
        await this._exeDelete(expense);
      },
      reject: () => {},
    });
  }

  async _exeDelete(expense: Expense) {
    this.expensesService.delete(expense.id).subscribe(
      (res) => {
        this.utils.exibirSucesso('Despesa excluída com sucesso!');
        this.findAll();
      },
      (erro) => {
        console.error('erro', erro);
        this.utils.exibirErro(
          'Erro ao excluir despesa, contate a equipe de TI.'
        );
      }
    );
  }
}
