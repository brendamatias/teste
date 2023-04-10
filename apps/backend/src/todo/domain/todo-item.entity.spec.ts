import { TodoItemInvalidError } from './errors';
import { TodoItem } from './todo-item.entity';

describe('TodoItem Entity Unit Test', () => {
  it('should create a valid TodoItem entity with a given value', () => {
    let todoItem = TodoItem.create({ content: 'Content', todoId: 'a35d400e-5851-4b23-ada7-6799cc9701ff', order: 0 });
    expect(todoItem.id).toBeDefined();
    expect(todoItem.content).toBe('Content');
    expect(todoItem.order).toBe(0);
    expect(todoItem.isCompleted).toBe(false);
    expect(todoItem.ownerId).toBe('a35d400e-5851-4b23-ada7-6799cc9701ff');
    expect(todoItem.parentId).toBeNull();
    expect(todoItem.createdAt).toBeInstanceOf(Date);
    expect(todoItem.updatedAt).toBeInstanceOf(Date);

    todoItem = TodoItem.create({
      content: 'Content',
      todoId: 'a35d400e-5851-4b23-ada7-6799cc9701ff',
      order: 0,
      parentId: '26783425-7732-413d-abd2-d8403697ed99'
    });
    expect(todoItem.parentId).toBe('26783425-7732-413d-abd2-d8403697ed99');
  });

  it('should restore a TodoItem entity from a JSON object', () => {
    const createdAt = new Date('2021-01-01');
    const updatedAt = new Date('2021-01-02');
    let todoItem = TodoItem.restore({
      id: 'dff8a476-7118-469f-aef1-cb62bbf92f8d',
      content: 'Content',
      order: 5,
      todoId: 'a35d400e-5851-4b23-ada7-6799cc9701ff',
      isCompleted: true,
      createdAt,
      updatedAt,
    });
    expect(todoItem.id).toBe('dff8a476-7118-469f-aef1-cb62bbf92f8d');
    expect(todoItem.content).toBe('Content');
    expect(todoItem.order).toBe(5);
    expect(todoItem.isCompleted).toBe(true);
    expect(todoItem.ownerId).toBe('a35d400e-5851-4b23-ada7-6799cc9701ff');
    expect(todoItem.createdAt).toBe(createdAt);
    expect(todoItem.updatedAt).toBe(updatedAt);

    todoItem = TodoItem.restore({
      id: 'dff8a476-7118-469f-aef1-cb62bbf92f8d',
      content: 'Content',
      order: 5,
      todoId: 'a35d400e-5851-4b23-ada7-6799cc9701ff',
      isCompleted: true,
      parentId: '26783425-7732-413d-abd2-d8403697ed99',
      createdAt,
      updatedAt,
    });
    expect(todoItem.parentId).toBe('26783425-7732-413d-abd2-d8403697ed99');
  });

  it('should convert a TodoItem entity to JSON', () => {
    const createdAt = new Date('2021-01-01');
    const updatedAt = new Date('2021-01-02');
    const todoItem = TodoItem.restore({
      id: 'dff8a476-7118-469f-aef1-cb62bbf92f8d',
      content: 'Content',
      order: 6,
      todoId: 'a35d400e-5851-4b23-ada7-6799cc9701ff',
      isCompleted: true,
      createdAt,
      updatedAt,
    });
    expect(todoItem.toJSON()).toEqual({
      id: 'dff8a476-7118-469f-aef1-cb62bbf92f8d',
      content: 'Content',
      order: 6,
      todoId: 'a35d400e-5851-4b23-ada7-6799cc9701ff',
      isCompleted: true,
      completedAt: null,
      parentId: null,
      createdAt,
      updatedAt,
    });
  });

  it('should throw an error when creating a TodoItem entity with an invalid content', () => {
    expect(() => TodoItem.create({ content: '', todoId: 'a35d400e-5851-4b23-ada7-6799cc9701ff', order: 1 }))
      .toThrow(new TodoItemInvalidError('Content is required'));

    expect(() => TodoItem.create({ content: '      ', todoId: 'a35d400e-5851-4b23-ada7-6799cc9701ff', order: 1 }))
      .toThrow(new TodoItemInvalidError('Content is required'));

    expect(() => TodoItem.create({ content: 'Desc', todoId: 'a35d400e-5851-4b23-ada7-6799cc9701ff', order: 1 }))
      .toThrow(new TodoItemInvalidError('Content must be at least 5 characters long'));

    expect(() => TodoItem.create({ content: 'A'.repeat(501), todoId: 'a35d400e-5851-4b23-ada7-6799cc9701ff', order: 1 }))
      .toThrow(new TodoItemInvalidError('Content must be at most 500 characters long'));
  });

  it('should throw an error when creating a TodoItem entity with an invalid order', () => {
    expect(() => TodoItem.create({ content: 'Content', todoId: 'a35d400e-5851-4b23-ada7-6799cc9701ff', order: -1 }))
      .toThrow(new TodoItemInvalidError('The order must be greater than or equal to 0'));

    expect(() => TodoItem.create({ content: 'Content', ownerId: 'a35d400e-5851-4b23-ada7-6799cc9701ff', order: 'teste' } as any))
      .toThrow(new TodoItemInvalidError('The order must be greater than or equal to 0'));

    expect(() => TodoItem.create({ content: 'Content', todoId: 'a35d400e-5851-4b23-ada7-6799cc9701ff', order: +'teste' }))
      .toThrow(new TodoItemInvalidError('The order must be greater than or equal to 0'));
  });

  it('should complete a TodoItem', () => {
    const todoItem = TodoItem.create({ content: 'Content', todoId: 'a35d400e-5851-4b23-ada7-6799cc9701ff', order: 1 });
    todoItem.complete();
    expect(todoItem.isCompleted).toBe(true);
    expect(todoItem.completedAt).toBeInstanceOf(Date);
  });

  it('should update a todoItem parentId', () => {
    const todoItem = TodoItem.create({ content: 'Content', todoId: 'a35d400e-5851-4b23-ada7-6799cc9701ff', order: 1 });
    todoItem.updateParentId('26783425-7732-413d-abd2-d8403697ed99');
    expect(todoItem.parentId).toBe('26783425-7732-413d-abd2-d8403697ed99');
    todoItem.updateParentId();
    expect(todoItem.parentId).toBeNull();
  });

  describe('updateContent', () => {
    const todoItem = TodoItem.create({ content: 'Content', todoId: 'a35d400e-5851-4b23-ada7-6799cc9701ff', order: 1 });
    it('should update a TodoItem content', () => {
      todoItem.updateContent('New Content');
      expect(todoItem.content).toBe('New Content');
    });

    it('should throw an error when updating a TodoItem content with an invalid value', () => {
      const spy = jest.spyOn(todoItem as any, 'validateContent');
      expect(() => todoItem.updateContent('')).toThrow(new TodoItemInvalidError('Content is required'));
      expect(spy).toHaveBeenCalled();
    });
  });

  describe('updateOrder', () => {
    const todoItem = TodoItem.create({ content: 'Content', todoId: 'a35d400e-5851-4b23-ada7-6799cc9701ff', order: 1 });
    it('should update a todoItem order', () => {
      todoItem.updateOrder(2);
      expect(todoItem.order).toBe(2);
    });

    it('should throw an error when updating a TodoItem order with an invalid value', () => {
      const spy = jest.spyOn(todoItem as any, 'validateOrder');
      expect(() => todoItem.updateOrder(-1)).toThrow(new TodoItemInvalidError('The order must be greater than or equal to 0'));
      expect(spy).toHaveBeenCalled();
    });
  });
});
