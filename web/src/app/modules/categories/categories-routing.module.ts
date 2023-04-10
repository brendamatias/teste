import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CategoriesComponent } from './categories.component';
import { CategoriesFormComponent } from './form/categories_form.compoment';

@NgModule({
  imports: [
    RouterModule.forChild([
      { path: '', component: CategoriesComponent },
      { path: 'form', component: CategoriesFormComponent },
    ]),
  ],
  exports: [RouterModule],
})
export class CategoriesRoutingModule {}
