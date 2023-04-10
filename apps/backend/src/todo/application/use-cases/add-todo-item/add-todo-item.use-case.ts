import { TodoNotFoundError, TodoProps, TodoRepository } from '../../../domain';
import { TodoAccessService } from '../../services';

export class AddTodoItemUseCase {
  constructor(
    private readonly todoAccessService: TodoAccessService,
    private readonly todoRepository: TodoRepository,
  ) { }

  public async execute(input: AddTodoItemInput): Promise<AddTodoItemOutput> {
    const todo = await this.todoRepository.getById(input.todoId);
    if (!todo) {
      throw new TodoNotFoundError(input.todoId);
    }

    await this.todoAccessService.execute(todo, input.userId);
    todo.addTodoItem(input.content);
    await this.todoRepository.update(todo);
    return todo.toJSON();
  }
}

type AddTodoItemInput = {
  todoId: string;
  content: string;
  userId: string;
};

type AddTodoItemOutput = TodoProps;
