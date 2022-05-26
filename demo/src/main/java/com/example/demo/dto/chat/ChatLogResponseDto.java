package com.example.demo.dto.chat;

import com.example.demo.domain.chat.ChatLog;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class ChatLogResponseDto {
    private Long logId;
    private String message;
    private String sender;
    private String receiver;
    private String createDate;

    public ChatLogResponseDto(ChatLog chatLog) {
        this.message = chatLog.getMessage();
        this.sender = chatLog.getSender();
        this.receiver = chatLog.getReceiver();
        this.createDate = DateTimeFormatter.ofPattern("yyyy-MM-dd HH시").format(LocalDateTime.now());
    }

    public ChatLogResponseDto(ChatRequestDto requestDto) {
        this.sender = requestDto.getSender();
        this.receiver = requestDto.getReceiver();
        this.message = requestDto.getMessage();
        this.createDate = DateTimeFormatter.ofPattern("yyyy-MM-dd HH시").format(LocalDateTime.now());
    }
}