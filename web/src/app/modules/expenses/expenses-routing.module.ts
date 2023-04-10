import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ExpensesComponent } from './expenses.component';
import { ExpensesFormComponent } from './form/expenses_form.compoment';

@NgModule({
  imports: [
    RouterModule.forChild([
      { path: '', component: ExpensesComponent },
      { path: 'form', component: ExpensesFormComponent, },
    ]),
  ],
  exports: [RouterModule],
})
export class ExpensesRoutingModule {}
