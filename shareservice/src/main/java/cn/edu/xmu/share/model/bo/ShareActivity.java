package cn.edu.xmu.share.model.bo;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.share.model.po.ShareActivityPo;
import cn.edu.xmu.share.model.vo.ShareActivityRetVo;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

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

    /**
     * 后台用户状态
     */
    public enum State {

        ONLINE(1,"上线"),
        OFFLINE(0, "下线");

        private static final Map<Integer, ShareActivity.State> stateMap;

        static { //由类加载机制，静态块初始加载对应的枚举属性到map中，而不用每次取属性时，遍历一次所有枚举值
            stateMap = new HashMap();
            for (ShareActivity.State enum1 : values()) {
                stateMap.put(enum1.code, enum1);
            }
        }

        private int code;
        private String description;

        State(int code, String description) {
            this.code = code;
            this.description = description;
        }

        public static ShareActivity.State getTypeByCode(Integer code) {
            return stateMap.get(code);
        }

        public Integer getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }
    }

    public ShareActivity(){}

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
