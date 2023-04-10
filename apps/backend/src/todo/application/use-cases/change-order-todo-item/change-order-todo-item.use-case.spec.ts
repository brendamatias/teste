import { UserInMemoryRepository } from '../../../../user';
import { Todo, TodoRepository } from '../../../domain';
import { TodoInMemoryRepository } from '../../../infra';
import { TodoAccessService } from '../../services';
import { AddTodoItemUseCase } from '../add-todo-item/add-todo-item.use-case';
import { CreateTodoUseCase } from '../create-todo/create-todo.use-case';
import { ChangeOrderTodoItemUseCase } from './change-order-todo-item.use-case';

describe('AddTodoItem Tests', () => {
  let addTodoItemUseCase: AddTodoItemUseCase;
  let createTodoUseCase: CreateTodoUseCase;
  let changeOrderTodoItem: ChangeOrderTodoItemUseCase;
  let todoRepository: TodoRepository;

  beforeEach(() => {
    const userRepository = new UserInMemoryRepository();
    const todoAccessService = new TodoAccessService(userRepository);
    todoRepository = new TodoInMemoryRepository();
    addTodoItemUseCase = new AddTodoItemUseCase(todoAccessService, todoRepository);
    createTodoUseCase = new CreateTodoUseCase(todoRepository);
    changeOrderTodoItem = new ChangeOrderTodoItemUseCase(todoAccessService, todoRepository);
  });

  it('should change order of todo item', async () => {
    const { todoId } = await createTodoUseCase.execute({ title: 'Todo Title', userId: '123e4567-e89b-12d3-a456-426614174000' });

    await addTodoItemUseCase.execute({
      todoId,
      content: 'Content of the todo item 1',
      userId: '123e4567-e89b-12d3-a456-426614174000',
    });
    await addTodoItemUseCase.execute({
      todoId,
      content: 'Content of the todo item 2',
      userId: '123e4567-e89b-12d3-a456-426614174000',
    });
    await addTodoItemUseCase.execute({
      todoId,
      content: 'Content of the todo item 3',
      userId: '123e4567-e89b-12d3-a456-426614174000',
    });

    const todo = await todoRepository.getById(todoId) as Todo;
    expect(todo).toBeDefined();
    expect(todo.items[0].content).toBe('Content of the todo item 1');
    expect(todo.items[1].content).toBe('Content of the todo item 2');
    expect(todo.items[2].content).toBe('Content of the todo item 3');

    let itemToMove = todo.items[1];
    await changeOrderTodoItem.execute({
      todoId,
      todoItemId: itemToMove.id,
      userId: '123e4567-e89b-12d3-a456-426614174000',
      newOrder: 0,
    });

    expect(todo.items[0].content).toBe('Content of the todo item 2');
    expect(todo.items[1].content).toBe('Content of the todo item 1');
    expect(todo.items[2].content).toBe('Content of the todo item 3');

    itemToMove = todo.items[2];
    await changeOrderTodoItem.execute({
      todoId,
      todoItemId: itemToMove.id,
      userId: '123e4567-e89b-12d3-a456-426614174000',
      newOrder: 0,
    });

    expect(todo.items[0].content).toBe('Content of the todo item 3');
    expect(todo.items[1].content).toBe('Content of the todo item 2');
    expect(todo.items[2].content).toBe('Content of the todo item 1');

    itemToMove = todo.items[0];
    await changeOrderTodoItem.execute({
      todoId,
      todoItemId: itemToMove.id,
      userId: '123e4567-e89b-12d3-a456-426614174000',
      newOrder: 1,
    });

    expect(todo.items[0].content).toBe('Content of the todo item 2');
    expect(todo.items[1].content).toBe('Content of the todo item 3');
    expect(todo.items[2].content).toBe('Content of the todo item 1');

    itemToMove = todo.items[0];
    await changeOrderTodoItem.execute({
      todoId,
      todoItemId: itemToMove.id,
      userId: '123e4567-e89b-12d3-a456-426614174000',
      newOrder: 2,
    });

    expect(todo.items[0].content).toBe('Content of the todo item 3');
    expect(todo.items[1].content).toBe('Content of the todo item 1');
    expect(todo.items[2].content).toBe('Content of the todo item 2');
  });
});
