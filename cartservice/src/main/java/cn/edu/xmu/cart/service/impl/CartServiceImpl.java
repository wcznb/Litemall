package cn.edu.xmu.cart.service.impl;

import cn.edu.xmu.cart.dao.CartDao;
import cn.edu.xmu.cart.rpcclient.GoodsClient;
import cn.edu.xmu.cart.model.bo.Cart;
import cn.edu.xmu.cart.model.po.CartPo;
import cn.edu.xmu.cart.model.vo.CartGetVo;
import cn.edu.xmu.cart.model.vo.GoodsVo;
import cn.edu.xmu.cart.service.CartService;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 陈渝璇
 * @description 购物车相关服务
 * @date Created in 16:02 2020/11/29
 **/
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    CartDao cartDao;

    @Autowired
    GoodsClient goodsClient;

    @Override
    public ReturnObject<PageInfo<VoObject>> findPageOfCarts(Long userId, Integer page, Integer pageSize) {
        ReturnObject<PageInfo<CartPo>> ret = cartDao.findPageOfCarts(userId, page, pageSize);
        if(ret.getCode() == ResponseCode.OK){
            //成功查询
            PageInfo<CartPo> cartPoPage = ret.getData();
            List<CartPo> cartPos = cartPoPage.getList();
            List<VoObject> retObj = new ArrayList<>(cartPos.size());
            for (CartPo cartPo:cartPos) {
                GoodsVo goodsVo = goodsClient.getGood(cartPo.getGoodsSkuId());
                Cart cart = new Cart(cartPo);
                cart.setGoods(goodsVo);
                retObj.add(cart);
            }
            PageInfo<VoObject> cartPage = new PageInfo<>(retObj);
            cartPage.setPages(cartPoPage.getPages());
            cartPage.setPageNum(cartPoPage.getPageNum());
            cartPage.setPageSize(cartPoPage.getPageSize());
            cartPage.setTotal(cartPoPage.getTotal());
            return new ReturnObject<>(cartPage);
        }
        else{
            return new ReturnObject<>(ret.getCode());
        }
    }

    @Transactional
    @Override
    public ReturnObject<VoObject> insertCart(Long userId, CartGetVo vo) {
        LocalDateTime now = LocalDateTime.now();
        Long goodSkuId = vo.getGoodSkuID();

        CartPo po = new CartPo();
        po.setGoodsSkuId(goodSkuId);
        po.setQuantity(vo.getQuantity());
        po.setCustomerId(userId);
        po.setPrice(goodsClient.getGoodPrice(goodSkuId));
        po.setGmtCreate(now);
        po.setGmtModified(now);

        ReturnObject<CartPo> cartPoRetObj = cartDao.insertCart(po);
        if(cartPoRetObj.getCode()==ResponseCode.OK){
            CartPo cartPo = cartPoRetObj.getData();
            GoodsVo goodsVo = goodsClient.getGood(goodSkuId);
            Cart cart = new Cart(cartPo);
            cart.setGoods(goodsVo);
            return new ReturnObject<>(cart);
        }
        return new ReturnObject<>(cartPoRetObj.getCode());
    }

    @Transactional
    @Override
    public ReturnObject<Object> deleteAllCarts(Long userId){
        ReturnObject<Object> retObj = cartDao.deleteAllCarts(userId);
        return retObj;
    }

    @Transactional
    @Override
    public ReturnObject<Object> modifyCart(Long userId, Long id, CartGetVo vo){
        ReturnObject<CartPo> cartPoRetObj = cartDao.findCart(id);
        ReturnObject<Object> retObj = new ReturnObject<>(cartPoRetObj.getCode());
        if(retObj.getCode()==ResponseCode.OK){
            CartPo cartPo = cartPoRetObj.getData();
            //判断操作对象是否为自己的资源
            if(cartPo.getCustomerId().compareTo(userId) == 0){
                retObj = cartDao.modifyCart(id, vo.getGoodSkuID(), vo.getQuantity());
            } else{
                //操作对象不是自己的
                retObj = new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE);
            }
        }
        return retObj;
    }

    @Transactional
    @Override
    public ReturnObject<Object> deleteCart(Long userId, Long id) {
        ReturnObject<CartPo> cartPoRetObj = cartDao.findCart(id);
        ReturnObject<Object> retObj = new ReturnObject<>(cartPoRetObj.getCode());
        if (cartPoRetObj.getCode() == ResponseCode.OK) {
            CartPo cartPo = cartPoRetObj.getData();
            //判断操作对象是否为自己的资源
            if (cartPo.getCustomerId().compareTo(userId) == 0) {
                retObj = cartDao.deleteCart(id);
            } else {
                //操作对象不是自己的
                retObj = new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE);
            }
        }
        return retObj;
    }
}
