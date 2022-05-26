package com.example.demo.domain.chat;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatLogRepository extends JpaRepository<ChatLog, Long> {

    @EntityGraph(attributePaths = {"chatRoom"})
    List<ChatLog> findAllByChatRoomIdAndCheckedAndReceiver(Long id, boolean isRead, String name);

    //int countAllByChatRoomIdAndCheckedAndReceiver(Long id, boolean isRead, String name);

    int countAllByReceiverAndChecked(String name, boolean isRead);
}