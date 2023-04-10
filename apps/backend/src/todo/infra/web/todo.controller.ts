import { NextFunction, Request, Response } from 'express';
import { TokenPayload } from '../../../user';
import {
  AddTodoItemUseCase,
  ChangeOrderTodoItemUseCase,
  CreateTodoUseCase,
  DeleteTodoItemUseCase,
  GetTodoUseCase,
  ListTodosUseCase,
  ShareTodoUseCase,
  ToggleTodoItemCompletionUseCase,
  UpdateTodoItemUseCase
} from '../../application';

export class TodoController {
  constructor(
    private readonly addTodoItemUseCase: AddTodoItemUseCase,
    private readonly changeOrderTodoItemUseCase: ChangeOrderTodoItemUseCase,
    private readonly createTodoUseCase: CreateTodoUseCase,
    private readonly deleteTodoItemUseCase: DeleteTodoItemUseCase,
    private readonly getTodoUseCase: GetTodoUseCase,
    private readonly listTodosUseCase: ListTodosUseCase,
    private readonly shareTodoUseCase: ShareTodoUseCase,
    private readonly toggleTodoItemCompletionUseCase: ToggleTodoItemCompletionUseCase,
    private readonly updateTodoItemUseCase: UpdateTodoItemUseCase,
  ) { }

  async create(req: Request, res: Response, next: NextFunction) {
    try {
      const user = req.user as TokenPayload;
      const createTodoOutput = await this.createTodoUseCase.execute({
        title: req.body.title,
        userId: user.id,
      });

      res.status(201).send(createTodoOutput);
    } catch (error) {
      next(error);
    }
  }

  async getById(req: Request, res: Response, next: NextFunction) {
    try {
      const user = req.user as TokenPayload;
      const todo = await this.getTodoUseCase.execute({ todoId: req.params.todoId, userId: user.id });

      res.status(200).send(todo);
    } catch (error) {
      next(error);
    }
  }

  async listTodos(req: Request, res: Response, next: NextFunction) {
    try {
      const user = req.user as TokenPayload;
      const todos = await this.listTodosUseCase.execute({ userId: user.id, email: user.email });

      res.status(200).send(todos);
    } catch (error) {
      next(error);
    }
  }

  async share(req: Request, res: Response, next: NextFunction) {
    try {
      const user = req.user as TokenPayload;
      await this.shareTodoUseCase.execute({
        todoId: req.params.todoId,
        userId: user.id,
        emailToShare: req.body.email,
      });

      res.status(200).send();
    } catch (error) {
      next(error);
    }
  }

  async addItem(req: Request, res: Response, next: NextFunction) {
    try {
      const user = req.user as TokenPayload;
      const addTodoItemOutput = await this.addTodoItemUseCase.execute({
        todoId: req.params.todoId,
        content: req.body.content,
        userId: user.id,
      });

      res.status(201).send(addTodoItemOutput);
    } catch (error) {
      next(error);
    }
  }

  async updateItem(req: Request, res: Response, next: NextFunction) {
    try {
      const user = req.user as TokenPayload;
      await this.updateTodoItemUseCase.execute({
        todoId: req.params.todoId,
        todoItemId: req.params.itemId,
        content: req.body.content,
        userId: user.id,
      });

      res.status(200).send();
    } catch (error) {
      next(error);
    }
  }

  async toggleTodoItemCompletion(req: Request, res: Response, next: NextFunction) {
    try {
      const user = req.user as TokenPayload;
      await this.toggleTodoItemCompletionUseCase.execute({
        todoId: req.params.todoId,
        todoItemId: req.params.itemId,
        userId: user.id,
      });

      res.status(200).send();
    } catch (error) {
      next(error);
    }
  }

  async changeOrderItem(req: Request, res: Response, next: NextFunction) {
    try {
      const user = req.user as TokenPayload;
      const changeOrderOutput = await this.changeOrderTodoItemUseCase.execute({
        todoId: req.params.todoId,
        todoItemId: req.params.itemId,
        newOrder: req.body.newOrder,
        userId: user.id,
      });

      res.status(200).send(changeOrderOutput);
    } catch (error) {
      next(error);
    }
  }

  async deleteItem(req: Request, res: Response, next: NextFunction) {
    try {
      const user = req.user as TokenPayload;
      await this.deleteTodoItemUseCase.execute({
        todoId: req.params.todoId,
        itemId: req.params.itemId,
        userId: user.id,
      });

      res.status(200).send();
    } catch (error) {
      next(error);
    }
  }
}
