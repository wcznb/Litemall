package cn.edu.xmu.provider.server;
import cn.edu.xmu.provider.model.bo.GoodsSKU;
import cn.edu.xmu.provider.model.vo.CouponActivityVo;
import cn.edu.xmu.provider.model.vo.GoodsCartVo;

import java.util.List;

public interface GoodsService {

    /**
     * @description: 获取SKU的BO对象
     * @author: Feiyan Liu
     * @date: Created at 2020/12/8 19:54
     */
    GoodsSKU getGoodsSkuById(Long skuid);

    /**
     * @description: 通过SKU Id获取价格
     * @author: Feiyan Liu
     * @date: Created at 2020/12/8 19:54
     */
    Integer getPriceBySkuId(Long skuId);

    /**
     * @description:判断sku是否是此商店的商品
     * @author: Feiyan Liu
     * @date: Created at 2020/12/8 19:46
     */
    Boolean checkSkuIdByShopId(Long shopId,Long skuId);

    /**
     * @description: 通过店铺id获取SKU Id列表
     * @author: Feiyan Liu
     * @date: Created at 2020/12/8 19:48
     */
    List<Long> getSkuIdListByShopId(Long shopId);

    /**
     * @description: 通过店铺id获取SKU的bo列表
     * @author: Feiyan Liu
     * @date: Created at 2020/12/8 19:48
     */
    List<GoodsSKU> getSkuListByShopId(Long shopId);

    /**
     * @description: 通过skuId组装GoodsCartVo
     * @author: Feiyan Liu
     * @date: Created at 2020/12/10 19:35
     */

    GoodsCartVo getCartByskuId(Long Sku);

    /**
     * @description: 根据skuid获得当前有效的活动
     * @author: Feiyan Liu
     * @date: Created at 2020/12/10 19:34
     */
    CouponActivityVo getCouponActivityIdBySkuId(Long id);
}
