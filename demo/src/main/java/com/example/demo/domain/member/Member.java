package com.example.demo.domain.member;

import com.example.demo.domain.BaseTimeEntity;
import com.example.demo.domain.post.postGood.Good;
import com.example.demo.domain.post.posts.Posts;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import javax.persistence.*;
import java.util.ArrayList;

//값 채울 때는 생성자(빌더)를 통해서 하고, 세터로하면 인스턴스 변경파악 어려워짐
//값 변경은 이벤트 메소드에 맞는 public 메소드 호출하자.
//도메인 엔티티에서 DB의 CRUD 기능 로직을 추가하는게 좋음 , 서비스에서 하지말고
//모든 연관관계는 지연로딩 LAZY로 하자 -- 즉시로딩하면 다 로딩함

//@Table(name="member",uniqueConstraints= {@UniqueConstraint(columnNames ="email")})
//@Table(name="member", uniqueConstraints = {@UniqueConstraint(
//        name = "NAME_AGE_UNIQUE",
//        columnNames = {"email", "name"} )})

@Table(name="member")
@Getter
@Entity
@NoArgsConstructor
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="member_id")
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String name;

    @Column(nullable = false, unique = true, length = 30)
    private String email;

    @Column(nullable = false, length = 100)
    private String password;

    @Embedded
    Address address;

    @JsonIgnore
    @OneToMany(mappedBy = "member")
    private List<Good> goods = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "member")
    private List<Posts> posts = new ArrayList<>();

    @Builder
    public Member(String name, String email, String password){
        this.name = name;
        this.email= email;
        this.password = password;
    }
    public void setPosts(Posts posts){
        this.posts.add(posts);
    }

    public void setGoods(Good goods) {this.goods.add(goods);}

    public Member update(PasswordEncoder passwordEncoder, String password, Address address){
        this.password = passwordEncoder.encode(password);
        this.address = address;
        return this;
    }
    public void setAddress(Address address){
        this.address = address;
    }

    public void encodePassword(PasswordEncoder passwordEncoder){
        this.password = passwordEncoder.encode(password);
    }

    public boolean matchPassword(PasswordEncoder passwordEncoder, String checkPassword){
        return passwordEncoder.matches(checkPassword, getPassword());
    }

}
