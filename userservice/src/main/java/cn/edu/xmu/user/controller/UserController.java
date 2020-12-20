package cn.edu.xmu.user.controller;

import cn.edu.xmu.ooad.annotation.Audit;
import cn.edu.xmu.ooad.annotation.LoginUser;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ResponseUtil;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.user.dao.UserDao;
import cn.edu.xmu.user.model.bo.Customer;
import cn.edu.xmu.user.model.vo.*;
import cn.edu.xmu.user.service.UserService;
import cn.edu.xmu.user.util.IpUtil;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Api(value = "用户服务", tags = "user")
@RestController /*Restful的Controller对象*/
@RequestMapping(value = "/", produces = "application/json;charset=UTF-8")
public class UserController {

    @Autowired
    private HttpServletResponse httpServletResponse;

    @Autowired
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(UserDao.class);
    /**
     * 注册用户
     * @param vo:vo对象
     * @param result 检查结果
     * @return  Object
     */
    @ApiOperation(value="注册用户")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "NewUserVo", name = "vo", value = "newUserInfo", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 732, message = "邮箱已被注册"),
            @ApiResponse(code = 733, message = "电话已被注册"),
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = 404, message = "参数不合法")
    })
    @PostMapping("users")
    public Object register(@Validated @RequestBody NewUserVo vo, BindingResult result,HttpServletResponse httpServletResponse){
        if(result.hasErrors()){
            return Common.processFieldErrors(result,httpServletResponse);
        }
        ReturnObject returnObject= userService.register(vo);
        if(returnObject.getCode()== ResponseCode.OK){
            httpServletResponse.setStatus(HttpServletResponse.SC_CREATED);
            return ResponseUtil.ok(returnObject.getData());
        }
        else return ResponseUtil.fail(returnObject.getCode());
    }

    /**
     * 用户登录
     * @param loginVo
     * @param bindingResult
     * @param httpServletResponse
     * @param httpServletRequest
     * @return
     */
    @ApiOperation(value = "登录")
    @PostMapping("users/login")
    public Object login(@Validated @RequestBody LoginVo loginVo, BindingResult bindingResult
            , HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest){
        /* 处理参数校验错误 */
        Object o = Common.processFieldErrors(bindingResult, httpServletResponse);
        if(o != null){
            return o;
        }

        String ip = IpUtil.getIpAddr(httpServletRequest);
        ReturnObject<String> jwt = userService.login(loginVo.getUserName(), loginVo.getPassword(), ip);

        if(jwt.getData() == null){
            return ResponseUtil.fail(jwt.getCode(), jwt.getErrmsg());
        }else{
            httpServletResponse.setStatus(HttpServletResponse.SC_CREATED);
            return ResponseUtil.ok(jwt.getData());
        }
    }

    /**
     * 用户注销
     * @param userId
     * @return
     * @author 24320182203266
     */
    @ApiOperation(value = "登出")
    @Audit
    @GetMapping("users/logout")
    public Object logout(@LoginUser Long userId){

        ReturnObject<Boolean> success = userService.Logout(userId);
        if (success.getData() == null)  {
            return ResponseUtil.fail(success.getCode(), success.getErrmsg());
        }else {
            return ResponseUtil.ok();
        }
    }

    @ApiOperation(value="获得买家的所有状态")
    @ApiResponses({
            @ApiResponse(code = 0,message = "成功")
    })
    @Audit
    @GetMapping("users/states")
    public Object getAllStates(){
        Customer.State[] states=Customer.State.class.getEnumConstants();
        List<StateVo> stateVos=new ArrayList<StateVo>();
        for(int i=0;i<states.length;i++){
            stateVos.add(new StateVo(states[i]));
        }
        return ResponseUtil.ok(new ReturnObject<List>(stateVos).getData());
    }



    @ApiOperation(value = "平台管理员获取所有用户列表")
    @Audit
    @GetMapping("users/all")
    public Object getallusers(@RequestParam(value="userName", required=false, defaultValue="") String userName ,
                              @RequestParam(value="email", required=false, defaultValue="") String email ,
                              @RequestParam(value="mobile", required=false, defaultValue="") String mobile ,
                              @RequestParam(value="page", required=false, defaultValue="1") Integer page ,
                              @RequestParam(value="pageSize", required=false, defaultValue="20") Integer pageSize){

        ReturnObject<PageInfo<VoObject>> ret = userService.getallusers(userName, email, mobile, page, pageSize);
        return Common.getPageRetObject(ret);
    }

    /**
     * 平台管理员封禁买家
     * @param id
     * @return
     * @author 24320182203181 陈渝璇
     */
    @ApiOperation(value = "封禁买家")
    @Audit
    @PutMapping("users/{id}/ban")
    public Object banCustomer(@PathVariable Long id){
        ReturnObject<Object> ret = userService.banCustomer(id);
        return Common.decorateReturnObject(ret);
    }

    /**
     * 平台管理员解禁买家
     * @param id
     * @return
     * @author 24320182203181 陈渝璇
     */
    @ApiOperation(value = "解禁买家")
    @Audit
    @PutMapping("users/{id}/release")
    public Object releaseCustomer(@PathVariable Long id){
        ReturnObject<Object> ret = userService.releaseCustomer(id);
        return Common.decorateReturnObject(ret);
    }

    /**
     * 修改买家信息
     *
     * @author 24320182203284 单
     * @param id 买家id
     * @param vo 买家视图
     * @return Object 买家返回视图
     */
    @ApiOperation(value = "修改买家信息", produces = "application/json")
    @Audit
    @PutMapping("/users")
    public Object updateCustomer(@LoginUser Long id, @Validated @RequestBody CustomerSetVo vo) {
        ReturnObject<Object> success = userService.modifyCustomer(id,vo);
        //校验前端数据

        if (success.getData() == null)  {
            return ResponseUtil.fail(success.getCode(), success.getErrmsg());
        }else {
            return ResponseUtil.ok();
        }
    }
    /**
     * 查看买家信息
     *
     * @author 24320182203284 单
     * @param id 买家id
     * @return Object 买家返回视图
     */
    @ApiOperation(value = "查看买家信息", produces = "application/json")
    @Audit
    @GetMapping("/users")
    public Object getCustomer(@LoginUser Long id) {
        ReturnObject<Object> success = userService.getCustomer(id);
        //校验前端数据
//        if(success.getCode()==ResponseCode.INTERNAL_SERVER_ERR)
//            return new ResponseEntity(
//                    ResponseUtil.fail(success.getCode(), success.getErrmsg()),
//                    HttpStatus.is5xxServerError);
        return Common.decorateReturnObject(success);

    }

    /**
     * 查看任意用户信息
     */
    @ApiOperation(value="查看任意用户信息",produces="application/json")
    @Audit
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header",dataType = "String",name = "authorization",value = "用户token",required = true),
            @ApiImplicitParam(paramType = "path",dataType = "Integer",name = "id",value = "用户id",required = true)
    })
    @ApiResponses({
    })

    @GetMapping("users/{id}")
    public Object getUserById(@PathVariable("id") Long id) {
        ReturnObject<VoObject> user = userService.findUserById(id);   //返回对象 创建vo对象 id获取用户信息

        ResponseCode code = user.getCode();

        logger.debug("findUserById: user = " + user.getData() + " code = " + user.getCode());

        switch (code){
            case RESOURCE_ID_NOTEXIST:
                httpServletResponse.setStatus(HttpStatus.NOT_FOUND.value());
                return ResponseUtil.fail(user.getCode(), user.getErrmsg());
            case OK:
                CustomerRetVo customerRetVo = (CustomerRetVo)user.getData().createSimpleVo();
                return ResponseUtil.ok(customerRetVo);
            default:
                return ResponseUtil.fail(code);
        }
    }

}
