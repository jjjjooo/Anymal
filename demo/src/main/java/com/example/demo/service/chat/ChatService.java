package com.example.demo.service.chat;

import com.example.demo.domain.chat.*;
import com.example.demo.domain.member.Member;
import com.example.demo.domain.member.MemberRepository;
import com.example.demo.domain.post.posts.Posts;
import com.example.demo.domain.post.posts.PostsRepository;
import com.example.demo.dto.chat.ChatRequestDto;
import com.example.demo.dto.chat.ChatRoomListResponseDto;
import com.example.demo.dto.chat.ChatRoomResponseDto;
import com.example.demo.exception.post.PostException;
import com.example.demo.exception.post.PostExceptionType;
import com.example.demo.service.member.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatLogRepository chatLogRepository;
    private final PostsRepository postsRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public ChatRoomResponseDto findAllChatLogByRoomId(Long roomId, String name) {
        ChatLogAllRead(roomId, name);
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).get();
        return ChatRoomResponseDto.toDto(chatRoom);
    }

    @Transactional
    public int findAllChatLogByRoomIdAndNotRead(List<ChatLog> chatLogs) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByName(username).get();
        String currentName= member.getName();
        int result = 0;
        for (ChatLog chatLog : chatLogs) {
            if (!chatLog.isChecked() && currentName.equals(chatLog.getReceiver())) {result++;}
        }
        return result;
    }
    @Transactional
    public List<ChatRoomListResponseDto> findAllChatRoomByName() {
        String name = SecurityUtil.getLoginUsername();
        return chatRoomRepository.findAllBySenderOrReceiver(name, name).stream()
                .filter(room -> room.getChatLogs().size() > 0)
                .map(logs -> {
                    int count = findAllChatLogByRoomIdAndNotRead(logs.getChatLogs());
                    String lastMessage = logs.getChatLogs().get(logs.getChatLogs().size() - 1).getMessage();
                    return ChatRoomListResponseDto.toDto(logs, count, lastMessage);
                })
                .sorted(Comparator.comparing(ChatRoomListResponseDto::getUpdateDate, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    @Transactional
    public Long addChatRoom(ChatRequestDto chatRequestDto) {

        ChatRoom existRoom = chatRoomRepository.
                findBySenderAndReceiverAndPostsId(
                        chatRequestDto.getSender(),
                        chatRequestDto.getReceiver(),
                        chatRequestDto.getPostId());

        if(existRoom != null) {
            return existRoom.getId();
        }
        else {
            ChatRoom chatRoom = ChatRoom.builder()
                    .sender(chatRequestDto.getSender())
                    .receiver(chatRequestDto.getReceiver())
                    .build();

            Posts posts = postsRepository.findById(chatRequestDto.getPostId()).orElseThrow(()
                    -> new PostException(PostExceptionType.POST_NOT_POUND));
            chatRoom.addPost(posts);

            return chatRoomRepository.save(chatRoom).getId();
        }
    }

    @Transactional
    public void addChatLog(ChatRequestDto chatRequestDto) {
        String sender = chatRequestDto.getSender();
        String receiver = chatRequestDto.getReceiver();
        String message = chatRequestDto.getMessage();

        ChatRoom chatRoom = chatRoomRepository.findById(chatRequestDto.getRoomId())
                .orElseThrow(NoSuchElementException::new);

        ChatLog chatLog = ChatLog.builder()
                .sender(sender)
                .receiver(receiver)
                .message(message)
                .build();

        chatLog.addChatRoom(chatRoom);
        chatRoom.changeDate();
    }

    @Transactional
    public void deleteChat(Long id){
        chatRoomRepository.deleteById(id);
    }

    @Transactional
    public void ChatLogAllRead(Long roomId, String name) {
        List<ChatLog> list = chatLogRepository.findAllByChatRoomIdAndCheckedAndReceiver(roomId, false, name);
        list.forEach(ChatLog::changeCheckedState);
    }

    @Transactional
    public int countAllNotReadChatLog(String name) {
        return chatLogRepository.countAllByReceiverAndChecked(name, false);
    }

}