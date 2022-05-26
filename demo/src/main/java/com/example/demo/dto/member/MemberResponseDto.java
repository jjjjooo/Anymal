package com.example.demo.dto.member;

import com.example.demo.domain.member.Address;
import com.example.demo.domain.member.Member;
import lombok.Builder;
import lombok.Data;
import java.util.List;
@Data
@Builder
public class MemberResponseDto {
    private String token;
    private String name;
    private String email;
    private Address address;
}
