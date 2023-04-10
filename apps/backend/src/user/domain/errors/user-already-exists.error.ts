import { DomainError, ErrorCodes } from '../../../shared';

export class UserAlreadyExistsError extends DomainError {
  constructor(email: string) {
    super(`User with email ${email} already exists`, ErrorCodes.DOMAIN_USER_ALREADY_EXISTS_ERROR);
    this.name = 'UserAlreadyExistsError';
  }
}
