import { TodoItemNotFoundError, TodoNotFoundError, TodoRepository } from '../../../domain';
import { TodoAccessService } from '../../services';

export class DeleteTodoItemUseCase {
  constructor(
    private readonly todoAccessService: TodoAccessService,
    private readonly todoRepository: TodoRepository,
  ) { }

  public async execute(input: DeleteTodoItemInput): Promise<DeleteTodoItemOutput> {
    const todo = await this.todoRepository.getById(input.todoId);
    if (!todo) {
      throw new TodoNotFoundError(input.todoId);
    }

    await this.todoAccessService.execute(todo, input.userId);
    const item = todo.items.find((item) => item.id === input.itemId);
    if (!item) {
      throw new TodoItemNotFoundError(todo.id, input.itemId);
    }

    await this.todoRepository.deleteTodoItem(todo, item.id);
  }
}

type DeleteTodoItemInput = {
  todoId: string;
  itemId: string;
  userId: string;
};

type DeleteTodoItemOutput = void;
