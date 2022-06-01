package com.example.demo.service.post;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import com.example.demo.domain.chat.ChatRoomRepository;
import com.example.demo.domain.post.postGood.Good;
import com.example.demo.domain.post.postGood.GoodRepository;
import com.example.demo.domain.post.postImg.PostsImageRepository;
import com.example.demo.domain.member.Member;
import com.example.demo.domain.member.MemberRepository;
import com.example.demo.domain.post.posts.PostsQueryRepository;
import com.example.demo.domain.post.posts.PostsRepository;
import com.example.demo.domain.post.postImg.PostsImage;
import com.example.demo.domain.post.posts.Posts;
import com.example.demo.dto.*;
import com.example.demo.exception.member.MemberException;
import com.example.demo.exception.member.MemberExceptionType;
import com.example.demo.exception.post.PostException;
import com.example.demo.exception.post.PostExceptionType;
import com.example.demo.service.fileHandler.FileHandler;
import com.example.demo.service.member.SecurityUtil;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostsService {
    private final PostsTagService postsTagService;
    private final PostsRepository postsRepository;
    private final PostsImageRepository postsImageRepository;
    private final FileHandler fileHandler;
    private final MemberRepository memberRepository;
    private final PostsQueryRepository queryRepository;
    private final GoodRepository goodRepository;
    private final PostsGoodService goodService;
    private final ChatRoomRepository chatRoomRepository;


    @Transactional
    public void save(PostsSaveRequestDto requestDto,List<MultipartFile> files) throws Exception {

        String username = SecurityUtil.getLoginUsername();
        Member member = memberRepository.findByName(username)
                .orElseThrow(()->new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));
        Posts posts = requestDto.toEntity();
        posts.addMember(member);
        List<PostsImage> images = fileHandler.parseFileInfo(posts,files);
        if(!images.isEmpty()){
            for(PostsImage image : images){
                PostsImage ip = postsImageRepository.save(image);
                posts.addImages(ip);
            }
        }
        postsRepository.save(posts);
        postsTagService.addPostTag(posts, requestDto.getTags());
    }

    @Transactional
    public PostsResponseDto find(Long postId){
        Posts posts = postsRepository.findById(postId).orElseThrow(()
                -> new PostException(PostExceptionType.POST_NOT_POUND));
        return getPostsResponseDto(posts);
    }

    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto, List<MultipartFile> files
    )throws Exception {
        Posts posts = postsRepository.findById(id).orElseThrow(()
                -> new PostException(PostExceptionType.POST_NOT_POUND));

        if(files != null){

            List<PostsImage> photoList = fileHandler.parseFileInfo(posts, files);
            if (!photoList.isEmpty()) {

                for (PostsImage photo : photoList) {
                    postsImageRepository.save(photo);
                    posts.addImages(photo);
                }
            }
        }

        if(requestDto.getRemoveTag() != null) {
            postsTagService.deletePostTag(posts);
            postsTagService.addPostTag(posts, requestDto.getTags());
        }
        posts.update(requestDto);
        return id;
    }
    @Transactional
    public void deletePost(Long id){
        String username  = SecurityUtil.getLoginUsername();
        Posts posts = postsRepository.findById(id)
                .orElseThrow(()->new PostException(PostExceptionType.POST_NOT_POUND));

        if(username.equals(posts.getMember().getName())){
            chatRoomRepository.deleteAllByPostsId(posts.getId());
            posts.deleteMember(posts);
            postsRepository.delete(posts);
        }
        else {
            throw new MemberException(MemberExceptionType.NOT_ALLOW);
        }
    }
    @Transactional
    public Result paging(int page, String dType, String area) {

        List<Posts> postsList = queryRepository.findPosts(page, 16,null,dType,area);
        if(postsList.isEmpty()) {
            throw new PostException(PostExceptionType.POST_NOT_POUND);
        }
        List<PostsResponseDto> list = new ArrayList<>();
        for(Posts posts : postsList){
            list.add(new PostsResponseDto(posts,false));
        }
        int totalDataSize = queryRepository.getPages(dType,area);

        int totalPage = totalDataSize/16;
        if(totalDataSize < 16){totalPage=1;}
        else if(totalDataSize%16 !=0){totalPage+=1;}
        return new Result(list, totalPage);
    }
    @Transactional
    public List<PostsResponseDto> findAllGoodPostsByName() {
        String username = SecurityUtil.getLoginUsername();
        List<Good> goodList = goodService.findAllByName(username);
        return goodList.stream()
                .map(good -> getPostsResponseDto(good.getPosts()))
                .collect(Collectors.toList());
    }
    @Transactional
    public List<PostsResponseDto> findAllPostsByName() {
        String username = SecurityUtil.getLoginUsername();
        List<Posts> postsList = postsRepository.findAllByMemberName(username)
                .orElseThrow(()->new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));
        List<PostsResponseDto> list =
                postsList.stream().map(post->getPostsResponseDto(post)).collect(Collectors.toList());
        return list;
    }

    /**
     * 탐색된 게시글이 있으면 좋아요 표시
     */
    public PostsResponseDto getPostsResponseDto(Posts posts) {
        if (isGood(posts.getId())) {
            return new PostsResponseDto(posts, true);
        }
        return new PostsResponseDto(posts, false);
    }
    /**
     * 로그인 유저와 조회 게시글을 통해 좋아요 게시글 탐색
     */
    public boolean isGood(Long postsId) {
        String username = SecurityUtil.getLoginUsername();
        if(username == null) {return false;}
        if (goodRepository.findByMemberNameAndPostsId(username, postsId).isPresent()) {
            return true;
        }
        return false;
    }
}
