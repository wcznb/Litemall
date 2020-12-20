package cn.edu.xmu.address.model.vo;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
public class NewRegionVo {
    @NotEmpty
    private String name;
    @Pattern(regexp = "^[0-9]{6}$")
    private String postalCode;
}
