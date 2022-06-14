package com.example.demo.domain.post.postTag;

import com.example.demo.domain.post.posts.Posts;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import java.util.*;
import static com.example.demo.domain.post.posts.QPosts.posts;
import static com.example.demo.domain.post.postTag.QPostsTag.postsTag;
import static com.example.demo.domain.tag.QTag.tag1;
@Slf4j
@Repository
public class TagQueryRepository extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;
    public TagQueryRepository(JPAQueryFactory queryFactory) {
        super(Posts.class);
        this.queryFactory = queryFactory;
    }
    public List<Posts> findTags(String tag,int page) {
        return queryFactory
                .selectFrom(posts)
                .innerJoin(postsTag)
                    .on(posts.id.eq(postsTag.posts.id))
                .innerJoin(tag1)
                    .on(tag1.id.eq(postsTag.tag.id))
                .where(eqName(tag))
                .distinct()
                .offset((page-1) * 16)
                .limit(16)
                .orderBy(posts.id.desc())
                .fetch();
    }
    private BooleanExpression eqName(String tag) {
        return tag != null ? tag1.tag.contains(tag) : null ;
    }

}
