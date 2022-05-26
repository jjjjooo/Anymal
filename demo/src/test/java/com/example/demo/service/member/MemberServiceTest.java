package com.example.demo.service.member;


import com.example.demo.domain.member.Member;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;


//@RunWith(SpringRunner.class) //단위테스트말고 전체 도는거 볼려고 인티그레이션
//@SpringBootTest //얘도 인티그레이션
//@Transactional // 데이터 변경을 해서 롤백을 해야해서 필요
//class MemberServiceTest {
//    @Autowired
//    MemberService memberService;
//    @Autowired
//    MemberRepositoryImpl memberRepository;
//
//    @Test
//    @Rollback(false) //테스트 케이스는 퍼시스턴스에서 롤백안하고 바로 DB에 인설트문(플러쉬) 날림
//    public void 회원가입() throws Exception{
//        //given
//        Member member = Member.builder()
//                .name("Kim").build();
//        //when
//        Long savedId = memberService.join(member);
//        //then
//
//        assertEquals(member, memberRepository.findOne(savedId));
//    };
//    @Test
//    public void 중복_회원_예외() throws Exception{
//        //given
//        Member member1 = Member.builder().name("kim").build();
//        Member member2 = Member.builder().name("kim").build();
//        //when
//        memberService.join(member1);
//        try{
//            memberService.join(member2);
//        }catch(IllegalStateException e){
//            return;
//        }
//        //then
//        fail("예외가 발생해야한다.");
//    };
//}