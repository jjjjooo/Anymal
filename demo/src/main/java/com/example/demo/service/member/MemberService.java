package com.example.demo.service.member;

import com.example.demo.domain.member.Address;
import com.example.demo.domain.member.MemberRepository;
import com.example.demo.domain.member.Member;
import com.example.demo.dto.member.MemberLoginDto;
import com.example.demo.dto.member.MemberRequestDto;
import com.example.demo.dto.member.MemberResponseDto;
import com.example.demo.dto.member.MemberUpdateDto;
import com.example.demo.exception.member.MemberException;
import com.example.demo.exception.member.MemberExceptionType;
import com.example.demo.config.security.TokenProvider;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//@AllArgsConstructor
@Slf4j
@RequiredArgsConstructor //final 생성자만 만듬
@Service
@Getter
@Transactional //읽기 전용
public class MemberService {

    //생성자 인젝션 권장장
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    public void signUp(MemberRequestDto requestDto) {
        Member member = requestDto.toEntity();
        Address memberAddress = new Address(requestDto.getAddress());
        member.setAddress(memberAddress);
        member.encodePassword(passwordEncoder);

        if (memberRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new MemberException(MemberExceptionType.ALREADY_EXIST_EMAIL);
            //log.warn("Email already exists {}", requestDto.getEmail());
            //throw new RuntimeException("이미 존재하는 이메일");
        }
        if (memberRepository.findByName(requestDto.getName()).isPresent()) {
            throw new MemberException(MemberExceptionType.ALREADY_EXIST_NAME);
            //throw new RuntimeException("이미 존재하는 사용자");
        }
        memberRepository.save(member);
    }

    public void update(MemberUpdateDto requestDto) {
        Member member = memberRepository.findByName(
                SecurityUtil.getLoginUsername()).orElseThrow(
                () -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));
                //new RuntimeException("회원이 존재하지 않습니다"));
        if (!member.matchPassword(passwordEncoder, requestDto.getCheckPassword())) {
            throw new MemberException(MemberExceptionType.WRONG_PASSWORD);
            //throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }
        member.update(passwordEncoder, requestDto.getNewPassword(), requestDto.getAddress());
    }

    public void delete(String checkPassword) {
        Member member = memberRepository.findByName(
                SecurityUtil.getLoginUsername()).orElseThrow(() ->
                new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));
                //new RuntimeException("회원이 존재하지 않습니다"));

        if (!member.matchPassword(passwordEncoder, checkPassword)) {
            throw new MemberException(MemberExceptionType.WRONG_PASSWORD);
            //throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        memberRepository.delete(member);
    }

    public MemberLoginDto login(MemberLoginDto requestDto) {
        Member member =memberRepository.findByEmail(
                requestDto.getEmail()).orElseThrow(()->new MemberException(MemberExceptionType.NOT_FOUND_MEMBER_EMAIL));
        boolean check = passwordEncoder.matches(requestDto.getPassword(), member.getPassword());
        if (check) {
            final String token = tokenProvider.create(member);
            MemberLoginDto responseDto = MemberLoginDto.builder()
                    .token(token)
                    .email(member.getEmail())
                    .name(member.getName())
                    .address(member.getAddress())
                    .build();
            return responseDto;
        }
        else{throw new MemberException(MemberExceptionType.WRONG_PASSWORD);}
    }
    public MemberResponseDto getInfo () {
        Member member = memberRepository.findByName(
                SecurityUtil.getLoginUsername()).orElseThrow(() ->
                new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));
                //new RuntimeException("회원이 없습니다"));
        MemberResponseDto responseDto = MemberResponseDto.builder()
                .email(member.getEmail())
                .name(member.getName())
                .address(member.getAddress())
                .build();
        return responseDto;
    }
}