package cn.edu.xmu.cart.dao;

import cn.edu.xmu.cart.mapper.CartPoMapper;
import cn.edu.xmu.cart.model.po.CartPo;
import cn.edu.xmu.cart.model.po.CartPoExample;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author 陈渝璇
 * createdBy 陈渝璇 2020-11-29
 * modifiedBy 陈渝璇 2020-12-04
 */
@Repository
public class CartDao {
    @Autowired
    CartPoMapper cartPoMapper;
    /**
     * 买家获得购物车列表
     *
     * @param userId
     * @return ReturnObject<Object>
     * createdBy 陈渝璇 2020-11-29
     * modifiedBy 陈渝璇 2020-11-29
     */
    public ReturnObject<PageInfo<CartPo>> findPageOfCarts(Long userId, Integer page, Integer pageSize){
        //添加条件
        CartPoExample example = new CartPoExample();
        CartPoExample.Criteria criteria = example.createCriteria();
        criteria.andCustomerIdEqualTo(userId);
        //分页
        PageHelper.startPage(page,pageSize);
        List<CartPo> cartPos = null;
        try{
            cartPos = cartPoMapper.selectByExample(example);
        } catch (DataAccessException e){
            //logger.error("selectAllRole: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR,
                    String.format("数据库错误：%s", e.getMessage()));
        } catch (Exception e) {
            // 属未知错误
            //logger.error("严重错误：" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR,
                    String.format("发生了严重的未知错误：%s", e.getMessage()));
        }
        PageInfo<CartPo> cartPoPage = PageInfo.of(cartPos);
        return new ReturnObject<>(cartPoPage);
    }

    /**
     * 主键查找购物车
     * @param id:购物车id
     * @return
     */
    public ReturnObject<CartPo> findCartById(Long id){
        ReturnObject<CartPo> retObj = null;
        try{
            CartPo cartPo = cartPoMapper.selectByPrimaryKey(id);
            if(cartPo==null)
                retObj = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
            else
                retObj = new ReturnObject<>(cartPo);
        } catch (DataAccessException e){
            //logger.error("selectAllRole: DataAccessException:" + e.getMessage());
            retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR,
                    String.format("数据库错误：%s", e.getMessage()));
        } catch (Exception e) {
            // 属未知错误
            //logger.error("严重错误：" + e.getMessage());
            retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR,
                    String.format("发生了严重的未知错误：%s", e.getMessage()));
        }
        return retObj;
    }

    public ReturnObject<CartPo> findCartBySkuIdAndCustomerId(Long customerId, Long skuId){
        ReturnObject<CartPo> retObj = null;
        try{
            CartPoExample example = new CartPoExample();
            CartPoExample.Criteria criteria = example.createCriteria();
            criteria.andGoodsSkuIdEqualTo(skuId);
            criteria.andCustomerIdEqualTo(customerId);

            List<CartPo> cartPos = cartPoMapper.selectByExample(example);
            CartPo cartPo = null;
            if(cartPos.size()!=0)
                cartPo = cartPos.get(0);
            retObj = new ReturnObject<>(cartPo);

        } catch (DataAccessException e){
            retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR,
                    String.format("数据库错误：%s", e.getMessage()));
        } catch (Exception e) {
            // 属未知错误
            retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR,
                    String.format("发生了严重的未知错误：%s", e.getMessage()));
        }
        return retObj;
    }

    /**
     * 买家将商品加入购物车
     *
     * @param po
     * @return
     */
    public ReturnObject<CartPo> insertCart(CartPo po){
        ReturnObject<CartPo> retObj = null;
        try{
            int ret = cartPoMapper.insert(po);
            if(ret == 0){
                //新增失败
                retObj = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST,
                        String.format("新增失败：" + po.getClass()));
            } else{
                retObj = new ReturnObject<>(po);
            }
        } catch (DataAccessException e){
            retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR,
                    String.format("数据库错误：%s", e.getMessage()));
        } catch (Exception e) {
            // 属未知错误
            retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR,
                    String.format("发生了严重的未知错误：%s", e.getMessage()));
        }
        return retObj;
    }

    /**
     * 买家清空购物车(物理删除)
     *
     * @param userId
     * @return ReturnObject<Object>
     * createdBy 陈渝璇 2020-11-29
     * modifiedBy 陈渝璇 2020-11-29
     */
    public ReturnObject<Object> deleteAllCartsByCoustomerId(Long userId){
        ReturnObject<Object> retObj = null;
        try{
            CartPoExample example = new CartPoExample();
            //增加条件
            CartPoExample.Criteria criteria = example.createCriteria();
            criteria.andCustomerIdEqualTo(userId);

            int ret = cartPoMapper.deleteByExample(example);
            retObj = new ReturnObject<>();
        } catch (DataAccessException e){
            //logger.error("selectAllRole: DataAccessException:" + e.getMessage());
            retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR,
                    String.format("数据库错误：%s", e.getMessage()));
        } catch (Exception e) {
            // 属未知错误
            //logger.error("严重错误：" + e.getMessage());
            retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR,
                    String.format("发生了严重的未知错误：%s", e.getMessage()));
        }
        return retObj;
    }

    /**
     * 买家修改购物车单个商品的数量或规格
     * @param po
     * @return
     */
    public ReturnObject<Object> modifyCartByPo(CartPo po){
        ReturnObject<Object> retObj = null;
        try{
            CartPoExample example = new CartPoExample();
            //添加条件
            CartPoExample.Criteria criteria = example.createCriteria();
            criteria.andIdEqualTo(po.getId());
            //数据库修改
            int ret = cartPoMapper.updateByExampleSelective(po, example);
            if(ret == 0){
                //修改失败
                retObj = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
            } else{
                retObj = new ReturnObject<>();
            }
        } catch (DataAccessException e){
            //logger.error("selectAllRole: DataAccessException:" + e.getMessage());
            retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR,
                    String.format("数据库错误：%s", e.getMessage()));
        } catch (Exception e) {
            // 属未知错误
            //logger.error("严重错误：" + e.getMessage());
            retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR,
                    String.format("发生了严重的未知错误：%s", e.getMessage()));
        }
        return retObj;
    }

    /**
     * 买家删除购物车中商品
     * @param id:Cart id
     * @return ReturnObject<Object>
     */
    public ReturnObject<Object> deleteCartById(Long id){
        ReturnObject<Object> retObj = null;
        try{
            int ret = cartPoMapper.deleteByPrimaryKey(id);
            if(ret == 0){
                //删除失败
                retObj = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
            } else{
                retObj = new ReturnObject<>();
            }
        } catch (DataAccessException e){
            //logger.error("selectAllRole: DataAccessException:" + e.getMessage());
            retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR,
                    String.format("数据库错误：%s", e.getMessage()));
        } catch (Exception e) {
            // 属未知错误
            //logger.error("严重错误：" + e.getMessage());
            retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR,
                    String.format("发生了严重的未知错误：%s", e.getMessage()));
        }
        return retObj;
    }
}
