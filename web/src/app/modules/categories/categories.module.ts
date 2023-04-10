import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CategoriesComponent } from './categories.component';
import { CategoriesRoutingModule } from './categories-routing.module';
import { SharedModule } from 'src/app/shared/shared.module';
import { CategoriesService } from './categories.service';
import { RxReactiveFormsModule } from "@rxweb/reactive-form-validators";
import { CategoriesFormComponent } from './form/categories_form.compoment';

@NgModule({
	imports: [
		CommonModule,
		CategoriesRoutingModule,
		SharedModule,
		RxReactiveFormsModule,
	],
	declarations: [CategoriesComponent, CategoriesFormComponent],
	providers: [
		CategoriesService
	]
})
export class CategoriesModule { }
