package com.capstone.unwind.exception;

import com.capstone.unwind.model.ExceptionDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserDoesNotExistException.class)
    public ResponseEntity<?> handleUserDoesNotExist(UserDoesNotExistException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ExceptionDTO.builder()
                        .message(ex.getMessage())
                        .build());
    }
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<?> handleUserAlreadyExistsException(UserAlreadyExistsException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ExceptionDTO.builder()
                .message(ex.getMessage())
                .build());
    }
    @ExceptionHandler(AccountSuspendedException.class)
    public ResponseEntity<?> handleAccountSuspendedException(AccountSuspendedException ex){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ExceptionDTO.builder()
                .message(ex.getMessage())
                .build());
    }
    @ExceptionHandler(InvalidateException.class)
    public ResponseEntity<?> handleInvalidateException(UserDoesNotExistException ex){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ExceptionDTO.builder()
                .message(ex.getMessage())
                .build());
    }
    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<?> handleTokenExpiredException(TokenExpiredException ex){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ExceptionDTO.builder()
                .message(ex.getMessage())
                .build());
    }
    @ExceptionHandler(RoleDoesNotAcceptException.class)
    public ResponseEntity<?> handleRoleDoesNotExistException(RoleDoesNotAcceptException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ExceptionDTO.builder()
                .message(ex.getMessage())
                .build());
    }

    @ExceptionHandler(EntityAlreadyExist.class)
    public ResponseEntity<?> handleEntityAlreadyExistException(EntityAlreadyExist ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ExceptionDTO.builder()
                .message(ex.getMessage())
                .build());
    }
    @ExceptionHandler(EntityDoesNotExistException.class)
    public ResponseEntity<?> handleEntityDoesNotExistException(EntityDoesNotExistException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ExceptionDTO.builder()
                .message(ex.getMessage())
                .build());
    }
    @ExceptionHandler(ErrMessageException.class)
    public ResponseEntity<?> handleErrMessageException(ErrMessageException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ExceptionDTO.builder()
                .message(ex.getMessage())
                .build());
    }
    @ExceptionHandler(OptionalNotFoundException.class)
    public ResponseEntity<?> handleOptionalNotFoundException(OptionalNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ExceptionDTO.builder()
                .message(ex.getMessage())
                .build());
    }
}
