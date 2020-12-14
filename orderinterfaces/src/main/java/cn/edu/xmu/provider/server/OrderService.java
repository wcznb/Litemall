package cn.edu.xmu.provider.server;

import cn.edu.xmu.provider.model.bo.FreightModel;
import cn.edu.xmu.provider.model.vo.OrderItemRetVo;

import java.util.List;

public interface OrderService {
    //通过orderitemid查orderitem
    public OrderItemRetVo getOrderItemById(Long id);

    //通过skuid，spuid，userid查询，期中skuid，spuid可能为为null，此时不作为查询的条件
    public List<OrderItemRetVo> getOrderItemListByUserId(Long skuId, Long userId);

    //通过skuid，spuid，shopid查询，期中skuid，spuid可能为为null，此时不作为查询的条件
    public List<OrderItemRetVo> getOrderItemListByShopId(Long skuId, Long userId);

    /**
     * @description: 根据orderitemId获取userId
     * @author: Feiyan Liu
     * @date: Created at 2020/12/6 19:27
     */
    public Long getUserIdByOrderItemId(Long id);

    /**
     * @description: 根据orderitemId获取skuid
     * @author: Feiyan Liu
     * @date: Created at 2020/12/6 19:27
     */
    public Long getGoodsSkuIdByOrderItemId(Long id);

    public FreightModel getFreightModelById(Long id);
}