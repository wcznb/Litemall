package cn.edu.xmu.aftersales.model.vo;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class confirmVo {

    @NotNull
    private boolean confirm;

//    @NotBlank
    private String conclusion;
}
