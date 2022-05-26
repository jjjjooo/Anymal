package com.example.demo.domain.post.posts;

import com.example.demo.domain.BaseTimeEntity;
import com.example.demo.domain.member.Address;
import com.example.demo.domain.post.postTag.PostsTag;
import com.example.demo.domain.post.postGood.Good;
import com.example.demo.domain.post.postImg.PostsImage;
import com.example.demo.domain.member.Member;
import com.example.demo.dto.PostsUpdateRequestDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.*;


//@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
//@DiscriminatorColumn
@Table(name="posts")
@Getter
@Entity
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Posts extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="posts_id")
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY ,cascade = CascadeType.ALL)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @OneToMany(mappedBy = "posts",cascade = CascadeType.ALL)
    private Set<Good> postGood = new LinkedHashSet<>();

    @OneToMany(mappedBy ="posts",cascade = CascadeType.REMOVE)
    private List<PostsImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "posts", cascade = CascadeType.ALL)
    private Set<PostsTag> postsTags = new LinkedHashSet<>();

    @Column(nullable = false)
    private String dType;
    @Column(nullable = false)
    private String gender;
    @Column(nullable = false,length = 50)
    private String title;
    @Column(nullable = false,length = 100)
    private String feature;

    @Embedded
    @Column(length=100)
    private Address address;

    @Builder
    public Posts(String dType, String title, Member member, String gender, String feature, Address address) {
        this.dType = dType;
        this.title = title;
        this.member = member;
        this.gender = gender;
        this.feature = feature;
        this.address = address;
    }
    public void update(PostsUpdateRequestDto updateDto){
        this.dType = updateDto.getType2();
        this.title = updateDto.getTitle();
        this.gender = updateDto.getGender();
        this.feature = updateDto.getFeature();
        this.address = updateDto.getAddress();
    }

    public void addImages(PostsImage image){
        this.images.add(image);
        if(image.getPosts() != this){
            image.setPosts(this);
        }
    }
    public void addMember(Member member){this.member = member;}
    public void deleteMember(Posts posts){
        this.member = null;
    }

}
