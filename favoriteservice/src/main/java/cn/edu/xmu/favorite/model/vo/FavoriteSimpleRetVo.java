package cn.edu.xmu.favorite.model.vo;

import cn.edu.xmu.favorite.model.bo.Favorite;
import lombok.Data;

@Data
public class FavoriteSimpleRetVo {
    private Long id;

    private Long goodsSkuId;

    public FavoriteSimpleRetVo(Favorite favorite){
        this.id = favorite.getId();
        this.goodsSkuId = favorite.getGoodsSkuId();
    }
}
