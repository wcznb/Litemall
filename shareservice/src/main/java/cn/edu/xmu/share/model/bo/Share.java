package cn.edu.xmu.share.model.bo;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.provider.model.vo.GoodsSkuSimpleRetVo;
import cn.edu.xmu.share.model.po.SharePo;
import cn.edu.xmu.share.model.vo.SharesRetVo;
import cn.edu.xmu.share.model.vo.SharesSimpleRetVo;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class Share implements VoObject {
    Long id;
    Long sharerId;
    Long goodsSkuId;
    Integer quantity;
    LocalDateTime gmt_created;
    LocalDateTime gmt_modified;
    Long shareActivityId;
    GoodsSkuSimpleRetVo sku;

    public Share(){
    }

    public Share(SharePo sharePo){
        this.id=sharePo.getId();
        this.sharerId = sharePo.getSharerId();
        this.goodsSkuId = sharePo.getGoodsSkuId();
        this.quantity = sharePo.getQuantity();
        this.gmt_created = sharePo.getGmtCreate();
        this.gmt_modified = sharePo.getGmtModified();
        this.shareActivityId = sharePo.getShareActivityId();
    }


    public Share(SharePo sharePo, GoodsSkuSimpleRetVo sku){
        this.id=sharePo.getId();
        this.sharerId = sharePo.getSharerId();
        this.goodsSkuId = sharePo.getGoodsSkuId();
        this.quantity = sharePo.getQuantity();
        this.gmt_created = sharePo.getGmtCreate();
        this.gmt_modified = sharePo.getGmtModified();
        this.shareActivityId = sharePo.getShareActivityId();
        this.sku = sku;
    }

    @Override
    public SharesRetVo createVo() {
        return new SharesRetVo(this);
    }

    @Override
    public SharesRetVo createSimpleVo() {
        return new SharesRetVo(this);
    }
}
