import { CreateUserUseCase, UserInMemoryRepository, UserRepository } from '../../../../user';
import { Todo } from '../../../domain';
import { TodoInMemoryRepository } from '../../../infra';
import { CreateTodoUseCase } from '../create-todo/create-todo.use-case';
import { ShareTodoUseCase } from './share-todo.use-case';

describe('CreateTodoListUseCase Tests', () => {
  let createTodoUseCase: CreateTodoUseCase;
  let shareTodoUseCase: ShareTodoUseCase;
  let todoInMemoryRepository: TodoInMemoryRepository;
  let userRepository: UserRepository;
  let createUserUseCase: CreateUserUseCase;

  beforeEach(() => {
    todoInMemoryRepository = new TodoInMemoryRepository();
    userRepository = new UserInMemoryRepository();
    createTodoUseCase = new CreateTodoUseCase(todoInMemoryRepository);
    createUserUseCase = new CreateUserUseCase(userRepository);
    shareTodoUseCase = new ShareTodoUseCase(todoInMemoryRepository, userRepository);
  });

  it('should create a todo', async () => {
    const { userId } = await createUserUseCase.execute({
      name: 'John Doe',
      email: 'jonh.doe@email.com',
      password: '12345678',
    });

    await createUserUseCase.execute({
      name: 'Jone Doe',
      email: 'jane.doe@email.com',
      password: '87654321',
    });

    const { todoId } = await createTodoUseCase.execute({
      title: 'Todo Title',
      userId,
    });

    await shareTodoUseCase.execute({
      emailToShare: 'jane.doe@email.com',
      todoId,
      userId,
    });

    const todo = await todoInMemoryRepository.getById(todoId) as Todo;
    expect(todo.sharedWith.length).toBe(1);
    expect(todo.sharedWith[0]).toBe('jane.doe@email.com');
  });
});
