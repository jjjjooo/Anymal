package com.example.demo.domain.tag;

import com.example.demo.domain.post.postTag.PostsTag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.*;
import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name ="tag")
@Embeddable
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private Long id;
    private String tag;

    @Column
    @OneToMany(mappedBy = "tag",orphanRemoval = true)
    private List<PostsTag> postsTags = new ArrayList<>();

    @Column
    private Long view;

    @Builder
    public Tag(String tag) {
        this.tag = tag;
    }



}