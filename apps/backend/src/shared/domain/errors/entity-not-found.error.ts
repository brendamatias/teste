import { DomainError } from './base-error';
import { ErrorCodes } from './error-codes';

export class EntityNotFoundError extends DomainError {
  constructor(entityName: string, id: string) {
    super(`${entityName} with id ${id} not found`, ErrorCodes.DOMAIN_ENTITY_NOT_FOUND_ERROR);
  }
}
