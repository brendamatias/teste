import { UserInMemoryRepository } from '../../../../user';
import { Todo, TodoRepository } from '../../../domain';
import { TodoInMemoryRepository } from '../../../infra';
import { TodoAccessService } from '../../services';
import { AddTodoItemUseCase } from '../add-todo-item/add-todo-item.use-case';
import { CreateTodoUseCase } from '../create-todo/create-todo.use-case';
import { DeleteTodoItemUseCase } from './delete-todo-item.use-case';

describe('DeleteTodoItemUseCase Tests', () => {
  let addTodoItemUseCase: AddTodoItemUseCase;
  let deleteTodoItemUseCase: DeleteTodoItemUseCase;
  let createTodoUseCase: CreateTodoUseCase;
  let todoRepository: TodoRepository;

  beforeEach(() => {
    const userRepository = new UserInMemoryRepository();
    const todoAccessService = new TodoAccessService(userRepository);
    todoRepository = new TodoInMemoryRepository();
    addTodoItemUseCase = new AddTodoItemUseCase(todoAccessService, todoRepository);
    deleteTodoItemUseCase = new DeleteTodoItemUseCase(todoAccessService, todoRepository);
    createTodoUseCase = new CreateTodoUseCase(todoRepository);
  });

  it('should delete a todo item', async () => {
    const { todoId } = await createTodoUseCase.execute({ title: 'Todo Title', userId: '123e4567-e89b-12d3-a456-426614174000' });

    await addTodoItemUseCase.execute({
      todoId,
      content: 'Content of the todo item',
      userId: '123e4567-e89b-12d3-a456-426614174000',
    });

    let todo = await todoRepository.getById(todoId) as Todo;
    const [item] = todo.items;
    await deleteTodoItemUseCase.execute({
      todoId,
      itemId: item.id,
      userId: '123e4567-e89b-12d3-a456-426614174000',
    });

    todo = await todoRepository.getById(todoId) as Todo;
    expect(todo).toBeDefined();
    expect(todo.items.length).toBe(0);
  });
});
