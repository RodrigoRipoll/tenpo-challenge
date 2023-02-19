package ripoll.challenge.tenpoapi.config;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ripoll.challenge.tenpoapi.exception.ApiError;
import ripoll.challenge.tenpoapi.exception.CannotRetrieveTaxException;

import java.util.List;

@RestControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(
            ConstraintViolationException ex, WebRequest request) {
        List<String> errors = ex.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .toList();
        ApiError errorResponse = new ApiError(HttpStatus.BAD_REQUEST.value(), errors.toString());
        return handleExceptionInternal(ex, errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(CannotRetrieveTaxException.class)
    protected ResponseEntity<ApiError> handleCannotRetrieveTaxException(CannotRetrieveTaxException ex) {
        // NewRelic.noticeError(ex); -> this can help to set alarm
        ApiError errorResponse = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
