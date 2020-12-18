package cn.edu.xmu.share.model.vo;

import cn.edu.xmu.provider.model.vo.GoodsSkuSimpleRetVo;
import cn.edu.xmu.share.model.bo.BeShared;
import cn.edu.xmu.share.model.bo.Sku;
import cn.edu.xmu.share.model.po.BeSharePo;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class BeSharedRetVo {

    Long id;
    GoodsSkuSimpleRetVo sku;
    Long sharerId;
    Long customerId;
    Long orderId;
    Integer rebate;
    LocalDateTime gmtCreate;


    public BeSharedRetVo(BeShared beShared){
        this.id = beShared.getId();
        this.sku = beShared.getSku();
        this.sharerId = beShared.getSharerId();
        this.customerId = beShared.getCustomerId();
        this.orderId = beShared.getOrderId();
        this.rebate = beShared.getRebate();
        this.gmtCreate = beShared.getGmtCreate();
    }

    public BeSharedRetVo(BeSharePo po){
        return;
    }
}


//@Data
//public class BeSharedRetVo {
//
//    Long id;
//    Long goodsSpuId;
//    Long shareId;
//    Long customerId;
//    Long orderId;//是订单id，还是order_item
//    Integer rebate;
//    String gmtCreate;
//
//    public BeSharedRetVo(BeShared beShared){
//        this.id = beShared.getId();
//        this.goodsSpuId = beShared.getGoodsSpuId();
//        this.shareId = beShared.getShareId();
//        this.customerId = beShared.getCustomerId();
//        this.orderId = beShared.getOrderItemId();
//        this.rebate = beShared.getRebate();
//        this.gmtCreate = beShared.getGmtCreate().toString();
//    }
//
//    public BeSharedRetVo(BeSharePo po){
//        this.id = po.getId();
//        this.goodsSpuId = po.getGoodsSkuId();
//        this.shareId = po.getShareId();
//        this.customerId = po.getCustomerId();
//        this.orderId = po.getOrderId();
//        this.rebate = po.getRebate();
//        this.gmtCreate = po.getGmtCreate().toString();
//    }
//}


