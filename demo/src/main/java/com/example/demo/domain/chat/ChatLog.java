package com.example.demo.domain.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Table(name="chat_log")
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_log_id")
    private Long id;
    private String message;
    private String sender;
    private String receiver;
    private LocalDateTime createDate;
    private boolean checked;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    public void changeCheckedState() {
        this.checked = true;
    }

    @Builder
    public ChatLog(String message, String sender, String receiver) {
        this.message = message;
        this.sender = sender;
        this.receiver = receiver;
        this.createDate = LocalDateTime.now();
        this.checked = false;
    }

    public void addChatRoom(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
        this.chatRoom.getChatLogs().add(this);
    }
}