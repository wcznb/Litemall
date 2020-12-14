package cn.edu.xmu.cart.service;


import cn.edu.xmu.cart.dao.CartDao;
import cn.edu.xmu.cart.rpcclient.GoodsClient;
import cn.edu.xmu.cart.model.bo.CouponActivity;
import cn.edu.xmu.cart.model.po.CartPo;
import cn.edu.xmu.cart.model.vo.GoodsVo;
import cn.edu.xmu.cart.service.impl.CartServiceImpl;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class CartServiceTest {
    @InjectMocks
    private CartServiceImpl cartService;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    @Mock
    CartDao cartDao;
    @Mock
    GoodsClient goodsClient;

    LocalDateTime beginTime = LocalDateTime.now();
    LocalDateTime endTime = LocalDateTime.now();

    public GoodsVo buildDefaultGoodsVo(Long goodSkuId){
        List<CouponActivity> couponActivities = Lists.newArrayList();
        couponActivities.add(new CouponActivity(goodSkuId,"name", beginTime, endTime));
        return new GoodsVo("skuName","spuName",couponActivities);
    }

    /**
     *
     * @param userID
     * @param page
     * @param pageSize
     * @return
     */
    public ReturnObject<PageInfo<CartPo>> buildDefaultCartPoPage(Long userID, Integer page,Integer pageSize){
        List<CartPo> cartPos = Lists.newArrayList();
        PageHelper.startPage(page,pageSize);
        for(int i=1;i<3;i++){
            Long l = Long.valueOf(i);
            CartPo cartPo = new CartPo();
            cartPo.setId(l);
            cartPo.setPrice(l);
            cartPo.setCustomerId(userID);
            cartPo.setGmtCreate(beginTime);
            cartPo.setGmtModified(beginTime);
            cartPo.setGoodsSkuId(l);
            cartPo.setQuantity(i);
            cartPos.add(cartPo);
        }
        PageInfo<CartPo> cartPoPage = PageInfo.of(cartPos);
        return new ReturnObject<>(cartPoPage);
    }

    @Test
    public void testFindAllCarts(){
        List<Long> goodSkuIds = Lists.newArrayList(0L,1L,2L);
        Long userId = 1L;

        for(int i=1;i<3;i++)
            Mockito.when(goodsClient.getGood(goodSkuIds.get(i))).thenReturn(buildDefaultGoodsVo(goodSkuIds.get(i)));
        Mockito.when(cartDao.findAllCarts(userId,2,100)).thenReturn(buildDefaultCartPoPage(userId,2,100));
        ReturnObject<PageInfo<VoObject>> returnObject = cartService.findAllCarts(userId,2,100);
        System.out.println(returnObject);
//        Assert.assertEquals();
    }

    @Test
    public void testDeleteCarts2(){

        ReturnObject<CartPo> ret = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);

        Mockito.when(cartDao.findCart(1L)).thenReturn(ret);
        ReturnObject<Object> returnObject = cartService.deleteCart(1L,1L);
        ReturnObject<Object> expectret = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        Assert.assertEquals(returnObject.getCode(),expectret.getCode());
    }
}
