package cn.edu.xmu.cart.model.vo;

import cn.edu.xmu.cart.model.bo.Cart;
import cn.edu.xmu.cart.model.bo.CouponActivity;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.provider.model.vo.CouponActivityVo;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CartRetVo {
    private Long id;
    private Long goodsSkuId;
    private String skuName;
    private Integer quantity;
    private Long price;
    private List<CouponActivityVo> couponActivity;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;

    public CartRetVo(Cart cart){
        id=cart.getId();
        goodsSkuId=cart.getGoodsSkuId();
        skuName=cart.getSkuName();
        quantity=cart.getQuantity();
        price=cart.getPrice();
        couponActivity=cart.getCouponActivity();
        gmtCreate=cart.getGmtCreate();
        gmtModified=cart.getGmtModified();
    }
}
