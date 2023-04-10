
import cors from "cors";
import 'dotenv/config';
import express from "express";
import * as admin from 'firebase-admin';
import { authenticateMiddleware } from './shared';
import errorHandler from './shared/infra/middlewares/error-handler.middleware';
import { router as todoRoutes } from './todo';
import { AddTodoItemUseCase, ChangeOrderTodoItemUseCase, CreateTodoUseCase, DeleteTodoItemUseCase, GetTodoUseCase, ListTodosUseCase, ShareTodoUseCase, ToggleTodoItemCompletionUseCase, UpdateTodoItemUseCase } from './todo/application';
import { TodoAccessService } from './todo/application/services';
import { TodoFirestoreRepository } from './todo/infra/repositories/firestore/todo-firestore.repository';
import { TodoController } from './todo/infra/web/todo.controller';
import {
  CreateUserUseCase,
  GetByEmailUseCase,
  LoginUseCase,
  TokenService,
  UserController,
  UserFirestoreRepository,
  router as userRoutes
} from './user';

const serviceAccount = process.env.GOOGLE_APPLICATION_CREDENTIALS || JSON.parse(process.env.GOOGLE_APPLICATION_CREDENTIALS_JSON as string);
admin.initializeApp({
  credential: admin.credential.cert(serviceAccount)
});

const app = express();
app.use(cors());
app.use(express.json());

const userRepository = new UserFirestoreRepository();
const createUserUseCase = new CreateUserUseCase(userRepository);
const getUserByEmalUseCase = new GetByEmailUseCase(userRepository);
const tokenService = new TokenService();
const loginUseCase = new LoginUseCase(userRepository, tokenService);
const userController = new UserController(
  createUserUseCase,
  getUserByEmalUseCase,
  loginUseCase
);
app.use("/users", (req, res, next) => {
  if (req.path === "/login" || req.path === "/register") {
    return next();
  } else {
    authenticateMiddleware(tokenService)(req, res, next);
  }
}, userRoutes(userController));

const todoAccessService = new TodoAccessService(userRepository);
const todoRepository = new TodoFirestoreRepository();
const addTodoItemUseCase = new AddTodoItemUseCase(todoAccessService, todoRepository);
const changeOrderTodoItemUseCase = new ChangeOrderTodoItemUseCase(todoAccessService, todoRepository);
const createTodoUseCase = new CreateTodoUseCase(todoRepository);
const getTodoUseCase = new GetTodoUseCase(todoAccessService, todoRepository);
const shareTodoUseCase = new ShareTodoUseCase(todoRepository, userRepository);
const updateTodoItemUseCase = new UpdateTodoItemUseCase(todoAccessService, todoRepository);
const listTodoUseCase = new ListTodosUseCase(todoRepository);
const toggleTodoItemCompletionUseCase = new ToggleTodoItemCompletionUseCase(todoAccessService, todoRepository);
const deleteTodoItemUseCase = new DeleteTodoItemUseCase(todoAccessService, todoRepository);

const todoController = new TodoController(
  addTodoItemUseCase,
  changeOrderTodoItemUseCase,
  createTodoUseCase,
  deleteTodoItemUseCase,
  getTodoUseCase,
  listTodoUseCase,
  shareTodoUseCase,
  toggleTodoItemCompletionUseCase,
  updateTodoItemUseCase,
);
app.use("/todos", authenticateMiddleware(tokenService), todoRoutes(todoController));



// Middleware de tratamento de erros
app.use(errorHandler);

const port = process.env.PORT || 3000;

app.listen(port, () => {
  console.log(`Server is running on http://localhost:${port}`);
});
