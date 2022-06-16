# :pushpin: ANYMAL
>유기 동물 커뮤니티  
>http://ec2-3-37-84-38.ap-northeast-2.compute.amazonaws.com/ 

</br>

## 1. 제작 기간 & 참여 인원
- 2022 4월 13일 ~ 2022년 5월 28일 ~ 진행 중
- 개인 프로젝트

</br>

## 2. 사용 기술
#### `Back-end`
  - Java 11
  - Spring Boot 2.6.3
  - Gradle
  - Spring Data JPA
  - QueryDSL
  - MariaDB
  - Spring Security
  - WebSocket
  - STOMP
  - 
#### `Front-end`
  - Vue.js 2.0
  - Vuex
  - Vuetify

</br>

## 3. ERD 설계
![mLFBdwcBgwH8sgn6u](https://user-images.githubusercontent.com/71645224/167236384-be4c7fc1-4b78-41c8-9d74-c5706f809e52.png)


## 4. 핵심 기능
프로젝트의 핵심 기능은 분실/습득 동물의 정보를 등록 및 조회할 수 있습니다.  
또한 1:1 채팅 기능으로 사용자 간의 정보교환에 용이하며, 태그 기능으로   
좀 더 나은 조회를 할 수 있습니다.  

<details>
<summary><b>핵심 기능 설명 펼치기</b></summary>
<div markdown="1">

### 4.1. 시스템 구성도
![image](https://user-images.githubusercontent.com/71645224/171626721-5433f95c-334a-4a77-b672-3d37353fa6e8.png)

<details>
<summary><b>4.2 사용자 요청</b></summary>
<div markdown="2">
  
### 4.2. 사용자 요청
#### 4.2.1 사용자 인증 및 인가
![image](https://user-images.githubusercontent.com/71645224/173191697-c811a648-b982-4270-a6b3-e3f1d92049a8.png)

- **토큰 인증** :pushpin: [코드 확인](https://github.com/jjjjooo/Anymal/blob/master/demo/src/main/java/com/example/demo/config/security/JwtAuthenticationFilter.java)
  - 스프링 시큐리티에 커스텀 필터를 등록하여 토큰 인증 구현합니다.

#### 4.2.2 사용자 요청
![사용자 요청](https://user-images.githubusercontent.com/71645224/167648425-50829e9d-a7ef-4e5b-a1b5-17e820aae7ad.png)

- **스토리지 활용** 
  - 유저 정보를 로컬스토리지와 VUEX 스토리지에 저장하여 API 호출 시 헤더에 포함시켜 요청 :pushpin: [코드 확인](https://github.com/Integerous/goQuality/blob/b587bbff4dce02e3bec4f4787151a9b6fa326319/frontend/src/components/PostInput.vue#L67)
  - 인증 및 인가의 여부나, 기능별로 API 요청을 분할하여 VUEX를 통해 상태관리를 용이하게 합니다.  :pushpin: [코드 확인](https://github.com/Integerous/goQuality/blob/b587bbff4dce02e3bec4f4787151a9b6fa326319/frontend/src/components/PostInput.vue#L67) 
</div>
</details>  

  
<details>
<summary><b>4.3 태그 기능 </b></summary>
<div markdown="2">
  
### 4.3. 게시글 부가 기능
  
#### 4.3.1. 태그 검색 능
  
~~~java
/**
 * 태그 검색 서비스
 */
public List<PostsResponseDto> findByTag(String tag, int page){

        tagRepository.findByTagContaining(tag).orElseThrow(
                ()-> new PostException(PostExceptionType.TAG_NOT_FOUND));

        List<Posts> posts = tagQueryRepository.findTags(tag, page);

        if(posts.isEmpty()){throw new PostException(PostExceptionType.POST_NOT_POUND);}

        return posts.stream().map(
                post->new PostsResponseDto(post,false)).collect(Collectors.toList());  
}
/**
 *  포스트 태그 검색 시 쿼리문
 */  
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
~~~  

- **태그 검색** :pushpin: [코드 확인](https://github.com/jjjjooo/Anymal/blob/master/demo/src/main/java/com/example/demo/service/post/PostsTagService.java)
  - 화면단에서 등록된 태그를 클릭하거나, 태그 검색창을 통해 조회를 할 경우 관련있는 문구의 게시글을 조회하도록 합니다.

#### 4.3.2. 좋아요 
#### `좋아요 표시 여부 처리`
~~~java
    /**
     * 유저 정보를 통해 탐색된 게시글이 있으면 좋아요 true 설정
     * 반대의 경우 좋아요 false 설정
     */
    public PostsResponseDto getPostsResponseDto(Posts posts) {
        if (isGood(posts.getId())) {
            return new PostsResponseDto(posts, true);
        }
        return new PostsResponseDto(posts, false);
    }
    /**
     * 로그인 유저와 조회 게시글을 통해 좋아요 게시글 탐색
     */
    public boolean isGood(Long postsId) {
        String username = SecurityUtil.getLoginUsername();
        if(username == null) {return false;}
        if (goodRepository.findByMemberNameAndPostsId(username, postsId).isPresent()) {
            return true;
        }
        return false;
    }

~~~
#### `좋아요 푸쉬` 
~~~java
    /**
     *  요청 받은 좋아요 취소 또는 등록
     */  
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
~~~  
- **좋아요 푸쉬 및 조회** :pushpin: [코드 확인](https://github.com/jjjjooo/Anymal/blob/master/demo/src/main/java/com/example/demo/service/post/PostsTagService.java)
  - 게시글을 조회하거나 좋아요 등록 및 취소 요청에서 로그인 유저를 기준으로 좋아여 여부를 판단하여 반환합니다.
</div>
</details>

<details>
<summary><b>4.4 채팅기능 </b></summary>
<div markdown="2">

### 4.4. 채팅

#### 4.4.1. 채팅 구조도
![채팅구조도](https://user-images.githubusercontent.com/71645224/168974974-d39e01a7-be24-4425-9942-68684bd962bf.JPG)


#### 4.4.2. 채팅 요청
![채팅클라이언트 요청](https://user-images.githubusercontent.com/71645224/168975117-dd6e35af-cc55-4436-bf08-9f63832f680d.JPG)  

![채팅 컨트롤러](https://user-images.githubusercontent.com/71645224/168975948-8b163d52-c85e-4d14-a503-e9349d75937d.JPG)

- **Publish-Subscribe 메커니즘**
  - STOMP를 이용하여 게시글의 각 채팅방을 구분하여 해당 채티방에 메시지를 전파합니다.
  - 이때, 채팅방 생성, 입장 또는 조회 시 게시글, 받는 사람, 보내는 사람 기준으로 채팅방을 생성 및 탐색합니다.
  
#### 4.4.3. 부가 기능
![image](https://user-images.githubusercontent.com/71645224/173825093-24b947d7-b9f8-498f-b96f-6a49149881ee.png)

  - **읽음 표시** 
  - 해당 채팅방의 각 메시지 기록, 읽음 표시를 조회하여 읽지 않은 메시지 수를 반환합니다.
  - 채팅방 입장 시 모두 읽음 표시로 변경합니다.

  
  
</div>
</details>
  
<details>
<summary><b>4.5 예외처리 </b></summary>
<div markdown="2">

### 4.5. 예외처리
  
#### 4.5.1. 예외처리 구성
![image](https://user-images.githubusercontent.com/71645224/167841803-2e8f7f03-b9d8-4924-b4b7-059bdb364f7b.png)
  

#### 4.5.2. 커스텀 예외처리 예시
![커스텀 예외](https://user-images.githubusercontent.com/71645224/172182399-bd777be6-15b5-4d14-9f8e-0bcb646c3440.JPG)
  
- **예외 정의** :pushpin: [코드 확인]()
  - 기본적인 로그인, 회원가입, 게시글 작성과 같은 데이터 형식은 화면단에서 먼저 검증합니다.
  - 예측가능한 예외는 커스텀 메세지를 생성하여 클라이언트 단에서 사용할 수 있도록 처리합니다.
  
  
#### 4.5.3. 일반 예외처리 예시
![image](https://user-images.githubusercontent.com/71645224/167844372-1e4edf3a-158e-4830-bbdc-5cbbf421f2ce.png)
  
- **런타임 예외, 컴파일 예외** :pushpin: [코드 확인]()
  - 처리불가능한 컴파일 예외는 서버 측에서 로그 형식으로 출력하며, 
  - 커스텀 예외를 제외한 런타임 예외는 마찬가지로 RestControllerAdvice를 이용하여 에러 메세지를 생성하여 처리합니다.
  
</div>
</details>
  
</div>
</details>


</br>

## 5. 핵심 트러블 슈팅
### 5.1. 컨텐츠 필터와 페이징 처리 문제
- 저는 이 서비스가 페이스북이나 인스타그램 처럼 가볍게, 자주 사용되길 바라는 마음으로 개발했습니다.  
때문에 페이징 처리도 무한 스크롤을 적용했습니다.

- 하지만 [무한스크롤, 페이징 혹은 “더보기” 버튼? 어떤 걸 써야할까](https://cyberx.tistory.com/82) 라는 글을 읽고 무한 스크롤의 단점들을 알게 되었고,  
다양한 기준(카테고리, 사용자, 등록일, 인기도)의 게시물 필터 기능을 넣어서 이를 보완하고자 했습니다.

- 그런데 게시물이 필터링 된 상태에서 무한 스크롤이 동작하면,  
필터링 된 게시물들만 DB에 요청해야 하기 때문에 아래의 **기존 코드** 처럼 각 필터별로 다른 Query를 날려야 했습니다.

<details>
<summary><b>기존 코드</b></summary>
<div markdown="1">

~~~java
/**
 * 게시물 Top10 (기준: 댓글 수 + 좋아요 수)
 * @return 인기순 상위 10개 게시물
 */
public Page<PostResponseDto> listTopTen() {

    PageRequest pageRequest = PageRequest.of(0, 10, Sort.Direction.DESC, "rankPoint", "likeCnt");
    return postRepository.findAll(pageRequest).map(PostResponseDto::new);
}

/**
 * 게시물 필터 (Tag Name)
 * @param tagName 게시물 박스에서 클릭한 태그 이름
 * @param pageable 페이징 처리를 위한 객체
 * @return 해당 태그가 포함된 게시물 목록
 */
public Page<PostResponseDto> listFilteredByTagName(String tagName, Pageable pageable) {

    return postRepository.findAllByTagName(tagName, pageable).map(PostResponseDto::new);
}

// ... 게시물 필터 (Member) 생략 

/**
 * 게시물 필터 (Date)
 * @param createdDate 게시물 박스에서 클릭한 날짜
 * @return 해당 날짜에 등록된 게시물 목록
 */
public List<PostResponseDto> listFilteredByDate(String createdDate) {

    // 등록일 00시부터 24시까지
    LocalDateTime start = LocalDateTime.of(LocalDate.parse(createdDate), LocalTime.MIN);
    LocalDateTime end = LocalDateTime.of(LocalDate.parse(createdDate), LocalTime.MAX);

    return postRepository
                    .findAllByCreatedAtBetween(start, end)
                    .stream()
                    .map(PostResponseDto::new)
                    .collect(Collectors.toList());
    }
~~~

</div>
</details>

- 이 때 카테고리(tag)로 게시물을 필터링 하는 경우,  
각 게시물은 최대 3개까지의 카테고리(tag)를 가질 수 있어 해당 카테고리를 포함하는 모든 게시물을 질의해야 했기 때문에  
- 아래 **개선된 코드**와 같이 QueryDSL을 사용하여 다소 복잡한 Query를 작성하면서도 페이징 처리를 할 수 있었습니다.

<details>
<summary><b>개선된 코드</b></summary>
<div markdown="1">

~~~java
/**
 * 게시물 필터 (Tag Name)
 */
@Override
public Page<Post> findAllByTagName(String tagName, Pageable pageable) {

    QueryResults<Post> results = queryFactory
            .selectFrom(post)
            .innerJoin(postTag)
                .on(post.idx.eq(postTag.post.idx))
            .innerJoin(tag)
                .on(tag.idx.eq(postTag.tag.idx))
            .where(tag.name.eq(tagName))
            .orderBy(post.idx.desc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
            .fetchResults();

    return new PageImpl<>(results.getResults(), pageable, results.getTotal());
}
~~~

</div>
</details>

</br>

## 6. 그 외 트러블 슈팅
<details>
<summary>npm run dev 실행 오류</summary>
<div markdown="1">

- Webpack-dev-server 버전을 3.0.0으로 다운그레이드로 해결
- `$ npm install —save-dev webpack-dev-server@3.0.0`

</div>
</details>

<details>
<summary>vue-devtools 크롬익스텐션 인식 오류 문제</summary>
<div markdown="1">
  
  - main.js 파일에 `Vue.config.devtools = true` 추가로 해결
  - [https://github.com/vuejs/vue-devtools/issues/190](https://github.com/vuejs/vue-devtools/issues/190)
  
</div>
</details>

<details>
<summary>ElementUI input 박스에서 `v-on:keyup.enter="메소드명"`이 정상 작동 안하는 문제</summary>
<div markdown="1">
  
  - `v-on:keyup.enter.native=""` 와 같이 .native 추가로 해결
  
</div>
</details>

<details>
<summary> Post 목록 출력시에 Member 객체 출력 에러 </summary>
<div markdown="1">
  
  - 에러 메세지(500에러)
    - No serializer found for class org.hibernate.proxy.pojo.javassist.JavassistLazyInitializer and no properties discovered to create BeanSerializer (to avoid exception, disable SerializationConfig.SerializationFeature.FAIL_ON_EMPTY_BEANS)
  - 해결
    - Post 엔티티에 @ManyToOne 연관관계 매핑을 LAZY 옵션에서 기본(EAGER)옵션으로 수정
  
</div>
</details>
    
<details>
<summary> 프로젝트를 git init으로 생성 후 발생하는 npm run dev/build 오류 문제 </summary>
<div markdown="1">
  
  ```jsx
    $ npm run dev
    npm ERR! path C:\Users\integer\IdeaProjects\pilot\package.json
    npm ERR! code ENOENT
    npm ERR! errno -4058
    npm ERR! syscall open
    npm ERR! enoent ENOENT: no such file or directory, open 'C:\Users\integer\IdeaProjects\pilot\package.json'
    npm ERR! enoent This is related to npm not being able to find a file.
    npm ERR! enoent

    npm ERR! A complete log of this run can be found in:
    npm ERR!     C:\Users\integer\AppData\Roaming\npm-cache\_logs\2019-02-25T01_23_19_131Z-debug.log
  ```
  
  - 단순히 npm run dev/build 명령을 입력한 경로가 문제였다.
   
</div>
</details>    


<details>
<summary> JSON: Infinite recursion (StackOverflowError)</summary>
<div markdown="1">
  
  - @JsonIgnoreProperties 사용으로 해결
    - 참고
        - [http://springquay.blogspot.com/2016/01/new-approach-to-solve-json-recursive.html](http://springquay.blogspot.com/2016/01/new-approach-to-solve-json-recursive.html)
        - [https://stackoverflow.com/questions/3325387/infinite-recursion-with-jackson-json-and-hibernate-jpa-issue](https://stackoverflow.com/questions/3325387/infinite-recursion-with-jackson-json-and-hibernate-jpa-issue)
        
</div>
</details>  
    
    
<details>
<summary> 컨텐츠수정 모달창에서 태그 셀렉트박스 드랍다운이 뒤쪽에 보이는 문제</summary>
<div markdown="1">
  
   - ElementUI의 Global Config에 옵션 추가하면 해결
     - main.js 파일에 `Vue.us(ElementUI, { zIndex: 9999 });` 옵션 추가(9999 이하면 안됌)
   - 참고
     - [https://element.eleme.io/#/en-US/component/quickstart#global-config](https://element.eleme.io/#/en-US/component/quickstart#global-config)
        
</div>
</details> 

<details>
<summary> HTTP delete Request시 개발자도구의 XHR(XMLHttpRequest )에서 delete요청이 2번씩 찍히는 이유</summary>
<div markdown="1">
  
  - When you try to send a XMLHttpRequest to a different domain than the page is hosted, you are violating the same-origin policy. However, this situation became somewhat common, many technics are introduced. CORS is one of them.

        In short, server that you are sending the DELETE request allows cross domain requests. In the process, there should be a **preflight** call and that is the **HTTP OPTION** call.

        So, you are having two responses for the **OPTION** and **DELETE** call.

        see [MDN page for CORS](https://developer.mozilla.org/en-US/docs/Web/HTTP/Access_control_CORS).

    - 출처 : [https://stackoverflow.com/questions/35808655/why-do-i-get-back-2-responses-of-200-and-204-when-using-an-ajax-call-to-delete-o](https://stackoverflow.com/questions/35808655/why-do-i-get-back-2-responses-of-200-and-204-when-using-an-ajax-call-to-delete-o)
        
</div>
</details> 

<details>
<summary> 이미지 파싱 시 og:image 경로가 달라서 제대로 파싱이 안되는 경우</summary>
<div markdown="1">
  
  - UserAgent 설정으로 해결
        - [https://www.javacodeexamples.com/jsoup-set-user-agent-example/760](https://www.javacodeexamples.com/jsoup-set-user-agent-example/760)
        - [http://www.useragentstring.com/](http://www.useragentstring.com/)
        
</div>
</details> 
    
<details>
<summary>화면단에서 새로고침 시 VEUX 상태 초기화 되는 문제</summary>
<div markdown="1">
 
        
</div>
</details> 
    
<details>
<summary>JPA 양방향 엔티티 무한재귀 문제</summary>
<div markdown="1">
  
  - @JsonIgnore 직렬화 방지
  - Entity 자체 반환보다는 DTO를 이용
  - 양방향 -> 단방향으로 변경
        
</div>
</details> 
    
</br>

