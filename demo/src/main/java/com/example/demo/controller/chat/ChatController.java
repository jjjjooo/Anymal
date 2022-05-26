package com.example.demo.controller.chat;

import com.example.demo.dto.chat.ChatLogResponseDto;
import com.example.demo.dto.chat.ChatRequestDto;
import com.example.demo.dto.chat.ChatRoomListResponseDto;
import com.example.demo.dto.chat.ChatRoomResponseDto;
import com.example.demo.service.chat.ChatService;
import com.example.demo.service.member.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
public class ChatController{
    private final SimpMessageSendingOperations template;
    private final ChatService chatService;

    //메시지 보내기
    @MessageMapping("/chat/msg")
    public void send(@RequestBody @Valid ChatRequestDto chatRequestDto){
        template.convertAndSend("/sub/msg" + chatRequestDto.getRoomId(),
                new ChatLogResponseDto(chatRequestDto));
        chatService.addChatLog(chatRequestDto);
    }
    //방 생성성
   @PostMapping("/chat")
    public ResponseEntity<?> addChatRoom(@Valid @RequestBody ChatRequestDto chatRequestDto) {
        return new ResponseEntity<>(chatService.addChatRoom(chatRequestDto), HttpStatus.CREATED);

    }
    //채팅 내역 조회
    @GetMapping("/chat/{roomId}")
    public ResponseEntity<ChatRoomResponseDto> getChatLogByRoomId(@PathVariable Long roomId,
                                                                  @RequestParam String name) {
        return new ResponseEntity<>(chatService.findAllChatLogByRoomId(roomId, name), HttpStatus.OK);
    }
    //전체 채팅방 조회
    @GetMapping("/chat")
    public ResponseEntity<List<ChatRoomListResponseDto>> getChatRooms() {

        return new ResponseEntity<>(chatService.findAllChatRoomByName(), HttpStatus.OK);
    }

    @DeleteMapping("/chat/{id}")
    public void deleteRooms(@PathVariable Long id){
        chatService.deleteChat(id);
    }

    //읽지 않은 채팅 수 조회 //뷰에 호출 및 뱃지 추가해야함.
    @GetMapping("/chat/read")
    public ResponseEntity<Integer> getChatLogNotRead(@RequestParam String name) {
        return new ResponseEntity<>(chatService.countAllNotReadChatLog(name), HttpStatus.OK);
    }

}