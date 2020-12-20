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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
    @PostMapping("shops/{did}/advertisement/timesegments")
    public Object addTimeSegment(@RequestBody NewTimeSegmentVo vo){
        if(vo.getBeginTime()==null||vo.getBeginTime().isEmpty()) {

        return new ResponseEntity(ResponseUtil.fail(ResponseCode.Log_BEGIN_NULL), HttpStatus.BAD_REQUEST);

        }

        if(vo.getEndTime()==null||vo.getEndTime().isEmpty()) {

            return new ResponseEntity(ResponseUtil.fail(ResponseCode.Log_END_NULL), HttpStatus.BAD_REQUEST);

        }
        LocalDateTime BeginTime=LocalDateTime.parse(vo.getBeginTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime EndTime=LocalDateTime.parse(vo.getEndTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        if(BeginTime.isAfter(EndTime)){
            return new ResponseEntity(ResponseUtil.fail(ResponseCode.Log_Bigger), HttpStatus.BAD_REQUEST);

        }

        Byte type=0;
        ReturnObject returnObject=timeService.addTimeSegment(vo,type);

        if(returnObject.getCode()== ResponseCode.TIMESEG_CONFLICT){
            return new ResponseEntity(ResponseUtil.fail(ResponseCode.TIMESEG_CONFLICT), HttpStatus.BAD_REQUEST);
        }
        if(returnObject.getCode()== ResponseCode.OK){
            return new ResponseEntity(ResponseUtil.ok(returnObject.getData()), HttpStatus.CREATED);
        }
        else {
            return ResponseUtil.fail(returnObject.getCode());
        }
    }
    @Audit
    @PostMapping("shops/{did}/flashsale/timesegments")
    public Object addTimeSegment1(@RequestBody NewTimeSegmentVo vo){
        if(vo.getBeginTime()==null||vo.getBeginTime().isEmpty()) {

            return new ResponseEntity(ResponseUtil.fail(ResponseCode.Log_BEGIN_NULL), HttpStatus.BAD_REQUEST);

        }

        if(vo.getEndTime()==null||vo.getEndTime().isEmpty()) {

            return new ResponseEntity(ResponseUtil.fail(ResponseCode.Log_END_NULL), HttpStatus.BAD_REQUEST);

        }
        LocalDateTime BeginTime=LocalDateTime.parse(vo.getBeginTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime EndTime=LocalDateTime.parse(vo.getEndTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        if(BeginTime.isAfter(EndTime)){
            return new ResponseEntity(ResponseUtil.fail(ResponseCode.Log_Bigger), HttpStatus.BAD_REQUEST);

        }
        Byte type=1;
        ReturnObject returnObject=timeService.addTimeSegment(vo,type);
        if(returnObject.getCode()== ResponseCode.TIMESEG_CONFLICT){
            return new ResponseEntity(ResponseUtil.fail(ResponseCode.TIMESEG_CONFLICT), HttpStatus.BAD_REQUEST);
        }
        if(returnObject.getCode()== ResponseCode.OK){
            return new ResponseEntity(ResponseUtil.ok(returnObject.getData()), HttpStatus.CREATED);
        }
        else {
            return ResponseUtil.fail(returnObject.getCode());
        }
    }
    /**
     * 要判断本id的时间段种类，以防它操作非本类型的时间段
     */
    @Audit
    @DeleteMapping("shops/{did}/advertisement/timesegments/{id}")
    public Object deleteTimeSegment0(@PathVariable("id") Long id){
        ReturnObject returnObject = timeService.deleteTimeSegment0(id);
        if(returnObject.getCode()== ResponseCode.RESOURCE_ID_OUTSCOPE){
            return new ResponseEntity(ResponseUtil.fail(ResponseCode.RESOURCE_ID_OUTSCOPE), HttpStatus.FORBIDDEN);
        }
        return Common.decorateReturnObject(returnObject);
    }
    @Audit
    @DeleteMapping("shops/{did}/flashsale/timesegments/{id}")
    public Object deleteTimeSegment1(@PathVariable("id") Long id){
        ReturnObject returnObject = timeService.deleteTimeSegment1(id);
        if(returnObject.getCode()== ResponseCode.RESOURCE_ID_OUTSCOPE){
            return new ResponseEntity(ResponseUtil.fail(ResponseCode.RESOURCE_ID_OUTSCOPE), HttpStatus.FORBIDDEN);
        }

        return Common.decorateReturnObject(returnObject);
    }
    @Audit
    @GetMapping("shops/{did}/advertisement/timesegments")
    public Object getAllSegments0(
            @RequestParam(required = false, defaultValue = "1")  Integer page,
            @RequestParam(required = false, defaultValue = "10")  Integer pageSize
    ){
        Object object = null;
   if(page <= 0 || pageSize <= 0) {
            object = Common.getNullRetObj(new ReturnObject<>(ResponseCode.FIELD_NOTVALID), httpServletResponse);
        } else {
            Byte type=0;
            ReturnObject<PageInfo<VoObject>> returnObject = timeService.getAllTimeSegments(type,page, pageSize);

            object = Common.getPageRetObject(returnObject);
        }




        return object;
    }

    @Audit
    @GetMapping("shops/{did}/flashsale/timesegments")
    public Object getAllSegments1(
            @RequestParam(required = false, defaultValue = "1")  Integer page,
            @RequestParam(required = false, defaultValue = "10")  Integer pageSize
    ){
        Object object = null;

        if(page <= 0 || pageSize <= 0) {
            object = Common.getNullRetObj(new ReturnObject<>(ResponseCode.FIELD_NOTVALID), httpServletResponse);
        } else {
            Byte type=1;
            ReturnObject<PageInfo<VoObject>> returnObject = timeService.getAllTimeSegments( type,page, pageSize);

            object = Common.getPageRetObject(returnObject);
        }

        return object;
    }

}
