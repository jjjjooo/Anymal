package com.example.demo.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.net.BindException;
import java.util.*;

@Slf4j
@RestControllerAdvice //RestController -> httpMsgConverter -> json+httpStatus
public class ExceptionAdvice {
    @ExceptionHandler(BaseException.class)
    public ResponseEntity handleBaseEx(BaseException exception){
        log.error("BaseException errorMessage(): {}",exception.getExceptionType().getErrorMessage());
        log.error("BaseException errorCode(): {}",exception.getExceptionType().getErrorCode());

        return new ResponseEntity(new ExceptionDto(exception.getExceptionType().getErrorCode(),
                                       exception.getExceptionType().getErrorMessage()),
                                       exception.getExceptionType().getHttpStatus());
    }
    //@Valid 에서 예외 발생할 수 있음
    @ExceptionHandler({BindException.class})
    public ResponseEntity handleValidEx(BindException exception){
        log.error("@ValidException 발생! {}", exception.getMessage());
        return new ResponseEntity(new ExceptionDto(2000,
                "@ValidException 발생! {}"),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity MissingServletRequestPartExceptionEx(BindException exception){
        log.error("@RequestPartException", exception.getMessage() );
        return new ResponseEntity(new ExceptionDto(1000,"이미지는 한 장 이상 업로드해주세요."),HttpStatus.BAD_REQUEST);
    }
    //HttpMessageNotReadableException  => json 파싱 오류
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity httpMessageNotReadableExceptionEx(HttpMessageNotReadableException exception){

        log.error("Json을 파싱하는 과정에서 예외 발생! {}", exception.getMessage() );
        return new ResponseEntity(new ExceptionDto(3000,"Json을 파싱하는 과정에서 예외 발생! {}"),HttpStatus.BAD_REQUEST);
    }
    //@Valid예외
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity processValidationError(MethodArgumentNotValidException exception) {
        log.error("검증에러", exception.getBindingResult().getFieldErrors());

        List<String> errors = new ArrayList<>();

        for(FieldError result : exception.getBindingResult().getFieldErrors()){
            errors.add(result.getDefaultMessage());
        }

        return new ResponseEntity(new ExceptionsDto(4000,errors),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleMemberEx(Exception exception) {
        //exception.printStackTrace(); 성능저하, 보안 이슈
        exception.getMessage();
        exception.toString();
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }


    @Data
    @AllArgsConstructor
    static class ExceptionDto {
        private Integer errorCode;
        private String errorMessage;
    }
    @Data
    @AllArgsConstructor
    static class ExceptionsDto {
        private Integer errorCode;
        private List<String> errorMessage;
    }
}