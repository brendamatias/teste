import { TodoNotFoundError, TodoProps, TodoRepository } from '../../../domain';
import { TodoAccessService } from '../../services';

export class GetTodoUseCase {
  constructor(
    private readonly todoAccessService: TodoAccessService,
    private readonly todoRepository: TodoRepository,
  ) { }

  public async execute(input: GetTodoInput): Promise<GetTodoOutput> {
    const todo = await this.todoRepository.getById(input.todoId);
    if (!todo) {
      throw new TodoNotFoundError(input.todoId);
    }

    await this.todoAccessService.execute(todo, input.userId);
    return todo.toJSON();
  }
}

type GetTodoInput = {
  todoId: string;
  userId: string;
};

type GetTodoOutput = TodoProps;
