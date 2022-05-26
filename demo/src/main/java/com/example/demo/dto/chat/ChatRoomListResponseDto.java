package com.example.demo.dto.chat;

import com.example.demo.domain.chat.ChatRoom;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatRoomListResponseDto {
    private final Long roomId;

    private final Long postId;
    private final String postTitle;
    private final String sender;
    private final String receiver;
    private final int notRead;
    private final String lastMessage;
    private final String updateDate;
    private final Long image;

    public static ChatRoomListResponseDto toDto(ChatRoom chatRoom, int count, String lastMessage) {
        return ChatRoomListResponseDto.builder()
                .roomId(chatRoom.getId())
                .postId(chatRoom.getPosts().getId())
                .image(chatRoom.getPosts().getImages().get(0).getId())
                .postTitle(chatRoom.getPosts().getTitle())
                .notRead(count)
                .sender(chatRoom.getSender())
                .receiver(chatRoom.getReceiver())
                .lastMessage(lastMessage)
                .updateDate(chatRoom.getUpdateDate().toString())
                .build();
    }
}
