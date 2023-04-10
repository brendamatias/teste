import { DomainError, ErrorCodes } from '../../../shared';

export class TodoItemNotFoundError extends DomainError {
  constructor(todoId: string, todoItemId: string) {
    super(`Todo item not found: TodoId: ${todoId}, TodoItemId: ${todoItemId}`, ErrorCodes.DOMAIN_TODO_ITEM_NOT_FOUND_ERROR);
  }
}
