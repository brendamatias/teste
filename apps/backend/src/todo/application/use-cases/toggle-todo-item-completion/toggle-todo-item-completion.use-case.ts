import { TodoNotFoundError, TodoRepository } from '../../../domain';
import { TodoAccessService } from '../../services';

export class ToggleTodoItemCompletionUseCase {
  constructor(
    private readonly todoAccessService: TodoAccessService,
    private readonly todoRepository: TodoRepository,
  ) { }

  public async execute(input: ToggleTodoItemCompletionInput): Promise<ToggleTodoItemCompletionOutput> {
    const todo = await this.todoRepository.getById(input.todoId);
    if (!todo) {
      throw new TodoNotFoundError(input.todoId);
    }

    await this.todoAccessService.execute(todo, input.userId);
    const todoItem = todo.items.find((item) => item.id === input.todoItemId);
    if (!todoItem) {
      throw new Error('Todo item not found');
    }

    if (todoItem.isCompleted) {
      todoItem.uncomplete();
    } else {
      todoItem.complete();
    }

    await this.todoRepository.update(todo);
  }
}

type ToggleTodoItemCompletionInput = {
  todoId: string;
  todoItemId: string;
  userId: string;
};

type ToggleTodoItemCompletionOutput = void;
