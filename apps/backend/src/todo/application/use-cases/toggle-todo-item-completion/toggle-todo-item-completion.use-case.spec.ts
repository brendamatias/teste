import { UserInMemoryRepository } from '../../../../user';
import { Todo, TodoRepository } from '../../../domain';
import { TodoInMemoryRepository } from '../../../infra';
import { TodoAccessService } from '../../services';
import { AddTodoItemUseCase } from '../add-todo-item/add-todo-item.use-case';
import { CreateTodoUseCase } from '../create-todo/create-todo.use-case';
import { ToggleTodoItemCompletionUseCase } from './toggle-todo-item-completion.use-case';

describe('AddTodoItem Tests', () => {
  let completeTodoItemUseCase: ToggleTodoItemCompletionUseCase;
  let addTodoItemUseCase: AddTodoItemUseCase;
  let createTodoUseCase: CreateTodoUseCase;
  let todoRepository: TodoRepository;

  beforeEach(() => {
    const userRepository = new UserInMemoryRepository();
    const todoAccessService = new TodoAccessService(userRepository);
    todoRepository = new TodoInMemoryRepository();
    addTodoItemUseCase = new AddTodoItemUseCase(todoAccessService, todoRepository);
    createTodoUseCase = new CreateTodoUseCase(todoRepository);
    completeTodoItemUseCase = new ToggleTodoItemCompletionUseCase(todoAccessService, todoRepository);
  });

  it('should add a todo item', async () => {
    const { todoId } = await createTodoUseCase.execute({ title: 'Todo Title', userId: '123e4567-e89b-12d3-a456-426614174000' });

    const todoProps = await addTodoItemUseCase.execute({
      todoId,
      content: 'Content of the todo item',
      userId: '123e4567-e89b-12d3-a456-426614174000',
    });
    const todoItem = todoProps.items[0];
    await completeTodoItemUseCase.execute({
      todoId,
      todoItemId: todoItem.id,
      userId: '123e4567-e89b-12d3-a456-426614174000',
    });

    let todo = await todoRepository.getById(todoId) as Todo;
    expect(todo.items[0].isCompleted).toBeTruthy();

    await completeTodoItemUseCase.execute({
      todoId,
      todoItemId: todoItem.id,
      userId: '123e4567-e89b-12d3-a456-426614174000',
    });
    todo = await todoRepository.getById(todoId) as Todo;
    expect(todo.items[0].isCompleted).toBeFalsy();
  });
});
