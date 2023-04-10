import { ErrorCodes } from './error-codes';

export abstract class BaseError extends Error {
  public readonly timestamp: Date;

  constructor(message: string, readonly code = ErrorCodes.BASE_ERROR, readonly payload?: any) {
    super(message);
    this.timestamp = new Date();
  }

  public toJSON() {
    return {
      message: this.message,
      code: this.code,
      timestamp: this.timestamp,
      payload: this.payload,
    };
  }
}

export class DomainError extends BaseError {
  constructor(message: string, code = ErrorCodes.DOMAIN_ERROR, payload?: any) {
    super(message, code, payload);
  }
}

export class ApplicationError extends BaseError {
  constructor(message: string, code = ErrorCodes.APPLICATION_ERROR, payload?: any) {
    super(message, code, payload);
  }
}

export class InfrastructureError extends BaseError {
  constructor(message: string, code = ErrorCodes.INFRASTRUCTURE_ERROR, payload?: any) {
    super(message, code, payload);
  }
}
