package cn.edu.xmu.favorite.dao;

import cn.edu.xmu.favorite.mapper.FavoritePoMapper;
import cn.edu.xmu.favorite.model.bo.Favorite;
import cn.edu.xmu.favorite.model.po.FavoritePo;
import cn.edu.xmu.favorite.model.po.FavoritePoExample;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 陈渝璇
 * createdBy 陈渝璇 2020-11-27
 * modifiedBy 陈渝璇 2020-11-27
 */
@Repository
public class FavoriteDao {

    private static final Logger logger = LoggerFactory.getLogger(FavoriteDao.class);

    @Autowired
    FavoritePoMapper favoritePoMapper;

    /**
     * 分页获取一页收藏的商品
     *
     * @param userId
     * @param pageNum
     * @param pageSize
     * @return ReturnObject<PageInfo<VoObject>> 分页返回买家足迹
     */
    public ReturnObject<PageInfo<FavoritePo>> findPageOfFavorites(Long userId, Integer pageNum, Integer pageSize){
        FavoritePoExample favoritePoExample = new FavoritePoExample();
        //添加查询条件
        FavoritePoExample.Criteria criteria = favoritePoExample.createCriteria();
        criteria.andCustomerIdEqualTo(userId);

        //分页查询
        PageHelper.startPage(pageNum, pageSize);
        List<FavoritePo> favoritePos = null;

        try{
            favoritePos = favoritePoMapper.selectByExample(favoritePoExample);

        } catch (DataAccessException e){
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR,
                    String.format("数据库错误：%s", e.getMessage()));
        } catch (Exception e) {
            // 属未知错误
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR,
                    String.format("发生了严重的未知错误：%s", e.getMessage()));
        }
        PageInfo<FavoritePo> favoritePage = PageInfo.of(favoritePos);
        return new ReturnObject<>(favoritePage);
    }

    /**
     * 通过skuid获取单个收藏信息
     * @param skuId
     * @return
     */
    public ReturnObject<FavoritePo> findFavoriteByCoustomerIdAndSkuId(Long customerId, Long skuId){
        ReturnObject<FavoritePo> favoritePoRetObj = null;
        try{
            FavoritePoExample example = new FavoritePoExample();
            FavoritePoExample.Criteria criteria = example.createCriteria();
            criteria.andGoodsSkuIdEqualTo(skuId);
            criteria.andCustomerIdEqualTo(customerId);

            List<FavoritePo> favoritePos = favoritePoMapper.selectByExample(example);
            FavoritePo po = null;
            if(favoritePos.size()!=0)
                po = favoritePos.get(0);
            favoritePoRetObj = new ReturnObject<>(po);
        } catch (DataAccessException e){
            //数据库错误
            favoritePoRetObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR,
                    String.format("数据库错误：%s", e.getMessage()));
        } catch (Exception e) {
            // 属未知错误
            favoritePoRetObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR,
                    String.format("发生了严重的未知错误：%s", e.getMessage()));
        }
        return favoritePoRetObj;
    }

    /**
     * 通过主键获取单个收藏信息
     * @param id
     * @return
     */
    public ReturnObject<FavoritePo> findFavoriteById(Long id){
        ReturnObject<FavoritePo> favoritePoRetObj = null;
        try{
            FavoritePo favoritePo = favoritePoMapper.selectByPrimaryKey(id);
            favoritePoRetObj = new ReturnObject<>(favoritePo);
        } catch (DataAccessException e){
            //数据库错误
            favoritePoRetObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR,
                    String.format("数据库错误：%s", e.getMessage()));
        } catch (Exception e) {
            // 属未知错误
            favoritePoRetObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR,
                    String.format("发生了严重的未知错误：%s", e.getMessage()));
        }
        return favoritePoRetObj;
    }

    /**
     * 买家收藏商品
     * @param favoritePo
     * @return
     */
    public ReturnObject<FavoritePo> insertFavorite(FavoritePo favoritePo){
        ReturnObject<FavoritePo> retObj = null;
        try{
            int ret = favoritePoMapper.insert(favoritePo);
            if(ret == 0){
                //新增失败
                retObj = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST,
                        String.format("新增失败：" + favoritePo.getClass()));
            }
            else{
                retObj = new ReturnObject<>(favoritePo);
            }
        } catch (DataAccessException e){
            //数据库错误
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
     * 买家删除某个收藏的商品
     * @param id:Favorite id
     * @return ReturnObject<Object>
     */
    public ReturnObject<Object> deleteFavorite(Long id){
        ReturnObject<Object> retObj = null;
        try {
            int ret = favoritePoMapper.deleteByPrimaryKey(id);
            if (ret == 0) {
                //删除失败
                retObj = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
            } else {
                //删除成功
                retObj = new ReturnObject<>();
            }
        } catch (DataAccessException e){
            //数据库错误
            retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR,
                    String.format("数据库错误：%s", e.getMessage()));
        } catch (Exception e) {
            // 属未知错误
            retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR,
                    String.format("发生了严重的未知错误：%s", e.getMessage()));
        }
        return retObj;
    }
}
