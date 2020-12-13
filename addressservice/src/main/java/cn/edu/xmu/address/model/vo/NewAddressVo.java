package cn.edu.xmu.address.model.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class NewAddressVo {
    @NotNull
    private Long regionId;
    @NotNull
    private String detail;
    @NotNull
    private String consignee;
    @NotNull
    @Pattern(regexp="[+]?[0-9*#]+",message="手机号格式不正确")
    private String mobile;

}
