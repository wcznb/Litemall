package cn.edu.xmu.cart.model.bo;

import cn.edu.xmu.cart.model.po.CartPo;
import cn.edu.xmu.cart.model.vo.CartRetVo;
import cn.edu.xmu.cart.model.vo.GoodsVo;
import cn.edu.xmu.ooad.model.VoObject;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class Cart implements VoObject {
    private Long id;

    private Long goodSkuId;

    private String skuName;

    private Integer quantity;

    private Long price;

    private List<CouponActivity> couponActivity;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

    public Cart(CartPo cartPo){
        this.id=cartPo.getId();
        this.goodSkuId=cartPo.getGoodsSkuId();
        this.quantity=cartPo.getQuantity();
        this.gmtCreate=cartPo.getGmtCreate();
        this.gmtModified=cartPo.getGmtModified();
    }

    public void setGoods(GoodsVo goodsVo){
        this.skuName = goodsVo.getSkuName();
        this.couponActivity = goodsVo.getCouponActivity();
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
