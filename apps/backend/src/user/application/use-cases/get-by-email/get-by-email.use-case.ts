import { UserRepository } from '../../../domain';

export class GetByEmailUseCase {
  constructor(private readonly userRepository: UserRepository) { }

  public async execute(input: GetByEmailInput): Promise<GetByEmalOutput> {
    const user = await this.userRepository.getByEmail(input.email);
    if (!user) {
      return null;
    }

    return {
      id: user.id,
      name: user.name,
      email: user.email,
      createdAt: user.createdAt,
      updatedAt: user.updatedAt,
    };
  }
}

type GetByEmailInput = {
  email: string;
};

type GetByEmalOutput = {
  id: string;
  name: string;
  email: string;
  createdAt: Date;
  updatedAt: Date;
} | null;
