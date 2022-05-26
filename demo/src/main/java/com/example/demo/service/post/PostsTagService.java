package com.example.demo.service.post;

import com.example.demo.domain.post.postTag.PostsTag;

import com.example.demo.domain.post.postTag.PostsTagRepository;
import com.example.demo.domain.post.postTag.TagQueryRepository;
import com.example.demo.domain.post.posts.Posts;
import com.example.demo.domain.tag.Tag;
import com.example.demo.domain.tag.TagRepository;
import com.example.demo.dto.PostsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;



@Service
@RequiredArgsConstructor
@Transactional
public class PostsTagService {
    private final TagRepository tagRepository;
    private final PostsTagRepository postsTagRepository;
    private final TagQueryRepository tagQueryRepository;
    @Transactional
    public List<PostsResponseDto> findByTag(String tag, Long id){

        List<Tag> tags = tagRepository.findByTagContaining(tag).orElseThrow(
                ()-> new IllegalArgumentException("관련 검색태그가 없습니다."));

        List<Posts> posts = tagQueryRepository.findTags(tag);

        List<PostsResponseDto> tagPost = posts.stream().map(
                post->new PostsResponseDto(post,false)).collect(Collectors.toList());
        return tagPost;

    }
    @Transactional
    public void addPostTag(Posts posts, Set<String> tags) {
        tags.forEach(t -> {
            PostsTag postTag = new PostsTag();
            Tag tag = saveTag(t);
            postTag.addPosts(posts);
            postTag.addTag(tag);
            postsTagRepository.save(postTag);
        });
    }

    @Transactional
    public Tag saveTag(String tag) {
        Tag getTag = tagRepository.findTagByTag(tag)
                .orElse(Tag.builder().tag(tag).build());
        return tagRepository.save(getTag);
    }

    @Transactional
    public void deletePostTag(Posts posts) {
            Set<PostsTag> tags = posts.getPostsTags();
            Iterator<PostsTag> iTags = tags.iterator();
            while(iTags.hasNext()) {
                PostsTag tag=iTags.next();
                tag.deletePost(posts);
                postsTagRepository.deleteById(tag.getId());
            }
        }
    }

