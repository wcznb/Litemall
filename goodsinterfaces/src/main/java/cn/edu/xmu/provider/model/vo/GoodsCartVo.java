package cn.edu.xmu.provider.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author Yancheng Lai
 * createdBy Yancheng Lai 2020/12/10 12:27
 * modifiedBy Yancheng Lai 12:27
 **/

@Data
public class GoodsCartVo implements Serializable {
    String skuName;
    Long skuId;
    List<CouponActivityVo> couponActivity;
}
