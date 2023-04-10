import { DomainError } from './base-error';
import { ErrorCodes } from './error-codes';

export class InvalidEntityError extends DomainError {
  constructor(message: string, code = ErrorCodes.DOMAIN_ERROR) {
    super(message, code);
  }
}
