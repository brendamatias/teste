
import { getFirestore } from 'firebase-admin/firestore';
import { User, UserProps, UserRepository } from '../../../domain';
import { UserFirestoreMapper } from './user-firestore.mapper';

export class UserFirestoreRepository implements UserRepository {
  private readonly collectionName = 'users';
  private db: FirebaseFirestore.Firestore;
  private userFirestoreMapper: UserFirestoreMapper;

  constructor() {
    this.userFirestoreMapper = new UserFirestoreMapper();
    this.db = getFirestore();
  }

  private get collection() {
    return this.db.collection(this.collectionName);
  }

  public async create(entity: User): Promise<void> {
    try {
      const user = this.userFirestoreMapper.fromEntity(entity);
      await this.collection.doc(entity.id).set(user);
    } catch (error) {
      console.error('Error creating user', error);
      throw error;
    }
  }

  public async update(entity: User): Promise<void> {
    try {
      const user = this.userFirestoreMapper.fromEntity(entity);
      await this.collection.doc(entity.id).update(user);
    } catch (error) {
      console.error('Error updating user', error);
      throw error;
    }
  }

  public async getById(id: string): Promise<User | null> {
    try {
      const userDoc = await this.collection.doc(id).get();
      if (!userDoc.exists) {
        return null;
      }
      return this.userFirestoreMapper.toEntity(userDoc.data() as UserProps);
    } catch (error) {
      console.error('Error getting user by email', error);
      throw error;
    }

  }

  public async getByEmail(email: string): Promise<User | null> {
    try {
      const snapshot = await this.collection.where('email', '==', email).get();
      if (snapshot.empty) {
        return null;
      }
      const userDoc = snapshot.docs[0];
      return this.userFirestoreMapper.toEntity(userDoc.data() as UserProps);
    } catch (error) {
      console.error('Error getting user by email', error);
      throw error;
    }
  }
}
