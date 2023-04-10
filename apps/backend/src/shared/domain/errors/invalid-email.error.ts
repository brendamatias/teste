import { DomainError } from './base-error';
import { ErrorCodes } from './error-codes';

export class InvalidEmailError extends DomainError {
  constructor(email: string) {
    super(`The email ${email} is invalid.`, ErrorCodes.DOMAIN_INVALID_EMAIL_ERROR);
  }
}
