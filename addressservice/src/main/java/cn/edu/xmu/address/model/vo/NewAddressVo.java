package cn.edu.xmu.address.model.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class NewAddressVo {
    @NotNull(message = "字段不合法")
    private Long regionId;
    @NotEmpty(message = "字段不合法")
    private String detail;
    @NotEmpty(message = "字段不合法")
    private String consignee;
    @Pattern(regexp="^[1]([3][0-9]{1}|59|58|88|89)[0-9]{8}$",message = "字段不合法")
    private String mobile;

}
