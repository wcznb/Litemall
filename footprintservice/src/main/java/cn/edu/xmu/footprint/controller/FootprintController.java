package cn.edu.xmu.footprint.controller;

import cn.edu.xmu.footprint.service.FootprintService;
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

import java.util.Map;

/**
 * 足迹服务
 * @author 陈渝璇
 * Modified at 2020/11/26 16:54
 **/
@Api(value = "足迹服务", tags = "footprint")
@RestController /*Restful的Controller对象*/
@RequestMapping(value = "/", produces = "application/json;charset=UTF-8")
public class FootprintController {

    private  static  final Logger logger = LoggerFactory.getLogger(FootprintController.class);

    @Autowired
    private FootprintService footprintService;

    /**
     * @author 陈渝璇
     * @date Created in 2020/11/26 16:53
     **/
    @ApiOperation(value = "平台管理员获取买家足迹")
    @Audit
    @GetMapping("/shops/{did}/footprints")
    public Object findPageOfFootprints(@PathVariable Long did,
                                       @RequestParam(required=false) Long userId,
                                       @RequestParam(required=false) String beginTime,
                                       @RequestParam(required=false) String endTime,
                                       @RequestParam(required=false, defaultValue = "1") Integer page,
                                       @RequestParam(required=false, defaultValue = "10") Integer pageSize){
        Object object = null;
        ReturnObject<PageInfo<VoObject>> ret = footprintService.findPageOfFootprints(did, userId, beginTime, endTime, page, pageSize);
        object = Common.getPageRetObject(ret);
        return object;
    }

    /**
     * @author 陈渝璇
     * @date Created in 2020/11/26 16:53
     **/
    @ApiOperation(value = "增加足迹")
    @Audit
    @PostMapping("skus/{id}/footprints")
    public Object insertFootprint(@LoginUser Long userId, @PathVariable Long id){
        ReturnObject<Object> ret = footprintService.insertFootprint(userId, id);//id是skuId
        return Common.decorateReturnObject(ret);
    }
}
