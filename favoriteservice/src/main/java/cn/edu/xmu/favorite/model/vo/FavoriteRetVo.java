package cn.edu.xmu.favorite.model.vo;

import cn.edu.xmu.favorite.model.bo.Favorite;
import cn.edu.xmu.favorite.model.bo.GoodSku;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FavoriteRetVo {
    private Long id;

    private GoodSku goodSku;

    private LocalDateTime gmtCreate;

    public FavoriteRetVo(Favorite favorite){
        this.id = favorite.getId();
        this.goodSku = favorite.getGoodSku();
        this.gmtCreate = favorite.getGmtCreate();
    }
}
