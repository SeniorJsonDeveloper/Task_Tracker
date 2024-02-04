package web.task_tracker.exception;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
    @SneakyThrows
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Object> checkEx(Exception e, WebRequest request){
        log.error("Exception is :",e);
        return handleException(e, request);
    }
}
