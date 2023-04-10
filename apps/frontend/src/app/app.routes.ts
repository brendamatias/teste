import { Routes } from '@angular/router';
import { AuthenticatedLayoutComponent } from './core/components/authenticated-layout/authenticated-layout.component';
import { authGuard } from './core/guards/auth.guard';
import { isLoggedGuard } from './core/guards/is-logged.guard';
import { LoginComponent } from './features/auth/pages/login/login.component';

export const routes: Routes = [
  { path: 'login', canActivate: [isLoggedGuard], component: LoginComponent },
  {
    path: '', component: AuthenticatedLayoutComponent, canActivate: [authGuard], children: [
      { path: '', loadChildren: () => import('./features/todo/todo.routes').then(r => r.TODO_ROUTES), },
    ]
  },

];
