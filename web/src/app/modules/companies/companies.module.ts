import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CompaniesComponent } from './companies.component';
import { CompaniesRoutingModule } from './companies-routing.module';
import { SharedModule } from 'src/app/shared/shared.module';
import { CompaniesService } from './companies.service';
import {  RxReactiveFormsModule } from "@rxweb/reactive-form-validators";
import { CompaniesFormComponent } from './form/companies_form.compoment';

@NgModule({
	imports: [
		CommonModule,
		CompaniesRoutingModule,
		SharedModule,
		RxReactiveFormsModule,
	],
	declarations: [CompaniesComponent, CompaniesFormComponent],
	providers: [
		CompaniesService
	]
})
export class CompaniesModule { }
