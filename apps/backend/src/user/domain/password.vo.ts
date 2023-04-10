import * as crypto from 'crypto';
import { ValueObject } from '../../shared';
import { InvalidPasswordError } from './errors';

const SALT_LENGTH = 16;
const HASH_ITERATIONS = 10000;
const KEY_LENGTH = 64;
const DIGEST = 'sha256';
export class Password extends ValueObject {


  private constructor(public readonly value: string) {
    super();
  }

  public static create(plainPassword: string): Password {
    Password.validatePassword(plainPassword);
    const hashPassword = Password.hashPassword(plainPassword);
    const password = new Password(hashPassword);
    return password;
  }

  private static validatePassword(plainPassword: string): void {
    if (!plainPassword) {
      throw new InvalidPasswordError('Password is required');
    }

    if (plainPassword.length < 8) {
      throw new InvalidPasswordError('Password must have at least 8 characters');
    }

    if (plainPassword.length > 16) {
      throw new InvalidPasswordError('Password must have at most 16 characters');
    }

    if (typeof plainPassword !== 'string') {
      throw new InvalidPasswordError('Password must be a string');
    }
  }

  private static hashPassword(plainPassword: string): string {
    const salt = crypto.randomBytes(SALT_LENGTH).toString('hex');
    const hash = Password.hashWithSalt(plainPassword, salt);
    return `${salt}:${hash}`;
  }

  private static hashWithSalt(plainPassword: string, salt: string): string {
    return crypto
      .pbkdf2Sync(plainPassword, salt, HASH_ITERATIONS, KEY_LENGTH, DIGEST)
      .toString('hex');
  }

  public static restore(hashPassword: string): Password {
    const validHash = Password.isValidHash(hashPassword);
    if (!validHash) {
      throw new InvalidPasswordError('Invalid password hash format');
    }

    return new Password(hashPassword);
  }

  private static isValidHash(hashedPassword: string): boolean {
    return /^[a-f0-9]{32}:[a-f0-9]{128}$/.test(hashedPassword);
  }

  public matches(plainPassword: string): boolean {
    const [salt, storedHash] = this.value.split(':');
    const hash = Password.hashWithSalt(plainPassword, salt);
    return storedHash === hash;
  }
}
