package per.duyd.interview.tre.exception;

import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;
import per.duyd.interview.tre.dto.response.GenericErrorResponse;
import per.duyd.interview.tre.dto.response.ResponseErrorCode;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
  @ExceptionHandler({InvalidInputFileException.class, InvalidSearchCriteriaException.class,
      InvalidReportNameException.class, InvalidInputFolderException.class})
  protected ResponseEntity<GenericErrorResponse> handleCustomClientErrors(Exception ex) {
    logException(ex);
    return new ResponseEntity<>(buildErrorResponse(ResponseErrorCode.INVALID_REQUEST,
        ex.getMessage()),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler({HttpMessageNotReadableException.class,
      MethodArgumentTypeMismatchException.class})
  protected ResponseEntity<GenericErrorResponse> handleInvalidRequestSyntaxExceptions(
      Exception ex) {
    logException(ex);
    return new ResponseEntity<>(buildErrorResponse(ResponseErrorCode.INVALID_REQUEST,
        "Request data contain malformed values"),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler({MethodArgumentNotValidException.class})
  protected ResponseEntity<GenericErrorResponse> handleInvalidJsonSyntaxExceptions(
      MethodArgumentNotValidException ex) {
    logException(ex);
    String errorMessage =
        ex.getBindingResult().getAllErrors().stream().map(this::getFieldErrorMessage)
            .collect(Collectors.joining("; "));
    return new ResponseEntity<>(buildErrorResponse(ResponseErrorCode.INVALID_REQUEST, errorMessage),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler({DataIntegrityViolationException.class})
  protected ResponseEntity<GenericErrorResponse> handleDuplicateDataExceptions(Exception ex) {
    logException(ex);
    return new ResponseEntity<>(buildErrorResponse(ResponseErrorCode.INVALID_REQUEST,
        "Data has already existed"), HttpStatus.BAD_REQUEST);

  }

  @ExceptionHandler({Exception.class})
  protected ResponseEntity<GenericErrorResponse> handleUncaughtExceptions(Exception ex)
      throws Exception {
    logException(ex);

    if (ex instanceof ResponseStatusException) {
      throw ex;
    } else {
      return new ResponseEntity<>(buildErrorResponse(ResponseErrorCode.INTERNAL_SERVER_ERROR,
          "Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  private String getFieldErrorMessage(ObjectError error) {
    if (error instanceof FieldError fieldError) {
      return String.format("%s %s", fieldError.getField(), fieldError.getDefaultMessage());
    } else {
      return String.format("%s %s", error.getObjectName(), error.getDefaultMessage());
    }
  }

  private GenericErrorResponse buildErrorResponse(ResponseErrorCode errorCode,
                                                  String errorMessage) {
    return GenericErrorResponse.builder().errorCode(errorCode)
        .errorMessage(errorMessage).build();
  }

  private void logException(Exception ex) {
    log.error("exception=\"{}\", rootCause=\"{}\"", ex.getClass().getName(),
        ExceptionUtils.getRootCauseMessage(ex));
    log.debug("exception details", ex);
  }
}
