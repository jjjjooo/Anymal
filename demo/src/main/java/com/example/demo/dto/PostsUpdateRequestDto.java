package com.example.demo.dto;

import com.example.demo.domain.member.Address;
import com.example.demo.domain.post.posts.Posts;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostsUpdateRequestDto {
    @NotNull(message = "비정상적인 접근입니다.")
    private Long postId;
    @NotBlank(message = "에러")
    private String type2;
    @NotBlank(message = "제목을 적어주세요.")
    @Size(min=1,max=30,message="1자이상 5자미만으로 작성해야 합니다.")
    private String title;
    @NotBlank(message = "성별을 선택해주세요.")
    private String gender;
    @NotBlank(message = "특징을 적어주세요.")
    @Size(min=1,max=100,message="1자이상 100자미만으로 작성해야 합니다.")
    private String feature;

    private Address address;
    private Set<String> tags;
    private Set<String> removeTag;
    private List<MultipartFile> images;

}
