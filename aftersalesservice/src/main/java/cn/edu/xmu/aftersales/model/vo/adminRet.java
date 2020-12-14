package cn.edu.xmu.aftersales.model.vo;

import cn.edu.xmu.aftersales.model.bo.AftersalesBo;
import cn.edu.xmu.aftersales.model.bo.adminSaleBo;
import cn.edu.xmu.aftersales.model.po.AftersaleServicePo;
import lombok.Data;

@Data
public class adminRet {
    Long id;
    Long orderId;
    Long orderItemId;
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

    public adminRet(AftersaleServicePo po) {
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

    public adminRet(adminSaleBo bo) {
        this.id = bo.getId();
        this.orderId=bo.getOrderId();
        this.orderItemId=bo.getOrderItemId();
        this.serviceSn=bo.getServiceSn();
        this.customerId = bo.getCustomerId();
        this.shopId = String.valueOf(bo.getShopId());
        this.type = bo.getType();
        this.reason = bo.getReason();
        this.refund = bo.getRefund();
        this.quantity = bo.getQuantity();
        this.regionId = bo.getRegionId();
        this.detail = bo.getDetail();
        this.consignee = bo.getConsignee();
        this.mobile=bo.getMobile();
        this.state = bo.getState();
    }

}
