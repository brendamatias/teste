import { TodoNotFoundError, TodoProps, TodoRepository } from '../../../domain';
import { TodoAccessService } from '../../services';

export class ChangeOrderTodoItemUseCase {
  constructor(
    private readonly todoAccessService: TodoAccessService,
    private readonly todoRepository: TodoRepository,
  ) { }

  public async execute(input: ChangeOrderTaskInput): Promise<ChangeOrderTaskOutput> {
    const todo = await this.todoRepository.getById(input.todoId);
    if (!todo) {
      throw new TodoNotFoundError(input.todoId);
    }

    await this.todoAccessService.execute(todo, input.userId);
    const itemToMove = todo.items.find((item) => item.id === input.todoItemId);
    if (!itemToMove) {
      throw new Error('Todo item not found');
    }

    const originalOrder = itemToMove.order;
    itemToMove.updateOrder(input.newOrder);

    for (const item of todo.items) {
      if (item.id !== itemToMove.id) {
        const inferiorLimit = Math.min(originalOrder, input.newOrder);
        const superiorLimit = Math.max(originalOrder, input.newOrder);
        const isBetween = item.order >= inferiorLimit && item.order <= superiorLimit;
        if (isBetween) {
          item.updateOrder(originalOrder < input.newOrder ? item.order - 1 : item.order + 1);
        }
      }
    }

    await this.todoRepository.update(todo);
    return todo.toJSON();
  }
}

type ChangeOrderTaskInput = {
  todoId: string;
  todoItemId: string;
  userId: string;
  newOrder: number;
};

type ChangeOrderTaskOutput = TodoProps;
