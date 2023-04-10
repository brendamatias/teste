import { InvalidEmailError } from '../errors';
import { Email } from './email.vo';

describe('Email Value Object Unit Test', () => {
  it('should create a valid Email value object with a given value', () => {
    const email = new Email('email@test.com');
    expect(email.value).toBe('email@test.com');
  });

  test.each([
    'invalid-email',
    '',
    null,
    undefined,
    1234,
    {},
    [],
    false,
    true,
    'test@email'
  ])('should throw an error when creating an Email value object with an invalid value: %p', (value: any) => {
    expect(() => new Email(value)).toThrow(new InvalidEmailError(value));
  });
});
