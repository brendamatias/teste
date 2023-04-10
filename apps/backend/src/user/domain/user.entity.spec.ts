import { InvalidEmailError } from '../../shared';
import { InvalidPasswordError, InvalidUserError } from './errors';
import { User } from './user.entity';

describe('User Entity', () => {
  const validEmail = 'test@example.com';
  const validPassword = 'valid_password';
  const validName = 'John Doe';

  it('should create a valid user entity', () => {
    const user = User.create({
      name: validName,
      email: validEmail,
      password: validPassword,
    });

    expect(user).toBeInstanceOf(User);
    expect(user.name).toBe(validName);
    expect(user.email).toBe(validEmail);
    expect(user.password).toMatch(/^[a-f0-9]{32}:[a-f0-9]{128}$/); // Verifica o formato salt:hash
  });

  it('should throw an error if the name is invalid', () => {
    expect(() => User.create({ name: '', email: validEmail, password: validPassword }))
      .toThrow(new InvalidUserError('Name is required'));

    expect(() => User.create({ name: 'Na', email: validEmail, password: validPassword }))
      .toThrow(new InvalidUserError('Name must be at least 3 characters long'));

    expect(() => User.create({ name: 'A'.repeat(256), email: validEmail, password: validPassword }))
      .toThrow(new InvalidUserError('Name must be at most 255 characters long'));

    expect(() => User.create({ name: 'John Doe!', email: validEmail, password: validPassword }))
      .toThrow(new InvalidUserError('Name contains invalid characters. Only letters and spaces are allowed'));
  });

  it('should throw an error if email is invalid', () => {
    const invalidEmail = 'invalid-email';
    expect(() => User.create({
      name: validName,
      email: invalidEmail,
      password: validPassword,
    })).toThrowError(InvalidEmailError);  // Supondo que a classe Email valide e lance erro
  });

  it('should throw an error if password is invalid', () => {
    const invalidPassword = 'short';  // Senha inválida (menos de 8 caracteres)
    expect(() => User.create({
      name: validName,
      email: validEmail,
      password: invalidPassword,
    })).toThrowError(InvalidPasswordError);
  });

  describe('getters', () => {
    let user: User;

    beforeEach(() => {
      user = User.create({
        name: validName,
        email: validEmail,
        password: validPassword,
      });
    });

    it('should return the correct name', () => {
      expect(user.name).toBe(validName);
    });

    it('should return the correct email', () => {
      expect(user.email).toBe(validEmail);
    });

    it('should return the correct password value (hashed)', () => {
      expect(user.password).toMatch(/^[a-f0-9]{32}:[a-f0-9]{128}$/); // Verifica o formato salt:hash
    });
  });

  describe('updateName', () => {
    let user: User;

    beforeEach(() => {
      user = User.create({
        name: validName,
        email: validEmail,
        password: validPassword,
      });
    });

    it('should allow changing the name', () => {
      const newName = 'Jane Doe';
      user.updateName(newName);
      expect(user.name).toBe(newName);
    });

    it('should throw an error when updating a name with an invalid value', () => {
      const spy = jest.spyOn(user as any, 'validateName');
      expect(() => user.updateName('')).toThrow(new InvalidUserError('Name is required'));
      expect(spy).toHaveBeenCalled();
    });
  });

  describe('updateEmail', () => {
    let user: User;

    beforeEach(() => {
      user = User.create({
        name: validName,
        email: validEmail,
        password: validPassword,
      });
    });


    it('should allow changing the email', () => {
      const newEmail = 'new@example.com';
      user.updateEmail(newEmail);
      expect(user.email).toBe(newEmail);
    });

    it('should throw an error when updating a email with an invalid value', () => {
      expect(() => user.updateEmail('')).toThrow(new InvalidEmailError(''));
    });
  });

  describe('updatePassword', () => {
    let user: User;

    beforeEach(() => {
      user = User.create({
        name: validName,
        email: validEmail,
        password: validPassword,
      });
    });

    it('should allow changing the password', () => {
      const newPassword = 'new_password';
      user.updatePassword(newPassword);
      expect(user.password).toMatch(/^[a-f0-9]{32}:[a-f0-9]{128}$/); // Verifica o formato salt:hash
    });

    it('should throw an error when updating a email with an invalid value', () => {
      expect(() => user.updatePassword('')).toThrow(new InvalidPasswordError('Password is required'));
    });
  });
});
