import { InMemoryRepository } from '../../../../shared';
import { User, UserRepository } from '../../../domain';

export class UserInMemoryRepository extends InMemoryRepository<User> implements UserRepository {
  public async getByEmail(email: string): Promise<User | null> {
    return this.entities.find((user) => user.email === email) ?? null;
  }
}
