import { Repository } from '../../shared';
import { Todo } from './todo.entity';

export type TodoGetByUserAccessOutput = {
  id: string;
  title: string;
  ownerId: string;
  sharedWith: string[];
  createdAt: Date;
  updatedAt: Date;
};

export interface TodoRepository extends Repository<Todo> {
  getByUserAccess(ownerId: string, email: string): Promise<TodoGetByUserAccessOutput[]>;
  deleteTodoItem(todo: Todo, todoItemId: string): Promise<void>;
}
