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
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

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
    public Object addAddress(@Validated @RequestBody NewAddressVo vo, BindingResult result){
        //using @LoginUser to get userId need fix
        Long userId= Long.valueOf(01);

        if(result.hasErrors()){
            return Common.processFieldErrors(result,httpServletResponse);
        }

        ReturnObject returnObject=addressService.addAddress(vo,userId);

        if(returnObject.getCode()== ResponseCode.OK){
            return ResponseUtil.ok(returnObject.getData());
        }
        else {
            return ResponseUtil.fail(returnObject.getCode());
        }

    }

    /**
     * 用户获得所有地址
     *
     * @return 地址列表
     */
    //need fix customer id
    @Audit
    @GetMapping("addresses")
    public Object getAllAddressById(
            @RequestParam(required = false, defaultValue = "1")  Integer page,
            @RequestParam(required = false, defaultValue = "10")  Integer pagesize
    ){

        Long id= Long.valueOf(1);
        Object object = null;

        if(page <= 0 || pagesize <= 0) {
            object = Common.getNullRetObj(new ReturnObject<>(ResponseCode.FIELD_NOTVALID), httpServletResponse);
        } else {

            ReturnObject<PageInfo<VoObject>> returnObject = addressService.getAllAddressById(id,page, pagesize);

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
    public Object updateAddress(@PathVariable Long id,@Validated @RequestBody NewAddressVo newAddressVo){
        ReturnObject success = addressService.updateAddress(id,newAddressVo);
        System.out.print(success);
        return Common.decorateReturnObject(success);

    }

    /**
     * 设为默认地址
     * @param id
     * @return
     */
    @Audit
    @PutMapping("addresses/{id}/default")
    public Object setAddressAsDefault(@PathVariable Long id){
        Long customerId=1L;
        ReturnObject<Boolean> success = addressService.setAddressAsDefault(id,customerId);
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
