package cn.edu.xmu.favorite.service;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ReturnObject;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 陈渝璇
 * @description 商品收藏服务
 * @date Created in 16:02 2020/11/29
 **/
@Service
public interface FavoriteService {
    /**
     * 获取所有收藏的商品
     *
     * @param userId
     * @param pageNum
     * @param pageSize
     * @return ReturnObject<PageInfo<VoObject>> 分页返回买家足迹
     */
    ReturnObject<PageInfo<VoObject>> findPageOfFavorites(Long userId, int pageNum, int pageSize);

    /**
     * 买家收藏商品
     *
     * @param userId
     * @param skuId
     * @return ReturnObject<PageInfo<VoObject>> 分页返回买家足迹
     */
    @Transactional
    ReturnObject<VoObject> insertFavorite(Long userId, Long skuId);

    /**
     * 买家删除某个收藏的商品
     *
     * @param userId
     * @param id:Favorite id
     * @return ReturnObject<Object>
     */
    @Transactional
    ReturnObject<Object> deleteFavorite(Long userId, Long id);
}
