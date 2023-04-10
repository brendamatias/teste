import { InvalidCredentialsError } from '../../../domain/errors';
import { UserInMemoryRepository } from '../../../infra';
import { TokenService } from '../../services';
import { CreateUserUseCase } from '../create-user/create-user.use-case';
import { LoginUseCase } from './login.use-case';

describe('LoginUseCase', () => {
  let loginUseCase: LoginUseCase;
  let createUserUseCase: CreateUserUseCase;
  let tokenService: jest.Mocked<TokenService>;

  const validEmail = 'test@example.com';
  const validPassword = 'valid_password';
  const validToken = 'valid_token';

  beforeEach(() => {
    const userRepository = new UserInMemoryRepository();
    tokenService = {
      generateToken: jest.fn(),
    } as unknown as jest.Mocked<TokenService>;

    createUserUseCase = new CreateUserUseCase(userRepository);
    loginUseCase = new LoginUseCase(userRepository, tokenService);
  });

  it('should return a token for valid credentials', async () => {
    const { userId } = await createUserUseCase.execute({ email: validEmail, password: validPassword, name: 'Test User' });
    tokenService.generateToken.mockReturnValue(validToken);

    const result = await loginUseCase.execute({ email: validEmail, password: validPassword });

    expect(result).toEqual({
      userId,
      email: validEmail,
      name: 'Test User',
      token: validToken
    });
  });

  it('should throw an error for invalid credentials', async () => {
    await (expect(loginUseCase.execute({ email: validEmail, password: validPassword })) as any).rejects.toThrowError(new InvalidCredentialsError());
    await createUserUseCase.execute({ email: validEmail, password: validPassword, name: 'Test User' });
    await (expect(loginUseCase.execute({ email: validEmail, password: 'invalid-password' })) as any).rejects.toThrowError(new InvalidCredentialsError());
  });
});
