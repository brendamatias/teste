import jwt from 'jsonwebtoken';

const SECRET_KEY = process.env.JWT_SECRET as string;
const EXPIRES_IN = process.env.JWT_EXPIRES_IN || '1h';
export type TokenPayload = { id: string; email: string };
export class TokenService {

  public generateToken(payload: TokenPayload): string {
    return jwt.sign(payload, SECRET_KEY, { expiresIn: EXPIRES_IN });
  }

  public verifyToken(token: string): TokenPayload | null {
    try {
      return jwt.verify(token, SECRET_KEY) as TokenPayload;
    } catch (error) {
      return null;
    }
  }
}
