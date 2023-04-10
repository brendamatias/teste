import { DomainError, ErrorCodes } from '../../../shared';

export class InvalidUserError extends DomainError {
  constructor(message: string) {
    super(message, ErrorCodes.DOMAIN_USER_INVALID_ERROR);
  }
}
