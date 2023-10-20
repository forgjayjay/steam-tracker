package eo.forg.steamtracker.controller;

/*
 * This one is absolutely broken and requires more time to finish
 */


// import org.springframework.http.HttpHeaders;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
 import org.springframework.web.bind.annotation.ControllerAdvice;
// import org.springframework.web.bind.annotation.ExceptionHandler;
// import org.springframework.web.context.request.WebRequest;
// import org.springframework.web.servlet.NoHandlerFoundException;
 import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

// import eo.forg.steamtracker.exceptions.UserNotFoundException;

@ControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {
    // @ExceptionHandler(value = { UserNotFoundException.class })
    // protected ResponseEntity<Object> handleMissingUser() {
    //     return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    // }

    // @ExceptionHandler(value ={ NoHandlerFoundException.class })
    // protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
    //     return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    // }

    // @ExceptionHandler(value = { Exception.class })
    // public ResponseEntity<Object> handleServerError() {
    //     return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    // }
}
