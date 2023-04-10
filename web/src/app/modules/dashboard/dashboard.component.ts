import { Component, OnInit } from '@angular/core';
import { MessageService, ConfirmationService } from 'primeng/api';
import { DashboardService } from './dashboard.service';
import { Utils } from 'src/app/core/shared/utils';
import { FormBuilder, FormGroup } from '@angular/forms';
import { UserService } from 'src/app/core/services/user.service';
import { LocalService } from 'src/app/core/services/local.service';

@Component({
  templateUrl: './dashboard.component.html',
  providers: [MessageService, ConfirmationService],
})
export class DashboardComponent implements OnInit {
  constructor(
    private utils: Utils,
    private userService: UserService,
    private localStore: LocalService,
    private dashboardService: DashboardService,
    private confirmationService: ConfirmationService,
    private fb: FormBuilder
  ) {}

  currentDate: Date = new Date();
  lastYear!: Date;
  availableInvoiceAmount = {
    data: {},
    options: {
      cutout: '50%',
      circumference: 180,
      rotation: -90,
    },
  };

  totalInvoiceAmounts = {
    data: {},
    options: {},
  };

  totalExpenseAmounts = {
    data: {},
    options: {},
  };

  monthlyBalance = {
    data: {},
    options: {},
  };

  expensesCategory = {
    data: {},
    options: {},
  };

  filter: FormGroup = this.fb.group({
    compentenceYear: this.currentDate,
  });

  ngOnInit() {
    this.lastYear = this.currentDate;
    this.init();
  }

  isRowSelectable(event: { data: any }) {
    return event.data != null && event.data.excluir == 0;
  }

  init() {
    this.findAvailableInvoiceAmount();
    this.findTotalInvoiceAmounts();
    this.findTotalExpenseAmounts();
    this.findMothlyBalance();
    this.findExpensesCategory();
  }

  async findAvailableInvoiceAmount() {
    const year = this.filter.value.compentenceYear.getFullYear();
    this.dashboardService.availableInvoiceAmount(year).subscribe((res) => {
      const body = res.body!;
      const availableLimit = (body.annualInvoiceLimit - body.totalInvoiceAmount);

      this.availableInvoiceAmount.data = {
        labels: ['Limite Usado', 'Limite Disponível'],
        datasets: [
          {
            data: [body.totalInvoiceAmount, (availableLimit < 0 ? 0 : availableLimit)],
            backgroundColor: [(body.totalInvoiceAmount >= body.annualInvoiceLimit ? '#d9342b' : ((body.totalInvoiceAmount/body.annualInvoiceLimit)*100) >= 80 ? '#f97316' : '#14b8a6'), '#609af8'],
          }
        ]
      }
    });
  }

  async findTotalInvoiceAmounts() {
    const year = this.filter.value.compentenceYear.getFullYear();
    this.dashboardService.totalInvoiceAmounts(year).subscribe((res) => {
      const body = res.body!;

      const monthers = body.map((t) => this.utils.getMonthBR(t.competenceMonth));
      const amounts = body.map((t) => t.totalAmount);

      this.totalInvoiceAmounts.data = {
        labels: monthers,
        datasets: [
          {
            label: 'Notas Fiscais',
            data: amounts
          }
        ]
      }
    });
  }

  async findTotalExpenseAmounts() {
    const year = this.filter.value.compentenceYear.getFullYear();
    this.dashboardService.totalExpenseAmounts(year).subscribe((res) => {
      const body = res.body!;

      const monthers = body.map((t) => this.utils.getMonthBR(t.competenceMonth));
      const amounts = body.map((t) => t.totalAmount);

      this.totalExpenseAmounts.data = {
        labels: monthers,
        datasets: [
          {
            label: 'Despesas',
            data: amounts
          }
        ]
      }
    });
  }
  
  async findMothlyBalance() {
    const year = this.filter.value.compentenceYear.getFullYear();
    this.dashboardService.monthlyBalance(year).subscribe((res) => {
      const body = res.body!;

      const monthers = body.map((t) => this.utils.getMonthBR(t.competenceMonth));
      const expenses = body.map((t) => t.totalAmountExpense);
      const invoices = body.map((t) => t.totalAmountInvoice);

      this.monthlyBalance.data = {
        labels: monthers,
        datasets: [          
          {
            label: 'Receitas',
            data: invoices
          },{
            label: 'Despesas',
            data: expenses
          },
        ]
      }
    });
  }

  async findExpensesCategory() {
    const year = this.filter.value.compentenceYear.getFullYear();
    this.dashboardService.expensesCategory(year).subscribe((res) => {
      const body = res.body!;

      const categories = body.map((t) => t.category);
      const amounts = body.map((t) => t.totalAmount);

      this.expensesCategory.data = {
        labels: categories,
        datasets: [          
          {
            label: 'Categorias',
            data: amounts
          },
        ]
      }
    });
  }
}
