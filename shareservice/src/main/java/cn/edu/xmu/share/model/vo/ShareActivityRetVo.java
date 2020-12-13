package cn.edu.xmu.share.model.vo;

import cn.edu.xmu.share.model.bo.ShareActivity;
import lombok.Data;

@Data
public class ShareActivityRetVo {

    Long id;
    Long shopId;
    Long goodSkuId;
    String beginTime;
    String endTime;
    Integer state;

    public ShareActivityRetVo(){
    }

    public ShareActivityRetVo(ShareActivity shareActivity){
        this.id = shareActivity.getId();
        this.shopId = shareActivity.getShopId();
        this.goodSkuId = shareActivity.getGoodsSkuId();
        this.beginTime = shareActivity.getBeginTime().toString();
        this.endTime = shareActivity.getEndTime().toString();
        this.state = Integer.valueOf(shareActivity.getState());
    }

}
