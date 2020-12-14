package cn.edu.xmu.favorite.controller;

import cn.edu.xmu.favorite.service.FavoriteService;
import cn.edu.xmu.favorite.service.impl.FavoriteServiceImpl;
import cn.edu.xmu.ooad.annotation.Audit;
import cn.edu.xmu.ooad.annotation.LoginUser;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.ooad.util.ReturnObject;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 商品收藏服务
 * @author 陈渝璇
 * Modified at 2020/11/27
 **/
@Api(value = "商品收藏服务", tags = "favorite")
@RestController /*Restful的Controller对象*/
@RequestMapping(value = "/favorites", produces = "application/json;charset=UTF-8")
public class FavoriteController {
    private static final Logger logger = LoggerFactory.getLogger(FavoriteController.class);

    @Autowired
    private FavoriteService favoriteService;

    /**
     * @author 陈渝璇
     * @date Created in 2020/11/27
     **/
    @ApiOperation(value = "买家查看所有收藏的商品")
    @Audit
    @GetMapping("")
    public Object findPageOfFavorites(@LoginUser Long userId,
                                       @RequestParam(required=false, defaultValue = "1") Integer page,
                                       @RequestParam(required=false, defaultValue = "10") Integer pageSize){
        ReturnObject<PageInfo<VoObject>> ret = favoriteService.findPageOfFavorites(userId, page, pageSize);
        return Common.getPageRetObject(ret);
    }

    /**
     * @author 陈渝璇
     * @date Created in 2020/11/27
     **/
    @ApiOperation(value = "买家收藏商品")
    @Audit
    @PostMapping("/goods/{skuId}")
    public Object insertFavorite(@LoginUser Long userId,@PathVariable Long skuId){
        ReturnObject<VoObject> retObj = favoriteService.insertFavorite(userId, skuId);
        return Common.getRetObject(retObj);
    }

    /**
     * @author 陈渝璇
     * @date Created in 2020/11/27
     **/
    @ApiOperation(value = "买家删除某个收藏的商品")
    @Audit
    @DeleteMapping("/{id}")
    public Object deleteFavorite(@LoginUser Long userId, @PathVariable Long id){
        ReturnObject<Object> retObj = favoriteService.deleteFavorite(userId, id);
        return Common.decorateReturnObject(retObj);
    }
}
