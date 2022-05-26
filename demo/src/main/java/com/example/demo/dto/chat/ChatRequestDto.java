package com.example.demo.dto.chat;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ChatRequestDto {


    private Long roomId;
    private Long postId;

    @NotBlank(message = "로그인이 필요합니다.")
    private String sender;

    @NotBlank(message = "채팅 수신인은 필수 항목입니다.")
    private String receiver;
    private String message;

}
