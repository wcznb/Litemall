package cn.edu.xmu.cart.model.bo;

import cn.edu.xmu.cart.model.po.CartPo;
import cn.edu.xmu.cart.model.vo.CartRetVo;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.provider.model.vo.CouponActivityVo;
import cn.edu.xmu.provider.model.vo.GoodsCartVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cart implements VoObject {
    private Long id;

    private Long goodsSkuId;

    private String skuName;

    private Integer quantity;

    private Long price;

    private List<CouponActivityVo> couponActivity;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

    public Cart(CartPo cartPo){
        this.id=cartPo.getId();
        this.price=cartPo.getPrice();
        this.goodsSkuId=cartPo.getGoodsSkuId();
        this.quantity=cartPo.getQuantity();
        this.gmtCreate=cartPo.getGmtCreate();
        this.gmtModified=cartPo.getGmtModified();
    }

    public void setGoods(GoodsCartVo goodsCartVo){
        this.skuName = goodsCartVo.getSkuName();
        this.couponActivity = goodsCartVo.getCouponActivity();
    }
    @Override
    public Object createVo() {
        return new CartRetVo(this);
    }

    @Override
    public Object createSimpleVo() {
        return null;
    }
}
