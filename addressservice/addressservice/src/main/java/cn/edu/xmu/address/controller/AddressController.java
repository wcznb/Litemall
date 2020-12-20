package cn.edu.xmu.address.controller;

import cn.edu.xmu.address.model.vo.NewAddressVo;
import cn.edu.xmu.address.service.AddressService;
import cn.edu.xmu.ooad.annotation.Audit;
import cn.edu.xmu.ooad.annotation.LoginUser;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ResponseUtil;
import cn.edu.xmu.ooad.util.ReturnObject;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * @author shyanne 3184
 */
@Api(value = "地址服务", tags = "address")
@RestController
@RequestMapping(value = "/", produces = "application/json;charset=UTF-8")
public class AddressController {

    HttpServletResponse httpServletResponse;

    @Autowired
    AddressService addressService;

    /**
     * 添加地址
     * @return 添加地址操作结果
     * //
     */
    @Audit
    @PostMapping("addresses")
    public Object addAddress(@LoginUser Long userId ,@Validated @RequestBody NewAddressVo vo,BindingResult result,HttpServletResponse httpServletResponse){
        Object obj = Common.processFieldErrors(result, httpServletResponse);
        if(obj != null){
            return obj;
        }

        ReturnObject returnObject=addressService.addAddress(vo,userId);

        if(returnObject.getCode()== ResponseCode.OK){
            return new ResponseEntity(ResponseUtil.ok(returnObject.getData()), HttpStatus.CREATED);
        }
        if(returnObject.getCode()==ResponseCode.REGION_OBSOLETE||returnObject.getCode()==ResponseCode.RESOURCE_ID_NOTEXIST){
            return new ResponseEntity(ResponseUtil.fail(ResponseCode.FIELD_NOTVALID), HttpStatus.BAD_REQUEST);
        }
        return Common.decorateReturnObject(returnObject);

    }

    /**
     * 用户获得所有地址
     *
     * @return 地址列表
     */
    @Audit
    @GetMapping("addresses")
    public Object getAllAddressById(@LoginUser Long userId ,
            @RequestParam(required = true , defaultValue = "1")  Integer page,
            @RequestParam(required = true, defaultValue = "10")  Integer pageSize
    ){

        Object object = null;

        if(page <= 0 || pageSize <= 0) {
            object = Common.getNullRetObj(new ReturnObject<>(ResponseCode.FIELD_NOTVALID), httpServletResponse);
        } else {

            ReturnObject<PageInfo<VoObject>> returnObject = addressService.getAllAddressById(userId,page, pageSize);

            object = Common.getPageRetObject(returnObject);
        }

        return object;
    }

    /**
     * 修改地址
     * @param newAddressVo 地址信息
     * @return 修改地址操作结果
     */
    @Audit
    @PutMapping("addresses/{id}")
    public Object updateAddress(@PathVariable Long id,@LoginUser Long userId,@Validated @RequestBody NewAddressVo newAddressVo,BindingResult result,HttpServletResponse httpServletResponse){
        Object obj = Common.processFieldErrors(result, httpServletResponse);
        if(obj != null){
            return obj;
        }
        ReturnObject success = addressService.updateAddress(userId,id,newAddressVo);
        if(success.getCode()==ResponseCode.RESOURCE_ID_OUTSCOPE){
            return new ResponseEntity(ResponseUtil.fail(ResponseCode.RESOURCE_ID_OUTSCOPE), HttpStatus.FORBIDDEN);

        }
        if(success.getCode()==ResponseCode.REGION_OBSOLETE){
            return new ResponseEntity(ResponseUtil.fail(ResponseCode.FIELD_NOTVALID), HttpStatus.BAD_REQUEST);
        }


        return Common.decorateReturnObject(success);

    }

    /**
     * 设为默认地址
     * @param id
     * @return
     */
    @Audit
    @PutMapping("addresses/{id}/default")
    public Object setAddressAsDefault(@LoginUser Long userId ,@PathVariable Long id){
        ReturnObject<Boolean> success = addressService.setAddressAsDefault(id,userId);
        if(success.getCode()==ResponseCode.RESOURCE_ID_OUTSCOPE){
            return new ResponseEntity(ResponseUtil.fail(ResponseCode.RESOURCE_ID_OUTSCOPE), HttpStatus.FORBIDDEN);
        }
        if(success.getCode()==ResponseCode.REGION_OBSOLETE){
            return new ResponseEntity(ResponseUtil.ok(), HttpStatus.BAD_REQUEST);
        }

        return Common.decorateReturnObject(success);
    }

    /**
     * 删除选定地址
     * @param id
     */
    @Audit
    @DeleteMapping("/addresses/{id}")
    public Object deleteAddress(@PathVariable Long id){
        ReturnObject returnObject = addressService.deleteAddress(id);
        return Common.decorateReturnObject(returnObject);
    }

}
