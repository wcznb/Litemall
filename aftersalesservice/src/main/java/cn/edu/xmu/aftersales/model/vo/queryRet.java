package cn.edu.xmu.aftersales.model.vo;

import cn.edu.xmu.aftersales.model.bo.querySaleBo;
import cn.edu.xmu.aftersales.model.po.AftersaleServicePo;
import lombok.Data;

@Data
public class queryRet {
    Long id;
    Long orderId;
    Long orderItemId;
    Long customerId;
    Long shopId;
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

    public queryRet(AftersaleServicePo po) {
        this.id = po.getId();
        this.orderItemId=po.getOrderItemId();
        this.customerId = po.getCustomerId();
        this.shopId = po.getShopId();
        this.serviceSn=po.getServiceSn();
        this.type = po.getType();
        this.reason = po.getReason();
        this.refund = po.getRefund();
        this.quantity = po.getQuantity();
        this.regionId = po.getRegionId();
        this.detail = po.getDetail();
        this.consignee = po.getConsignee();
        this.mobile=po.getMobile();
        this.customerLogSn=po.getCustomerLogSn();
        this.shopLogSn=po.getShopLogSn();
        this.state = po.getState();
    }

    public queryRet(querySaleBo bo) {
        this.id = bo.getId();
        this.orderId=bo.getOrderId();
        this.orderItemId=bo.getOrderItemId();
        this.serviceSn=bo.getServiceSn();
        this.customerId = bo.getCustomerId();
        this.shopId = bo.getShopId();
        this.type = bo.getType();
        this.reason = bo.getReason();
        this.refund = bo.getRefund();
        this.quantity = bo.getQuantity();
        this.regionId = bo.getRegionId();
        this.detail = bo.getDetail();
        this.consignee = bo.getConsignee();
        this.mobile=bo.getMobile();
        this.customerLogSn=bo.getCustomerLogSn();
        this.shopLogSn=bo.getShopLogSn();
        this.state = bo.getState();
    }

}
