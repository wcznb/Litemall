package cn.edu.xmu.share.model.bo;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.provider.model.vo.GoodsSkuSimpleRetVo;
import cn.edu.xmu.share.model.po.BeSharePo;
import cn.edu.xmu.share.model.vo.BeSharedRetVo;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class BeShared implements VoObject {

    Long id;
    Long goodsSpuId;
    Long sharerId;
    Long shareId;
    Long customerId;
    Long orderId;
    Integer rebate;
    LocalDateTime gmtCreate;
    LocalDateTime gmtModified;
    Long shareActivityId;
    GoodsSkuSimpleRetVo sku;

    @Override
    public BeSharedRetVo createVo() {
        return new BeSharedRetVo(this);
    }

    @Override
    public Object createSimpleVo() {
        return null;
    }

    public BeShared(BeSharePo beSharePo){
        this.id = beSharePo.getId();
        this.goodsSpuId = beSharePo.getGoodsSkuId();
        this.shareId = beSharePo.getShareId();
        this.sharerId = beSharePo.getSharerId();
        this.customerId = beSharePo.getCustomerId();
        this.orderId = beSharePo.getOrderId();
        this.rebate = beSharePo.getRebate();
        this.gmtCreate = beSharePo.getGmtCreate();
        this.gmtModified = beSharePo.getGmtModified();
        this.shareActivityId = beSharePo.getShareActivityId();
    }

    public BeShared(BeSharePo beSharePo, GoodsSkuSimpleRetVo sku){
        this.id = beSharePo.getId();
        this.goodsSpuId = beSharePo.getGoodsSkuId();
        this.shareId = beSharePo.getShareId();
        this.sharerId = beSharePo.getSharerId();
        this.customerId = beSharePo.getCustomerId();
        this.orderId = beSharePo.getOrderId();
        this.rebate = beSharePo.getRebate();
        this.gmtCreate = beSharePo.getGmtCreate();
        this.gmtModified = beSharePo.getGmtModified();
        this.shareActivityId = beSharePo.getShareActivityId();
        this.sku = sku;
    }

}
