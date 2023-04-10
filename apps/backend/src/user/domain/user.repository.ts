import { Repository } from '../../shared';
import { User } from './user.entity';

export interface UserRepository extends Repository<User> {
  getByEmail(email: string): Promise<User | null>;
}
