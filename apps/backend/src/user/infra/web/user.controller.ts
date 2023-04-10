import { NextFunction, Request, Response } from 'express';
import { CreateUserUseCase, GetByEmailUseCase, LoginUseCase } from '../../application';

export class UserController {
  constructor(
    private readonly createUserUseCase: CreateUserUseCase,
    private readonly getByEmailUseCase: GetByEmailUseCase,
    private readonly loginUseCase: LoginUseCase,
  ) { }

  async create(req: Request, res: Response, next: NextFunction) {
    try {
      const createUserOutput = await this.createUserUseCase.execute({
        name: req.body.name,
        email: req.body.email,
        password: req.body.password,
      });

      res.status(201).send(createUserOutput);
    } catch (error) {
      next(error);
    }
  }

  async getByEmail(req: Request, res: Response, next: NextFunction) {
    try {
      const user = await this.getByEmailUseCase.execute({ email: req.params.email });
      res.status(200).send(user);
    } catch (error) {
      next(error);
    }
  }

  async login(req: Request, res: Response, next: NextFunction) {
    try {
      const loginOutput = await this.loginUseCase.execute({
        email: req.body.email,
        password: req.body.password,
      });

      res.status(201).send(loginOutput);
    } catch (error) {
      next(error);
    }
  }
}
