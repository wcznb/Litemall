package cn.edu.xmu.footprint.model.bo;

import cn.edu.xmu.footprint.model.po.FootPrintPo;
import cn.edu.xmu.footprint.model.vo.FootprintRetVo;
import cn.edu.xmu.footprint.model.vo.FootprintSimpleRetVo;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.provider.model.vo.GoodsSkuSimpleRetVo;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Footprint implements VoObject {
    private Long id;

    private Long CustomerId;

    private Long goodsSkuId;

    private GoodsSkuSimpleRetVo goodsSku;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

    public Footprint(FootPrintPo footPrintPo){
        this.id = footPrintPo.getId();
        this.CustomerId = footPrintPo.getCustomerId();
        this.goodsSkuId = footPrintPo.getGoodsSkuId();
        this.gmtCreate = footPrintPo.getGmtCreate();
        this.gmtModified = footPrintPo.getGmtModified();
    }

    @Override
    public Object createVo() {
        return new FootprintRetVo(this);
    }

    @Override
    public Object createSimpleVo() {
        return new FootprintSimpleRetVo(this);
    }
}
