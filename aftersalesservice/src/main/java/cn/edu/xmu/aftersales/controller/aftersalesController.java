package cn.edu.xmu.aftersales.controller;

import cn.edu.xmu.aftersales.model.bo.AftersalesBo;
import cn.edu.xmu.aftersales.model.vo.*;
import cn.edu.xmu.aftersales.service.AfterSaleService;
import cn.edu.xmu.ooad.annotation.Audit;
import cn.edu.xmu.ooad.annotation.LoginUser;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ResponseUtil;
import cn.edu.xmu.ooad.util.ReturnObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Api(value="售后服务",tags="aftersale")
@RestController
@RequestMapping(value="/",produces = "application/json;charset=UTF-8")
public class aftersalesController {

    @Autowired
    private AfterSaleService afterSaleService;
    //日志 private static final Logger logger= LoggerFactory.getLogger()

    /**
     *获得售后单的所有状态
     */
    @ApiOperation(value="获得售后单的所有状态")
    @ApiResponses({
            @ApiResponse(code=0,message = "成功")
    })
    @Audit
    @GetMapping("aftersales/states")
    public Object getSaleState(){
        AftersalesBo.State[] states=AftersalesBo.State.class.getEnumConstants();
        List<StateVo> stateVos=new ArrayList<>();
        for(int i=0;i<states.length;i++){
            stateVos.add(new StateVo(states[i]));
        }

        return ResponseUtil.ok(stateVos);
    }

    /**
     * 买家提交售后单
     */
    @ApiOperation(value ="买家提交售后单")
    @Audit
    @PostMapping("/orderItems/{id}/aftersales")
    public Object newSale(@LoginUser Long userId, @PathVariable("id") Long orderItemId, @Validated @RequestBody NewSaleVo vo, BindingResult bindingResult, HttpServletResponse httpServletResponse){
        Object obj = Common.processFieldErrors(bindingResult, httpServletResponse);
        if(obj != null){
            return obj;
        }

        ReturnObject<VoObject> retObject= afterSaleService.newSale(userId, orderItemId, vo);
        if (retObject.getData() != null) {
            httpServletResponse.setStatus(HttpStatus.CREATED.value());
            return Common.getRetObject(retObject);
        } else {
            return Common.getNullRetObj(new ReturnObject<>(retObject.getCode(), retObject.getErrmsg()), httpServletResponse);
        }
    }

    /**
     * 买家查询所有的售后单信息（可根据售后类型和状态选择）
     */
    @ApiOperation(value="买家查询所有的售后单信息（可根据售后类型和状态选择）")
    @Audit
    @GetMapping("/aftersales")
    public Object getSale(@LoginUser Long userId,@RequestParam(value="spuId",required = false,defaultValue = " ")Long spuId,
                          @RequestParam(value="skuId",required = false,defaultValue = " ")Long skuId,
                          @RequestParam(value = "beginTime",required = false,defaultValue = " ")String beginTime,
                          @RequestParam(value="endTime",required = false,defaultValue = " ")String endTime,
                          @RequestParam(value="page",required = false,defaultValue = " ")Integer page,
                          @RequestParam(value="pageSize",required = false,defaultValue = " ")Integer pageSize,
                          @RequestParam(value="type",required = false,defaultValue = " ")Byte type,
                          @RequestParam(value="state",required = false,defaultValue = " ")Byte state
                          ){

        ReturnObject<PageInfo<VoObject>> retObj=afterSaleService.getSale(userId,spuId,skuId,beginTime,endTime,page,pageSize,type,state);
        return Common.getPageRetObject(retObj);
    }


    /**
     * 管理员通过售后单Id查询所有售后单
     */
    @ApiOperation(value="管理员查看所有售后单（可根据售后类型和状态选择）")
    @Audit
    @GetMapping("/shops/{id}/aftersales")
    public Object getSaleByShop(@LoginUser Long userId,@PathVariable("id")Long shopId,
                                @RequestParam(value="spuId",required = false,defaultValue = " ")Long spuId,
                                @RequestParam(value="skuId",required = false,defaultValue = " ")Long skuId,
                                @RequestParam(value = "beginTime",required = false,defaultValue = " ")String beginTime,
                                @RequestParam(value="endTime",required = false,defaultValue = " ")String endTime,
                                @RequestParam(value="page",required = false,defaultValue = " ")Integer page,
                                @RequestParam(value="pageSize",required = false,defaultValue = " ")Integer pageSize,
                                @RequestParam(value="type",required = false,defaultValue = " ")Byte type,
                                @RequestParam(value="state",required = false,defaultValue = " ")Byte state){
        ReturnObject<PageInfo<VoObject>> retObj=afterSaleService.getSaleByShop(shopId,spuId,skuId,beginTime,endTime,page,pageSize,type,state);
        return Common.getPageRetObject(retObj);
    }

    /**
     * 买家根据售后单id查询售后单信息    集成订单模块
     */
    @ApiOperation(value ="买家根据售后单id查询售后单信息")
    @Audit
    @GetMapping("/aftersales/{id}")
    public Object searchSaleById(@LoginUser Long userId, @PathVariable("id") Long id){
        ReturnObject<VoObject> returnObject= afterSaleService.searchSaleById(userId,id);
        if (returnObject.getCode() == ResponseCode.OK) {
            return Common.getRetObject(returnObject);
        } else {
            return Common.decorateReturnObject(returnObject);
        }
    }

    /**
     * 买家修改售后单信息
     */
    @ApiOperation(value = "买家修改售后单信息")
    @Audit
    @PutMapping("/aftersales/{id}")
    public Object modifySale(@LoginUser Long userId,@PathVariable("id") Long id,@Validated @RequestBody UpdateVo newVo,BindingResult bindingResult, HttpServletResponse httpServletResponse){

        Object obj = Common.processFieldErrors(bindingResult, httpServletResponse);
        if(obj != null){
            return obj;
        }

        ReturnObject<VoObject> ret= afterSaleService.modifySale(userId,id,newVo);
        if (ret.getCode() == ResponseCode.OK) {
            return Common.getRetObject(ret);
        } else {
            return Common.decorateReturnObject(ret);
        }
    }

    /**
     * 买家取消售后单和逻辑删除售后单 售后单完成之前，买家取消售后单；售后单完成之后，买家逻辑删除售后单   卖家已经发货还可以取消售后吗
     */
    @ApiOperation(value = "买家取消售后单和逻辑删除售后单")
    @Audit
    @DeleteMapping("/aftersales/{id}")
    public Object deleteSale(@LoginUser Long userId,@PathVariable("id") Long id){
        ReturnObject<VoObject> ret= afterSaleService.deleteSale(userId, id);
        if (ret.getCode() == ResponseCode.OK) {
            return Common.getRetObject(ret);
        } else {
            return Common.decorateReturnObject(ret);
        }
    }


    /**
     * 买家填写售后的运单信息 （同时需要修改售后单状态）
     */
    @ApiOperation(value = "买家填写售后的运单信息")
    @Audit
    @PutMapping("/aftersales/{id}/sendback")
    public Object customerLogsn(@LoginUser Long userId, @PathVariable("id") Long id, @Validated @RequestBody LogSnVo vo, BindingResult bindingResult, HttpServletResponse httpServletResponse){

        Object obj = Common.processFieldErrors(bindingResult, httpServletResponse);
        if(obj != null){
            return obj;
        }

        String logSn=vo.getLogSn();
        ReturnObject<VoObject> ret= afterSaleService.customerLogSn(userId, id, logSn);
        if (ret.getCode() == ResponseCode.OK) {
            return Common.getRetObject(ret);
        } else {
            return Common.decorateReturnObject(ret);
        }
    }

    /**
     * 买家确认售后单结束 需要判断是否结束吗？？
     */
    @ApiOperation(value="买家确认售后单结束")
    @Audit
    @PutMapping("/aftersales/{id}/confirm")
    public Object confirm(@LoginUser Long userId,@PathVariable("id")Long id){
        ReturnObject<VoObject> ret=afterSaleService.confirmOver(userId, id);
        if (ret.getCode() == ResponseCode.OK) {
            return Common.getRetObject(ret);
        } else {
            return Common.decorateReturnObject(ret);
        }
    }

    /**
     * 买家根据售后单Id查询售后单信息
     */
    @ApiOperation(value="买家根据售后单Id查询售后单信息")
    @Audit
    @GetMapping("/shops/{shopId}/aftersales/{id}")
    public Object searchSale(@LoginUser Long userId,@PathVariable("shopId")Long shopId,@PathVariable("id")Long id){
        ReturnObject<VoObject> ret=afterSaleService.searchSale(userId, shopId, id);
        return Common.getRetObject(ret);
    }

    /**
     * 管理员同意/不同意（退款，换货，维修）
     */
    @ApiOperation(value="管理员同意/不同意（退款，换货，维修）")
    @Audit
    @PutMapping("/shops/{shopId}/aftersales/{id}/confirm")
    public Object agreeRequest(@LoginUser Long userId,@PathVariable("shopId")Long shopId,@PathVariable("id")Long id,@RequestBody checkVo vo,BindingResult bindingResult, HttpServletResponse httpServletResponse){

        Object obj = Common.processFieldErrors(bindingResult, httpServletResponse);
        if(obj != null){
            return obj;
        }

        ReturnObject<VoObject> retObj=afterSaleService.agreeRequest(shopId, id, vo);
        return Common.getRetObject(retObj);
    }

    /**
     * 店家确认收到买家的退（换）货
     * 如果是退款，则退款给用户，如果换货则产生一个新订单，如果是维修则等待下一个动作
     */
    @ApiOperation(value="店家确认收到买家的退（换）货")
    @Audit
    @PutMapping("/shops/{shopId}/aftersales/{id}/receive")
    public Object receive(@PathVariable("shopId")Long shopId,@PathVariable("id")Long id,@RequestBody confirmVo vo,BindingResult bindingResult, HttpServletResponse httpServletResponse){

        Object obj = Common.processFieldErrors(bindingResult, httpServletResponse);
        if(obj != null){
            return obj;
        }

        ReturnObject<VoObject> retObj=afterSaleService.receive(shopId, id, vo);
        return Common.getRetObject(retObj);
    }

    /**
     * 店家寄出维修好（调换）的货物
     */
    @ApiOperation(value="店家寄出维修好（调换）的货物")
    @Audit
    @PutMapping("/shops/{shopId}/aftersales/{id}/deliver")
    public Object deliver(@PathVariable("shopId")Long shopId,@PathVariable("id")Long id,@RequestBody LogSnVo vo,BindingResult bindingResult, HttpServletResponse httpServletResponse){

        Object obj = Common.processFieldErrors(bindingResult, httpServletResponse);
        if(obj != null){
            return obj;
        }

        String logSn=vo.getLogSn();
        ReturnObject<VoObject> retObj=afterSaleService.deliver(shopId,id,logSn);
        return Common.getRetObject(retObj);
    }
}
