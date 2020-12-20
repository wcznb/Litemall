package cn.edu.xmu.cart.service.impl;

import cn.edu.xmu.cart.dao.CartDao;
import cn.edu.xmu.cart.model.bo.Cart;
import cn.edu.xmu.cart.model.po.CartPo;
import cn.edu.xmu.cart.model.vo.CartGetVo;
import cn.edu.xmu.cart.service.CartService;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.provider.model.vo.GoodsCartVo;
import cn.edu.xmu.provider.server.GoodsService;
import com.github.pagehelper.PageInfo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author 陈渝璇
 * @description 购物车相关服务
 * @date Created in 16:02 2020/11/29
 **/
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    CartDao cartDao;

    @DubboReference(version = "1.1.1")
    GoodsService goodsService;

    @Override
    public ReturnObject<PageInfo<VoObject>> findPageOfCarts(Long userId, Integer page, Integer pageSize) {
        ReturnObject<PageInfo<CartPo>> ret = cartDao.findPageOfCarts(userId, page, pageSize);
        if(ret.getCode() == ResponseCode.OK){
            //成功查询
            PageInfo<CartPo> cartPoPage = ret.getData();
            List<CartPo> cartPos = cartPoPage.getList();
            List<VoObject> retObj = new ArrayList<>(cartPos.size());

            //通过商品内部api获得goodscartvo
            List<Long> skuIds = cartPos.stream().map(po->po.getGoodsSkuId()).collect(Collectors.toList());
            ReturnObject<List<GoodsCartVo>> retGoodsCartVos = null;
            if(skuIds.size() != 0)
                retGoodsCartVos = goodsService.getGoodsCartListBySkuIdList(skuIds);
            if(retGoodsCartVos == null||retGoodsCartVos.getCode() == ResponseCode.OK){
                if(retGoodsCartVos != null){
                    List<GoodsCartVo> goodsCartVos = retGoodsCartVos.getData();
                    Map<Long, GoodsCartVo> map = goodsCartVos.stream().collect(Collectors.toMap(GoodsCartVo::getSkuId, Function.identity()));
                    for (CartPo cartPo:cartPos) {
                        Cart cart = new Cart(cartPo);
                        cart.setGoods(map.get(cartPo.getGoodsSkuId()));
                        retObj.add(cart);
                    }
                }

                PageInfo<VoObject> cartPage = new PageInfo<>(retObj);
                cartPage.setPages(cartPoPage.getPages());
                cartPage.setPageNum(cartPoPage.getPageNum());
                cartPage.setPageSize(cartPoPage.getPageSize());
                cartPage.setTotal(cartPoPage.getTotal());
                return new ReturnObject<>(cartPage);
            } else{
                return new ReturnObject<>(retGoodsCartVos.getCode());
            }
        } else{
            return new ReturnObject<>(ret.getCode());
        }
    }

    @Transactional
    @Override
    public ReturnObject<VoObject> insertCart(Long userId, CartGetVo vo) {
        LocalDateTime now = LocalDateTime.now();
        Long newGoodSkuId = vo.getGoodsSkuId();

        //查找是否在购物车中
        ReturnObject<CartPo> retCartPo = cartDao.findCartBySkuIdAndCustomerId(userId, newGoodSkuId);
        if(retCartPo.getCode()==ResponseCode.OK){
            CartPo cartPoFromFind = retCartPo.getData();

            //调用商品内部api，更新价格
            ReturnObject<Long> retPrice = goodsService.getOriginalPriceBySkuId(newGoodSkuId);
            if(retPrice.getCode() != ResponseCode.OK)
                return new ReturnObject<>(retPrice.getCode());
            Long price = retPrice.getData();

            if(cartPoFromFind != null){
                //该用户购物车有该skuId的商品，修改商品的数量
                Long id = cartPoFromFind.getId();
                Integer quantity = cartPoFromFind.getQuantity()+vo.getQuantity();

                //设置po，更新数据库
                CartPo po = new CartPo();
                po.setId(id);
                po.setPrice(price);
                po.setQuantity(quantity);
                po.setGmtModified(now);
                ReturnObject<Object> retObj = cartDao.modifyCartByPo(po);
                if(retObj.getCode() == ResponseCode.OK){
                    //修改成功，设置返回参数
                    cartPoFromFind.setQuantity(quantity);
                    cartPoFromFind.setPrice(price);
                    cartPoFromFind.setGmtModified(now);
                    return setCartByPoAndSkuId(cartPoFromFind);
                } else{
                    return new ReturnObject<>(retObj.getCode());
                }
            }//end of if(cartPoFromFind != null)
            else{
                //该用户购物车里没有这个商品，新增
                CartPo po = new CartPo();
                po.setGoodsSkuId(newGoodSkuId);
                po.setQuantity(vo.getQuantity());
                po.setPrice(price);
                po.setCustomerId(userId);
                po.setGmtCreate(now);
                po.setGmtModified(now);

                ReturnObject<CartPo> cartPoRetObj = cartDao.insertCart(po);
                if(cartPoRetObj.getCode()==ResponseCode.OK){
                    CartPo cartPo = cartPoRetObj.getData();
                    return setCartByPoAndSkuId(cartPo);
                }
                return new ReturnObject<>(cartPoRetObj.getCode());
            }
        } else {
            return new ReturnObject<>(retCartPo.getCode());
        }
    }

    @Transactional
    @Override
    public ReturnObject<Object> deleteAllCarts(Long userId){
        ReturnObject<Object> retObj = cartDao.deleteAllCartsByCoustomerId(userId);
        return retObj;
    }

    @Transactional
    @Override
    public ReturnObject<Object> modifyCart(Long userId, Long id, CartGetVo vo){
        Long newSkuId = vo.getGoodsSkuId();
        Integer newQuantity = vo.getQuantity();

        ReturnObject<CartPo> cartPoRetObj = cartDao.findCartById(id);
        ReturnObject<Object> retObj = new ReturnObject<>(cartPoRetObj.getCode());
        if(retObj.getCode()==ResponseCode.OK){
            CartPo cartPo = cartPoRetObj.getData();//form findCartById
            //判断操作对象是否为该用户的资源
            if(cartPo.getCustomerId().compareTo(userId) == 0){
                //是该用户的资源，如果修改了skuid，则需要判断两个skuid是否来自于一个spu，以及获取新的价格
                Long price = cartPo.getPrice();//从findCartByid出来的取出price
                if(newSkuId!=cartPo.getGoodsSkuId()) {
                    //判断两个skuid是否来自于一个spu
                    ReturnObject<Boolean> retCheck = goodsService.checkSkuIdsFromASpu(newSkuId, cartPo.getGoodsSkuId());
                    if(!retCheck.getData())
                        //不是来自一个spu直接返回错误
                        return new ReturnObject<>(ResponseCode.FIELD_NOTVALID);

                    //判断newSkuid是不是已经在购物车里了
                    ReturnObject<CartPo> poInCart = cartDao.findCartBySkuIdAndCustomerId(userId,newSkuId);
                    if(poInCart.getData()!=null&&poInCart.getData().getId().compareTo(id)!=0){
                        //newSkuid在购物车里了，要删掉这个id的cart，修改newSkuid的cart数量
                        deleteCart(userId,id);
                        id = poInCart.getData().getId();
                        newQuantity += poInCart.getData().getQuantity();
                    }

                    //不管在不在购物车里，都要更新价格
                    ReturnObject<Long> retPrice = goodsService.getOriginalPriceBySkuId(newSkuId);
                    if(retPrice.getCode() != ResponseCode.OK)
                        return new ReturnObject<>(retPrice.getCode());
                    price = retPrice.getData();
                }
                //开始修改购物车数据库
                CartPo po = new CartPo();
                po.setId(id);
                po.setPrice(price);
                po.setGoodsSkuId(newSkuId);
                po.setQuantity(newQuantity);
                po.setGmtModified(LocalDateTime.now());
                retObj = cartDao.modifyCartByPo(po);
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
        ReturnObject<CartPo> cartPoRetObj = cartDao.findCartById(id);
        ReturnObject<Object> retObj = new ReturnObject<>(cartPoRetObj.getCode());
        if (cartPoRetObj.getCode() == ResponseCode.OK) {
            CartPo cartPo = cartPoRetObj.getData();
            //判断操作对象是否为自己的资源
            if (cartPo.getCustomerId().compareTo(userId) == 0) {
                retObj = cartDao.deleteCartById(id);
            } else {
                //操作对象不是自己的
                retObj = new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE);
            }
        }
        return retObj;
    }

    public ReturnObject<VoObject> setCartByPoAndSkuId(CartPo po){
        ReturnObject<GoodsCartVo> retGoodsCartVo = goodsService.getGoodsCartBySkuId(po.getGoodsSkuId());
        if(retGoodsCartVo.getCode() == ResponseCode.OK){
            Cart cart = new Cart(po);
            cart.setGoods(retGoodsCartVo.getData());
            return new ReturnObject<>(cart);
        } else{
            return new ReturnObject<>(retGoodsCartVo.getCode());
        }
    }
}
