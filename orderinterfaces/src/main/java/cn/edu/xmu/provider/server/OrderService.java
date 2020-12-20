package cn.edu.xmu.provider.server;

import cn.edu.xmu.ooad.util.ReturnObject;

public interface OrderService {
    //通过orderitemid查orderitem

    /**
     * @description: 根据orderitemId获取userId
     * @author: Feiyan Liu
     * @date: Created at 2020/12/6 19:27
     */
    public ReturnObject<Long> getUserIdByOrderItemId(Long id);

    public ReturnObject<Long> getGoodsSkuIdByOrderItemId(Long id);

    public ReturnObject<Long> getOrderIdByOrderItemId(Long id);

    public ReturnObject<String> getSkuName(Long id);
    public ReturnObject<Boolean> validItem(Long orderItemId);
    public ReturnObject<Long> getShopIdByOrderId(Long orderId);

    public ReturnObject<Long> getnumber(Long orderItemId);
    //创建售后订单 返回订单id 售后订单的初始状态为付款完成
    public Long NewShopOrder(String consignee, Long regionId, String address, String mobile, Long orderItemId, int quantity);

    //创建退款信息
    public boolean createRefund(Long afterSaleId, Long orderitemId, int quantity, Long shopId);

    //查询订单状态
    public Byte checkState(Long orderitemId);

    public boolean createPay(Long id, Long refund);
}