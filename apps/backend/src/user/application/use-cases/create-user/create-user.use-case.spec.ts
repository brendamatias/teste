import { User } from '../../../domain';
import { UserAlreadyExistsError } from '../../../domain/errors';
import { UserInMemoryRepository } from '../../../infra';
import { CreateUserUseCase } from './create-user.use-case';

describe('CreateUserUseCase Tests', () => {
  let createUserUseCase: CreateUserUseCase;
  let userInMemoryRepository: UserInMemoryRepository;

  beforeEach(() => {
    userInMemoryRepository = new UserInMemoryRepository();
    createUserUseCase = new CreateUserUseCase(userInMemoryRepository);
  });

  it('should create a new user', async () => {
    const { userId } = await createUserUseCase.execute({
      name: 'John Doe',
      email: 'jonh.doe@email.com',
      password: 'password',
    });

    const user = await userInMemoryRepository.getById(userId) as User;
    expect(user).toBeDefined();
    expect(user.name).toBe('John Doe');
    expect(user.email).toBe('jonh.doe@email.com');
  });

  it('should throw an error when creating a user with an existing email', async () => {
    const input = {
      name: 'John Doe',
      email: 'jonh.doe@email.com',
      password: 'password',
    };

    await createUserUseCase.execute(input);
    await (expect(createUserUseCase.execute(input)) as any).rejects.toThrow(new UserAlreadyExistsError(input.email));
  });
});
