import { User, UserRepository } from '../../../domain';
import { UserAlreadyExistsError } from '../../../domain/errors';

export class CreateUserUseCase {
  constructor(private readonly userRepository: UserRepository) { }

  public async execute(input: CreateUserInput): Promise<CreateUserOutput> {
    const user = await this.userRepository.getByEmail(input.email);
    if (user) {
      throw new UserAlreadyExistsError(input.email);
    }

    const newUser = User.create(input);
    await this.userRepository.create(newUser);
    return { userId: newUser.id };
  }
}

type CreateUserInput = {
  name: string;
  email: string;
  password: string;
};

type CreateUserOutput = {
  userId: string;
};
