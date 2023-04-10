import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { environment } from 'src/environments/environment';
import { NotfoundComponent } from './core/components/notfound/notfound.component';
import { ModulesModule } from './modules/modules.module';
import { AppLayoutComponent } from './layout/app.layout.component';
import { AuthTokenGuard } from './core/config/authToken.guard';

const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  {
    path: 'dashboard',
    component: AppLayoutComponent,
    children: [
      {
        path: '',
        loadChildren: () =>
          import('./modules/modules.module').then(
            (m) => m.ModulesModule
          ),
      },
    ],
    canActivate: [AuthTokenGuard]
  },
  {
    path: 'login',
    loadChildren: () =>
      import('./core/components/auth/auth.module').then((m) => m.AuthModule),
  },
  { path: 'notfound', component: NotfoundComponent, pathMatch: 'full'},
  { path: '**', component: NotfoundComponent, pathMatch: 'full' },
];

@NgModule({
  imports: [
    RouterModule.forRoot(routes, {
      scrollPositionRestoration: 'enabled',
      anchorScrolling: 'enabled',
      enableTracing: !environment.production,
    }),
  ],
  exports: [RouterModule],
})
export class AppRoutingModule {}
