package com.example.demo.domain.member;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.*;
@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    private String sido;
    private String sigungu;
    private String bname;
    private String bname2;

    public Address(List<String> address){
        if(!address.isEmpty()) {
            this.sido = address.get(0);
            this.sigungu = address.get(1);
            this.bname = address.get(2);
        }
    }
}
