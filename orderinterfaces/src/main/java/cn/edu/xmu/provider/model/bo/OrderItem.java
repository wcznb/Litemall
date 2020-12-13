package cn.edu.xmu.provider.model.bo;

import cn.edu.xmu.provider.model.po.OrderItemPo;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class OrderItem implements Serializable {

    private Long id;

    private Long orderId;

    private Long goodsSkuId;

    private Integer quantity;

    private Long price;

    private Long discount;

    private String name;

    private Long couponId;

    private Long couponActivityId;

    private Long beSharedId;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

    /**
     * 默认构造函数
     */
    public OrderItem() {

    }

    /**
     * 根据po构建bo
     * @param orderItemPo
     */
    public OrderItem(OrderItemPo orderItemPo) {
        this.id=orderItemPo.getId();
        this.orderId=orderItemPo.getOrderId();
        this.goodsSkuId=orderItemPo.getGoodsSkuId();
        this.quantity=orderItemPo.getQuantity();
        this.price=orderItemPo.getPrice();
        this.discount=orderItemPo.getDiscount();
        this.name=orderItemPo.getName();
        this.couponId=orderItemPo.getCouponId();
        this.couponActivityId=orderItemPo.getCouponActivityId();
        this.beSharedId=orderItemPo.getBeShareId();
        this.gmtCreate=orderItemPo.getGmtCreate();
        this.gmtModified=orderItemPo.getGmtModified();
    }

}
