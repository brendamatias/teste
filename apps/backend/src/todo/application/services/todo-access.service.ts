import { UserRepository } from '../../../user';
import { UserNotFoundError } from '../../../user/domain/errors';
import { Todo, TodoNotFoundError } from '../../domain';

export class TodoAccessService {
  constructor(private readonly userRepository: UserRepository) { }

  public async execute(todo: Todo, userId: string): Promise<boolean> {
    if (todo.hasAccess(userId)) {
      return true;
    }

    const user = await this.userRepository.getById(userId);
    if (!user) {
      throw new UserNotFoundError();
    }

    if (!todo.hasAccess(user.email)) {
      throw new TodoNotFoundError(todo.id);
    }

    return true;
  }
}
