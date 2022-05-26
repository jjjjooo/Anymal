package com.example.demo.domain.post.postImg;

import com.example.demo.domain.BaseTimeEntity;
import com.example.demo.domain.post.posts.Posts;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Table(name = "img")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@Entity
public class PostsImage extends BaseTimeEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="img_id")
    private Long id;

    @Column(nullable=false)
    private String origFileName;

    @Column(nullable=false,unique = true)
    private String filePath;

    @Column(nullable = false)
    private Long fileSize;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "posts_id")
    private Posts posts;

    @Builder
    public PostsImage(String origFileName, String filePath, Long fileSize) {
        this.origFileName = origFileName;
        this.filePath = filePath;
        this.fileSize = fileSize;
    }

    public void setPosts(Posts posts){
        this.posts = posts;
        if(!posts.getImages().contains(this)){
            posts.getImages().add(this);
        }
    }
}
