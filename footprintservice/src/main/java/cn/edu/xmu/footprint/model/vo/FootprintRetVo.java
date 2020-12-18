package cn.edu.xmu.footprint.model.vo;

import cn.edu.xmu.footprint.model.bo.Footprint;
import cn.edu.xmu.provider.model.vo.GoodsSkuSimpleRetVo;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FootprintRetVo {

    private Long id;

    private GoodsSkuSimpleRetVo goodsSku;

    private LocalDateTime gmtCreate;

    public FootprintRetVo(Footprint footPrint){
        this.id = footPrint.getId();
        this.goodsSku = footPrint.getGoodsSku();
        this.gmtCreate = footPrint.getGmtCreate();
    }
}
