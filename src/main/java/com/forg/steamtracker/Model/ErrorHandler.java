package com.forg.steamtracker.Model;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.forg.steamtracker.Model.Exceptions.UserNotFoundException;

@ControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { UserNotFoundException.class })
    protected ResponseEntity<Object> handleMissingUser(){
        return new ResponseEntity<>(HttpStatusCode.valueOf(500));
    }

    @ExceptionHandler(value = { NoHandlerFoundException.class })
    public ResponseEntity<Object> handleError404(){
        return new ResponseEntity<>(HttpStatusCode.valueOf(404));
    }

    @ExceptionHandler(value = { Exception.class })
    public ResponseEntity<Object> handleServerError(){
        return new ResponseEntity<>(HttpStatusCode.valueOf(500));
    }
}