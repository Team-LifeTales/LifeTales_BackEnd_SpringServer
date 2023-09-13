package com.LifeTales.domain.user.repository.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserBasicDataDTO {
    private Long seq;
    private Long familySeq;
    private String id;
    private String nickName;
    private String profile;

    public boolean isValied(){
        // 값 정확한지는? mysql 저장할 때 검증하기 때문에 널값인지만 체크
        if(seq == 0 || familySeq == 0){
            return false;
        }else if (id.isEmpty() || id == null || nickName.isEmpty() || nickName == null){
            return false;
        }else{
            return true;
        }
    }
}
