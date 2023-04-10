import { Todo, TodoItem, TodoItemProps, TodoProps } from '../../../domain';

export class TodoFirestoreMapper {
  public toEntity(data: TodoProps, todoItems: TodoItemProps[] = []): Todo {
    const items = todoItems.map((item) => TodoItem.restore({
      id: item.id,
      content: item.content,
      isCompleted: item.isCompleted,
      order: item.order,
      todoId: data.id,
      completedAt: item.completedAt ? (item.completedAt as any).toDate() : null,
      createdAt: (item.createdAt as any).toDate(),
      updatedAt: (item.updatedAt as any).toDate(),
      parentId: item.parentId ?? undefined,
    }));
    return Todo.restore({
      id: data.id,
      title: data.title,
      ownerId: data.ownerId,
      shareWith: data.sharedWith,
      items,
      createdAt: (data.createdAt as any).toDate(),
      updatedAt: (data.updatedAt as any).toDate(),
    });
  }

  public fromEntity(entity: Todo): TodoProps {
    return entity.toJSON();
  }
}
