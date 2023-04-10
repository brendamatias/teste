import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ExpensesComponent } from './expenses.component';
import { ExpensesRoutingModule } from './expenses-routing.module';
import { SharedModule } from 'src/app/shared/shared.module';
import { ExpensesService } from './expenses.service';
import { ExpensesFormComponent } from './form/expenses_form.compoment';
import {  RxReactiveFormsModule } from "@rxweb/reactive-form-validators";

@NgModule({
	imports: [
		CommonModule,
		ExpensesRoutingModule,
		SharedModule,
		RxReactiveFormsModule,
	],
	declarations: [ExpensesComponent, ExpensesFormComponent],
	providers: [
		ExpensesService
	]
})
export class ExpensesModule { }
