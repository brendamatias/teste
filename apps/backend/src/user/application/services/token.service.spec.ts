import jwt from 'jsonwebtoken';
import { TokenService } from './token.service';

jest.mock('jsonwebtoken');

describe('TokenService', () => {
  const tokenService = new TokenService();
  const payload = { id: '123', email: 'test@example.com' };
  const token = 'mockedToken';
  const SECRET_KEY = process.env.JWT_SECRET as string;

  beforeEach(() => {
    jest.clearAllMocks();
  });

  describe('generateToken', () => {
    it('should generate a token with the correct payload and options', () => {
      (jwt.sign as jest.Mock).mockReturnValue(token);

      const result = tokenService.generateToken(payload);

      expect(jwt.sign as any).toHaveBeenCalledWith(payload, SECRET_KEY, { expiresIn: '1h' });
      expect(result).toBe(token);
    });
  });

  describe('verifyToken', () => {
    it('should return the decoded payload if the token is valid', () => {
      (jwt.verify as jest.Mock).mockReturnValue(payload);

      const result = tokenService.verifyToken(token);

      expect(jwt.verify).toHaveBeenCalledWith(token, SECRET_KEY);
      expect(result).toEqual(payload);
    });

    it('should return null if the token is invalid', () => {
      (jwt.verify as jest.Mock).mockImplementation(() => {
        throw new Error('Invalid token');
      });

      const result = tokenService.verifyToken(token);

      expect(jwt.verify).toHaveBeenCalledWith(token, SECRET_KEY);
      expect(result).toBeNull();
    });
  });
});
