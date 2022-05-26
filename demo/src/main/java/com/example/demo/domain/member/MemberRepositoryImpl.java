//package com.example.demo.domain.member;
//
//import com.example.demo.domain.member.Member;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Repository;
//
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import java.util.*;
//
//@Repository //Component 포함
//@RequiredArgsConstructor
//public class MemberRepositoryImpl {
//    //엔티티매니저에 알아서 주입 //매니져는 autowired로 안댐
//    @PersistenceContext
//    private EntityManager em;
//
//    public void save(Member member){ em.persist(member); }
//
//    public Member findOne(Long id){
//        return em.find(Member.class, id);
//    }
//    //JPQL의 쿼리문은 테이블말고 객체로 조회를 함
//    public List<Member> findAll(){
//        return em.createQuery("select m from Member m", Member.class)
//                .getResultList();
//    }
//
//    public List<Member> findByName(String name){
//        return em.createQuery("select m from Member m where m.name = :name",Member.class)
//                .setParameter("name",name)
//                .getResultList();
//    }
//
//}
