package cn.edu.xmu.aftersales.model.vo;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class confirmVo {

    @NotNull
    private boolean confirm;

    @NotBlank
    private String conclusion;
}
