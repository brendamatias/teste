import { Todo } from '../../../domain';
import { TodoInMemoryRepository } from '../../../infra';
import { CreateTodoUseCase } from './create-todo.use-case';

describe('CreateTodoListUseCase Tests', () => {
  let createTodoUseCase: CreateTodoUseCase;
  let todoInMemoryRepository: TodoInMemoryRepository;

  beforeEach(() => {
    todoInMemoryRepository = new TodoInMemoryRepository();
    createTodoUseCase = new CreateTodoUseCase(todoInMemoryRepository);
  });

  it('should create a todo', async () => {
    const { todoId } = await createTodoUseCase.execute({
      title: 'Todo Title',
      userId: '123e4567-e89b-12d3-a456-426614174000',
    });

    const todo = await todoInMemoryRepository.getById(todoId) as Todo;
    expect(todo).toBeDefined();
    expect(todo.toJSON()).toEqual({
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
