package com.example.demo.domain.post.posts;

import com.example.demo.domain.post.postGood.Good;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostsRepository extends JpaRepository<Posts, Long> {
    Optional<List<Posts>> findAllByMemberName(String name);
}
