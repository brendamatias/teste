import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  {
    path: '',
    data: { breadcrumb: 'Dashboard' },
    loadChildren: () =>
      import('./dashboard/dashboard.module').then((m) => m.DashboardModule),
  },
  {
    path: 'invoices',
    data: { breadcrumb: 'Notas Fiscais' },
    loadChildren: () =>
      import('./invoices/invoices.module').then((m) => m.InvoicesModule),
  },
  {
    path: 'expenses',
    data: { breadcrumb: 'Depesas' },
    loadChildren: () =>
      import('./expenses/expenses.module').then((m) => m.ExpensesModule),
  },
  {
    path: 'companies',
    data: { breadcrumb: 'Empresas' },
    loadChildren: () =>
      import('./companies/companies.module').then((m) => m.CompaniesModule),
  },
  {
    path: 'categories',
    data: { breadcrumb: 'Categorias' },
    loadChildren: () =>
      import('./categories/categories.module').then((m) => m.CategoriesModule),
  },
  {
    path: 'preferences',
    data: { breadcrumb: 'Preferências' },
    loadChildren: () =>
      import('./preferences/preferences.module').then((m) => m.PreferencesModule),
  },
  { path: '**', redirectTo: '/notfound', pathMatch: 'full' },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class ModulesRoutingModule {}
