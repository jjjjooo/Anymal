package com.example.demo.controller.posts;

import java.util.*;
import java.util.stream.Collectors;

import com.example.demo.domain.post.postImg.PostsImage;
import com.example.demo.domain.post.postTag.PostsTag;
import com.example.demo.domain.post.postTag.PostsTagRepository;
import com.example.demo.domain.post.posts.Posts;
import com.example.demo.domain.tag.Tag;
import com.example.demo.domain.tag.TagRepository;
import com.example.demo.dto.*;
import com.example.demo.service.post.PostsGoodService;
import com.example.demo.service.post.PostsImageService;
import com.example.demo.service.member.MemberService;
import com.example.demo.service.post.PostsService;
import com.example.demo.service.post.PostsTagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RequiredArgsConstructor
@RestController
@CrossOrigin("*")
@RequestMapping("/api/post")
public class PostsController {

    private final PostsService postsService;
    private final PostsImageService fileService;
    private final PostsGoodService postsGoodService;
    private final PostsTagService postsTagService;

    /**
     *  게시글 생성 -> 로그인 o
     */
    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public void save(@Valid @RequestPart(value="key",required = true) PostsSaveRequestDto requestDto,
                     @RequestPart(value="image",required = true) List<MultipartFile> images,
                     @AuthenticationPrincipal String userId ) throws Exception {
        postsService.save(requestDto,images);
    }

    /**
     *  게시글 개별조회 -> 로그인 x
     */
    @GetMapping("/{postId}")
    public PostsResponseDto find(@PathVariable Long postId){
            return postsService.find(postId);
        }
    /**
     *  전체조회 -> 페이징 -> 로그인 x
     */
    @GetMapping("/list/{page}")
    public ResponseEntity<Result> getPostsByPaging(@PathVariable int page,
                                                  @RequestParam(required = false) String dType,
                                                  @RequestParam(required = false) String area){
        return new ResponseEntity<>(postsService.paging(page, dType, area), HttpStatus.OK);
    }
    /**
     *  게시글 수정 ->  로그인 o
     */
    @PutMapping("/")
    public void update(@Valid @RequestPart(value="key",required = true) PostsUpdateRequestDto requestDto,
                       @RequestPart(value="image",required = false) List<MultipartFile> images) throws Exception{

        List<PostsImageResponseDto> dbPhotoList = fileService.findAllByPostsId(requestDto.getPostId());

        List<MultipartFile> multipartList = images;
        List<MultipartFile> addFileList = new ArrayList<>();

        if(CollectionUtils.isEmpty(images)){postsService.update(requestDto.getPostId(), requestDto, null);}

        else {

            if (CollectionUtils.isEmpty(dbPhotoList)) {
                for (MultipartFile multipartFile : images) addFileList.add(multipartFile);
            } else {
                List<String> dbOriginNameList = new ArrayList<>();
                for (MultipartFile multipartFile : images) {
                    String multipartOrigName = multipartFile.getOriginalFilename();
                    if (!dbOriginNameList.contains(multipartOrigName)) {
                        addFileList.add(multipartFile);
                    }
                }
                postsService.update(requestDto.getPostId(), requestDto, addFileList);
            }

        }
    }
    /**
     *  게시글 삭제 -> 로그인 o
     */
    @DeleteMapping("/{postId}")
    public void delete(@PathVariable Long postId){
        postsService.deletePost(postId);
    }

    /**
     *  좋아요 표시 -> 로그인 o
     */
    @PostMapping("/good/{postId}")
    public ResponseEntity<Boolean> setGood(@NotBlank @PathVariable(name="postId") Long postId) {
        return new ResponseEntity<>(postsGoodService.push(postId), HttpStatus.ACCEPTED);
    }
    /**
     *   내 좋아요 게시글 -> 로그인 o
     */
    @GetMapping("/my/good")
    public ResponseEntity<List<PostsResponseDto>> getAllGoodPostsByName(@RequestParam String name) {
        return new ResponseEntity<>(postsService.findAllGoodPostsByName(), HttpStatus.OK);
    }

    /**
     *   내 게시글 -> 로그인 o
     */
    @GetMapping("/my/{page}")
    public ResponseEntity<List<PostsResponseDto>> getAllPostsByName(@RequestParam String name) {
        return new ResponseEntity<>(postsService.findAllPostsByName(), HttpStatus.OK);
    }
    /**
     *  태그로 게시물 조회 -> 로그인 x
     */
    @GetMapping("/tag")
    public ResponseEntity<List<PostsResponseDto>> findByTag(@RequestParam(required = true)String tag,
                                                            @RequestParam(required = true)int page){

        return new ResponseEntity<>(postsTagService.findByTag(tag, page), HttpStatus.OK);
    }
}
