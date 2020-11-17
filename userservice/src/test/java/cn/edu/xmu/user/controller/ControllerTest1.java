package cn.edu.xmu.user.controller;

import cn.edu.xmu.ooad.util.JacksonUtil;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.user.UserserviceApplication;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = UserserviceApplication.class)   //标识本类是一个SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ControllerTest1 {
    @Autowired
    private MockMvc mvc;

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
        vo.setBirthday("2017-09-28 08:34:12");



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
        vo.setUserName("wcwcwc9");
        vo.setPassword("Ww123456789**");

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

        String authorization = this.login("wcwcwc","Ww123456789**");

        res = this.mvc.perform(get("/users/logout").header("authorization",authorization)
                .contentType("application/json;charset=UTF-8"));

        String responseString = res.andExpect(status().is(200))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();

        System.out.println("logout: "+responseString);
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
}
