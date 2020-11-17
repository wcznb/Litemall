package cn.edu.xmu.user.controller;

import cn.edu.xmu.ooad.annotation.Audit;
import cn.edu.xmu.ooad.annotation.LoginUser;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ResponseUtil;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.user.model.vo.LoginVo;
import cn.edu.xmu.user.model.vo.NewUserVo;
import cn.edu.xmu.user.service.UserService;
import cn.edu.xmu.user.util.IpUtil;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(value = "用户服务", tags = "user")
@RestController /*Restful的Controller对象*/
@RequestMapping(value = "/", produces = "application/json;charset=UTF-8")
public class UserController {

    @Autowired
    private HttpServletResponse httpServletResponse;

    @Autowired
    private UserService userService;
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
    public Object register(@Validated @RequestBody NewUserVo vo, BindingResult result){
        if(result.hasErrors()){
            return Common.processFieldErrors(result,httpServletResponse);
        }
        ReturnObject returnObject=userService.register(vo);
        if(returnObject.getCode()== ResponseCode.OK){
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

}
