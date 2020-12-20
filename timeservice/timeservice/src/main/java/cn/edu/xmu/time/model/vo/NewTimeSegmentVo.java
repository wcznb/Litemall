package cn.edu.xmu.time.model.vo;

import lombok.Data;

import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Data
public class NewTimeSegmentVo {
    private String beginTime;

    private String endTime;
}
