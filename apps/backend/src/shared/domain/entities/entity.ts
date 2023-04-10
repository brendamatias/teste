import { EntityId } from '../value-objects';

export type BaseEntityConstructorProps = {
  id?: string;
  createdAt?: Date;
  updatedAt?: Date;
};

export type BaseEntityProps = {
  id: string;
  createdAt: Date;
  updatedAt: Date;
};
export abstract class BaseEntity<T extends BaseEntityProps = any> {
  private readonly _entityId: EntityId;
  public readonly createdAt: Date;
  protected _updatedAt: Date;

  constructor(props: BaseEntityConstructorProps) {
    this._entityId = new EntityId(props.id);
    this.createdAt = props.createdAt || new Date();
    this._updatedAt = props.updatedAt || new Date();
  }

  public get id(): string {
    return this._entityId.value;
  }

  public get updatedAt(): Date {
    return this._updatedAt;
  }

  abstract toJSON(): T;
}
