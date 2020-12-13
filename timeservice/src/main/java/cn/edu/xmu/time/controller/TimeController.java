package cn.edu.xmu.time.controller;
import cn.edu.xmu.ooad.annotation.Audit;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ResponseUtil;
import cn.edu.xmu.ooad.util.ReturnObject;

import cn.edu.xmu.time.model.vo.NewTimeSegmentVo;
import cn.edu.xmu.time.service.TimeService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * @author shyanne 3184
 */


@Api(value = "timesegments")
@RestController
@RequestMapping(value = "/", produces = "application/json;charset=UTF-8")
public class TimeController {
    @Autowired
    private TimeService timeService;

    @Autowired
    private HttpServletResponse httpServletResponse;


    @Audit
    @PostMapping("advertisement/timesegments")
    public Object addTimeSegment(@Validated @RequestBody NewTimeSegmentVo vo, BindingResult result){
        if(result.hasErrors()){
            return Common.processFieldErrors(result,httpServletResponse);
        }
        Byte type=0;
        ReturnObject returnObject=timeService.addTimeSegment(vo,type);
        if(returnObject.getCode()== ResponseCode.OK){
            return ResponseUtil.ok(returnObject.getData());
        }
        else {
            return ResponseUtil.fail(returnObject.getCode());
        }
    }
    @Audit
    @PostMapping("flashsale/timesegments")
    public Object addTimeSegment1(@Validated @RequestBody NewTimeSegmentVo vo, BindingResult result){
        if(result.hasErrors()){
            return Common.processFieldErrors(result,httpServletResponse);
        }
        Byte type=1;
        ReturnObject returnObject=timeService.addTimeSegment(vo,type);
        if(returnObject.getCode()== ResponseCode.OK){
            return ResponseUtil.ok(returnObject.getData());
        }
        else {
            return ResponseUtil.fail(returnObject.getCode());
        }
    }
    /**
     * 要判断本id的时间段种类，以防它操作非本类型的时间段
     */
    @Audit
    @DeleteMapping("advertisement/timesegments/{id}")
    public Object deleteTimeSegment0(@PathVariable("id") Long id){
        ReturnObject returnObject = timeService.deleteTimeSegment0(id);
        return Common.decorateReturnObject(returnObject);
    }
    @Audit
    @DeleteMapping("flashsale/timesegments/{id}")
    public Object deleteTimeSegment1(@PathVariable("id") Long id){
        ReturnObject returnObject = timeService.deleteTimeSegment1(id);
        return Common.decorateReturnObject(returnObject);
    }
    @Audit
    @GetMapping("advertisement/timesegments")
    public Object getAllSegments0(
            @RequestParam(required = false, defaultValue = "1")  Integer page,
            @RequestParam(required = false, defaultValue = "10")  Integer pagesize
    ){
        Object object = null;
   if(page <= 0 || pagesize <= 0) {
            object = Common.getNullRetObj(new ReturnObject<>(ResponseCode.FIELD_NOTVALID), httpServletResponse);
        } else {
            Byte type=0;
            ReturnObject<PageInfo<VoObject>> returnObject = timeService.getAllTimeSegments(type,page, pagesize);

            object = Common.getPageRetObject(returnObject);
        }




        return object;
    }

    @Audit
    @GetMapping("flashsale/timesegments")
    public Object getAllSegments1(
            @RequestParam(required = false, defaultValue = "1")  Integer page,
            @RequestParam(required = false, defaultValue = "10")  Integer pagesize
    ){
        Object object = null;

        if(page <= 0 || pagesize <= 0) {
            object = Common.getNullRetObj(new ReturnObject<>(ResponseCode.FIELD_NOTVALID), httpServletResponse);
        } else {
            Byte type=1;
            ReturnObject<PageInfo<VoObject>> returnObject = timeService.getAllTimeSegments( type,page, pagesize);

            object = Common.getPageRetObject(returnObject);
        }

        return object;
    }

}
