import { BaseEntity } from '../entities';

export interface Repository<E extends BaseEntity> {
  create(entity: E): Promise<void>;
  update(entity: E): Promise<void>;
  getById(id: string): Promise<E | null>;
}
