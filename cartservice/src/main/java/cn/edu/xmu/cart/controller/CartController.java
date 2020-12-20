package cn.edu.xmu.cart.controller;

import cn.edu.xmu.cart.model.vo.CartGetVo;
import cn.edu.xmu.cart.service.CartService;
import cn.edu.xmu.cart.util.Common;
import cn.edu.xmu.ooad.annotation.Audit;
import cn.edu.xmu.ooad.annotation.LoginUser;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * @author 陈渝璇
 * createdBy 陈渝璇 2020-11-29
 * modifiedBy 陈渝璇 2020-11-29
 */
@Api(value = "购物车服务", tags = "cart")
@RestController /*Restful的Controller对象*/
@RequestMapping(value = "/carts", produces = "application/json;charset=UTF-8")
public class CartController {
    @Autowired
    CartService cartService;

    /**
     * 买家获得购物车列表
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "买家获得购物车列表")
    @Audit
    @GetMapping("")
    public Object findPageOfCarts(@LoginUser Long userId,
                              @RequestParam(required=false, defaultValue = "1") Integer page,
                              @RequestParam(required=false, defaultValue = "10") Integer pageSize){

        ReturnObject<PageInfo<VoObject>> ret = cartService.findPageOfCarts(userId, page, pageSize);
        return Common.getPageRetObject(ret);
    }

    /**
     * 买家将商品加入购物车
     * @param userId
     * @param vo
     * @return
     */
    @ApiOperation(value = "买家将商品加入购物车")
    @Audit
    @PostMapping("")
    public Object insertCart(@LoginUser Long userId,@Validated @RequestBody CartGetVo vo,
                             BindingResult bindingResult, HttpServletResponse httpServletResponse){
        /* 处理参数校验错误 */
        Object o = Common.processFieldErrors(bindingResult, httpServletResponse);
        if(o != null){
            return o;
        }
        ReturnObject<VoObject> ret = cartService.insertCart(userId, vo);
        if(ret.getCode() == ResponseCode.OK)
            httpServletResponse.setStatus(HttpStatus.CREATED.value());
        return Common.getRetObject(ret);
    }

    /**
     * 买家清空购物车
     * @param userId
     * @return Object
     */
    @ApiOperation(value = "买家清空购物车")
    @Audit
    @DeleteMapping("")
    public Object deleteAllCarts(@LoginUser Long userId){
        ReturnObject<Object> retObj = cartService.deleteAllCarts(userId);
        return Common.decorateReturnObject(retObj);
    }

    /**
     * 买家修改购物车单个商品的数量或规格
     * @param id
     * @param userId
     * @param vo:CartSetVo
     * @return Object
     */
    @ApiOperation(value = "买家修改购物车单个商品的数量或规格")
    @Audit
    @PutMapping("/{id}")
    public Object modifyCart(@LoginUser Long userId, @PathVariable Long id,@Validated @RequestBody CartGetVo vo,
                             BindingResult bindingResult, HttpServletResponse httpServletResponse){
        /* 处理参数校验错误 */
        Object o = Common.processFieldErrors(bindingResult, httpServletResponse);
        if(o != null){
            return o;
        }
        ReturnObject<Object> retObj = cartService.modifyCart(userId, id, vo);
        return Common.decorateReturnObject(retObj);
    }

    /**
     * 买家删除购物车中商品
     * @param id
     * @param userId
     * @return Object
     */
    @ApiOperation(value = "买家删除购物车中商品")
    @Audit
    @DeleteMapping("/{id}")
    public Object deleteCart(@LoginUser Long userId, @PathVariable Long id, HttpServletResponse httpServletResponse){
        ReturnObject<Object> retObj = cartService.deleteCart(userId, id);
        return Common.decorateReturnObject(retObj);
    }
}
