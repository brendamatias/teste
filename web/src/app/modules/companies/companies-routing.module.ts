import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CompaniesComponent } from './companies.component';
import { CompaniesFormComponent } from './form/companies_form.compoment';

@NgModule({
  imports: [
    RouterModule.forChild([
      { path: '', component: CompaniesComponent },
      { path: 'form', component: CompaniesFormComponent },
    ]),
  ],
  exports: [RouterModule],
})
export class CompaniesRoutingModule {}
