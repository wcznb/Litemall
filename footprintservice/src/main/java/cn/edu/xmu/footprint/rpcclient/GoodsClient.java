package cn.edu.xmu.footprint.rpcclient;

import cn.edu.xmu.footprint.model.bo.GoodsSku;
import org.springframework.stereotype.Component;

@Component
public class GoodsClient {
    public GoodsSku getGoodSku(Long did, Long skuId){
        GoodsSku goodsSku = new GoodsSku();
        return goodsSku;
    }
}
