import { BaseEntity, BaseEntityConstructorProps, BaseEntityProps, Email, EntityId } from '../../shared';
import { TodoInvalidError } from './errors';
import { TodoItem, TodoItemProps } from './todo-item.entity';

type TodoConstructorProps = BaseEntityConstructorProps & TodoCreateProps & {
  items?: TodoItem[];
};

type TodoCreateProps = {
  title: string;
  ownerId: string;
};

type TodoRestoreProps = TodoConstructorProps & {
  shareWith?: string[];
};

export type TodoProps = BaseEntityProps & {
  title: string;
  ownerId: string;
  sharedWith: string[];
  items: TodoItemProps[];
};

export class Todo extends BaseEntity {
  private _title: string;
  private readonly _ownerId: EntityId;
  private readonly _sharedWith = new Set<string>();
  private _items: TodoItem[] = [];

  private constructor(props: TodoConstructorProps) {
    super(props);
    this._title = props.title;
    if (!props.ownerId) {
      throw new TodoInvalidError('OwnerId is required');
    }
    this._ownerId = new EntityId(props.ownerId);
    this._items = props.items || [];
    this.validate();
  }

  static create(props: TodoCreateProps): Todo {
    return new Todo(props);
  }

  static restore(props: TodoRestoreProps): Todo {
    const todo = new Todo(props);
    const sharedWith = props.shareWith || [];
    sharedWith.forEach((email) => {
      todo.shareWith(email);
    });

    return todo;
  }

  public get title(): string {
    return this._title;
  }

  public get ownerId(): string {
    return this._ownerId.value;
  }

  public get sharedWith(): string[] {
    return Array.from(this._sharedWith);
  }

  public get items(): TodoItem[] {
    return this._items.sort((a, b) => a.order - b.order);
  }

  public updateTitle(newTitle: string): void {
    this._title = newTitle;
    this.validateTitle();
    this._updatedAt = new Date();
  }

  public addTodoItem(content: string): void {
    const item = TodoItem.create({
      content,
      todoId: this.id,
      order: this._items.length,
    });

    this._items.push(item);
  }

  public removeTodoItem(itemId: string): void {
    this._items = this._items.filter((item) => item.id !== itemId);
  }

  private validate(): void {
    this.validateTitle();
  }

  private validateTitle(): void {
    if (!this._title || this._title.trim() === '') {
      throw new TodoInvalidError('Title is required');
    }

    if (this._title.length < 3) {
      throw new TodoInvalidError('Title must be at least 3 characters long');
    }

    if (this._title.length > 50) {
      throw new TodoInvalidError('Title must be at most 50 characters long');
    }
  }

  public shareWith(email: string): void {
    const emailValue = new Email(email);
    this._sharedWith.add(emailValue.value);
  }

  /**
   * Checks if the given identifier has access to the todo item.
   *
   * @param identifier - OwnerId or Email of the user.
   * @returns `true` if the identifier is either the owner or is shared with, otherwise `false`.
   */
  public hasAccess(identifier: string): boolean {
    return this._ownerId.value === identifier || this._sharedWith.has(identifier);
  }

  public toJSON(): TodoProps {
    return {
      id: this.id,
      title: this._title,
      ownerId: this._ownerId.value,
      sharedWith: this.sharedWith,
      items: this.items.map((item) => item.toJSON()),
      createdAt: this.createdAt,
      updatedAt: this.updatedAt,
    };
  }
}
