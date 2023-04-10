import { Todo, TodoRepository } from '../../../domain';

export class CreateTodoUseCase {
  constructor(private todoRepository: TodoRepository) { }

  public async execute(input: CreateTodoInput): Promise<CreateTodoOutput> {
    const todo = Todo.create({
      title: input.title,
      ownerId: input.userId,
    });

    await this.todoRepository.create(todo);
    return { todoId: todo.id };
  }
}

type CreateTodoInput = {
  title: string;
  userId: string;
};

type CreateTodoOutput = {
  todoId: string;
};
