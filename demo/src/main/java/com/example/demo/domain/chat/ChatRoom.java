package com.example.demo.domain.chat;

import com.example.demo.domain.post.posts.Posts;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(name="chat_room")
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_room_id")
    private Long id;
    @NotNull
    private String sender;
    @NotNull
    private String receiver;
    @NotNull
    private LocalDateTime updateDate;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "posts_id", nullable = false)
    private Posts posts;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL)
    private List<ChatLog> chatLogs = new ArrayList<>();

    public void changeDate() {
        this.updateDate = LocalDateTime.now();
    }

    @Builder
    public ChatRoom(String sender, String receiver) {
        this.sender = sender;
        this.receiver = receiver;
        this.updateDate = LocalDateTime.now();
    }

    public void addPost(Posts posts) {
        this.posts = posts;
    }

    public void deletePost() { this.posts = null;}

}
