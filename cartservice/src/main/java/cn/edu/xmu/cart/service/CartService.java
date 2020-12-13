package cn.edu.xmu.cart.service;

import cn.edu.xmu.cart.model.vo.CartGetVo;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ReturnObject;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 陈渝璇
 * @description 购物车相关服务
 * @date Created in 16:02 2020/11/29
 **/
@Service
public interface CartService {

    /**
     * 买家获得购物车列表
     *
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    ReturnObject<PageInfo<VoObject>> findPageOfCarts(Long userId,Integer page,Integer pageSize);

    /**
     * 买家将商品加入购物车
     * @param userId
     * @param vo
     * @return
     */
    @Transactional
    ReturnObject<VoObject> insertCart(Long userId, CartGetVo vo);

    /**
     * 买家清空购物车(直接删除)
     * @param userId
     * @return ReturnObject<Object>
     */
    @Transactional
    ReturnObject<Object> deleteAllCarts(Long userId);

    /**
     * 买家修改购物车单个商品的数量或规格
     * @param userId
     * @param id:Cart id
     * @param vo:CartSetVo
     * @return ReturnObject<Object>
     */
    @Transactional
    ReturnObject<Object> modifyCart(Long userId, Long id, CartGetVo vo);

    /**
     * 买家删除购物车中商品
     * @param userId
     * @param id:Cart id
     * @return ReturnObject<Object>
     */
    @Transactional
    ReturnObject<Object> deleteCart(Long userId, Long id);
}
