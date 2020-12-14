package cn.edu.xmu.share.model.vo;

import cn.edu.xmu.provider.model.vo.GoodsSkuSimpleRetVo;
import cn.edu.xmu.share.model.bo.Share;
import cn.edu.xmu.share.model.bo.Sku;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class SharesRetVo{
    Long id;
    Long sharerId;
    GoodsSkuSimpleRetVo sku;
    Integer quantity;
    LocalDateTime gmtCreate;

    public SharesRetVo(Share share){
        this.id = share.getId();
        this.sharerId = share.getSharerId();
        this.sku = share.getSku();
        this.quantity = share.getQuantity();
        this.gmtCreate = share.getGmt_created();
    }
}

//public class SharesRetVo {
//    Long id;
//    Long ShareId;
//    Long goodsSkuId;
//    Integer quantity;
//    LocalDateTime gmtCreate;
//    String shareLink;
//
//
//    public SharesRetVo(Share share){
//        this.id = share.getId();
//        this.ShareId = share.getSharerId();
//        this.goodsSkuId = share.getGoodsSkuId();
//        this.quantity = share.getQuantity();
//        this.gmtCreate = share.getGmt_created();
//        this.shareLink="/test/"+this.goodsSkuId+"?shared="+this.ShareId;
//    }
//
//}



