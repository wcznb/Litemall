package cn.edu.xmu.cart.model.vo;

import cn.edu.xmu.cart.model.bo.Cart;
import cn.edu.xmu.cart.model.bo.CouponActivity;
import cn.edu.xmu.ooad.model.VoObject;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CartRetVo {
    private Long id;
    private Long goodSkuId;
    private String skuName;
    private Integer quantity;
    private Long price;
    private List<CouponActivity> couponActivity;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;

    public CartRetVo(Cart cart){
        id=cart.getId();
        goodSkuId=cart.getGoodSkuId();
        skuName=cart.getSkuName();
        quantity=cart.getQuantity();
        price=cart.getPrice();
        couponActivity=cart.getCouponActivity();
        gmtCreate=cart.getGmtCreate();
        gmtModified=cart.getGmtModified();
    }
}
