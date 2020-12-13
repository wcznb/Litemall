package cn.edu.xmu.aftersales.model.bo;

import cn.edu.xmu.aftersales.model.po.AftersaleServicePo;
import cn.edu.xmu.aftersales.model.vo.SaleRetVo;
import cn.edu.xmu.ooad.model.VoObject;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
public class AftersalesBo implements VoObject {


    private Long id;

    private Long orderId;

    private String orderSn;

    private Long orderItemId;

    private Long skuId;

    private String skuName;

    private Long customerId;

    private Long shopId;

    //售后单序号
    private String serviceSn;

    //0换货 1退货 2维修
    private saleType type;

    private String reason;

    //处理意见
    private String conclusion;

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

    private State state;

    private Byte beDeleted;

    private LocalDateTime gmtCreated;

    private LocalDateTime gmtModified;


    @Override
    public Object createVo() { return new SaleRetVo(this); }

    @Override
    public Object createSimpleVo() {
        return null;
    }

    /**
     * 售后服务类型
     */
    public enum saleType{

        EXCHANGE(0,"换货"),
        RETURN(1,"退货"),
        MAINTENANCE(2,"维修");

        private static final Map<Integer, saleType> typeMap;

        static{
            typeMap=new HashMap();
            for(AftersalesBo.saleType enum1:values()){
                typeMap.put(enum1.code,enum1);
            }
        }

        private int code;
        private String description;

        saleType(int code,String description){
            this.code=code;
            this.description=description;
        }

        public static AftersalesBo.saleType getTypeByCode(Integer code){ return typeMap.get(code);}

        public Integer getCode(){return code;}

        public String getDescription(){ return description;}
    }

    public enum State{
        NEW(0,"待管理员审核"),
        WAITSEND(1,"待买家发货"),
        SEND(2,"买家已发货"),
        WAITREFUND(3,"待店家退款"),
        WAITSENDBYSHOP(4,"待店家发货"),
        SENDBYSHOP(5,"店家已发货"),
        DISAGREE(6,"审核不通过"),
        CANCEL(7,"已取消"),
        FINISH(8,"已结束");

        private static final Map<Integer,AftersalesBo.State> stateMap;

        static{
            stateMap=new HashMap<>();
            for(AftersalesBo.State enum1:values()){
                stateMap.put(enum1.code,enum1);
            }
        }

        private int code;
        private String description;

        State(int code,String description){
            this.code=code;
            this.description=description;
        }

        public static AftersalesBo.State getTypeByCode(Integer code){return stateMap.get(code);}

        public Integer getCode(){return code;}

        public String getDescription(){return description;}
    }


    public AftersalesBo(){

    }
    /**
     * po创建bo
     */
    public AftersalesBo(AftersaleServicePo po){
        this.setId(po.getId());
        this.setOrderItemId(po.getOrderItemId());
        this.setCustomerId(po.getCustomerId());
        this.setShopId(po.getShopId());
        this.setServiceSn(po.getServiceSn());
        this.setType(AftersalesBo.saleType.getTypeByCode(Integer.valueOf(po.getType())));
        this.setReason(po.getReason());
        this.setConclusion(po.getConclusion());
        this.setRefund(po.getRefund());
        this.setQuantity(po.getQuantity());
        this.setRegionId(po.getRegionId());
        this.setDetail(po.getDetail());
        this.setConsignee(po.getConsignee());
        this.setMobile(po.getMobile());
        this.setCustomerLogSn(po.getCustomerLogSn());
        this.setShopLogSn(po.getShopLogSn());
        this.setState(AftersalesBo.State.getTypeByCode(Integer.valueOf(po.getState())));
        this.setBeDeleted(po.getBeDeleted());
        this.setGmtCreated(po.getGmtCreate());
        this.setGmtModified(po.getGmtModified());
    }

    /**
     *  用bo创建po DAO层调用 插入数据库 新增功能
     */
    public AftersaleServicePo gotSalePo(){
        AftersaleServicePo po=new AftersaleServicePo();
        if(this.id!=null){
            po.setId(this.id);
        }
        po.setOrderItemId(this.getOrderItemId());
        po.setCustomerId(this.customerId);
        po.setShopId(this.shopId);
        po.setServiceSn(this.serviceSn);
        if(this.type!=null) {
            po.setType(this.type.getCode().byteValue());
        }
        po.setReason(this.reason);
        po.setRefund(this.refund);
        po.setQuantity(this.quantity);
        po.setRegionId(this.regionId);
        po.setDetail(this.detail);
        po.setConsignee(this.consignee);
        po.setMobile(this.mobile);
        if(this.state!=null) {
            po.setState(this.state.getCode().byteValue());
        }
        if(this.beDeleted!=null) {
            po.setBeDeleted(this.beDeleted);
        }
        if(this.gmtCreated!=null) {
            po.setGmtCreate(this.gmtCreated);
        }
        po.setGmtModified(this.gmtModified);

        return po;
    }


}
