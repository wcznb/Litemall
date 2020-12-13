package cn.edu.xmu.favorite.controller;

import cn.edu.xmu.favorite.FavoriteserviceApplication;
import cn.edu.xmu.favorite.mapper.FavoritePoMapper;
import cn.edu.xmu.favorite.model.po.FavoritePo;
import cn.edu.xmu.ooad.util.JacksonUtil;
import cn.edu.xmu.ooad.util.JwtHelper;
import cn.edu.xmu.ooad.util.ResponseCode;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.nio.file.Files;
import java.util.Objects;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(classes = FavoriteserviceApplication.class)   //标识本类是一个SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class FavoriteControllerTest {
    private WebTestClient webClient;

    private JwtHelper jwtHelper = new JwtHelper();

    public FavoriteControllerTest(){
        this.webClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:8081")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8")
                .build();
    }


    /**
     * 买家查看所有收藏的商品 成功
     * @throws Exception
     */
    @Test
    public void findPageOfFavorites() throws Exception {
        String authorization = jwtHelper.createToken(1L,1L,1);

        String response = new String(Objects.requireNonNull(webClient.get().uri("/favorites").header("authorization",authorization).exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo("成功")
                .returnResult()
                .getResponseBodyContent()));

        System.out.println(response);

//        String expectedResponse = "{\"errno\":0,\"data\":{\"total\":3,\"pages\":1,\"pageSize\":3,\"page\":1,\"list\":[{\"id\":3243934,\"goodsSkuId\":502,\"gmtCreate\":\"2020-12-03T21:45:44\"},{\"id\":3276701,\"goodsSkuId\":512,\"gmtCreate\":\"2020-12-03T21:45:45\"},{\"id\":3309468,\"goodsSkuId\":348,\"gmtCreate\":\"2020-12-03T21:45:45\"}]},\"errmsg\":\"成功\"}";
//        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    /**
     * 买家收藏商品 成功
     * @throws Exception
     */
    @Test
    public void addFavorite() throws Exception {
        String authorization = jwtHelper.createToken(1L,1L,1);

        String response = new String(Objects.requireNonNull(webClient.post().uri("/favorites")
                .header("authorization",authorization).exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo("成功")
                .returnResult()
                .getResponseBodyContent()));

        System.out.println(response);
    }

    /**
     * 买家收藏商品 成功
     * @throws Exception
     */
    @Test
    public void deleteFavorite1() throws Exception {
        String authorization = jwtHelper.createToken(1L,1L,1);

        Long skuId = 3735440L;
        String response = new String(Objects.requireNonNull(webClient.delete().uri("/favorites/goods/{skuId}",skuId)
                .header("authorization",authorization).exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo("成功")
                .returnResult()
                .getResponseBodyContent()));

        System.out.println(response);
    }

    /**
     * 买家收藏商品 操作资源不存在
     * @throws Exception
     */
    @Test
    public void deleteFavorite2() throws Exception {
        String authorization = jwtHelper.createToken(1L,1L,1);

        Long id = 0L;
        String response = new String(Objects.requireNonNull(webClient.delete().uri("/favorites/{id}",id)
                .header("authorization",authorization).exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.RESOURCE_ID_NOTEXIST.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.RESOURCE_ID_NOTEXIST.getMessage())
                .returnResult()
                .getResponseBodyContent()));

        System.out.println(response);
    }
    /**
     * 买家收藏商品 操作资源id不是自己的对象
     * @throws Exception
     */
    @Test
    public void deleteFavorite3() throws Exception {
        String authorization = jwtHelper.createToken(2L,1L,1);

        Long id = 3735440L;
        String response = new String(Objects.requireNonNull(webClient.delete().uri("/favorites/{id}",id)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.RESOURCE_ID_OUTSCOPE.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.RESOURCE_ID_OUTSCOPE.getMessage())
                .returnResult()
                .getResponseBodyContent()));

        System.out.println(response);
    }
}
