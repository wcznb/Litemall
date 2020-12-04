package cn.edu.xmu.user.controller;

import cn.edu.xmu.ooad.util.JacksonUtil;
import cn.edu.xmu.ooad.util.JwtHelper;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.encript.AES;
import cn.edu.xmu.user.UserserviceApplication;
import cn.edu.xmu.user.mapper.CustomerPoMapper;
import cn.edu.xmu.user.model.bo.Customer;
import cn.edu.xmu.user.model.po.CustomerPo;
import cn.edu.xmu.user.model.po.CustomerPoExample;
import cn.edu.xmu.user.model.vo.LoginVo;
import cn.edu.xmu.user.model.vo.NewUserVo;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = UserserviceApplication.class)   //标识本类是一个SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ControllerTest1 {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private CustomerPoMapper customerPoMapper;

    private JwtHelper jwtHelper = new JwtHelper();

    private static final Logger logger = LoggerFactory.getLogger(ControllerTest1.class);

    @Test
    public void register() throws Exception {
        String responseString = null;
        ResultActions res = null;

        NewUserVo vo = new NewUserVo();
        vo.setMobile("11111111522");
        vo.setEmail("112141@qq.com");
        vo.setUserName("wcwcwc53");
        vo.setPassword("Ww123456789**");
        vo.setRealName("wcwc");
        Byte a=1;
        vo.setGender(a);
        vo.setBirthday("2017-09-28");



        String requireJson = JacksonUtil.toJson(vo);
        res = this.mvc.perform(post("/users")
                .contentType("application/json;charset=UTF-8")
                .content(requireJson));
        responseString = res.andExpect(status().is(200))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        //endregion

        System.out.println("register: "+responseString);
    }

    @Test
    public void login1() throws Exception {

        LoginVo vo = new LoginVo();
        vo.setUserName("65781027512");
        vo.setPassword("123456");

        String requireJson = JacksonUtil.toJson(vo);
        String response = this.mvc.perform(post("/users/login")
                .contentType("application/json;charset=UTF-8")
                .content(requireJson)).andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errno").value(ResponseCode.OK.getCode()))
                .andExpect(jsonPath("$.errmsg").value("成功"))
                .andReturn().getResponse().getContentAsString();
        //endregion

        System.out.println("login1: "+response);
    }
//
    @Test
    public void logout() throws Exception {
        ResultActions res = null;

        String authorization = this.login("65781027512","123456");

        res = this.mvc.perform(get("/users/logout").header("authorization",authorization)
                .contentType("application/json;charset=UTF-8"));

        String responseString = res.andExpect(status().is(200))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();

        System.out.println("logout: "+responseString);
    }

    @Test
    public void getAllUser() throws Exception{
        String authorization = this.login("65781027512","123456");

        ResultActions res = this.mvc.perform(get("/users/all?page=1&pageSize=20").header("authorization",authorization)
                .contentType("application/json;charset=UTF-8"));

        String responseString = res.andExpect(status().is(200))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();

        System.out.println("getAllUser() "+responseString);

    }

    private String login(String userName, String password) throws Exception{
        LoginVo vo = new LoginVo();
        vo.setUserName(userName);
        vo.setPassword(password);

        String requireJson = JacksonUtil.toJson(vo);
        String response = this.mvc.perform(post("/users/login")
                .contentType("application/json;charset=UTF-8")
                .content(requireJson)).andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errno").value(ResponseCode.OK.getCode()))
                .andExpect(jsonPath("$.errmsg").value("成功"))
                .andReturn().getResponse().getContentAsString();
        return  JacksonUtil.parseString(response, "data");
    }


    @Test
    public void getAllState() throws Exception {
        String token=login("65781027512","123456");
        String responseString=this.mvc.perform(get("/users/states").header("authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse="{ \"errno\": 0, \"data\": [ { \"name\": \"后台用户\", \"code\": 0 }, { \"name\": \"正常用户\", \"code\": 4 }, { \"name\": \"被封禁用户\", \"code\": 6 }], \"errmsg\": \"成功\" }";
        JSONAssert.assertEquals(expectedResponse,responseString,true);
    }

    /**
     * 管理员封禁买家 正常
     * @throws Exception
     */
    @Test
    public void banCustomer1() throws Exception{
        String authorization = jwtHelper.createToken(1L,1L,1);

        ResultActions res = this.mvc.perform(put("/users/1/ban")
                .header("authorization",authorization)
                .contentType("application/json;charset=UTF-8"));

        String responseString = res.andExpect(status().is(200))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();

        //检查是否禁止了买家
        CustomerPo customerPo = customerPoMapper.selectByPrimaryKey(1L);
        Assert.state(customerPo.getState() == Customer.State.FORBID.getCode().byteValue(), "用户并没有被解禁");

    }

    /**
     * 平台管理员封禁买家 用户不存在
     * @throws Exception
     */
    @Test
    public void banCustomer2() throws Exception{
        String authorization = jwtHelper.createToken(1L,1L,1);

        ResultActions res = this.mvc.perform(put("/users/0/ban")
                .header("authorization",authorization)
                .contentType("application/json;charset=UTF-8"));

        String responseString = res.andExpect(status().isNotFound())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errno").value(ResponseCode.RESOURCE_ID_NOTEXIST.getCode()))
                .andExpect(jsonPath("$.errmsg").value(ResponseCode.RESOURCE_ID_NOTEXIST.getMessage()))
                .andReturn().getResponse().getContentAsString();

    }

    /**
     * 解禁买家 正常
     * @throws Exception
     */
    @Test
    public void releaseCustomer1() throws Exception{
        String authorization = jwtHelper.createToken(1L,1L,1);

        ResultActions res = this.mvc.perform(put("/users/1/release")
                .header("authorization",authorization)
                .contentType("application/json;charset=UTF-8"));

        String responseString = res.andExpect(status().is(200))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errno").value(ResponseCode.OK.getCode()))
                .andExpect(jsonPath("$.errmsg").value("成功"))
                .andReturn().getResponse().getContentAsString();

        //检查是否禁解禁了买家
        CustomerPo customerPo = customerPoMapper.selectByPrimaryKey(1L);
        Assert.state(customerPo.getState() == Customer.State.NORM.getCode().byteValue(), "用户并没有被解禁");
    }

    /**
     * 解禁买家 用户不存在
     * @throws Exception
     */
    @Test
    public void releaseCustomer2() throws Exception{
        String authorization = jwtHelper.createToken(1L,1L,1);

        ResultActions res = this.mvc.perform(put("/users/0/release")
                .header("authorization",authorization)
                .contentType("application/json;charset=UTF-8"));

        String responseString = res.andExpect(status().isNotFound())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errno").value(ResponseCode.RESOURCE_ID_NOTEXIST.getCode()))
                .andExpect(jsonPath("$.errmsg").value(ResponseCode.RESOURCE_ID_NOTEXIST.getMessage()))
                .andReturn().getResponse().getContentAsString();

//        System.out.println("ReleaseCustomer2: "+responseString);
    }

    //notok
    @Test
    public void getUserById1() throws Exception{

        String token = this.login("65781027512","123456");
        String res = this.mvc.perform(get("/users/0").header("authorization",token))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();

        System.out.println(res);
    }

    //ok
    @Test
    public void getUserById2() throws  Exception{
        String token = this.login("65781027512","123456");
        String res = this.mvc.perform(get("/users/2").header("authorization",token))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();

        System.out.println(res);
    }
}
