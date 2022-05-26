package com.example.demo.exception.post;

import com.example.demo.exception.BaseException;
import com.example.demo.exception.BaseExceptionType;

public class PostException extends BaseException {
    private BaseExceptionType exceptionType;


    public PostException(BaseExceptionType exceptionType) {
        this.exceptionType = exceptionType;
    }

    @Override
    public BaseExceptionType getExceptionType() {
        return exceptionType;
    }
}
