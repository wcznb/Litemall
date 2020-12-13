package cn.edu.xmu.share.model.vo;

import cn.edu.xmu.share.model.bo.Share;
import lombok.Data;

@Data
public class SharesSimpleRetVo {

    Long id;
    Long shareId;
    Long goodsSkuId;
    Integer quantity;
    String gmtCreate;

    public SharesSimpleRetVo(Share share){
        this.id = share.getId();
        this.shareId = share.getSharerId();
        this.goodsSkuId = share.getGoodsSkuId();
        this.quantity = share.getQuantity();
        this.gmtCreate = share.getGmt_created().toString();
    }
}
