package cn.edu.xmu.address.controller;

import cn.edu.xmu.address.model.vo.NewRegionVo;
import cn.edu.xmu.address.service.RegionService;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ResponseUtil;
import cn.edu.xmu.ooad.util.ReturnObject;
import com.fasterxml.jackson.databind.util.ObjectBuffer;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.servlet.http.HttpServletResponse;


@Api(value = "addresses")
@RestController
@RequestMapping(value="/",produces = "application/json;charset=UTF-8")
public class RegionController {

    @Autowired
    private RegionService regionService;

    @Autowired
    private HttpServletResponse httpServletResponse;

    @PostMapping("shops/{did}/regions/{id}/subregions")
    public Object addRegion( @PathVariable ("id") Long id,@Validated @RequestBody NewRegionVo newRegionVo, BindingResult result){
        Object obj = Common.processFieldErrors(result, httpServletResponse);
        if(obj != null){
            return obj;
        }

        ReturnObject returnObject=regionService.addRegion(newRegionVo,id);

        if(returnObject.getCode()== ResponseCode.OK){
            return new ResponseEntity(ResponseUtil.ok(), HttpStatus.CREATED);
        }
        if(returnObject.getCode()==ResponseCode.REGION_OBSOLETE){
            return new ResponseEntity(ResponseUtil.fail(ResponseCode.REGION_OBSOLETE), HttpStatus.BAD_REQUEST);
        }

        return Common.decorateReturnObject(returnObject);
    }

    @PutMapping("shops/{did}/regions/{id}")
    public Object updateRegion(@Validated @RequestBody NewRegionVo newRegionVo,@PathVariable ("id") Long id, BindingResult result){

        if(result.hasErrors()){
            return Common.processFieldErrors(result,httpServletResponse);
        }

        ReturnObject returnObject=regionService.updateRegion(newRegionVo,id);
        if(returnObject.getCode()==ResponseCode.REGION_OBSOLETE){
            return new ResponseEntity(ResponseUtil.fail(ResponseCode.REGION_OBSOLETE), HttpStatus.BAD_REQUEST);
        }
        return Common.decorateReturnObject(returnObject);

    }


    @DeleteMapping("shops/{did}/regions/{id}")
    public Object disableRegion(@PathVariable ("id") Long id){

        ReturnObject returnObject=regionService.disableRegion(id);
        if(returnObject.getCode()== ResponseCode.REGION_OBSOLETE){
            return new ResponseEntity(ResponseUtil.fail(ResponseCode.REGION_OBSOLETE),HttpStatus.BAD_REQUEST);
        }
        return Common.decorateReturnObject(returnObject);

    }

    @GetMapping("region/{id}/ancestor")
    public Object getAncestorRegion(@PathVariable("id") Long id){

        ReturnObject returnObject=regionService.getAncestorRegion(id);
        if(returnObject.getCode()==ResponseCode.REGION_OBSOLETE){
            return new ResponseEntity(ResponseUtil.fail(ResponseCode.REGION_OBSOLETE), HttpStatus.BAD_REQUEST);
        }
        return Common.decorateReturnObject(returnObject);
    }


}
