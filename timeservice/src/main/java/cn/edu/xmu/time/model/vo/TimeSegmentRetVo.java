package cn.edu.xmu.time.model.vo;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.time.model.bo.TimeSegment;
import cn.edu.xmu.time.model.po.TimeSegmentPo;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TimeSegmentRetVo {

    Long id;

    LocalDateTime beginTime;

    LocalDateTime endTime;

    LocalDateTime gmtCreate;

    LocalDateTime gmtModified;


    public TimeSegmentRetVo(TimeSegment timeSegment){
        this.id = timeSegment.getId();
        this.beginTime = timeSegment.getBeginTime();
        this.endTime = timeSegment.getEndTime();

        this.gmtModified = timeSegment.getGmtModified();
        this.gmtCreate = timeSegment.getGmtCreate();
    }



}
