package com.LifeTales.domain.point.domain.DTO;

import com.LifeTales.domain.point.domain.Point;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RequestDetailPointDTO {
    private String userId;
    private int point;
    private String pointLog;

    public boolean isValid(){
        if(userId == null || userId.isEmpty()){
            return false;
        }else if(pointLog.isEmpty()){
            return false;
        }else{
            return true;
        }

    }
}
