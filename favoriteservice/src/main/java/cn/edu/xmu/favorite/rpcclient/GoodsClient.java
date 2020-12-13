package cn.edu.xmu.favorite.rpcclient;

import cn.edu.xmu.favorite.model.bo.GoodSku;
import org.springframework.stereotype.Component;

@Component
public class GoodsClient {

    public GoodSku getGoodsSku(Long skuId){
        GoodSku goodSku = new GoodSku();
        return goodSku;
    }
}
