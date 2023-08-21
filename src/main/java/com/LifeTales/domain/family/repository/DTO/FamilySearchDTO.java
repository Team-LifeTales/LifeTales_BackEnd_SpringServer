package com.LifeTales.domain.family.repository.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class FamilySearchDTO {

    private Long seq;
    private String nickName;

    public FamilySearchDTO(Long seq, String nickName) {
        this.seq = seq;
        this.nickName = nickName;
    }

}
