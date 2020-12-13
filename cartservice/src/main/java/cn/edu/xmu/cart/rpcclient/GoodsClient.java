package cn.edu.xmu.cart.rpcclient;

import cn.edu.xmu.cart.model.vo.GoodsVo;
import org.springframework.stereotype.Component;

@Component
public class GoodsClient {
    public GoodsVo getGood(Long goodSkuId){
        GoodsVo getGoodsVo = new GoodsVo();
        return getGoodsVo;
    }

    public Long getGoodPrice(Long goodSkuId){
        Long price = goodSkuId;
        return price;
    }
}
