package cn.edu.xmu.footprint.model.bo;

import cn.edu.xmu.footprint.model.po.FootprintPo;
import cn.edu.xmu.footprint.model.vo.FootprintRetVo;
import cn.edu.xmu.footprint.model.vo.FootprintSimpleRetVo;
import cn.edu.xmu.ooad.model.VoObject;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Footprint implements VoObject {
    private Long id;

    private Long CustomerId;

    private Long goodsSkuId;

    private GoodsSku goodsSku;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

    public Footprint(FootprintPo footPrintPo){
        this.id = footPrintPo.getId();
        this.CustomerId = footPrintPo.getCustomerId();
        this.goodsSkuId = footPrintPo.getGoodsSkuId();
        this.gmtCreate = footPrintPo.getGmtCreate();
        this.gmtModified = footPrintPo.getGmtModified();
    }

    public void setGoodsSku(GoodsSku goodsSku){
        this.goodsSku = goodsSku;
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
