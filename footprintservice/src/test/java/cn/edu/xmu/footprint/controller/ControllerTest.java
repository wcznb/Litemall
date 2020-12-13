package cn.edu.xmu.footprint.controller;

import cn.edu.xmu.footprint.FootprintserviceApplication;
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

import java.util.Objects;

@SpringBootTest(classes = FootprintserviceApplication.class)   //标识本类是一个SpringBootTest
@Transactional
public class ControllerTest {
    private WebTestClient webClient;

    private JwtHelper jwtHelper = new JwtHelper();

    public ControllerTest(){
        this.webClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:8080")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8")
                .build();
    }
    /**
     * 平台管理员获取用户足迹
     * userId:not null
     * beginTime:null
     * endTime:null
     * @throws Exception
     */
    @Test
    public void getCustomerFootPrints1() throws Exception {
        String authorization = jwtHelper.createToken(1L,1L,1);
        String response = new String(Objects.requireNonNull(webClient.get().uri("/footprints?userId=1").header("authorization",authorization).exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo("成功")
                .returnResult()
                .getResponseBodyContent()));

        System.out.println(response);
//        String expectedResponse = "{\"errno\":0,\"data\":{\"total\":1,\"pages\":1,\"pageSize\":10,\"page\":1,\"list\":[{\"id\":1048545,\"goodsSkuId\":503,\"gmt_created\":\"2020-12-03T21:45:44\"}]},\"errmsg\":\"成功\"}";
//        JSONAssert.assertEquals(expectedResponse, responseString, true);
//        System.out.println("getCustomerFootPrints1: "+responseString);
    }

    /**
     * 平台管理员获取用户足迹
     * userId:not null
     * beginTime:not null
     * endTime:null
     * @throws Exception
     */
    @Test
    public void getCustomerFootPrints2() throws Exception {
        String authorization = jwtHelper.createToken(1L,1L,1);
        String response = new String(Objects.requireNonNull(webClient.get().uri("/shops/1/footprints?userId=1&beginTime=2020-12-1 00:00:00").header("authorization",authorization).exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo("成功")
                .returnResult()
                .getResponseBodyContent()));

        System.out.println(response);

//        String expectedResponse = "{\"errno\":0,\"data\":{\"total\":1,\"pages\":1,\"pageSize\":10,\"page\":1,\"list\":[{\"id\":1048545,\"goodsSkuId\":503,\"gmt_created\":\"2020-12-03T21:45:44\"}]},\"errmsg\":\"成功\"}";
//        JSONAssert.assertEquals(expectedResponse, responseString, true);
//        System.out.println("getCustomerFootPrints2: "+responseString);
    }

    /**
     * 平台管理员获取用户足迹
     * userId:not null
     * beginTime:null
     * endTime:not null
     * @throws Exception
     */
    @Test
    public void getCustomerFootPrints3() throws Exception {
        String authorization = jwtHelper.createToken(1L,1L,1);
        String response = new String(Objects.requireNonNull(webClient.get().uri("/shops/1/footprints?userId=1&endTime=2020-12-10 23:59:59").header("authorization",authorization).exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo("成功")
                .returnResult()
                .getResponseBodyContent()));

        System.out.println(response);

//        String expectedResponse = "{\"errno\":0,\"data\":{\"total\":1,\"pages\":1,\"pageSize\":10,\"page\":1,\"list\":[{\"id\":1048545,\"goodsSkuId\":503,\"gmt_created\":\"2020-12-03T21:45:44\"}]},\"errmsg\":\"成功\"}";
//        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    /**
     * 平台管理员获取用户足迹
     * userId:not null
     * beginTime:not null
     * endTime:not null
     * @throws Exception
     */
    @Test
    public void getCustomerFootPrints4() throws Exception {
        String authorization = jwtHelper.createToken(1L,1L,1);
        String response = new String(Objects.requireNonNull(webClient.get().uri("/shops/1/footprints?userId=1&beginTime=2020-12-1 00:00:00&endTime=2020-12-10 23:59:59").header("authorization",authorization).exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo("成功")
                .returnResult()
                .getResponseBodyContent()));

        System.out.println(response);

//        String expectedResponse = "{\"errno\":0,\"data\":{\"total\":1,\"pages\":1,\"pageSize\":10,\"page\":1,\"list\":[{\"id\":1048545,\"goodsSkuId\":503,\"gmt_created\":\"2020-12-03T21:45:44\"}]},\"errmsg\":\"成功\"}";
//        JSONAssert.assertEquals(expectedResponse, responseString, true);
//        System.out.println("getCustomerFootPrints4: "+responseString);
    }

    /**
     * 平台管理员获取用户足迹
     * userId:null
     * beginTime:not null
     * endTime:not null
     * page:5
     * @throws Exception
     */
    @Test
    public void getCustomerFootPrints5() throws Exception {
        String authorization = jwtHelper.createToken(1L,1L,1);
        String response = new String(Objects.requireNonNull(webClient.get().uri("/shops/1/footprints?beginTime=2020-12-1 00:00:00&endTime=2020-12-10 23:59:59").header("authorization",authorization).exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo("成功")
                .returnResult()
                .getResponseBodyContent()));

        System.out.println(response);

//        String expectedResponse = "{\"errno\":0,\"data\":{\"total\":17231,\"pages\":1724,\"pageSize\":10,\"page\":5,\"list\":[{\"id\":1048585,\"goodsSkuId\":579,\"gmt_created\":\"2020-12-03T21:45:44\"},{\"id\":1048586,\"goodsSkuId\":402,\"gmt_created\":\"2020-12-03T21:45:44\"},{\"id\":1048587,\"goodsSkuId\":407,\"gmt_created\":\"2020-12-03T21:45:44\"},{\"id\":1048588,\"goodsSkuId\":558,\"gmt_created\":\"2020-12-03T21:45:44\"},{\"id\":1048589,\"goodsSkuId\":485,\"gmt_created\":\"2020-12-03T21:45:44\"},{\"id\":1048590,\"goodsSkuId\":476,\"gmt_created\":\"2020-12-03T21:45:44\"},{\"id\":1048591,\"goodsSkuId\":654,\"gmt_created\":\"2020-12-03T21:45:44\"},{\"id\":1048592,\"goodsSkuId\":350,\"gmt_created\":\"2020-12-03T21:45:44\"},{\"id\":1048593,\"goodsSkuId\":329,\"gmt_created\":\"2020-12-03T21:45:44\"},{\"id\":1048594,\"goodsSkuId\":320,\"gmt_created\":\"2020-12-03T21:45:44\"}]},\"errmsg\":\"成功\"}";
//        JSONAssert.assertEquals(expectedResponse, responseString, true);
//        System.out.println("getCustomerFootPrints5: "+responseString);
    }

    /**
     * 增加足迹  还想在增加一个商品不存在的？？不知道要不要
     * @throws Exception
     */
    @Test
    public void insertFootPrint1() throws Exception {
        String authorization = jwtHelper.createToken(1L,1L,1);
        Long skuId = 1L;
        String response = new String(Objects.requireNonNull(webClient.post().uri("/skus/{id}/footprints",skuId).header("authorization",authorization).exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo("成功")
                .returnResult()
                .getResponseBodyContent()));

        System.out.println(response);
    }
}
