import { BaseEntity, BaseEntityConstructorProps, Email } from '../../shared';
import { InvalidUserError } from './errors';
import { Password } from './password.vo';

type UserConstructorProps = BaseEntityConstructorProps & UserCreateProps & {
  id?: string;
};

type UserCreateProps = {
  name: string;
  email: string;
  password: string;
};

export type UserProps = {
  id: string;
  name: string;
  email: string;
  password: string;
  createdAt: Date;
  updatedAt: Date;
}

export class User extends BaseEntity {
  private _name: string;
  private _email: Email;
  private _password: Password;

  private constructor(props: UserConstructorProps) {
    super(props);
    this._name = props.name;
    this._email = new Email(props.email);
    this._password = Password.restore(props.password);
    this.validate();
  }

  public static create(props: UserCreateProps): User {
    const password = Password.create(props.password);
    return new User({
      ...props,
      password: password.value,
    });
  }

  public static restore(props: UserConstructorProps): User {
    return new User(props);
  }

  public get name(): string {
    return this._name;
  }

  public get email(): string {
    return this._email.value;
  }

  public get password(): string {
    return this._password.value;
  }

  private validate(): void {
    this.validateName();
  }

  public updateName(name: string): void {
    this._name = name;
    this.validateName();
  }

  private validateName(): void {
    if (!this._name) {
      throw new InvalidUserError('Name is required');
    }

    if (this._name.length < 3) {
      throw new InvalidUserError('Name must be at least 3 characters long');
    }

    if (this._name.length > 255) {
      throw new InvalidUserError('Name must be at most 255 characters long');
    }

    if (!/^[a-zA-Z ]+$/.test(this._name)) {
      throw new InvalidUserError('Name contains invalid characters. Only letters and spaces are allowed');
    }
  }

  public updateEmail(email: string): void {
    this._email = new Email(email);
  }

  public updatePassword(password: string): void {
    this._password = Password.create(password);
  }

  public matchesPassword(password: string): boolean {
    return this._password.matches(password);
  }

  public toJSON(): UserProps {
    return {
      id: this.id,
      name: this._name,
      email: this.email,
      password: this.password,
      createdAt: this.createdAt,
      updatedAt: this.updatedAt,
    };
  }
}
