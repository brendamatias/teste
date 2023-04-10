import { validate as uuidValidate } from 'uuid';
import { InvalidIdError } from '../errors';
import { BaseEntity } from './entity';

class StubEntity extends BaseEntity {
  toJSON() {
    return {
      id: this.id,
      createdAt: this.createdAt,
      updatedAt: this.updatedAt,
    };
  }
}

describe('BaseEntity Unit Test', () => {
  test('should create an entity with a valid UUID and timestamps', () => {
    const entity = new StubEntity({});
    expect(entity.id).toBeDefined();
    expect(uuidValidate(entity.id)).toBe(true);
    expect(entity.createdAt).toBeInstanceOf(Date);
    expect(entity.updatedAt).toBeInstanceOf(Date);
  });

  test('should create an entity with a valid UUID and custom timestamps', () => {
    const createdAt = new Date('2021-01-01');
    const updatedAt = new Date('2021-01-02');
    const id = 'd6688697-edfb-4401-9711-3260c44f6a1c';
    const entity = new StubEntity({ id, createdAt, updatedAt });
    expect(entity.id).toBe(id);
    expect(entity.createdAt).toBe(createdAt);
    expect(entity.updatedAt).toBe(updatedAt);
  });

  test('should throw error when creating an entity with an invalid UUID', () => {
    expect(() => new StubEntity({ id: 'invalid-uuid' })).toThrow(InvalidIdError);
  });
});
