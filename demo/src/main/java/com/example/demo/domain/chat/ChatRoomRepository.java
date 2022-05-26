package com.example.demo.domain.chat;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @EntityGraph(attributePaths = {"posts"})
    List<ChatRoom> findAllBySenderOrReceiver(String sender, String receiver);
    ChatRoom findBySenderAndReceiverAndPostsId(String sender, String receiver, Long id);
    void deleteAllByPostsId(Long postId);
}