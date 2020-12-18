package cn.edu.xmu.favorite.service.impl;

import cn.edu.xmu.favorite.dao.FavoriteDao;
import cn.edu.xmu.favorite.model.bo.Favorite;
import cn.edu.xmu.favorite.model.po.FavoritePo;
import cn.edu.xmu.favorite.service.FavoriteService;
import cn.edu.xmu.favorite.util.ListToMap;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.provider.model.vo.GoodsSkuSimpleRetVo;
import cn.edu.xmu.provider.server.GoodsService;
import com.github.pagehelper.PageInfo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 陈渝璇
 * @description 商品收藏服务
 * @date Created in 16:02 2020/11/29
 **/
@Service
public class FavoriteServiceImpl implements FavoriteService {
    private static final Logger logger = LoggerFactory.getLogger(FavoriteServiceImpl.class);

    @Autowired
    private FavoriteDao favoriteDao;

    @DubboReference(version = "1.1.1")
    GoodsService goodsService;

    @Override
    public ReturnObject<PageInfo<VoObject>> findPageOfFavorites(Long userId, int pageNum, int pageSize){
        ReturnObject<PageInfo<FavoritePo>> favoritePoRetObj = favoriteDao.findPageOfFavorites(userId,pageNum,pageSize);
        if(favoritePoRetObj.getCode()==ResponseCode.OK){
            //搜索成功
            PageInfo<FavoritePo> favoritePoPage = favoritePoRetObj.getData();
            List<FavoritePo> favoritePos = favoritePoPage.getList();

            //用skuids获取map<skuid,goodsSkuSimpleRetVo>
            List<Long> skuIds = favoritePos.stream().map(favoritePo->favoritePo.getGoodsSkuId()).collect(Collectors.toList());
            //从商品模块获得信息，调用内部接口
            ReturnObject<List<GoodsSkuSimpleRetVo>> retSkuSimpleRetVos = null;
            if(skuIds.size()!=0)
                retSkuSimpleRetVos = goodsService.getGoodsSkuListById(skuIds);
            List<VoObject> retObj = new ArrayList<>(favoritePos.size());
            if(retSkuSimpleRetVos == null||retSkuSimpleRetVos.getCode() == ResponseCode.OK){
                if(retSkuSimpleRetVos != null){
                    ListToMap listToMap = new ListToMap();
                    Map<Long,GoodsSkuSimpleRetVo> idMapGoodsSku = listToMap.GoodsSkuSimpleRetVoListToMap(retSkuSimpleRetVos.getData());
                    for(FavoritePo po : favoritePos){
                        Favorite favorite = new Favorite(idMapGoodsSku.get(po.getGoodsSkuId()),po);
                        retObj.add(favorite);
                    }
                }
                PageInfo<VoObject> favoritePage = new PageInfo<>(retObj);
                favoritePage.setPages(favoritePoPage.getPages());
                favoritePage.setPageNum(favoritePoPage.getPageNum());
                favoritePage.setPageSize(favoritePoPage.getPageSize());
                favoritePage.setTotal(favoritePoPage.getTotal());
                return new ReturnObject<>(favoritePage);
            } else{
                return new ReturnObject<>(retSkuSimpleRetVos.getCode());
            }
        }
        return new ReturnObject<>(favoritePoRetObj.getCode());
    }

    @Transactional
    @Override
    public ReturnObject<VoObject> insertFavorite(Long userId, Long skuId){
        LocalDateTime now = LocalDateTime.now();
        FavoritePo favoritePo = new FavoritePo();
        favoritePo.setCustomerId(userId);
        favoritePo.setGoodsSkuId(skuId);
        favoritePo.setGmtCreate(now);
        favoritePo.setGmtModified(now);

        //插入
        ReturnObject<FavoritePo> favoritePoRet = favoriteDao.insertFavorite(favoritePo);
        if(favoritePoRet.getCode()==ResponseCode.OK){
            //插入成功，组合信息
            FavoritePo po = favoritePoRet.getData();
            Favorite favorite = new Favorite(po);
            //从商品模块获得信息
            ReturnObject<GoodsSkuSimpleRetVo> skuSimpleRetVoReturnObject = goodsService.getGoodsSkuById(skuId);
            if(skuSimpleRetVoReturnObject.getCode() == ResponseCode.OK){
                favorite.setGoodsSku(skuSimpleRetVoReturnObject.getData());
                return new ReturnObject<>(favorite);
            }else{
                return new ReturnObject<>(skuSimpleRetVoReturnObject.getCode());
            }

        }
        return new ReturnObject<>(favoritePoRet.getCode());
    }

    @Transactional
    @Override
    public ReturnObject<Object> deleteFavorite(Long userId, Long id){
        ReturnObject<FavoritePo> favoritePoRetObj = favoriteDao.findFavorite(id);
        ReturnObject<Object> retObj = new ReturnObject<>(favoritePoRetObj.getCode(),favoritePoRetObj.getErrmsg());
        if(retObj.getCode()== ResponseCode.OK) {
            FavoritePo favoritePo = favoritePoRetObj.getData();
            if (favoritePo.getCustomerId().compareTo(userId) == 0) {
                retObj = favoriteDao.deleteFavorite(id);
            } else {
                //操作资源不是自己的
                retObj = new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE);
            }
        }
        return retObj;
    }
}
