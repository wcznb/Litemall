package cn.edu.xmu.favorite.util;

import cn.edu.xmu.provider.model.vo.GoodsSkuSimpleRetVo;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


public class ListToMap {
    public Map<Long, GoodsSkuSimpleRetVo> GoodsSkuSimpleRetVoListToMap(List<GoodsSkuSimpleRetVo> accounts) {
        return accounts.stream().collect(Collectors.toMap(GoodsSkuSimpleRetVo::getId, Function.identity(), (key1, key2) -> key2));
    }
}
