package lt.gzeskas.demo.albumsbrowser.web.exception;

import lt.gzeskas.demo.albumsbrowser.services.remote.clients.itunes.exception.ItunesRequestsExceededException;
import lt.gzeskas.demo.albumsbrowser.web.view.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({ ItunesRequestsExceededException.class})
    public final ResponseEntity<ApiError> handleItunesRequestsExceededException(ItunesRequestsExceededException ex) {
        return new ResponseEntity<>(new ApiError("To many request, try next hour."), HttpStatus.TOO_MANY_REQUESTS);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public final ResponseEntity<ApiError> handleConstraintViolationException(ConstraintViolationException ex) {
        return new ResponseEntity<>(new ApiError("Input field is not valid, message: " + ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

}
