package com.example.demo.domain.tag;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.*;
import java.util.Optional;
@Repository
public interface TagRepository extends JpaRepository<Tag,Long> {

    Optional<List<Tag>> findByTagContaining(String tag);
    Optional<Tag> findTagByTag(String tag);
    }