import { v4 as uuidv4, validate as uuidValidate } from 'uuid';
import { InvalidIdError } from '../errors';
import { ValueObject } from './value-object';

export class EntityId extends ValueObject<string> {
  public readonly value: string;

  constructor(value?: string) {
    super();
    this.value = value || uuidv4();
    this.validate();
  }

  private validate(): void {
    if (!uuidValidate(this.value)) {
      throw new InvalidIdError(this.value);
    }
  }
}
