package com.example.demo.dto;

import com.example.demo.domain.member.Address;
import com.example.demo.domain.post.posts.Posts;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
public class PostsSaveRequestDto {

    @NotBlank(message = "제목을 적어주세요.")
    @Size(max=30,message="30자미만으로 작성해야 합니다.")
    private String title;

    @NotBlank(message = "성별을 선택해주세요.")
    private String gender;

    @NotBlank(message = "게시글 유형을 선택해주세요.")
    private String type2;

    @NotBlank(message = "특징을 적어주세요.")
    @Size(min=1,max=100,message="1자이상 100자미만으로 작성해야 합니다.")
    private String feature;

    private Address address;
    private Set<String> tags;

    public Posts toEntity(){
        return Posts.builder()
                .title(title)
                .gender(gender)
                .dType(type2)
                .feature(feature)
                .address(address)
                .build();
    }
}
