package com.example.demo.dto;

import com.example.demo.domain.member.Address;
import com.example.demo.domain.post.posts.Posts;
import com.example.demo.dto.tag.TagResponseDto;
import lombok.Getter;


import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Getter
public class PostsResponseDto {
    private Long id;
    private String username;
    private String title;
    private String feature;
    private String gender;
    private String dType;
    private String lastTime;
    private Address address;
    private Long thumbnailId;
    private boolean good;

    private List<TagResponseDto> tag;
    private List<PostsImageResponseDto> fileId;
    private String originFile;
    public PostsResponseDto(Posts posts,Boolean good){
        this.id = posts.getId();
        this.title = posts.getTitle();
        this.feature = posts.getFeature();
        this.username = posts.getMember().getName();
        this.dType = posts.getDType();
        this.thumbnailId = posts.getImages().get(0).getId();
        this.fileId = posts.getImages().stream().map(PostsImageResponseDto::new).collect(Collectors.toList());
        this.good = good;
        this.address = posts.getAddress();
        this.gender = posts.getGender();
        this.lastTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH시").format(posts.getModified());
        this.tag=
                posts.getPostsTags().stream()
                        .map(tag -> new TagResponseDto(tag.getTag()))
                        .sorted(Comparator.comparing(TagResponseDto::getId, Comparator.reverseOrder()))
                        .collect(Collectors.toList());
    }
}
