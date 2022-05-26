package com.example.demo.controller.member;
import com.example.demo.dto.member.MemberLoginDto;
import com.example.demo.dto.member.MemberRequestDto;
import com.example.demo.dto.member.MemberResponseDto;
import com.example.demo.dto.member.MemberUpdateDto;
import com.example.demo.exception.member.MemberException;
import com.example.demo.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Slf4j//로거
@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
@RequestMapping("auth")
public class MemberController {

    private final MemberService memberService;

    /**
     * 회원 가입 ::: @Valid 예외처리 @ModelAttribute 사용 불가
     */
    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.OK)
    public void signUp(@RequestBody @Valid MemberRequestDto requestDto){
        memberService.signUp(requestDto);
    }
    /**
     * 회원 정보 수정
     */
    @PutMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public void updatePassword(@Valid @RequestBody MemberUpdateDto requestDto) {
        memberService.update(requestDto);
    }

    /**
     * 회원탈퇴
     */
    @DeleteMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@RequestParam(value="password",required = true) String password){
        memberService.delete(password);
    }

    /**
     * 내정보조회
     */
    @GetMapping("/")
    public ResponseEntity getInfo() throws Exception {
        MemberResponseDto info = memberService.getInfo();
        return new ResponseEntity(info, HttpStatus.OK);
    }
    /**
     *   로그인
     */
    @PostMapping("/login")
    //@ResponseStatus(HttpStatus.OK)
    public ResponseEntity login(@Valid @RequestBody MemberLoginDto requestDto){
         MemberLoginDto responseDto= memberService.login(requestDto);
        return new ResponseEntity(responseDto, HttpStatus.OK);
    }
}
