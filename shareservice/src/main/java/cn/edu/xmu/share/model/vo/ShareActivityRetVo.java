package cn.edu.xmu.share.model.vo;

import cn.edu.xmu.share.model.bo.ShareActivity;
import lombok.Data;

@Data
public class ShareActivityRetVo {

    Long id;
    Long shopId;
    Long skuId;
    String beginTime;
    String endTime;
    Integer state;

    public ShareActivityRetVo(){
    }

    public ShareActivityRetVo(ShareActivity shareActivity){
        this.id = shareActivity.getId();
        this.shopId = shareActivity.getShopId();
        this.skuId = shareActivity.getGoodsSkuId();
        this.beginTime = shareActivity.getBeginTime().toString();
        this.endTime = shareActivity.getEndTime().toString();
        this.state = Integer.valueOf(shareActivity.getState());
    }

}
