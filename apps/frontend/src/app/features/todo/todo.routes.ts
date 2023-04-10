import { Routes } from '@angular/router';
import { TodoDetailComponent } from './pages/todo-detail/todo-detail.component';
import { TodoListComponent } from './pages/todo-list/todo-list.component';

export const TODO_ROUTES: Routes = [
  { path: '', component: TodoListComponent },
  { path: ':todoId', component: TodoDetailComponent },
]
