
import { getFirestore } from 'firebase-admin/firestore';
import { Todo, TodoGetByUserAccessOutput, TodoItemProps, TodoProps, TodoRepository } from '../../../domain';
import { TodoFirestoreMapper } from './todo-firestore.mapper';

export class TodoFirestoreRepository implements TodoRepository {
  private readonly collectionName = 'todos';
  private readonly subCollectionName = 'items';
  private db: FirebaseFirestore.Firestore;
  private todoFirestoreMapper: TodoFirestoreMapper;

  constructor() {
    this.todoFirestoreMapper = new TodoFirestoreMapper();
    this.db = getFirestore();
  }

  private get collection() {
    return this.db.collection(this.collectionName);
  }

  private getSubCollection(todoId: string) {
    return this.collection.doc(todoId).collection(this.subCollectionName);
  }

  public async create(entity: Todo): Promise<void> {
    try {
      this.db.runTransaction(async (transaction) => {
        const todoDoc = this.collection.doc(entity.id);
        const { items, ...otherProps } = this.todoFirestoreMapper.fromEntity(entity);
        transaction.set(todoDoc, otherProps);
        const itemsCollection = this.getSubCollection(todoDoc.id);
        items.forEach(async (item) => {
          const itemDoc = itemsCollection.doc(item.id);
          transaction.set(itemDoc, item);
        });
      });
    } catch (error) {
      console.error('Error creating todo', error);
      throw error;
    }

  }

  public async getByUserAccess(ownerId: string, email: string): Promise<TodoGetByUserAccessOutput[]> {
    try {
      const snapshotOwnerId = await this.collection
        .where('ownerId', '==', ownerId)
        .orderBy('createdAt', 'desc')
        .get();
      const snapshotEmail = await this.collection
        .where('sharedWith', 'array-contains', email)
        .orderBy('createdAt', 'desc')
        .get();

      const todos: Todo[] = [];
      for (const doc of snapshotOwnerId.docs) {
        const todo = this.todoFirestoreMapper.toEntity(doc.data() as TodoProps);
        todos.push(todo);
      }

      for (const doc of snapshotEmail.docs) {
        const todoProps = doc.data() as TodoProps;
        if (!todos.some((t) => t.id === todoProps.id)) {
          const todo = this.todoFirestoreMapper.toEntity(doc.data() as TodoProps);
          todos.push(todo);
        }
      }

      return todos
        .map(todo => ({
          id: todo.id,
          title: todo.title,
          ownerId: todo.ownerId,
          sharedWith: todo.sharedWith,
          createdAt: todo.createdAt,
          updatedAt: todo.updatedAt
        }))
        .sort((a, b) => a.createdAt.getTime() - b.createdAt.getTime());
    } catch (error) {
      console.error('Error getting todos by owner id', error);
      throw error;
    }
  }

  private async mapTodoPropsToEntity(todoProps: TodoProps) {
    const todoItems = await this.getSubCollection(todoProps.id).get();
    const items = todoItems.docs.map((item) => item.data() as TodoItemProps);
    return this.todoFirestoreMapper.toEntity(todoProps, items);
  }

  public async update(entity: Todo): Promise<void> {
    try {
      this.db.runTransaction(async (transaction) => {
        const todoDoc = this.collection.doc(entity.id);
        const { items, ...otherProps } = this.todoFirestoreMapper.fromEntity(entity);
        transaction.update(todoDoc, otherProps);
        const itemsCollection = this.getSubCollection(todoDoc.id);
        items.forEach(async (item) => {
          const itemDoc = itemsCollection.doc(item.id);
          transaction.set(itemDoc, item);
        });
      });
    } catch (error) {
      console.error('Error updating todo', error);
      throw error;
    }
  }
  public async deleteTodoItem(todo: Todo, todoItemId: string): Promise<void> {
    await this.getSubCollection(todo.id).doc(todoItemId).delete();
  }

  public async getById(id: string): Promise<Todo | null> {
    const todoDoc = await this.collection.doc(id).get();
    if (!todoDoc.exists) {
      return null;
    }

    const todo = todoDoc.data() as TodoProps;
    const todoItems = await this.getSubCollection(todoDoc.id).get();
    const items = todoItems.docs.map((item) => item.data() as TodoItemProps);
    return this.todoFirestoreMapper.toEntity(todo, items);
  }
}
