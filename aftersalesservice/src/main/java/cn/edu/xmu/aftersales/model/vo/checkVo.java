package cn.edu.xmu.aftersales.model.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class checkVo {
    @NotNull
    private boolean confirm;

    private Long price;

//    @NotBlank
    private String conclusion;

    private Byte type;
}
