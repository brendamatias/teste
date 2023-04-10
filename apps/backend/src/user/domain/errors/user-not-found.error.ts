import { DomainError, ErrorCodes } from '../../../shared';

export class UserNotFoundError extends DomainError {
  constructor() {
    super(`User not found`, ErrorCodes.DOMAIN_USER_NOT_FOUND_ERROR);
  }
}
