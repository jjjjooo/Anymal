package com.example.demo.domain.post.postGood;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.Optional;
@Repository
public interface GoodRepository extends JpaRepository<Good, Long> {


    @EntityGraph(attributePaths = {"posts", "member"})
    Optional<Good> findByMemberNameAndPostsId(String username, Long productId);

    @EntityGraph(attributePaths = {"posts", "member"})
    Optional<List<Good>> findAllByMemberName(String Name);
}
