package cn.edu.xmu.provider.model.vo;

import cn.edu.xmu.provider.model.po.CouponActivityPo;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Feiyan Liu
 * @date Created at 2020/12/10 19:22
 */
@Data
public class CouponActivityVo implements Serializable {
    Long id;
    String name;
    LocalDateTime beginTime;
    LocalDateTime endTime;

    public CouponActivityVo(CouponActivityPo po)
    {
        this.id=po.getId();
        this.beginTime=po.getBeginTime();
        this.endTime=po.getEndTime();
        this.name=po.getName();
    }
public CouponActivityVo(){}
}
