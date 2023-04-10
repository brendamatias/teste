import { TodoGetByUserAccessOutput, TodoRepository } from '../../../domain';

export class ListTodosUseCase {
  constructor(
    private readonly todoRepository: TodoRepository
  ) { }

  public async execute(input: ListTodosInput): Promise<ListTodosOutput> {
    return await this.todoRepository.getByUserAccess(input.userId, input.email);
  }
}

type ListTodosInput = {
  userId: string;
  email: string;
};

type ListTodosOutput = TodoGetByUserAccessOutput[];
