import { UserRepository } from '../../../../user';
import { UserNotFoundError } from '../../../../user/domain/errors';
import { TodoNotFoundError, TodoRepository } from '../../../domain';

export class ShareTodoUseCase {
  constructor(
    private readonly todoRepository: TodoRepository,
    private readonly userRepository: UserRepository,
  ) { }

  public async execute(input: ShareTodoInput): Promise<ShareTodoOutput> {
    const todo = await this.todoRepository.getById(input.todoId);
    if (!todo || todo.ownerId !== input.userId) {
      throw new TodoNotFoundError(input.todoId);
    }

    const user = await this.userRepository.getByEmail(input.emailToShare);
    if (!user) {
      throw new UserNotFoundError();
    }

    todo.shareWith(input.emailToShare);
    await this.todoRepository.update(todo);
  }
}

type ShareTodoInput = {
  todoId: string;
  userId: string;
  emailToShare: string;
};

type ShareTodoOutput = void;
