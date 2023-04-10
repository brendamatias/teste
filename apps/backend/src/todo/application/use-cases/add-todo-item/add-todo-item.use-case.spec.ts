import { UserInMemoryRepository } from '../../../../user';
import { Todo, TodoRepository } from '../../../domain';
import { TodoInMemoryRepository } from '../../../infra';
import { TodoAccessService } from '../../services';
import { CreateTodoUseCase } from '../create-todo/create-todo.use-case';
import { AddTodoItemUseCase } from './add-todo-item.use-case';

describe('AddTodoItem Tests', () => {
  let addTodoItemUseCase: AddTodoItemUseCase;
  let createTodoUseCase: CreateTodoUseCase;
  let todoRepository: TodoRepository;

  beforeEach(() => {
    const userRepository = new UserInMemoryRepository();
    const todoAccessService = new TodoAccessService(userRepository);
    todoRepository = new TodoInMemoryRepository();
    addTodoItemUseCase = new AddTodoItemUseCase(todoAccessService, todoRepository);
    createTodoUseCase = new CreateTodoUseCase(todoRepository);
  });

  it('should add a todo item', async () => {
    const { todoId } = await createTodoUseCase.execute({ title: 'Todo Title', userId: '123e4567-e89b-12d3-a456-426614174000' });

    await addTodoItemUseCase.execute({
      todoId,
      content: 'Content of the todo item',
      userId: '123e4567-e89b-12d3-a456-426614174000',
    });

    const todo = await todoRepository.getById(todoId) as Todo;
    expect(todo).toBeDefined();
    expect(todo.items.length).toBe(1);
    expect(todo.items[0].content).toBe('Content of the todo item');
  });
});
