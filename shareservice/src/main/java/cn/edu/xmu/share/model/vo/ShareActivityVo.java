package cn.edu.xmu.share.model.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class ShareActivityVo {
    //开始时间
    @NotBlank
    String beginTime;

    //结束时间
    @NotBlank
    String endTime;

    //分享规则
    @NotBlank
    String strategy;
}
