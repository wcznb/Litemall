package cn.edu.xmu.footprint.model.vo;

import cn.edu.xmu.footprint.model.bo.Footprint;
import lombok.Data;

@Data
public class FootprintSimpleRetVo {
    private Long id;

    private Long goodsSkuId;

    public FootprintSimpleRetVo(Footprint footPrint){
        this.id = footPrint.getId();
        this.goodsSkuId = footPrint.getGoodsSkuId();
    }
}
