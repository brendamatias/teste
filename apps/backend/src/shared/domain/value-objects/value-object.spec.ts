import { ValueObject } from './value-object';

class ValueObjectStub extends ValueObject {
  constructor(
    readonly prop1: string,
    readonly prop2: number,
    readonly prop3: boolean,
    readonly prop4: Date,
  ) {
    super();
  }

  value: any;
}
describe('ValueObject Unit Test', () => {
  test('Deve retornar true se os objetos são iguais', () => {
    const dataAtual = new Date();
    const valueObject1 = new ValueObjectStub('prop1', 1, true, dataAtual);
    const valueObject2 = new ValueObjectStub('prop1', 1, true, dataAtual);
    expect(valueObject1.equals(valueObject2)).toBe(true);
  });

  test('Deve retornar false se os objetos são diferentes', () => {
    const dataAtual = new Date();
    let valueObject1 = new ValueObjectStub('prop1', 1, true, dataAtual);
    let valueObject2 = new ValueObjectStub('prop2', 1, true, dataAtual);
    expect(valueObject1.equals(valueObject2)).toBe(false);

    valueObject1 = new ValueObjectStub('prop1', 1, true, dataAtual);
    valueObject2 = new ValueObjectStub('prop1', 2, true, dataAtual);
    expect(valueObject1.equals(valueObject2)).toBe(false);

    valueObject1 = new ValueObjectStub('prop1', 1, true, dataAtual);
    valueObject2 = new ValueObjectStub('prop1', 1, false, dataAtual);
    expect(valueObject1.equals(valueObject2)).toBe(false);

    valueObject1 = new ValueObjectStub('prop1', 1, true, dataAtual);
    valueObject2 = new ValueObjectStub('prop1', 1, true, new Date(dataAtual.getFullYear(), dataAtual.getMonth(), dataAtual.getDate() + 1));
    expect(valueObject1.equals(valueObject2)).toBe(false);
  });
});
