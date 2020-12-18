package cn.edu.xmu.favorite.model.vo;

import cn.edu.xmu.favorite.model.bo.Favorite;
import cn.edu.xmu.provider.model.vo.GoodsSkuSimpleRetVo;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FavoriteRetVo {
    private Long id;

    private GoodsSkuSimpleRetVo goodsSku;

    private LocalDateTime gmtCreate;

    public FavoriteRetVo(Favorite favorite){
        this.id = favorite.getId();
        this.goodsSku = favorite.getGoodsSku();
        this.gmtCreate = favorite.getGmtCreate();
    }
}
