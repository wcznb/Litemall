package cn.edu.xmu.aftersales.model.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LogSnVo {
    @NotBlank(message = "买家运单信息不能为空")
    private String logSn;
}
