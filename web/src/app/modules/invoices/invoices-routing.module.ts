import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { InvoicesComponent } from './invoices.component';
import { InvoiceFormComponent } from './form/invoice_form.compoment';

@NgModule({
  imports: [
    RouterModule.forChild([
      { path: '', component: InvoicesComponent },
      { path: 'form', component: InvoiceFormComponent, },
    ]),
  ],
  exports: [RouterModule],
})
export class InvoicesRoutingModule {}
