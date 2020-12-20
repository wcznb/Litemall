package cn.edu.xmu.time.model.bo;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.time.model.po.TimeSegmentPo;
import cn.edu.xmu.time.model.vo.TimeSegmentRetVo;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TimeSegment implements VoObject {
    Long id;

    LocalDateTime beginTime;

    LocalDateTime endTime;

    LocalDateTime gmtCreate;

    LocalDateTime gmtModified;


    public TimeSegment(TimeSegmentPo timeSegmentPo){
        this.id = timeSegmentPo.getId();
        this.beginTime = timeSegmentPo.getBeginTime();
        this.endTime = timeSegmentPo.getEndTime();

        this.gmtModified = timeSegmentPo.getGmtModified();
        this.gmtCreate = timeSegmentPo.getGmtCreate();
    }

    @Override
    public Object createVo(){
        return new TimeSegmentRetVo(this);
    }

    @Override
    public Object createSimpleVo(){
        return new TimeSegmentRetVo(this);
    }
}
