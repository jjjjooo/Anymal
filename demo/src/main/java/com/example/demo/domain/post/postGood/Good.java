package com.example.demo.domain.post.postGood;

import com.example.demo.domain.member.Member;
import com.example.demo.domain.post.posts.Posts;
import lombok.Getter;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Table(name = "good")
@Getter
@Entity
public class Good {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "good_id")
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="posts_id")
    private Posts posts;

    public void addMember(Member member) {
        this.member = member;
    }

    public void addPosts(Posts posts) {
        this.posts = posts;
    }

}
