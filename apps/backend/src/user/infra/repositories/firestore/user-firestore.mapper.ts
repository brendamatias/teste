import { User, UserProps } from '../../../domain';

export class UserFirestoreMapper {
  public toEntity(data: UserProps): User {
    return User.restore({
      id: data.id,
      name: data.name,
      email: data.email,
      password: data.password,
      createdAt: (data.createdAt as any).toDate(),
      updatedAt: (data.updatedAt as any).toDate(),
    });
  }

  public fromEntity(entity: User): UserProps {
    return entity.toJSON();
  }
}
