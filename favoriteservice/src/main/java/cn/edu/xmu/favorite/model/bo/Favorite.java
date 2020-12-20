package cn.edu.xmu.favorite.model.bo;

import cn.edu.xmu.favorite.model.po.FavoritePo;
import cn.edu.xmu.favorite.model.vo.FavoriteRetVo;
import cn.edu.xmu.favorite.model.vo.FavoriteSimpleRetVo;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.provider.model.vo.GoodsSkuSimpleRetVo;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Favorite implements VoObject {
    private Long id;

    private Long goodsSkuId;

    private GoodsSkuSimpleRetVo goodsSku;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

    public Favorite(FavoritePo favoritePo){
        this.id = favoritePo.getId();
        this.goodsSkuId = favoritePo.getGoodsSkuId();
        this.gmtCreate = favoritePo.getGmtCreate();
        this.gmtModified = favoritePo.getGmtModified();
    }

    public Favorite(GoodsSkuSimpleRetVo sku,FavoritePo favoritePo){
        this.goodsSku = sku;
        this.id = favoritePo.getId();
        this.goodsSkuId = favoritePo.getGoodsSkuId();
        this.gmtCreate = favoritePo.getGmtCreate();
        this.gmtModified = favoritePo.getGmtModified();
    }

    @Override
    public Object createVo() {
        return new FavoriteRetVo(this);
    }

    @Override
    public Object createSimpleVo() {
        return new FavoriteSimpleRetVo(this);
    }
}
