import { DomainError, ErrorCodes } from '../../../shared';

export class InvalidPasswordError extends DomainError {
  constructor(message: string) {
    super(message, ErrorCodes.DOMAIN_PASSWORD_INVALID_ERROR);
  }
}
