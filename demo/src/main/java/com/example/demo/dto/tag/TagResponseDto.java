package com.example.demo.dto.tag;

import com.example.demo.domain.tag.Tag;
import lombok.Data;

@Data

public class TagResponseDto {
    private Long id;
    private String tag;

    public TagResponseDto(Tag tag){
        this.id = tag.getId();
        this.tag = tag.getTag();
    }
}

