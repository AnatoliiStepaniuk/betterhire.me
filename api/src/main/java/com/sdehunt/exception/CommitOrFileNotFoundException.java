package com.sdehunt.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Commit or file not found")
public class CommitOrFileNotFoundException extends RuntimeException { // TODO remove?
}
