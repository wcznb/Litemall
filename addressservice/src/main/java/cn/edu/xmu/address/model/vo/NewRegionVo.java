package cn.edu.xmu.address.model.vo;

import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
public class NewRegionVo {

    private String name;
    @Pattern(regexp = "[+]?[0-9*#]+")
    private String postalCode;
}
