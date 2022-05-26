package com.example.demo.dto.chat;

import com.example.demo.domain.chat.ChatRoom;
import lombok.Builder;
import lombok.Getter;
import java.util.List;
import java.util.stream.Collectors;


@Getter
@Builder
public class ChatRoomResponseDto {
    private final Long roomId;

    private final Long postId;
    private final String postAuth;
    private final String postTitle;
    private final List<ChatLogResponseDto> chatLogs;
    private final Long image;

    public static ChatRoomResponseDto toDto(ChatRoom chatRoom) {
        return ChatRoomResponseDto.builder()
                .roomId(chatRoom.getId())
                .postId(chatRoom.getPosts().getId())
                .image(chatRoom.getPosts().getImages().get(0).getId())
                .postAuth(chatRoom.getPosts().getMember().getName())
                .postTitle(chatRoom.getPosts().getTitle())
                .chatLogs(chatRoom.getChatLogs().stream()
                        .map(ChatLogResponseDto::new)
                        .collect(Collectors.toList()))
                .build();
    }
}
