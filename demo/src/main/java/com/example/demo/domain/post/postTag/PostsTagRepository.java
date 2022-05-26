package com.example.demo.domain.post.postTag;

import com.example.demo.domain.post.posts.Posts;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PostsTagRepository extends JpaRepository<PostsTag, Long> {

    List<PostsTag> findAllByTagIdIn(List<Long> tagIds);
    List<PostsTag> findAllByPostsId(Long postsId);
}
