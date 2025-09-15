package no.gjensidige.product.controller.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Response controller for IllegalArgumentException
 *
 */
@ControllerAdvice
public class IllegalArgumentAdvice {

    @ResponseBody
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String illegalArgumentAdvice(IllegalArgumentException ex) {
        return ex.getMessage();
    }

}
