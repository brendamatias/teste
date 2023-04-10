import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { InvoicesComponent } from './invoices.component';
import { InvoicesRoutingModule } from './invoices-routing.module';
import { SharedModule } from 'src/app/shared/shared.module';
import { InvoicesService } from './invoices.service';
import { InvoiceFormComponent } from './form/invoice_form.compoment';
import {  RxReactiveFormsModule } from "@rxweb/reactive-form-validators";

@NgModule({
	imports: [
		CommonModule,
		InvoicesRoutingModule,
		SharedModule,
		RxReactiveFormsModule,
	],
	declarations: [InvoicesComponent, InvoiceFormComponent],
	providers: [
		InvoicesService
	]
})
export class InvoicesModule { }
