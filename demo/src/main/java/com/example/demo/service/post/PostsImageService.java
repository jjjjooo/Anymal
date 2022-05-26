package com.example.demo.service.post;

import com.example.demo.domain.post.postImg.PostsImageRepository;
import com.example.demo.domain.post.postImg.PostsImage;
import com.example.demo.dto.PostsImageDto;
import com.example.demo.dto.PostsImageResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostsImageService {
    private final PostsImageRepository photoRepository;

    /**
     * 이미지 개별 조회
     */
    @Transactional(readOnly = true)
    public PostsImageDto findByFileId(Long id){

        PostsImage postsImage = photoRepository.findById(id).orElseThrow(()
                -> new NullPointerException("해당 파일이 존재하지 않습니다."));

        PostsImageDto postsImageDto = PostsImageDto.builder()
                .origFileName(postsImage.getOrigFileName())
                .filePath(postsImage.getFilePath())
                .fileSize(postsImage.getFileSize())
                .build();

        return postsImageDto;
    }

    /**
     * 이미지 전체 조회
     */
    @Transactional(readOnly = true)
    public List<PostsImageResponseDto> findAllByPostsId(Long postsId){

        List<PostsImage> photoList = photoRepository.findAllByPostsId(postsId).orElseThrow(()
                -> new NullPointerException("해당 파일이 존재하지 않습니다."));

        return photoList.stream()
                .map(PostsImageResponseDto::new)
                .collect(Collectors.toList());
    }

}

