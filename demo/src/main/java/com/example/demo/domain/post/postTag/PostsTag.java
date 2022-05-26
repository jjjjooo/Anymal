package com.example.demo.domain.post.postTag;

import com.example.demo.domain.post.posts.Posts;
import com.example.demo.domain.tag.Tag;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name= "posts_tag")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class PostsTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "posts_tag_id")
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "posts_id")
    private Posts posts;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tag;

    public void addPosts(Posts posts) {
        this.posts = posts;
        this.posts.getPostsTags().add(this);
    }
    public void addTag(Tag tag){
        this.tag = tag;
    }

    public void deletePost(Posts posts){
        this.posts = null;
        this.tag = null;
    }

}
