package com.example.demo.exception.member;

import com.example.demo.exception.BaseExceptionType;
import org.springframework.http.HttpStatus;

public enum MemberExceptionType implements BaseExceptionType {
    //회원관련 6xx
    ALREADY_EXIST_NAME(600, HttpStatus.BAD_REQUEST, "이미 존재하는 아이디입니다."),
    ALREADY_EXIST_EMAIL(601, HttpStatus.BAD_REQUEST,"이미 존재하는 이메일입니다."),
    WRONG_PASSWORD(602,HttpStatus.BAD_REQUEST, "비밀번호가 잘못되었습니다."),
    NOT_FOUND_MEMBER(603, HttpStatus.BAD_REQUEST, "회원 정보가 없습니다."),
    NOT_FOUND_MEMBER_EMAIL(604,HttpStatus.BAD_REQUEST, "올바르지 않은 이메일 입니다."),
    NOT_ALLOW(605, HttpStatus.NOT_ACCEPTABLE,"권한이 없습니다.");

    private int errorCode;
    private HttpStatus httpStatus;
    private String errorMessage;

    MemberExceptionType(int errorCode, HttpStatus httpStatus, String errorMessage) {
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }

    @Override
    public int getErrorCode() {
        return this.errorCode;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }

    @Override
    public String getErrorMessage() {
        return this.errorMessage;
    }
}