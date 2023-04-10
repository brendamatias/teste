import { TodoNotFoundError, TodoRepository } from '../../../domain';
import { TodoAccessService } from '../../services';

export class UpdateTodoItemUseCase {
  constructor(
    private readonly todoAccessService: TodoAccessService,
    private readonly todoRepository: TodoRepository,
  ) { }

  public async execute(input: UpdateTodoItemInput): Promise<UpdateTodoItemOutput> {
    const todo = await this.todoRepository.getById(input.todoId);
    if (!todo) {
      throw new TodoNotFoundError(input.todoId);
    }

    await this.todoAccessService.execute(todo, input.userId);

    const itemToUpdate = todo.items.find((item) => item.id === input.todoItemId);
    if (!itemToUpdate) {
      throw new Error('Todo item not found');
    }

    itemToUpdate.updateContent(input.content);
    await this.todoRepository.update(todo);
  }
}

type UpdateTodoItemInput = {
  todoId: string;
  todoItemId: string;
  content: string;
  userId: string;
};

type UpdateTodoItemOutput = void;
