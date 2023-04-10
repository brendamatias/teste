import { UserInMemoryRepository } from '../../../../user';
import { TodoInMemoryRepository } from '../../../infra';
import { TodoAccessService } from '../../services';
import { CreateTodoUseCase } from '../create-todo/create-todo.use-case';
import { GetTodoUseCase } from './get-todo.use-case';

describe('CreateTodoListUseCase Tests', () => {
  let createTodoUseCase: CreateTodoUseCase;
  let getTodoUseCase: GetTodoUseCase;
  let todoInMemoryRepository: TodoInMemoryRepository;

  beforeEach(() => {
    const userRepository = new UserInMemoryRepository();
    const todoAccessService = new TodoAccessService(userRepository);
    todoInMemoryRepository = new TodoInMemoryRepository();
    createTodoUseCase = new CreateTodoUseCase(todoInMemoryRepository);
    getTodoUseCase = new GetTodoUseCase(todoAccessService, todoInMemoryRepository);
  });

  it('should create a todo', async () => {
    const { todoId } = await createTodoUseCase.execute({
      title: 'Todo Title',
      userId: '123e4567-e89b-12d3-a456-426614174000',
    });

    const todo = await getTodoUseCase.execute({ todoId, userId: '123e4567-e89b-12d3-a456-426614174000' });
    expect(todo).toBeDefined();
    expect(todo).toEqual({
      id: todo.id,
      items: [],
      title: 'Todo Title',
      ownerId: '123e4567-e89b-12d3-a456-426614174000',
      sharedWith: [],
      createdAt: todo.createdAt,
      updatedAt: todo.updatedAt,
    });
  });
});
