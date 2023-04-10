import { BaseEntity, BaseEntityConstructorProps, BaseEntityProps, EntityId } from '../../shared';
import { TodoItemInvalidError } from './errors';

type TodoItemCreateProps = {
  content: string;
  todoId: string;
  order: number;
  parentId?: string;
};

type TodoItemConstructorProps = TodoItemCreateProps & BaseEntityConstructorProps & {
  isCompleted: boolean;
  completedAt?: Date;
};

export type TodoItemProps = BaseEntityProps & {
  content: string;
  todoId: string;
  isCompleted: boolean;
  completedAt: Date | null;
  order: number;
  parentId: string | null;
};

export class TodoItem extends BaseEntity {
  private _content: string;
  private _order: number;
  private _isCompleted: boolean;
  private _completedAt: Date | null;
  private readonly _todoId: EntityId;
  private _parentId: EntityId | null;

  private constructor(props: TodoItemConstructorProps) {
    super(props);
    this._content = props.content;
    this._isCompleted = props.isCompleted;
    this._completedAt = props.completedAt ? new Date(props.completedAt) : null;
    this._order = props.order;
    this._todoId = new EntityId(props.todoId);
    this._parentId = props.parentId ? new EntityId(props.parentId) : null;
    this.validate();
  }

  static create(props: TodoItemCreateProps): TodoItem {
    return new TodoItem({
      ...props,
      isCompleted: false,
    });
  }

  static restore(props: TodoItemConstructorProps): TodoItem {
    return new TodoItem(props);
  }

  public get content(): string {
    return this._content;
  }

  public get order(): number {
    return this._order;
  }

  public get isCompleted(): boolean {
    return this._isCompleted;
  }

  public get ownerId(): string {
    return this._todoId.value;
  }

  public get parentId(): string | null {
    return this._parentId ? this._parentId.value : null;
  }

  public get completedAt(): Date | null {
    return this._completedAt;
  }

  public updateContent(newContent: string): void {
    this._content = newContent;
    this.validateContent();
    this._updatedAt = new Date();
  }

  public updateOrder(newOrder: number): void {
    this._order = newOrder;
    this.validateOrder();
    this._updatedAt = new Date();
  }

  public complete(): void {
    this._isCompleted = true;
    this._completedAt = new Date();
  }

  public uncomplete(): void {
    this._isCompleted = false;
    this._completedAt = null;
  }

  public updateParentId(newParentId?: string): void {
    this._parentId = newParentId ? new EntityId(newParentId) : null;
    this._updatedAt = new Date();
  }

  private validate(): void {
    this.validateContent();
    this.validateOrder();
  }


  private validateContent(): void {
    if (!this._content || this._content.trim() === '') {
      throw new TodoItemInvalidError('Content is required');
    }

    if (this._content.length < 5) {
      throw new TodoItemInvalidError('Content must be at least 5 characters long');
    }

    if (this._content.length > 500) {
      throw new TodoItemInvalidError('Content must be at most 500 characters long');
    }
  }

  private validateOrder(): void {
    if (typeof this._order !== 'number' || this._order < 0 || isNaN(this._order)) {
      throw new TodoItemInvalidError('The order must be greater than or equal to 0');
    }
  }

  public toJSON(): TodoItemProps {
    return {
      id: this.id,
      content: this._content,
      todoId: this._todoId.value,
      order: this._order,
      isCompleted: this._isCompleted,
      completedAt: this._completedAt,
      parentId: this.parentId,
      createdAt: this.createdAt,
      updatedAt: this.updatedAt,
    }
  }
}
