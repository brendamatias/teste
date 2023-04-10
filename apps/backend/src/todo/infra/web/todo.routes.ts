import { Router } from 'express';
import { TodoController } from './todo.controller';

export const router = (todoController: TodoController) => {
  const router = Router();

  // Usar a instância do controller
  router.post('/', (req, res, next) => todoController.create(req, res, next));
  router.get('/', (req, res, next) => todoController.listTodos(req, res, next));
  router.get('/:todoId', (req, res, next) => todoController.getById(req, res, next));
  router.post('/:todoId/items', (req, res, next) => todoController.addItem(req, res, next));
  router.put('/:todoId/items/:itemId', (req, res, next) => todoController.updateItem(req, res, next));
  router.put('/:todoId/items/:itemId/order', (req, res, next) => todoController.changeOrderItem(req, res, next));
  router.put('/:todoId/share', (req, res, next) => todoController.share(req, res, next));
  router.put('/:todoId/items/:itemId/toggle-completion', (req, res, next) => todoController.toggleTodoItemCompletion(req, res, next));
  router.delete('/:todoId/items/:itemId', (req, res, next) => todoController.deleteItem(req, res, next));
  return router;
};
