package cn.edu.xmu.cart.controller;

import cn.edu.xmu.cart.CartserviceApplication;
import cn.edu.xmu.cart.LoginVo;
import cn.edu.xmu.cart.mapper.CartPoMapper;
import cn.edu.xmu.cart.model.po.CartPo;
import cn.edu.xmu.ooad.util.JacksonUtil;
import cn.edu.xmu.ooad.util.JwtHelper;
import cn.edu.xmu.ooad.util.ResponseCode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Objects;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(classes = CartserviceApplication.class)
@AutoConfigureMockMvc
@Transactional
public class CartControllerTest {
    private WebTestClient loginClient;

    private WebTestClient webClient;

    @Autowired
    private CartPoMapper cartPoMapper;

    private JwtHelper jwtHelper = new JwtHelper();

    public CartControllerTest(){
        this.loginClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:8081")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8")
                .build();

        this.webClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:8080")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8")
                .build();
    }

    @Test
    public void deleteAllCarts1() throws Exception {
        String token = this.login("8606245097", "123456");
        String response = new String(Objects.requireNonNull(webClient.delete().uri("/carts")
                .header("authorization",token).exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo("成功")
                .returnResult()
                .getResponseBodyContent()));

        System.out.println(response);
    }

    /**
     * 买家修改购物车 成功
     * @throws Exception
     */
    @Test
    public void modifyCart1() throws Exception {
        String token = this.login("8606245097", "123456");

        String requireJson = "{\"goodSkuID\":1,\"quantity\":3}";

        Long id = 1L;
        String response = new String(Objects.requireNonNull(webClient.post().uri("/carts/{id}",id)
                .bodyValue(requireJson)
                .header("authorization",token).exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo("成功")
                .returnResult()
                .getResponseBodyContent()));

        System.out.println(response);

        CartPo po = cartPoMapper.selectByPrimaryKey(id);
        Assert.state(po.getGoodsSkuId()==1&&po.getQuantity()==3,"修改数据库失败");
    }

    /**
     * 买家修改购物车 操作对象不是自己的
     * @throws Exception
     */
    @Test
    public void modifyCart2() throws Exception {
        String token = this.login("8606245097", "123456");

        Long id = 1L;
        String requireJson = "{\"goodSkuID\":1,\"quantity\":3}";

        String response = new String(Objects.requireNonNull(webClient.post().uri("/carts/{id}",id)
                .bodyValue(requireJson)
                .header("authorization",token).exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.RESOURCE_ID_OUTSCOPE.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.RESOURCE_ID_OUTSCOPE.getMessage())
                .returnResult()
                .getResponseBodyContent()));

        System.out.println(response);
    }

    /**
     * 买家删除购物车中商品 成功
     * @throws Exception
     */
    @Test
    public void deleteCart1() throws Exception {
        String token = this.login("8606245097", "123456");

        Long id = 1L;
        String response = new String(Objects.requireNonNull(webClient.delete().uri("/carts/{id}",id)
                .header("authorization",token).exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo("成功")
                .returnResult()
                .getResponseBodyContent()));

        System.out.println(response);
    }

    /**
     * 买家删除购物车中商品 操作对象不是自己的
     * @throws Exception
     */
    @Test
    public void deleteCart2() throws Exception {
        String token = this.login("8606245097", "123456");

        Long id = 1L;
        String response = new String(Objects.requireNonNull(webClient.delete().uri("/carts/{id}",id)
                .header("authorization",token).exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.RESOURCE_ID_OUTSCOPE.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.RESOURCE_ID_OUTSCOPE.getMessage())
                .returnResult()
                .getResponseBodyContent()));

        System.out.println(response);
    }

    /**
     * 买家删除购物车中商品 操作对象不存在
     * @throws Exception
     */
    @Test
    public void deleteCart3() throws Exception {
        String token = this.login("8606245097", "123456");

        Long id = 1L;
        String response = new String(Objects.requireNonNull(webClient.delete().uri("/carts/{id}",id)
                .header("authorization",token).exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.RESOURCE_ID_NOTEXIST.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.RESOURCE_ID_NOTEXIST.getMessage())
                .returnResult()
                .getResponseBodyContent()));

        System.out.println(response);
    }

    private String login(String userName, String password) throws Exception {
        LoginVo vo = new LoginVo();
        vo.setUserName(userName);
        vo.setPassword(password);
        String requireJson = JacksonUtil.toJson(vo);
        byte[] ret = loginClient.post().uri("/users/login").bodyValue(requireJson).exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo("成功")
                .returnResult()
                .getResponseBodyContent();
        return JacksonUtil.parseString(new String(ret, "UTF-8"), "data");
    }
}
