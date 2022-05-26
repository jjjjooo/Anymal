package com.example.demo.service.post;


import com.example.demo.domain.member.Member;
import com.example.demo.domain.member.MemberRepository;
import com.example.demo.domain.post.postGood.Good;
import com.example.demo.domain.post.postGood.GoodRepository;
import com.example.demo.domain.post.posts.Posts;
import com.example.demo.domain.post.posts.PostsRepository;
import com.example.demo.exception.member.MemberException;
import com.example.demo.exception.member.MemberExceptionType;
import com.example.demo.exception.post.PostException;
import com.example.demo.exception.post.PostExceptionType;
import com.example.demo.service.member.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import java.util.List;
@Service
@RequiredArgsConstructor
@Transactional
public class PostsGoodService {
    private final GoodRepository goodRepository;
    private final MemberRepository memberRepository;
    private final PostsRepository postsRepository;

    @Transactional
    public boolean push(Long postsId) {
        String username =  SecurityUtil.getLoginUsername();

        Member member = memberRepository.findByName(username)
                .orElseThrow(()->new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));
        Posts posts= postsRepository.findById(postsId)
                .orElseThrow(()-> new PostException(PostExceptionType.POST_NOT_POUND));

        Optional<Good> postGood =
                goodRepository.findByMemberNameAndPostsId(member.getName(), posts.getId());

        if (postGood.isPresent()) {
            removeGood(postGood.get().getId());
            return false;
        }
        addGood(username, postsId);
        return true;
    }

    @Transactional
    public void addGood(String name, Long postsId) {
        Posts posts = postsRepository.findById(postsId).get();
        Member member = memberRepository.findByName(name).get();
        Good good = new Good();
        good.addMember(member);
        good.addPosts(posts);
        goodRepository.save(good);
    }

    @Transactional
    public void removeGood(Long id) {
        goodRepository.deleteById(id);
    }

    @Transactional
    public List<Good> findAllByName(String username) {
        return goodRepository.findAllByMemberName(username).get();
    }

}
