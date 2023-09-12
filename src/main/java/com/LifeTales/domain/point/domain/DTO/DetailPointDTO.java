package com.LifeTales.domain.point.domain.DTO;

import com.LifeTales.domain.point.domain.Point;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class DetailPointDTO {
    private Point point;
    private int detailPoint;
    private String pointLog;


}
