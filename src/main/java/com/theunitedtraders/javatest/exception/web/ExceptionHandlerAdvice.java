package com.theunitedtraders.javatest.exception.web;

import com.theunitedtraders.javatest.domain.json.response.base.SimpleMessageResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

/**
 * Custom exception handler for controllers. For security handler see {@link com.theunitedtraders.javatest.controller.handler.WebApplicationExceptionHandlerController}.
 */
@ControllerAdvice
public class ExceptionHandlerAdvice {

    /**
     * Custom handler for {@link HttpRequestMethodNotSupportedException}.
     *
     * @param exception exception instance.
     * @param request   request instance.
     * @return "Method not supported" message container.
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    public ResponseEntity<SimpleMessageResponse> httpRequestMethodNotSupportedExceptionHandler(Exception exception, WebRequest request) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(new SimpleMessageResponse("Method not supported"), responseHeaders, HttpStatus.METHOD_NOT_ALLOWED);
    }
}
