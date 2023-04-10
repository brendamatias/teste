import { ErrorCodes } from '../../../shared';
import { InvalidEntityError } from '../../../shared/domain/errors/invalid-entity.error';

export class TodoItemInvalidError extends InvalidEntityError {
  constructor(message: string) {
    super(message, ErrorCodes.DOMAIN_TODO_ITEM_INVALID_ERROR);
  }
}
