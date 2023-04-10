import { NextFunction, Request, Response } from "express";
import { ApplicationError, BaseError, DomainError, InfrastructureError } from '../../domain';

const errorHandler = (err: any, req: Request, res: Response, next: NextFunction) => {
  if (err instanceof BaseError) {
    const statusCode = err instanceof DomainError ? 400 :
      err instanceof ApplicationError ? 500 :
        err instanceof InfrastructureError ? 502 :
          500;

    res.status(statusCode).json({
      message: err.message,
      code: err.code,
      timestamp: err.timestamp,
      payload: err.payload,
    });
  } else {
    console.error("Generic Error:", err);
    res.status(500).json({
      message: "Something went wrong",
      error: err.message || "Internal Server Error",
    });
  }
};

export default errorHandler;
