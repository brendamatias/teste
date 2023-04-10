import { BaseEntity, Repository } from '../../../domain';

export abstract class InMemoryRepository<E extends BaseEntity> implements Repository<E> {
  protected entities: E[] = [];

  async create(entity: E): Promise<void> {
    this.entities.push(entity);
  }

  async update(entity: E): Promise<void> {
    this.entities = this.entities.map((e) => (e.id === entity.id ? entity : e));
  }

  async delete(entity: E): Promise<void> {
    this.entities = this.entities.filter((e) => e.id !== entity.id);
  }

  async getAll(): Promise<E[]> {
    return this.entities;
  }

  async getById(id: string): Promise<E | null> {
    return this.entities.find((e) => (e as any).id === id) ?? null;
  }
}
