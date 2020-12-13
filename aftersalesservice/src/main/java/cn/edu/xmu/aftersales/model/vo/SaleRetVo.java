package cn.edu.xmu.aftersales.model.vo;

import cn.edu.xmu.aftersales.model.bo.AftersalesBo;
import cn.edu.xmu.aftersales.model.po.AftersaleServicePo;
import lombok.Data;

@Data
public class SaleRetVo {


    Long id;
    Long orderId;
    String orderSn;
    Long orderItemId;
    Long skuId;
    String skuName;
    Long customerId;
    String shopId;
    String serviceSn;
    Byte type;
    String reason;
    Long refund;
    Integer quantity;
    Long regionId;
    String detail;
    String consignee;
    String mobile;
    String customerLogSn;
    String shopLogSn;
    Byte state;

    public SaleRetVo(AftersaleServicePo po) {
        this.id = po.getId();
        this.customerId = po.getCustomerId();
        this.shopId = String.valueOf(po.getShopId());
        this.type = po.getType();
        this.reason = po.getReason();
        this.refund = po.getRefund();
        this.quantity = po.getQuantity();
        this.regionId = po.getRegionId();
        this.detail = po.getDetail();
        this.consignee = po.getConsignee();
        this.mobile=po.getMobile();
        this.state = po.getState();
    }

    public SaleRetVo(AftersalesBo bo) {
        this.id = bo.getId();
        this.orderId=bo.getOrderId();
        this.orderItemId=bo.getOrderItemId();
        this.serviceSn=bo.getServiceSn();
        this.skuId=bo.getSkuId();
        this.skuName=bo.getSkuName();
        this.orderSn=bo.getOrderSn();
        this.customerId = bo.getCustomerId();
        this.shopId = String.valueOf(bo.getShopId());
        this.type = bo.getType().getCode().byteValue();
        this.reason = bo.getReason();
        this.refund = bo.getRefund();
        this.quantity = bo.getQuantity();
        this.regionId = bo.getRegionId();
        this.detail = bo.getDetail();
        this.customerLogSn=bo.getCustomerLogSn();
        this.shopLogSn=bo.getShopLogSn();
        this.consignee = bo.getConsignee();
        this.mobile=bo.getMobile();
        this.state = bo.getState().getCode().byteValue();
    }
}





