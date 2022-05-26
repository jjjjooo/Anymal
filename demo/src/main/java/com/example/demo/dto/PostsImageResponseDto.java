package com.example.demo.dto;

import com.example.demo.domain.post.postImg.PostsImage;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostsImageResponseDto {
    private Long fileId;  // 파일 id
    private String originFile;

    public PostsImageResponseDto(PostsImage postsImage){
        this.fileId = postsImage.getId();
        this.originFile=postsImage.getOrigFileName();
    }
}

