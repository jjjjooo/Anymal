package com.example.demo.domain.post.posts;


import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.Entity;
import java.util.List;
import java.util.Optional;

import static com.example.demo.domain.post.posts.QPosts.posts;

@Slf4j
@Repository
public class PostsQueryRepository extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;
    public PostsQueryRepository(JPAQueryFactory queryFactory) {
        super(Posts.class);
        this.queryFactory = queryFactory;
    }

    public List<Posts> findPosts(int page, int size , String name, String dType, String area) {
        List<Long> ids = queryFactory
                .select(posts.id)
                .from(posts)
                .where(eqType(dType).or(eqArea(area)))
                .orderBy(posts.id.desc())
                .offset((page-1) * size)
                .limit(size)
                .fetch();
        return queryFactory
                .selectFrom(posts)
                .where(posts.id.in(ids))
                .orderBy(posts.id.desc())
                .limit(size)
                .fetch();
    }
    public int getPages(String dType,String area){
        return queryFactory
                .selectFrom(posts)
                .where(eqType(dType).or(eqArea(area)))
                .fetch()
                .size();
    }

    private BooleanExpression eqName(String name) {
        if (StringUtils.isEmpty(name)) {
            return null;
        }
        return posts.member.name.eq(name);
    }

    private BooleanExpression eqArea(String area) {
        if (StringUtils.isEmpty(area)) {
            return null;
        }
        return posts.member.address.sigungu.eq(area);
    }

    private BooleanExpression eqType(String dType) {
        if (StringUtils.isEmpty(dType)) {
            return null;
        }

        return posts.dType.eq(dType);
    }
}