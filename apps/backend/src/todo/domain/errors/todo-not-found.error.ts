import { DomainError, ErrorCodes } from '../../../shared';

export class TodoNotFoundError extends DomainError {
  constructor(todoId: string) {
    super(`Todo not found: TodoId: ${todoId}`, ErrorCodes.DOMAIN_TODO_NOT_FOUND_ERROR);
  }
}
