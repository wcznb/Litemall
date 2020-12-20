package cn.edu.xmu.advertisement.controller;


import cn.edu.xmu.advertisement.model.bo.Advertisement;
import cn.edu.xmu.advertisement.model.vo.*;
import cn.edu.xmu.advertisement.service.impl.AdvertisementServiceImpl;
import cn.edu.xmu.ooad.annotation.Audit;
import cn.edu.xmu.ooad.annotation.Depart;
import cn.edu.xmu.ooad.annotation.LoginUser;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ResponseUtil;
import cn.edu.xmu.ooad.util.ReturnObject;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Api(value = "广告服务", tags = "advertisement")
@RestController /*Restful的Controller对象*/
@RequestMapping(value = "/", produces = "application/json;charset=UTF-8")
public class AdvertisementController {

    private Logger logger = LoggerFactory.getLogger(AdvertisementController.class);


    @Autowired
    private HttpServletResponse httpServletResponse;

    @Autowired
    private AdvertisementServiceImpl advertisementServiceImpl;


    /**
     * 管理员设置默认广告
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "管理员设置默认广告")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "did", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true)
    })
    @Audit
    @PutMapping("/shops/{did}/advertisement/{id}/default")
    public Object becomeDefault(@LoginUser Long userId,@PathVariable Long id, @PathVariable Long did) {

        ReturnObject success = advertisementServiceImpl.becomeDefault(id);


        return Common.decorateReturnObject(success);


    }


    /**
     * 管理员下架广告
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "管理员下架广告")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(name = "id", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "did", required = true, dataType = "Long", paramType = "path")
    })
    @ApiResponses({
            @ApiResponse(code = 608, message = "广告状态禁止"),
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @PutMapping("/shops/{did}/advertisement/{id}/offshelves")
    public Object offshelf(@LoginUser Long userId,@PathVariable Long id, @PathVariable Long did) {

        ReturnObject<Boolean> success = advertisementServiceImpl.adoffshelf(id);
        if (success.getData() == null) {

            return Common.decorateReturnObject(success);
        } else {
            return ResponseUtil.ok();
        }
    }


    /**
     * 管理员上架广告
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "管理员上架广告")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(name = "id", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "did", required = true, dataType = "Long", paramType = "path")
    })
    @ApiResponses({
            @ApiResponse(code = 608, message = "广告状态禁止"),
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @PutMapping("/shops/{did}/advertisement/{id}/onshelves")
    public Object onshelf(@LoginUser Long userId,@PathVariable Long id, @PathVariable Long did) {

        ReturnObject<Boolean> success = advertisementServiceImpl.adonshelf(id);
        if (success.getData() == null) {

            return Common.decorateReturnObject(success);
        } else {
            return ResponseUtil.ok();
        }
    }


    /**
     * 删除某一个广告
     *
     * @param id
     * @return createdBy
     */
    @ApiOperation(value = "管理员删除某一个广告")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "Token", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "id", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "did", required = true, dataType = "Long", paramType = "path")

    })
    @ApiResponses({
            @ApiResponse(code = 608, message = "广告状态禁止"),
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @DeleteMapping("/shops/{did}/advertisement/{id}")
    public Object deleteAd(@LoginUser Long userId,@PathVariable Long id, @Depart @PathVariable Long did) {
        logger.debug("deleteAd: id = " + id);
        ReturnObject returnObject = advertisementServiceImpl.deleteAd(id);
        return Common.decorateReturnObject(returnObject);

    }


    /**
     * 管理员审核广告
     *
     * @param id 广告id
     * @param vo
     * @return Object
     */
    @ApiOperation(value = "管理员审核广告", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "Token", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "id", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "did", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(paramType = "body", dataType = " AdvertisementMessageVo", name = "vo", value = "审核的内容", required = true)

    })
    @ApiResponses({
            @ApiResponse(code = 608, message = "广告状态禁止"),
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @PutMapping("/shops/{did}/advertisement/{id}/audit")
    public Object messageAd(@LoginUser Long userId,@PathVariable Long id, @PathVariable Long did, @Validated @RequestBody AdvertisementMessageVo vo) {
        ReturnObject<Object> success = advertisementServiceImpl.messageAd(id, vo);

        if (success.getCode() == ResponseCode.FIELD_NOTVALID) {

            return new ResponseEntity(ResponseUtil.fail(success.getCode()), HttpStatus.BAD_REQUEST);
        } else
            return Common.decorateReturnObject(success);

    }


    /**
     * 获得广告所有状态
     */

    @ApiOperation(value = "获得广告所有状态")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "authorization", value = "Token", required = true, dataType = "String", paramType = "header"),
//    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    //@Audit
    @GetMapping("/advertisement/states")
    public Object getAllAdStates() {
        Advertisement.State[] states = Advertisement.State.class.getEnumConstants();
        List<AdvertisementStateVo> stateVos = new ArrayList<AdvertisementStateVo>();
        for (int i = 0; i < states.length; i++) {
            stateVos.add(new AdvertisementStateVo(states[i]));
        }
        return ResponseUtil.ok(new ReturnObject<List>(stateVos).getData());
    }


    /**
     * 管理员查看某一时段的广告
     */
    @ApiOperation(value = "管理员查看某一广告时段的广告")
    @Audit
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String",  name = "authorization", value ="用户token", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "beginDate", value = "开始日期", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "endDate", value = "结束日期", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "page", value = "页码", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "pagesize", value = "每页数目", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "id", value = "时段id", required = true)
    })
    @GetMapping("/shops/{did}/timesegments/{id}/advertisement")
    public Object getAdBySegID(@PathVariable Long id,
                               @PathVariable  Long did,
                               @LoginUser Long userId,
                               @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                               @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                               @RequestParam(value = "beginDate", required = false) String beginDate,
                               @RequestParam(value = "endDate", required = false) String endDate) {


        //若id不存在，返回404+504
        ReturnObject<PageInfo<VoObject>> ret = advertisementServiceImpl.getAdBySegId(id, page, pageSize, beginDate, endDate);
        if (ret.getCode() == ResponseCode.RESOURCE_ID_NOTEXIST)

            return Common.getNullRetObj(new ReturnObject<>(ret.getCode(), ret.getErrmsg()), httpServletResponse);
            //400+503
        else if (ret.getCode() == ResponseCode.FIELD_NOTVALID)
            //return Common.getNullRetObj(new ReturnObject<>(ResponseCode.FIELD_NOTVALID), httpServletResponse);

            return new ResponseEntity(ResponseUtil.fail(ResponseCode.FIELD_NOTVALID), HttpStatus.BAD_REQUEST);
        else
            return Common.getPageRetObject(ret);


    }


    /**
     * 管理员上传广告图片
     *
     * @param id
     * @param multipartFile
     * @return
     * @author 24320182203218
     **/
    @ApiOperation(value = "管理员上传广告图片", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "formData", dataType = "file", name = "img", value = "文件", required = true),
            @ApiImplicitParam(name = "did", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "id", required = true, dataType = "String", paramType = "path")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = 506, message = "该目录文件夹没有写入的权限"),
            @ApiResponse(code = 508, message = "图片格式不正确"),
            @ApiResponse(code = 509, message = "图片大小超限")
    })
    @Audit
    @PostMapping("/shops/{did}/advertisement/{id}/uploadImg")
    public Object uploadImg(@LoginUser Long userId,@RequestParam("img") MultipartFile multipartFile, @PathVariable Long id, @PathVariable Long did) {
        logger.debug("uploadImg: id = " + id + " img :" + multipartFile.getOriginalFilename());
        ReturnObject returnObject = advertisementServiceImpl.uploadImg(id, multipartFile);

        return Common.decorateReturnObject(returnObject);
    }


    /**
     * 管理员修改广告内容
     *
     * @param id  广告id
     * @param vo1
     * @return Object
     */
    @ApiOperation(value = "管理员修改广告内容", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "Token", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "id", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "did", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(paramType = "body", dataType = "AdvertisementUpdateVo", name = "vo1", value = "可修改的广告的内容", required = true)

    })
    @Audit
    @PutMapping("/shops/{did}/advertisement/{id}")
    public Object messageUpdate(@LoginUser Long userId,@PathVariable Long id, @PathVariable Long did, @Validated @RequestBody AdvertisementUpdateVo vo1) {
        ReturnObject<Object> success = advertisementServiceImpl.messageUpdate(id, vo1);

        if (success.getCode() == ResponseCode.FIELD_NOTVALID||success.getCode()==ResponseCode.Log_Bigger)
            return new ResponseEntity(ResponseUtil.fail(success.getCode()), HttpStatus.BAD_REQUEST);

        return Common.decorateReturnObject(success);

    }


    /**
     * 管理员在广告时段下新建广告
     *
     * @param vo
     * @return Object
     */
    @ApiOperation(value = "管理员在广告时段下新建广告")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "Token", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "id", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "did", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(paramType = "body", dataType = "NewAdvertisement", name = "vo", value = "newAdvertisementInfo", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = 603, message = "达到广告时段（8个）")
    })
    @Audit
    @PostMapping("/shops/{did}/timesegments/{id}/advertisement")
    public Object newAdBySegID(@LoginUser Long userId,@PathVariable Long id, @PathVariable Long did, @Validated @RequestBody NewAdvertisementVo vo) {

        ReturnObject returnObject = advertisementServiceImpl.createUnderSegID(id, vo);

        if(returnObject.getCode()==ResponseCode.FIELD_NOTVALID||returnObject.getCode()==ResponseCode.Log_Bigger)
            return new ResponseEntity(ResponseUtil.fail(returnObject.getCode()), HttpStatus.BAD_REQUEST);

        else  if (returnObject.getCode() == ResponseCode.OK)
            return new ResponseEntity(ResponseUtil.ok(returnObject.getData()), HttpStatus.CREATED);

        else
            return Common.getNullRetObj(new ReturnObject<>(returnObject.getCode(), returnObject.getErrmsg()), httpServletResponse);

    }

    /**
     * 管理员在广告时段下增加广告
     *
     * @param id
     * @return Object
     */
    @ApiOperation(value = "管理员在广告时段下增加广告")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "Token", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "tid", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "did", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "id", required = true, dataType = "String", paramType = "path")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = 603, message = "达到广告时段（8个）")
    })
    @Audit
    @PostMapping("/shops/{did}/timesegments/{tid}/advertisement/{id}")
    public Object AddAdBySegID(@LoginUser Long userId,@PathVariable Long tid, @PathVariable Long did, @PathVariable Long id) {

        ReturnObject returnObject = advertisementServiceImpl.addAdBySegID(tid, id);

        if(returnObject.getData()!=null)
            return new ResponseEntity(ResponseUtil.ok(returnObject.getData()),HttpStatus.CREATED);
        else
            return Common.decorateReturnObject(returnObject);
    }


    /**
     * 获取当前时段广告列表
     */
    @ApiOperation(value = "获取当前时段广告列表", produces = "application/json")
    //不需要登录那还需要验证吗？
//    @Audit
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header",dataType = "String",name = "authorization",value = "用户token",required = true),
    })
    @GetMapping("/advertisement/current")
    public Object getCurrentById() {


        ReturnObject<List> returnObject = advertisementServiceImpl.getCurrentAd();

        return Common.getListRetObject(returnObject);

    }


//    /**
//     * 时段删除该时段下的广告相应的segid=0，时段调用，测试
//     * @param id
//     * @return
//     */
//    @ApiOperation(value = "管理员设置默认广告")
//    @ApiImplicitParams({
//            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
//    })
//    @Audit
//    @PutMapping("advertisement/{id}/sd")
//    public Object becomeDefault1(@PathVariable Long id) {
//
//        ReturnObject success = advertisementServiceImpl.deleteSegIDThenZero(id);
//        if (success.getData() == null)  {
//
//            return Common.getNullRetObj(new ReturnObject<>(success.getCode(), success.getErrmsg()), httpServletResponse);
//
//        }else {
//            return ResponseUtil.ok();
//        }
//    }


}
