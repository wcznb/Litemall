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

    @PostMapping("regions/{id}/subregions")
    public Object addRegion(@Validated @RequestBody NewRegionVo newRegionVo, @PathVariable ("id") Long id, BindingResult result){

        if(result.hasErrors()){
            return Common.processFieldErrors(result,httpServletResponse);
        }

        ReturnObject returnObject=regionService.addRegion(newRegionVo,id);

        return Common.decorateReturnObject(returnObject);
    }

    @GetMapping("test")
    public Object test(){
        return ResponseUtil.ok();
    }
    @PutMapping("regions/{id}")
    public Object updateRegion(@Validated @RequestBody NewRegionVo newRegionVo,@PathVariable ("id") Long id, BindingResult result){

        if(result.hasErrors()){
            return Common.processFieldErrors(result,httpServletResponse);
        }

        ReturnObject returnObject=regionService.updateRegion(newRegionVo,id);

        return Common.decorateReturnObject(returnObject);

    }


    @DeleteMapping("regions/{id}")
    public Object disableRegion(@PathVariable ("id") Long id){

        ReturnObject returnObject=regionService.disableRegion(id);

        return Common.decorateReturnObject(returnObject);

    }

    @GetMapping("regions/{id}/ancestor")
    public Object getAncestorRegion(@PathVariable("id") Long id){

        ReturnObject returnObject=regionService.getAncestorRegion(id);

        return Common.decorateReturnObject(returnObject);
    }


}
