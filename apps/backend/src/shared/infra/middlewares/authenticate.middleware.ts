import { NextFunction, Request, Response } from 'express';
import { TokenService } from '../../../user';

export const authenticateMiddleware = (tokenService: TokenService) => {
  return async (req: Request, res: Response, next: NextFunction) => {
    const token = req.headers.authorization?.split(' ')[1]; // Geralmente o token vem no formato "Bearer <token>"

    if (!token) {
      res.status(401).json({ error: 'Access token is missing' });
      return;
    }

    const payload = tokenService.verifyToken(token);
    if (!payload) {
      res.status(401).json({ error: 'Invalid or expired token' });
      return;
    }

    // Adicione as informações do usuário na requisição para uso futuro, se necessário
    req.user = payload;
    next();
  };
};
