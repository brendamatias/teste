import { DomainError, ErrorCodes } from '../../../shared';

export class InvalidCredentialsError extends DomainError {

  constructor() {
    super('Invalid email or password', ErrorCodes.DOMAIN_USER_INVALID_CREDENTIALS_ERROR);
  }
}
