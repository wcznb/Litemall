package cn.edu.xmu.cart.model.vo;

import cn.edu.xmu.ooad.model.VoObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartGetVo {
    @NotNull
    private Long goodSkuID;

    @NotNull
    private Integer quantity;
}
