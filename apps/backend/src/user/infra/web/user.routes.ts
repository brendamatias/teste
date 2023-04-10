import { Router } from 'express';
import { UserController } from './user.controller';

export const router = (userController: UserController) => {
  const router = Router();

  router.post('/register', (req, res, next) => userController.create(req, res, next));
  router.get('/email/:email', (req, res, next) => userController.getByEmail(req, res, next));
  router.post('/login', (req, res, next) => userController.login(req, res, next));
  return router;
};
