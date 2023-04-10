import { Component, ElementRef, OnInit } from '@angular/core';
import { MessageService, ConfirmationService } from 'primeng/api';
import { Utils } from 'src/app/core/shared/utils';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Expense } from 'src/app/model/expense';
import { ExpensesService } from '../expenses.service';
import { RxwebValidators } from '@rxweb/reactive-form-validators';
import { ExpenseRequest } from './expenses_request';
import { Category } from 'src/app/model/category';
import { CategoriesService } from '../../categories/categories.service';
import { Company } from 'src/app/model/company';
import { CompaniesService } from '../../companies/companies.service';

@Component({
  templateUrl: './expenses_form.component.html',
  providers: [MessageService, ConfirmationService],
})
export class ExpensesFormComponent implements OnInit {
  constructor(
    private router: Router,
    public route: ActivatedRoute,
    public utils: Utils,
    private expensesService: ExpensesService,
    private categoriesService: CategoriesService,
    private companiesServices: CompaniesService,
    private fb: FormBuilder
  ) {}

  id!: number;
  maxDate!: Date;
  expense!: FormGroup;
  categories!: Category[];
  companies!: Company[];

  async ngOnInit() {
    this.maxDate = new Date();

    await this._initDrops();

    this.route.params.subscribe((params) => {
      this.createFormClean();
      this.id = params['id'];
      if (this.id) {
        this._loadEntity(this.id);
      }
    });
  }

  async _initDrops() {
    this.categoriesService.list().subscribe((res) => {
      var categories = res.body!;
      this.categories = categories.filter((i) => i.status == 'active');
    });
    this.companiesServices.list().subscribe((res) => {
      var companies = res.body!;
      this.companies = companies.filter((i) => i.inactivatedAt == null);
    });
  }

  createFormClean() {
    this.expense = this.fb.group({
      category: [null, Validators.required],
      amount: [
        null,
        [
          RxwebValidators.numeric({ allowDecimal: true, isFormat: false }),
          Validators.required,
        ],
      ],
      expenseName: [
        null,
        [Validators.minLength(3), Validators.required],
      ],
      paymentDate: [null, Validators.required],
      competence: [null, Validators.required],
      company: [null, []],
    });
  }

  _loadingFormGroup(expenseDTO: Expense) {
    var month =
    expenseDTO.competenceMonth < 10
        ? '0' + expenseDTO.competenceMonth
        : expenseDTO.competenceMonth;
    var dateCompetence =
    expenseDTO.competenceYear + '-' + month + '-' + '01' + 'T00:00:00';

    this.expense.controls['category'].setValue(expenseDTO.category.id);
    this.expense.controls['amount'].setValue(expenseDTO.amount);
    this.expense.controls['expenseName'].setValue(expenseDTO.expenseName);
    this.expense.controls['paymentDate'].setValue(
      new Date(expenseDTO.paymentDate + 'T00:00:00')
    );
    this.expense.controls['competence'].setValue(new Date(dateCompetence));

    if (expenseDTO.company) {
      this.expense.controls['company'].setValue(expenseDTO.company.id);
    }
  }

  _loadEntity(id: number) {
    this.expensesService.find(id).subscribe(
      (res) => {
        this._loadingFormGroup(res.body!);
      },
      (erro) => {
        console.error('erro', erro);
        this.utils.exibirErro('Erro ao buscar dados da despesa: ' + id);
      }
    );
  }

  onSave() {
    if (this.expense.valid) {
      var expenseRequest = this._formToEntity();

      this.expensesService.save(expenseRequest, this.id).subscribe(
        (res) => {
          this.router.navigate(['/dashboard/expenses']);
          this.utils.exibirSucesso('Despesa salva com sucesso!');
        },
        (erro) => {
          console.error('erro', erro);
          this.utils.exibirErroOperation('Salvar Despesa!');
        }
      );
    } else {
      this.utils.exibirErro('Preencha todos os campos!');
    }
  }

  _formToEntity() {
    var paymentDateIso = this.expense.get('paymentDate')?.value;
    var month = paymentDateIso.getMonth() + 1;
    month = month < 10 ? '0' + month : month;
    var day =
      paymentDateIso.getDate() < 10
        ? '0' + paymentDateIso.getDate()
        : paymentDateIso.getDate();
    var paymentDate = paymentDateIso.getFullYear() + '-' + month + '-' + day;

    var expenseRequest: ExpenseRequest = {
      categoryId: this.expense.get('category')?.value,
      amount: this.expense.get('amount')?.value,
      expenseName: this.expense.get('expenseName')?.value,
      competenceMonth: this.expense.get('competence')!.value.getMonth() + 1,
      competenceYear: this.expense.get('competence')!.value.getFullYear(),
      paymentDate: paymentDate,
      companyId: this.expense.get('company')?.value,
    };

    return expenseRequest;
  }
}
