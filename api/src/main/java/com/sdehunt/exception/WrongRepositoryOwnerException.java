package com.sdehunt.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Repository owner does not fit token user")
public class WrongRepositoryOwnerException extends RuntimeException {
}
