package cn.edu.xmu.aftersales.model.bo;
import cn.edu.xmu.aftersales.model.po.AftersaleServicePo;
import cn.edu.xmu.aftersales.model.vo.queryRet;
import cn.edu.xmu.ooad.model.VoObject;
import lombok.Data;

@Data
public class querySaleBo implements VoObject {

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
    public Object createVo() { return new queryRet(this); }

    @Override
    public Object createSimpleVo() {
        return null;
    }

    public querySaleBo(AftersaleServicePo po){
        this.setId(po.getId());
        this.setOrderItemId(po.getOrderItemId());
        this.setOrderId(po.getOrderId());
        this.setCustomerId(po.getCustomerId());
        this.setShopId(po.getShopId());
        this.setServiceSn(po.getServiceSn());
        this.setType(po.getType());
        this.setReason(po.getReason());
        this.setRefund(po.getRefund());
        this.setQuantity(po.getQuantity());
        this.setRegionId(po.getRegionId());
        this.setDetail(po.getDetail());
        this.setConsignee(po.getConsignee());
        this.setMobile(po.getMobile());
        this.setCustomerLogSn(po.getCustomerLogSn());
        this.setShopLogSn(po.getShopLogSn());
        this.setState(po.getState());
    }
}
