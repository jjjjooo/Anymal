package com.example.demo.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@NoArgsConstructor
public class PostsImageDto {
    private Long id;
    private String origFileName;
    private String filePath;
    private Long fileSize;

    @Builder
    public PostsImageDto(Long id, String origFileName, String filePath, Long fileSize){
        this.id = id;
        this.origFileName = origFileName;
        this.filePath = filePath;
        this.fileSize = fileSize;
    }
}
