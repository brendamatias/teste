import { InMemoryRepository } from '../../../../shared';
import { Todo, TodoRepository } from '../../../domain';

export class TodoInMemoryRepository extends InMemoryRepository<Todo> implements TodoRepository {
  public async getByUserAccess(ownerId: string, email: string): Promise<Todo[]> {
    return this.entities.filter((todo) => todo.ownerId === ownerId || todo.sharedWith.includes(email));
  }

  public async deleteTodoItem(todo: Todo, todoItemId: string): Promise<void> {
    const todoItemIndex = todo.items.findIndex((item) => item.id === todoItemId);
    if (todoItemIndex === -1) {
      return;
    }

    todo.items.splice(todoItemIndex, 1);
  }
}
