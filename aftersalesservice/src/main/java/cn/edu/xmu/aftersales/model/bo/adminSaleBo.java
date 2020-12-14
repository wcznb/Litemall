package cn.edu.xmu.aftersales.model.bo;
import cn.edu.xmu.aftersales.model.po.AftersaleServicePo;
import cn.edu.xmu.aftersales.model.vo.adminRet;
import cn.edu.xmu.ooad.model.VoObject;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class adminSaleBo implements VoObject {

    private Long id;

    private Long orderId;

    private Long orderItemId;

    private Long customerId;

    private Long shopId;

    //售后单序号
    private String serviceSn;

    //0换货 1退货 2维修
    private Byte type;

    private String reason;

    //正数为支付 负数为退款
    private Long refund;

    private Integer quantity;

    private Long regionId;

    //详细地址
    private String detail;

    private String consignee;

    private String mobile;

    //寄出运单号
    private String customerLogSn;

    //寄回运单号
    private String shopLogSn;

    private Byte state;

    @Override
    public Object createVo() { return new adminRet(this); }

    @Override
    public Object createSimpleVo() {
        return null;
    }

    public adminSaleBo(AftersaleServicePo sale){
        this.id=sale.getId();
        this.orderItemId=sale.getOrderItemId();
        this.customerId=sale.getCustomerId();
        this.shopId=sale.getShopId();
        this.serviceSn=sale.getServiceSn();
        this.type=sale.getType();
        this.reason=sale.getReason();
        this.refund=sale.getRefund();
        this.quantity=sale.getQuantity();
        this.regionId=sale.getRegionId();
        this.detail=sale.getDetail();
        this.consignee=sale.getConsignee();
        this.mobile=sale.getMobile();
        this.customerLogSn=sale.getCustomerLogSn();
        this.shopLogSn=sale.getShopLogSn();
        this.state=sale.getState();
    }
}
