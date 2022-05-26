package com.example.demo.domain.post.postImg;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostsImageRepository extends JpaRepository<PostsImage, Long> {

    Optional<List<PostsImage>> findAllByPostsId(Long postsId);
}
