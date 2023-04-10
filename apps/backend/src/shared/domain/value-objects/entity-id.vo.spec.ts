import { validate as uuidValidate } from 'uuid';
import { InvalidIdError } from '../errors';
import { EntityId } from './entity-id.vo';

describe('EntityId Unit Tests', () => {
  it('should create a valid entity id with a given value', () => {
    let entityId = new EntityId();
    expect(entityId.value).toBeDefined();
    expect(uuidValidate(entityId.value)).toBeTruthy();

    entityId = new EntityId('123e4567-e89b-12d3-a456-426614174000');
    expect(entityId.value).toBeDefined();
    expect(entityId.value).toBe('123e4567-e89b-12d3-a456-426614174000');
  });

  it('should throw an error when creating an entity id with an invalid value', () => {
    expect(() => new EntityId('invalid-uuid')).toThrow(InvalidIdError);
  });
});
