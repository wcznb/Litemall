package cn.edu.xmu.share.model.bo;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.share.model.po.ShareActivityPo;
import cn.edu.xmu.share.model.vo.ShareActivityRetVo;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ShareActivity implements VoObject {
    Long id;

    Long shopId;

    Long goodsSkuId;

    LocalDateTime beginTime;

    LocalDateTime endTime;

    String strategy;

    Byte beDeleted;

    LocalDateTime gmtCreated;

    LocalDateTime gmtModified;

    Byte state;//        "状态：0 待发布，1 发布"

    public ShareActivity(ShareActivityPo shareActivityPo){
        this.id = shareActivityPo.getId();
        this.shopId = shareActivityPo.getShopId();
        this.goodsSkuId = shareActivityPo.getGoodsSkuId();
        this.beginTime = shareActivityPo.getBeginTime();
        this.endTime = shareActivityPo.getEndTime();
        if(beginTime.isBefore(LocalDateTime.now())&&endTime.isAfter(LocalDateTime.now())){
            state = 1;
        }else {
            state = 0;
        }
        this.strategy = shareActivityPo.getStrategy();
//        this.beDeleted = shareActivityPo.getBeDeleted();
        this.gmtCreated = shareActivityPo.getGmtCreate();
        this.gmtModified = shareActivityPo.getGmtModified();

    }

    @Override
    public ShareActivityRetVo createVo() {
        return new ShareActivityRetVo(this);
    }

    @Override
    public Object createSimpleVo() {
        return null;
    }
}
