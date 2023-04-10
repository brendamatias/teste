import { Component, ElementRef, OnInit } from '@angular/core';
import { MessageService, ConfirmationService } from 'primeng/api';
import { Utils } from 'src/app/core/shared/utils';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Invoice } from 'src/app/model/invoice';
import { InvoicesService } from '../invoices.service';
import { Company } from 'src/app/model/company';
import { RxwebValidators } from '@rxweb/reactive-form-validators';
import { CompaniesService } from '../../companies/companies.service';
import { InvoiceRequest } from './invoice_request';

@Component({
  templateUrl: './invoice_form.component.html',
  providers: [MessageService, ConfirmationService],
})
export class InvoiceFormComponent implements OnInit {
  constructor(
    private router: Router,
    public route: ActivatedRoute,
    public utils: Utils,
    private invoicesService: InvoicesService,
    private companiesService: CompaniesService,
    private fb: FormBuilder
  ) {}

  id!: number;
  maxDate!: Date;
  invoice!: FormGroup;
  companies!: Company[];

  async ngOnInit() {
    this.maxDate = new Date();

    await this._initCompanies();

    this.route.params.subscribe((params) => {
      this.createFormClean();
      this.id = params['id'];
      if (this.id) {
        this._loadEntity(this.id);
      }
    });
  }

  async _initCompanies() {
    this.companiesService.list().subscribe((res) => {
      var companies = res.body!;
      this.companies = companies.filter((i) => i.inactivatedAt == null);
    });
  }

  createFormClean() {
    this.invoice = this.fb.group({
      company: [null, Validators.required],
      amount: [
        null,
        [
          RxwebValidators.numeric({ allowDecimal: true, isFormat: false }),
          Validators.required,
        ],
      ],
      number: [null, [Validators.pattern('^[0-9]*$'), Validators.required]],
      serviceDescription: [
        null,
        [Validators.minLength(3), Validators.required],
      ],
      competence: [null, Validators.required],
      paymentDate: [null, Validators.required],
    });
  }

  _loadingFormGroup(invoiceDTO: Invoice) {
    var month =
      invoiceDTO.competenceMonth < 10
        ? '0' + invoiceDTO.competenceMonth
        : invoiceDTO.competenceMonth;
    var dateCompetence =
      invoiceDTO.competenceYear + '-' + month + '-' + '01' + 'T00:00:00';

    this.invoice.controls['company'].setValue(invoiceDTO.company.id);
    this.invoice.controls['amount'].setValue(invoiceDTO.amount);
    this.invoice.controls['number'].setValue(invoiceDTO.number);
    this.invoice.controls['serviceDescription'].setValue(
      invoiceDTO.serviceDescription
    );
    this.invoice.controls['competence'].setValue(new Date(dateCompetence));
    this.invoice.controls['paymentDate'].setValue(
      new Date(invoiceDTO.paymentDate + 'T00:00:00')
    );
  }

  _loadEntity(id: number) {
    this.invoicesService.find(id).subscribe(
      (res) => {
        this._loadingFormGroup(res.body!);
      },
      (erro) => {
        console.error('erro', erro);
        this.utils.exibirErro('Erro ao buscar dados da nota fiscal: ' + id);
      }
    );
  }

  onSave() {
    if (this.invoice.valid) {
      var invoiceRequest = this._formToEntity();

      this.invoicesService.save(invoiceRequest, this.id).subscribe(
        (res) => {
          this.router.navigate(['/dashboard/invoices']);
          this.utils.exibirSucesso('Nota fiscal salva com sucesso!');
        },
        (erro) => {
          console.error('erro', erro);
          this.utils.exibirErroOperation('Salvar Nota Fiscal!');
        }
      );
    } else {
      this.utils.exibirErro('Preencha todos os campos!');
    }
  }

  _formToEntity() {
    var paymentDateIso = this.invoice.get('paymentDate')?.value;
    var month = paymentDateIso.getMonth() + 1;
    month = month < 10 ? '0' + month : month;
    var day =
      paymentDateIso.getDate() < 10
        ? '0' + paymentDateIso.getDate()
        : paymentDateIso.getDate();
    var paymentDate = paymentDateIso.getFullYear() + '-' + month + '-' + day;

    var invoiceRequest: InvoiceRequest = {
      number: this.invoice.get('number')?.value,
      amount: this.invoice.get('amount')?.value,
      serviceDescription: this.invoice.get('serviceDescription')?.value,
      competenceMonth: this.invoice.get('competence')!.value.getMonth() + 1,
      competenceYear: this.invoice.get('competence')!.value.getFullYear(),
      paymentDate: paymentDate,
      companyId: this.invoice.get('company')?.value,
    };

    return invoiceRequest;
  }
}
