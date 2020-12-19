package cn.edu.xmu.share.controller;

import cn.edu.xmu.ooad.annotation.Audit;
import cn.edu.xmu.ooad.annotation.LoginUser;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ResponseUtil;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.share.model.bo.ShareActivity;
import cn.edu.xmu.share.model.vo.ShareActivityVo;
import cn.edu.xmu.share.model.vo.SharesRetVo;
import cn.edu.xmu.share.model.vo.SharesVo;
import cn.edu.xmu.share.model.vo.StateVo;
import cn.edu.xmu.share.service.BeSharedService;
import cn.edu.xmu.share.service.ShareActivityService;
import cn.edu.xmu.share.service.SharesService;

import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * https://app.swaggerhub.com/apis/mingqcn/OOMALL/1.0.14
 */

@Api(value = "分享服务", tags = "share")
@RestController /*Restful的Controller对象*/
@RequestMapping(value = "/share", produces = "application/json;charset=UTF-8")
public class ShareController {
    @Autowired
    ShareActivityService shareActivityService;

    @Autowired
    SharesService sharesService;

    @Autowired
    BeSharedService beSharedService;


    @GetMapping("/advertisement/states")
    public Object getShareActivity(){
        ShareActivity.State[] states=ShareActivity.State.class.getEnumConstants();
        List<StateVo> stateVos=new ArrayList<StateVo>();
        for(int i=0;i<states.length;i++){
            stateVos.add(new StateVo(states[i]));
        }
        return ResponseUtil.ok(new ReturnObject<List>(stateVos).getData());
    }


    /**-------------------------------------------------------------------------需要修改-1---------------------------------------------
     * 新增分享,生成分享链接
     * @param id
     * @param userId
     * @return  Object
     */
    @ApiOperation(value="新增分享,生成分享链接")
    @ApiImplicitParams({
    })
    @ApiResponses({
    })
    @Audit
    @PostMapping("skus/{id}/shares")
    public Object addshares(@PathVariable Long id,
                            @LoginUser @ApiIgnore @RequestParam(required = false, defaultValue = "") Long userId,
                            HttpServletResponse response){

        ReturnObject<SharesRetVo> ret = sharesService.addShareService(id, userId);
        if(ret.getCode()==ResponseCode.OK){
            response.setStatus(HttpStatus.SC_CREATED);
        }
        return Common.decorateReturnObject(ret);
    }
    /**
     * http://127.0.0.1:8080/skus/300/shares
     * -------------------------------------------------------------------------需要修改----------------------------------------------
     */



    /**-------------------------------------------------------------------------需要修改-1---------------------------------------------
     * 买家查询分享
     * @param
     */

    @ApiOperation(value = "买家获取分享记录")
    @ApiImplicitParams({
    })
    @ApiResponses({
    })
    @Audit
    @GetMapping("shares")
    public Object getownshares(@LoginUser @ApiIgnore @RequestParam(required = true, defaultValue ="") Long userId,
                               @RequestParam(value="goodsSkuId", required=false, defaultValue="") Long  goodsSkuId,
                               @RequestParam(value="beginTime", required=false, defaultValue="") String beginTime ,
                               @RequestParam(value="endTime", required=false, defaultValue="") String endTime ,
                               @RequestParam(value="page", required=false, defaultValue="1") Integer page ,
                               @RequestParam(value="pageSize", required=false, defaultValue="10") Integer pageSize,
                               HttpServletResponse response){


        ReturnObject<PageInfo<VoObject>> ret = sharesService.getOwnSharesService(userId, goodsSkuId, beginTime, endTime, page, pageSize);
        if(ret.getCode()!=ResponseCode.OK){
            response.setStatus(HttpStatus.SC_BAD_REQUEST);
        }
        return Common.getPageRetObject(ret);
    }

    /**完成用户后进行解析token
     * 测试用例
     * http://127.0.0.1:8080/shares
     * http://127.0.0.1:8080/shares?pageSize=10&page=1&goodsSpuId=442
     *http://127.0.0.1:8080/shares?pageSize=10&page=1&beginTime=2021-11-22 21:10:30&endTime=2021-11-22 21:10:30
     * http://127.0.0.1:8080/shares?pageSize=10&page=1&beginTime=2020-11-22 21:10:30&endTime=2021-11-22 21:10:30
     * -------------------------------------------------------------------------需要修改----------------------------------------------
     */


    /**-------------------------------------------------------------------------可写测试用例-1---------------------------------------------
     * 商家查询分享
     * @param did 店铺id
     * @param id skuid
     * @param page 页
     * @param pageSize 页尺寸
     */

    @ApiOperation(value = "商家获取分享记录")
    @ApiImplicitParams({
    })
    @ApiResponses({
    })
    @Audit
    @GetMapping("/shops/{did}/skus/{id}/shares")
    public Object getsharesByShop(@PathVariable Long did,
                                  @PathVariable Long id,
                                  @RequestParam(value="page", required=false, defaultValue="1") Integer page ,
                                  @RequestParam(value="pageSize", required=false, defaultValue="10") Integer pageSize){
        return Common.getPageRetObject(sharesService.getSharesBySpuIdService(did, id, page, pageSize));
    }


    /**修改位置：service层验证spuId、shopId是否存在、通过调用其他组的api
     * service：调用接口获取店铺商品skuId列表，或验证spuId是店铺的商品,以及userid和shopId是否匹配
     * http://127.0.0.1:8080/shops/1/shares?spuId=521
     * http://127.0.0.1:8080/shops/1/shares
     * -------------------------------------------------------------------------可写测试用例----------------------------------------------
     */


    /**-------------------------------------------------------------------------需要修改---------------------------------------------
     * 获取自己分享成功
     * @param userId
     * @return  Object
     */
    @ApiOperation(value="买家查询所有分享成功记录")
    @ApiImplicitParams({
    })
    @ApiResponses({
    })
    @Audit
    @GetMapping("beshared")
    public Object getbeshared(@LoginUser @ApiIgnore @RequestParam(required = false, defaultValue = "0") Long userId,
                              @RequestParam(value="skuId", required=false, defaultValue="") Long  skuId,
                              @RequestParam(value="beginTime", required=false, defaultValue="") String beginTime ,
                              @RequestParam(value="endTime", required=false, defaultValue="") String endTime ,
                              @RequestParam(value="page", required=false, defaultValue="1") Integer page ,
                              @RequestParam(value="pageSize", required=false, defaultValue="10") Integer pageSize){

        return Common.getPageRetObject(beSharedService.getOwnBeshared(userId, skuId, beginTime, endTime, page, pageSize));
    }
    /**controller登录
     * http://127.0.0.1:8080/beshared
     * http://127.0.0.1:8080/beshared?spuId=577
     * http://127.0.0.1:8080/beshared?endTime=2020-12-03 00:00:00
     * 操作资源不存在即ids.size=0
     * 存在问题nacos，数据太多发不过来
     * -------------------------------------------------------------------------需要修改----------------------------------------------
     */

    /**-------------------------------------------------------------------------需要修改---------------------------------------------
     * 管理查询所有分享成功记录
     * @param did
     * @param id
     * @param beginTime
     * @param endTime
     * @param page
     * @param pageSize
     * @Return Object
     */
    @ApiOperation(value="管理查询所有分享成功记录")
    @ApiImplicitParams({
    })
    @ApiResponses({
    })
    @Audit
    @GetMapping("/shops/{did}/skus/{id}/beshared")
    public Object getShopBeShared(@PathVariable Long did,
                              @PathVariable Long id,
                              @RequestParam(value="beginTime", required=false, defaultValue="") String beginTime ,
                              @RequestParam(value="endTime", required=false, defaultValue="") String endTime ,
                              @RequestParam(value="page", required=false, defaultValue="1") Integer page ,
                              @RequestParam(value="pageSize", required=false, defaultValue="10") Integer pageSize){

        return Common.getPageRetObject(beSharedService.getShopBeshared(did, id, beginTime, endTime, page, pageSize));
    }
    /**service层获取店铺spuid，管理员0查询所有没做判断
     * http://127.0.0.1:8080/shops/0/beshared?endTime=2020-12-03 00:00:00&pageSize=30
     * http://127.0.0.1:8080/shops/0/beshared?endTime=2020-12-03 00:00:00&pageSize=-1
     * http://127.0.0.1:8080/shops/0/beshared?endTime=2020-12-03 00:00:00&page=-1
     * -------------------------------------------------------------------------需要修改----------------------------------------------
     */




    /**-------------------------------------------------------------------------可写测试---------------------------------------------
     * 新增分享活动
     * @param vo:vo对象
     * @param 检查结果
     * @return  Object
     */
    @ApiOperation(value="平台或店家创建新的分享活动")
    @ApiImplicitParams({
    })
    @ApiResponses({
    })
    @Audit
    @PostMapping("/shops/{shopId}/skus/{id}/shareactivities")
    public Object addshareactivity( @PathVariable Long shopId,
                                    @PathVariable Long id,
                                    @Validated @RequestBody ShareActivityVo vo,BindingResult bindingResult,
                                    HttpServletResponse response){
        if(bindingResult.hasErrors()){
            response.setStatus(HttpStatus.SC_BAD_REQUEST);
            return Common.decorateReturnObject(new ReturnObject(ResponseCode.FIELD_NOTVALID));
        }

        ReturnObject<Object> ret = shareActivityService.addShareActivityService(shopId, id, vo);
        if(ret.getCode()==ResponseCode.OK){
            response.setStatus(HttpStatus.SC_CREATED);
        }else{
            response.setStatus(HttpStatus.SC_BAD_REQUEST);
        }
        return Common.decorateReturnObject(ret);
    }
    /**
     * -------------------------------------------------------------------------可写测试----------------------------------------------
     */




    /**-------------------------------------------------------------------------可写测试---------------------------------------------
    /**
     * 获取分享活动
     * @param shopId
     * @param skuId
     * @param page
     * @param pageSize
     * @return  Object
     */
    @ApiOperation(value="获取分享活动")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "Long", name = "shopId", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "Long", name = "spuId", required = false),
            @ApiImplicitParam(paramType =  "query", dataType = "Integer", name = "page", required = false),
            @ApiImplicitParam(paramType =  "query", dataType = "Integer", name = "pageSize", required = false),
    })
    @ApiResponses({
    })
    @Audit
    @GetMapping("shareactivities")
    public Object getshareactivity(@RequestParam(value="shopId", required=false, defaultValue="") Long shopId ,
                                   @RequestParam(value="skuId", required=false, defaultValue="") Long  skuId,
                                   @RequestParam(value="page", required=false, defaultValue="1") Integer page ,
                                   @RequestParam(value="pageSize", required=false, defaultValue="10") Integer pageSize){

        return Common.getPageRetObject(shareActivityService.getShareActivitiesService(shopId, skuId, page, pageSize));
    }
    /**-------------------------------------------------------------------------可写测试---------------------------------------------



    /**-------------------------------------------------------------------------需要修改---------------------------------------------
    /**
     * 修改分享活动
     * @param shopId
     * @parm id
     * @return  Object
     */
    @ApiOperation(value="修改分享活动")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "shopId", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "id", required = true),
            @ApiImplicitParam(paramType = "body", dataType = "ShareActivityVo", name = "shareActivityVo", required = true)
    })
    @ApiResponses({
    })
    @Audit
    @PutMapping("/shops/{shopId}/shareactivities/{id}")
    public Object modifyshareactivity(@Validated @RequestBody ShareActivityVo shareActivityVo, BindingResult bindingResult,
                                      @PathVariable Long shopId ,
                                      @PathVariable Long id,
                                      HttpServletResponse response){
        if(bindingResult.hasErrors()){
            response.setStatus(HttpStatus.SC_BAD_REQUEST);
            return Common.decorateReturnObject(new ReturnObject(ResponseCode.FIELD_NOTVALID));
        }
        ReturnObject<Object> ret = shareActivityService.modifyShareActivityService(shopId, id, shareActivityVo);
        if(ret.getCode()!=ResponseCode.OK){
            response.setStatus(HttpStatus.SC_BAD_REQUEST);
        }
        return Common.decorateReturnObject(ret);
    }
    /**
     * 判断时间是否合法·
     * http://127.0.0.1:8080/shops/0/shareactivities/303068
     * {"beginTime":"2078-12-08 17:34:35",
     * "endTime":"2079-12-08 17:34:35",
     * "strategy":"asdsad"
     * } 处于上线不能修改
     *
     * 传入如
     * {"beginTime":"2078-12-08 17:34:35",
     * "endTime":"2079-12-08 17:34:35",
     * "strategy":""
     * }不修改其中某个字段
     * -------------------------------------------------------------------------需要修改----------------------------------------------
     */




    /**
     * 终止指定活动的分享活动
     * @param shopId
     * @parm id
     * @return  Object
     */
    @ApiOperation(value="终止指定商品的分享活动")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "shopId", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "spuId", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "id", required = true)
    })
    @ApiResponses({
    })
    @Audit
    @DeleteMapping("/shops/{shopId}/shareactivities/{id}")
    public Object deleteshareactivity(@PathVariable Long shopId ,
                                      @PathVariable Long id,
                                      HttpServletResponse response){
        ReturnObject<Object> ret = shareActivityService.deleteShareActivityService(shopId, id);
        if(ret.getCode()==ResponseCode.RESOURCE_ID_OUTSCOPE){
            response.setStatus(HttpStatus.SC_FORBIDDEN);
        }
        return Common.decorateReturnObject(ret);
    }


    /**
     * 上线指定的分享活动
     * @param shopId
     * @parm id
     * @return  Object
     */
    @ApiOperation(value="终止指定商品的分享活动")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "shopId", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "spuId", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "id", required = true)
    })
    @ApiResponses({
    })
    @Audit
    @PutMapping("/shops/{shopId}/shareactivities/{id}/online")
    public Object putshareactivityonline(@PathVariable Long shopId,
                                      @PathVariable Long id){
        return Common.decorateReturnObject(shareActivityService.onlineShareActivityService(shopId, id));
    }

}
