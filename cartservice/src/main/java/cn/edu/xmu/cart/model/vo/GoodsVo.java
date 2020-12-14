package cn.edu.xmu.cart.model.vo;

import cn.edu.xmu.cart.model.bo.CouponActivity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsVo {

    @NotBlank
    private String skuName;

    @NotNull
    private List<CouponActivity> couponActivity;
}
