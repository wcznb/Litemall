package cn.edu.xmu.footprint.model.vo;

import cn.edu.xmu.footprint.model.bo.Footprint;
import cn.edu.xmu.footprint.model.bo.GoodsSku;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FootprintRetVo {

    private Long id;

    private GoodsSku goodsSku;

    private LocalDateTime gmt_created;

    public FootprintRetVo(Footprint footPrint){
        this.id = footPrint.getId();
        this.goodsSku = footPrint.getGoodsSku();
        this.gmt_created = footPrint.getGmtCreate();
    }
}
