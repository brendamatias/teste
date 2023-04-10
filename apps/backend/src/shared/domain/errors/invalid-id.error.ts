import { DomainError } from './base-error';
import { ErrorCodes } from './error-codes';

export class InvalidIdError extends DomainError {
  constructor(id: string) {
    super(`The id ${id} is invalid.`, ErrorCodes.DOMAIN_INVALID_ID_ERROR);
  }
}
