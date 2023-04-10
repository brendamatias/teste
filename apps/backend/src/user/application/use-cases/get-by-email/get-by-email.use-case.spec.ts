import { User } from '../../../domain';
import { UserInMemoryRepository } from '../../../infra';
import { CreateUserUseCase } from '../create-user/create-user.use-case';
import { GetByEmailUseCase } from './get-by-email.use-case';

describe('GetByEmailUseCase Tests', () => {
  let createUserUseCase: CreateUserUseCase;
  let getByEmailUseCase: GetByEmailUseCase;
  let userInMemoryRepository: UserInMemoryRepository;

  beforeEach(() => {
    userInMemoryRepository = new UserInMemoryRepository();
    createUserUseCase = new CreateUserUseCase(userInMemoryRepository);
    getByEmailUseCase = new GetByEmailUseCase(userInMemoryRepository);
  });

  it('should return a user by email', async () => {
    await createUserUseCase.execute({
      name: 'John Doe',
      email: 'jonh.doe@email.com',
      password: 'password',
    });

    const user = await getByEmailUseCase.execute({ email: 'jonh.doe@email.com' }) as User;
    expect(user).toBeDefined();
    expect(user.name).toBe('John Doe');
    expect(user.email).toBe('jonh.doe@email.com');
  });

  it('should return null when the user does not exist', async () => {
    const user = await getByEmailUseCase.execute({ email: 'jonh.doe@email.com' });
    expect(user).toBeNull();
  });
});
