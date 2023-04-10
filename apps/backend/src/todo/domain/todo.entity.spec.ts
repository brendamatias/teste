import { InvalidEmailError } from '../../shared';
import { TodoInvalidError } from './errors';
import { TodoItem } from './todo-item.entity';
import { Todo } from './todo.entity';

describe('Todo Unit Tests', () => {
  it('should create a valid Todo entity with a given value', () => {
    const todo = Todo.create({ title: 'Title', ownerId: 'a35d400e-5851-4b23-ada7-6799cc9701ff' });
    expect(todo.id).toBeDefined();
    expect(todo.title).toBe('Title');
    expect(todo.ownerId).toBe('a35d400e-5851-4b23-ada7-6799cc9701ff');
    expect(todo.sharedWith.length).toBe(0);
    expect(todo.items.length).toBe(0);
    expect(todo.createdAt).toBeInstanceOf(Date);
    expect(todo.updatedAt).toBeInstanceOf(Date);
  });

  it('should restore a Todo entity from a JSON object', () => {
    const createdAt = new Date('2021-01-01');
    const updatedAt = new Date('2021-01-02');
    const items = [
      TodoItem.create({ content: 'Todo Item 1', todoId: 'dff8a476-7118-469f-aef1-cb62bbf92f8d', order: 0 }),
      TodoItem.create({ content: 'Todo Item 2', todoId: 'dff8a476-7118-469f-aef1-cb62bbf92f8d', order: 1 }),
      TodoItem.create({ content: 'Todo Item 3', todoId: 'dff8a476-7118-469f-aef1-cb62bbf92f8d', order: 2 }),
    ];
    const todo = Todo.restore({
      id: 'dff8a476-7118-469f-aef1-cb62bbf92f8d',
      title: 'Title',
      ownerId: 'a35d400e-5851-4b23-ada7-6799cc9701ff',
      shareWith: [
        'user1@email.com',
        'user2@email.com',
      ],
      items,
      createdAt,
      updatedAt,
    });
    expect(todo.id).toBe('dff8a476-7118-469f-aef1-cb62bbf92f8d');
    expect(todo.title).toBe('Title');
    expect(todo.ownerId).toBe('a35d400e-5851-4b23-ada7-6799cc9701ff');
    expect(todo.sharedWith.length).toBe(2);
    expect(todo.items.length).toBe(3);
    expect(todo.createdAt).toBe(createdAt);
    expect(todo.updatedAt).toBe(updatedAt);
  });

  it('should convert a Todo entity to JSON', () => {
    const createdAt = new Date('2021-01-01');
    const updatedAt = new Date('2021-01-02');
    const todo = Todo.restore({
      id: 'dff8a476-7118-469f-aef1-cb62bbf92f8d',
      title: 'Title',
      ownerId: 'a35d400e-5851-4b23-ada7-6799cc9701ff',
      createdAt,
      updatedAt,
    });
    expect(todo.toJSON()).toEqual({
      id: 'dff8a476-7118-469f-aef1-cb62bbf92f8d',
      title: 'Title',
      ownerId: 'a35d400e-5851-4b23-ada7-6799cc9701ff',
      sharedWith: [],
      items: [],
      createdAt,
      updatedAt,
    });
  });

  it('should return the items sorted by order', () => {
    const createdAt = new Date('2021-01-01');
    const updatedAt = new Date('2021-01-02');
    const items = [
      TodoItem.create({ content: 'Todo Item 3', todoId: 'dff8a476-7118-469f-aef1-cb62bbf92f8d', order: 3 }),
      TodoItem.create({ content: 'Todo Item 0', todoId: 'dff8a476-7118-469f-aef1-cb62bbf92f8d', order: 0 }),
      TodoItem.create({ content: 'Todo Item 1', todoId: 'dff8a476-7118-469f-aef1-cb62bbf92f8d', order: 1 }),
      TodoItem.create({ content: 'Todo Item 2', todoId: 'dff8a476-7118-469f-aef1-cb62bbf92f8d', order: 2 }),
    ];
    const todo = Todo.restore({
      id: 'dff8a476-7118-469f-aef1-cb62bbf92f8d',
      title: 'Title',
      ownerId: 'a35d400e-5851-4b23-ada7-6799cc9701ff',
      shareWith: [
        'user1@email.com',
        'user2@email.com',
      ],
      items,
      createdAt,
      updatedAt,
    });
    expect(todo.items[0].content).toBe('Todo Item 0');
    expect(todo.items[1].content).toBe('Todo Item 1');
    expect(todo.items[2].content).toBe('Todo Item 2');
    expect(todo.items[3].content).toBe('Todo Item 3');
  });

  it('should check if a user has access to a Todo', () => {
    const todo = Todo.create({ title: 'Title', ownerId: 'a35d400e-5851-4b23-ada7-6799cc9701ff' });
    todo.shareWith('user10@email.com');
    expect(todo.hasAccess('a35d400e-5851-4b23-ada7-6799cc9701ff')).toBe(true);
    expect(todo.hasAccess('user10@email.com')).toBe(true);
  });

  it('should throw an error when creating a TodoItem entity with an invalid ownerId', () => {
    expect(() => Todo.create({ title: 'Title', ownerId: '' }))
      .toThrow(new TodoInvalidError('OwnerId is required'));
  });

  it('should throw an error when creating a TodoItem entity with an invalid title', () => {
    expect(() => Todo.create({ title: '', ownerId: 'a35d400e-5851-4b23-ada7-6799cc9701ff' }))
      .toThrow(new TodoInvalidError('Title is required'));

    expect(() => Todo.create({ title: '    ', ownerId: 'a35d400e-5851-4b23-ada7-6799cc9701ff' }))
      .toThrow(new TodoInvalidError('Title is required'));

    expect(() => Todo.create({ title: 'T', ownerId: 'a35d400e-5851-4b23-ada7-6799cc9701ff' }))
      .toThrow(new TodoInvalidError('Title must be at least 3 characters long'));

    expect(() => Todo.create({ title: 'A'.repeat(51), ownerId: 'a35d400e-5851-4b23-ada7-6799cc9701ff' }))
      .toThrow(new TodoInvalidError('Title must be at most 50 characters long'));
  });

  describe('shareWith', () => {
    it('should share a Todo with an email', () => {
      const todo = Todo.create({ title: 'Title', ownerId: 'a35d400e-5851-4b23-ada7-6799cc9701ff' });
      todo.shareWith('user1@email.com');
      todo.shareWith('user3@email.com');
      expect(todo.sharedWith.length).toBe(2);
    });

    it('should not share a Todo with the same email twice', () => {
      const todo = Todo.create({ title: 'Title', ownerId: 'a35d400e-5851-4b23-ada7-6799cc9701ff' });
      todo.shareWith('user1@email.com');
      todo.shareWith('user1@email.com');
      expect(todo.sharedWith.length).toBe(1);
    });

    it('should throw an error when sharing a Todo with an invalid email', () => {
      const todo = Todo.create({ title: 'Title', ownerId: 'a35d400e-5851-4b23-ada7-6799cc9701ff' });
      expect(() => todo.shareWith('invalid-email')).toThrowError(InvalidEmailError);
    });
  });

  describe('updateTitle', () => {
    const todoItem = Todo.create({ title: 'Title', ownerId: 'a35d400e-5851-4b23-ada7-6799cc9701ff' });
    it('should update a Todo title', () => {
      todoItem.updateTitle('New Title');
      expect(todoItem.title).toBe('New Title');
    });

    it('should throw an error when updating a TodoItem title with an invalid value', () => {
      const spy = jest.spyOn(todoItem as any, 'validateTitle');
      expect(() => todoItem.updateTitle('')).toThrow(new TodoInvalidError('Title is required'));
      expect(spy).toHaveBeenCalled();
    });
  });
});
