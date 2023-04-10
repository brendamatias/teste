import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { finalize, tap } from 'rxjs';
import { STORAGE_KEYS } from '../../../../core/utils/storage-keys';
import { InputComponent } from '../../../../shared/components/input/input.component';
import { LoadingService } from '../../../../shared/services/loading.service';
import { Todo, TodoService } from '../../services/todo.service';

@Component({
  selector: 'app-todo-list',
  standalone: true,
  imports: [
    InputComponent,
    FormsModule,
    ReactiveFormsModule,
    RouterLink,
  ],
  templateUrl: './todo-list.component.html',
  styleUrl: './todo-list.component.scss'
})
export class TodoListComponent implements OnInit {
  public readonly user = JSON.parse(localStorage.getItem(STORAGE_KEYS.USER) as string);

  public todos: Todo[] = [];
  public newTodoTitle = '';

  public emailToShare = '';
  public isModalOpen = false;
  public todoToShare: Todo | null = null;
  public form: FormGroup;

  constructor(
    private readonly fb: FormBuilder,
    private readonly loadingService: LoadingService,
    private readonly todoService: TodoService,
  ) {
    this.form = this.fb.group({
      email: [null, [Validators.required, Validators.email]],
    });
  }

  ngOnInit(): void {
    this.loadingService.present();
    this.todoService.getTodos()
      .pipe(
        finalize(() => this.loadingService.dismiss())
      )
      .subscribe(todos => {
        this.todos = todos;
      });
  }

  public openShareModal(todo: Todo): void {
    this.todoToShare = todo;
    this.isModalOpen = true;
  }

  public closeShareModal(): void {
    this.isModalOpen = false;
    this.emailToShare = '';
  }

  public shareTodo(): void {
    this.form.markAllAsTouched();
    if (this.form.invalid) {
      return;
    }

    this.loadingService.present();
    const todo = this.todoToShare as Todo;
    this.todoService.shareTodoWithUser(todo.id, this.form.value.email)
      .pipe(
        tap(() => this.closeShareModal()),
        finalize(() => this.loadingService.dismiss())
      )
      .subscribe();
  }


  public addTodo(): void {
    this.loadingService.present();
    this.todoService.createTodo(this.newTodoTitle)
      .pipe(
        tap(todos => console.log(todos)),
        tap(todos => this.todos = todos),
        finalize(() => {
          this.loadingService.dismiss();
          this.newTodoTitle = '';
        })
      )
      .subscribe();
  }
}
