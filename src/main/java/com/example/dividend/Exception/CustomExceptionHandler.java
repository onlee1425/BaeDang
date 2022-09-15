package com.example.dividend.Exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice //필터와 비슷하게 바깥쪽 레이어에서 작동(서비스에서 지정된 에러가 발생하면 에러를 잡아 response로 전달)
public class CustomExceptionHandler {

    @ExceptionHandler(AbstractException.class)
    protected ResponseEntity<ErrorResponse> handleCustomException(AbstractException e){
       ErrorResponse errorResponse = ErrorResponse.builder()
                                       .code(e.getStatusCode())
                                       .message(e.getMessage())
                                       .build();

       return new ResponseEntity<>(errorResponse, HttpStatus.resolve(e.getStatusCode()));
    }

}
