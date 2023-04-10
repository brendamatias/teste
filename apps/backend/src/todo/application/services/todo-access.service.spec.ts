import { CreateUserUseCase, UserInMemoryRepository } from '../../../user';
import { UserNotFoundError } from '../../../user/domain/errors';
import { Todo, TodoNotFoundError } from '../../domain';
import { TodoAccessService } from './todo-access.service';

describe('TodoAccessService', () => {
  let todoAccessService: TodoAccessService;
  let createUserUseCase: CreateUserUseCase;
  let todo: Todo;
  const userId = '3e544b61-57e6-4764-b2e1-a03aa262b306';
  beforeEach(() => {
    const userRepository = new UserInMemoryRepository();
    todoAccessService = new TodoAccessService(userRepository);
    createUserUseCase = new CreateUserUseCase(userRepository);
    todo = Todo.create({
      title: 'Todo Title',
      ownerId: userId,
    });
  });

  it('should allow access to a todo', async () => {
    const userId = '3e544b61-57e6-4764-b2e1-a03aa262b306';
    const hasAccess = await todoAccessService.execute(todo, userId);
    expect(hasAccess).toBe(true);

    const createUserOutput = await createUserUseCase.execute({
      name: 'John Doe',
      email: 'jonh.doe@email.com',
      password: '12345678'
    });
    todo.shareWith('jonh.doe@email.com');

    const hasAccessShared = await todoAccessService.execute(todo, createUserOutput.userId);
    expect(hasAccessShared).toBe(true);
  });

  it('should throw an error when the user not exist', async () => {
    await (expect(todoAccessService.execute(todo, 'invalid-user-id')) as any).rejects.toThrow(new UserNotFoundError());
  });

  it('should throw an error when the user does not have access', async () => {
    const createUserOutput = await createUserUseCase.execute({
      name: 'John Doe',
      email: 'jonh.doe@emai..com',
      password: '12345678'
    });
    await (expect(todoAccessService.execute(todo, createUserOutput.userId)) as any).rejects.toThrow(new TodoNotFoundError(todo.id));
  });
});
