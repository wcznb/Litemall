package cn.edu.xmu.cart.publicTest;

import cn.edu.xmu.cart.CartserviceApplication;
import cn.edu.xmu.ooad.util.ResponseCode;
import com.alibaba.fastjson.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.nio.charset.StandardCharsets;

@SpringBootTest(classes = CartserviceApplication.class)
public class test {

    private WebTestClient manageClient;

    private WebTestClient mallClient;

    @BeforeEach
    public void setUp(){

        this.manageClient = WebTestClient.bindToServer()
                .baseUrl("http://114.215.198.238:4600")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8")
                .build();

        this.mallClient = WebTestClient.bindToServer()
                .baseUrl("http://114.215.198.238:4655")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8")
                .build();

    }

    /**
     * 买家登录，获取token
     *
     * @author yang8miao
     * @param userName
     * @param password
     * @return token
     * createdBy yang8miao 2020/11/26 21:34
     * modifiedBy yang8miao 2020/11/26 21:34
     */
    private String userLogin(String userName, String password) throws Exception{

        JSONObject body = new JSONObject();
        body.put("userName", userName);
        body.put("password", password);
        String requireJson = body.toJSONString();
        byte[] responseString = manageClient.post().uri("/users/login").bodyValue(requireJson).exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.OK.getMessage())
                .returnResult()
                .getResponseBodyContent();
        return JSONObject.parseObject(new String(responseString, StandardCharsets.UTF_8)).getString("data");
    }

    /**
     * 购物车服务-买家获得购物车列表  普通测试1，查询成功 page=1&pageSize=5。
     * @throws Exception
     * @author yang8miao
     * @date Created in 2020/12/9 16:18
     */
    @Test
    @Order(0)
    public void getCarts1() throws Exception {

        // userId = 20
        String token = this.userLogin("10101113105", "123456");

        byte[] responseString = mallClient.get().uri("/carts?page=1&pageSize=5").header("authorization",token).exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .returnResult()
                .getResponseBodyContent();

        String expectedResponse = "{\n" +
                "  \"errno\": 0,\n" +
                "  \"data\": {\n" +
                "    \"page\": 1,\n" +
                "    \"pageSize\": 5,\n" +
                "    \"total\": 3,\n" +
                "    \"pages\": 1,\n" +
                "    \"list\": [\n" +
                "      {\n" +
                "        \"id\": 1001,\n" +
                "        \"goodsSkuId\": 393,\n" +
                "        \"skuName\": \"+\",\n" +
                "        \"quantity\": 100,\n" +
                "        \"price\": 2,\n" +
                "        \"gmtCreate\": \"2020-11-24T17:06:28\",\n" +
                "        \"gmtModified\": \"2020-11-24T17:06:28\",\n" +
                "        \"couponActivity\": [\n" +
                "        ]\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": 1002,\n" +
                "        \"goodsSkuId\": 658,\n" +
                "        \"skuName\": \"+\",\n" +
                "        \"quantity\": 100,\n" +
                "        \"price\": 2,\n" +
                "        \"gmtCreate\": \"2020-11-24T17:06:28\",\n" +
                "        \"gmtModified\": \"2020-11-24T17:06:28\",\n" +
                "        \"couponActivity\": [\n" +
                "        ]\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": 1003,\n" +
                "        \"goodsSkuId\": 377,\n" +
                "        \"skuName\": \"+\",\n" +
                "        \"quantity\": 100,\n" +
                "        \"price\": 2,\n" +
                "        \"gmtCreate\": \"2020-11-24T17:06:28\",\n" +
                "        \"gmtModified\": \"2020-11-24T17:06:28\",\n" +
                "        \"couponActivity\": [\n" +
                "        ]\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, new String(responseString, StandardCharsets.UTF_8), false);
    }

    /**
     * 购物车服务-买家获得购物车列表  普通测试2，查询成功 page=1&pageSize=3。
     * @throws Exception
     * @author yang8miao
     * @date Created in 2020/12/9 16:18
     */
    @Test
    @Order(0)
    public void getCarts2() throws Exception {

        // userId = 400
        String token = this.userLogin("35642539836", "123456");

        byte[] responseString = mallClient.get().uri("/carts?page=1&pageSize=3").header("authorization",token).exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .returnResult()
                .getResponseBodyContent();

        String expectedResponse = "{\n" +
                "  \"errno\": 0,\n" +
                "  \"data\": {\n" +
                "    \"page\": 1,\n" +
                "    \"pageSize\": 3,\n" +
                "    \"total\": 1,\n" +
                "    \"pages\": 1,\n" +
                "    \"list\": [\n" +
                "      {\n" +
                "        \"id\": 1004,\n" +
                "        \"goodsSkuId\": 446,\n" +
                "        \"skuName\": \"+\",\n" +
                "        \"quantity\": 100,\n" +
                "        \"price\": 2,\n" +
                "        \"gmtCreate\": \"2020-11-24T17:06:28\",\n" +
                "        \"gmtModified\": \"2020-11-24T17:06:28\",\n" +
                "        \"couponActivity\": [\n" +
                "        ]\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, new String(responseString, StandardCharsets.UTF_8), false);
    }

    /**
     * 购物车服务-买家获得购物车列表  普通测试3，查询成功，购物车中无商品。
     * @throws Exception
     * @author yang8miao
     * @date Created in 2020/12/9 16:18
     */
    @Test
    @Order(0)
    public void getCarts3() throws Exception {

        // userId = 4000
        String token = this.userLogin("28883882732", "123456");

        byte[] responseString = mallClient.get().uri("/carts?page=1&pageSize=20").header("authorization",token).exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .returnResult()
                .getResponseBodyContent();

        String expectedResponse = "{\n" +
                "  \"errno\": 0,\n" +
                "  \"data\": {\n" +
                "    \"page\": 1,\n" +
                "    \"pageSize\": 20,\n" +
                "    \"total\": 0,\n" +
                "    \"pages\": 0,\n" +
                "    \"list\": [\n" +
                "    ]\n" +
                "  }\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, new String(responseString, StandardCharsets.UTF_8), false);
    }


    /**
     * 购物车服务-买家获得购物车列表  普通测试4，查询成功，购物车中无商品
     * @throws Exception
     * @author yang8miao
     * @date Created in 2020/12/9 16:18
     */
    @Test
    @Order(0)
    public void getCarts4() throws Exception {

        // userId = 9782
        String token = this.userLogin("7912044979", "123456");

        byte[] responseString = mallClient.get().uri("/carts?page=1&pageSize=2").header("authorization",token).exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .returnResult()
                .getResponseBodyContent();

        String expectedResponse = "{\n" +
                "  \"errno\": 0,\n" +
                "  \"data\": {\n" +
                "    \"page\": 1,\n" +
                "    \"pageSize\": 2,\n" +
                "    \"total\": 0,\n" +
                "    \"pages\": 0,\n" +
                "    \"list\": [\n" +
                "    ]\n" +
                "  }\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, new String(responseString, StandardCharsets.UTF_8), false);
    }

    /**
     * 购物车服务-买家获得购物车列表  普通测试5，查询成功，购物车中无商品
     * @throws Exception
     * @author yang8miao
     * @date Created in 2020/12/9 16:18
     */
    @Test
    @Order(0)
    public void getCarts5() throws Exception {

        // userId = 9781
        String token = this.userLogin("29970839554", "123456");

        byte[] responseString = mallClient.get().uri("/carts?page=4&pageSize=222").header("authorization",token).exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .returnResult()
                .getResponseBodyContent();

        String expectedResponse = "{\n" +
                "  \"errno\": 0,\n" +
                "  \"data\": {\n" +
                "    \"page\": 4,\n" +
                "    \"pageSize\": 222,\n" +
                "    \"total\": 0,\n" +
                "    \"pages\": 0,\n" +
                "    \"list\": [\n" +
                "    ]\n" +
                "  }\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, new String(responseString, StandardCharsets.UTF_8), false);
    }

    /**
     * 购物车服务-买家获得购物车列表  普通测试6，查询成功，购物车中无商品
     * @throws Exception
     * @author yang8miao
     * @date Created in 2020/12/9 16:18
     */
    @Test
    @Order(0)
    public void getCarts6() throws Exception {

        // userId = 9782
        String token = this.userLogin("7912044979", "123456");

        byte[] responseString = mallClient.get().uri("/carts?page=1&pageSize=2223").header("authorization",token).exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .returnResult()
                .getResponseBodyContent();

        String expectedResponse = "{\n" +
                "  \"errno\": 0,\n" +
                "  \"data\": {\n" +
                "    \"page\": 1,\n" +
                "    \"pageSize\": 2223,\n" +
                "    \"total\": 0,\n" +
                "    \"pages\": 0,\n" +
                "    \"list\": [\n" +
                "    ]\n" +
                "  }\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, new String(responseString, StandardCharsets.UTF_8), false);
    }

    /**
     * 购物车服务-买家将商品加入购物车  普通测试1，加入成功并查询
     * @throws Exception
     * @author yang8miao
     * @date Created in 2020/12/9 16:18
     */
    @Test
    @Order(0)
    public void postCarts1() throws Exception {

        // userId = 99
        String token = this.userLogin("16436076738", "123456");

        JSONObject body = new JSONObject();
        body.put("goodsSkuId", 300);
        body.put("quantity", 1111);
        String requireJson = body.toJSONString();

        byte[] responseString = mallClient.post().uri("/carts").header("authorization",token).bodyValue(requireJson).exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .returnResult()
                .getResponseBodyContent();

        String expectedResponse = "{\n" +
                "  \"errno\": 0,\n" +
                "  \"data\": {\n" +
                "    \"goodsSkuId\": 300,\n" +
                "    \"skuName\": \"+\",\n" +
                "    \"quantity\": 1111,\n" +
                "    \"price\": 68000,\n" +
                "    \"couponActivity\": [\n" +
                "    ]\n" +
                "  }\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, new String(responseString, StandardCharsets.UTF_8), false);

        // 买家查询自己购物车中商品，进行验证
        byte[] queryResponseString = mallClient.get().uri("/carts?page=1&pageSize=10").header("authorization",token).exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .returnResult()
                .getResponseBodyContent();

        String queryExpectedResponse = "{\n" +
                "  \"errno\": 0,\n" +
                "  \"data\": {\n" +
                "    \"page\": 1,\n" +
                "    \"pageSize\": 10,\n" +
                "    \"total\": 1,\n" +
                "    \"pages\": 1,\n" +
                "    \"list\": [\n" +
                "      {\n" +
                "        \"goodsSkuId\": 300,\n" +
                "        \"skuName\": \"+\",\n" +
                "        \"quantity\": 1111,\n" +
                "        \"price\": 68000,\n" +
                "        \"couponActivity\": [\n" +
                "        ]\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";
        JSONAssert.assertEquals(queryExpectedResponse, new String(queryResponseString, StandardCharsets.UTF_8), false);
    }

    /**
     * 购物车服务-买家将商品加入购物车  普通测试2，加入成功并查询
     * @throws Exception
     * @author yang8miao
     * @date Created in 2020/12/9 16:18
     */
    @Test
    @Order(0)
    public void postCarts2() throws Exception {

        // userId = 999
        String token = this.userLogin("59506839941", "123456");

        JSONObject body = new JSONObject();
        body.put("goodsSkuId", 300);
        body.put("quantity", 111);
        String requireJson = body.toJSONString();

        byte[] responseString = mallClient.post().uri("/carts").header("authorization",token).bodyValue(requireJson).exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .returnResult()
                .getResponseBodyContent();

        String expectedResponse = "{\n" +
                "  \"errno\": 0,\n" +
                "  \"data\": {\n" +
                "    \"goodsSkuId\": 300,\n" +
                "    \"skuName\": \"+\",\n" +
                "    \"quantity\": 111,\n" +
                "    \"price\": 68000,\n" +
                "    \"couponActivity\": [\n" +
                "    ]\n" +
                "  }\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, new String(responseString, StandardCharsets.UTF_8), false);

        // 买家查询自己购物车中商品，进行验证
        byte[] queryResponseString = mallClient.get().uri("/carts?page=1&pageSize=10").header("authorization",token).exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .returnResult()
                .getResponseBodyContent();

        String queryExpectedResponse = "{\n" +
                "  \"errno\": 0,\n" +
                "  \"data\": {\n" +
                "    \"page\": 1,\n" +
                "    \"pageSize\": 10,\n" +
                "    \"total\": 1,\n" +
                "    \"pages\": 1,\n" +
                "    \"list\": [\n" +
                "      {\n" +
                "        \"goodsSkuId\": 300,\n" +
                "        \"skuName\": \"+\",\n" +
                "        \"quantity\": 111,\n" +
                "        \"price\": 68000,\n" +
                "        \"couponActivity\": [\n" +
                "        ]\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";
        JSONAssert.assertEquals(queryExpectedResponse, new String(queryResponseString, StandardCharsets.UTF_8), false);
    }

    /**
     * 购物车服务-买家清空购物车  普通测试1，清空购物车成功
     * @throws Exception
     * @author yang8miao
     * @date Created in 2020/12/9 16:18
     */
    @Test
    @Order(0)
    public void deleteCarts1() throws Exception {

        // userId = 1
        String token = this.userLogin("8606245097", "123456");

        byte[] responseString = mallClient.delete().uri("/carts").header("authorization",token).exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .returnResult()
                .getResponseBodyContent();

        // 买家查询自己购物车中商品，进行验证
        byte[] queryResponseString = mallClient.get().uri("/carts?page=1&pageSize=10").header("authorization",token).exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .returnResult()
                .getResponseBodyContent();

        String queryExpectedResponse = "{\n" +
                "  \"errno\": 0,\n" +
                "  \"data\": {\n" +
                "    \"page\": 1,\n" +
                "    \"pageSize\": 10,\n" +
                "    \"total\": 0,\n" +
                "    \"pages\": 0,\n" +
                "    \"list\": [\n" +
                "    ]\n" +
                "  }\n" +
                "}";
        JSONAssert.assertEquals(queryExpectedResponse, new String(queryResponseString, StandardCharsets.UTF_8), false);
    }

    /**
     * 购物车服务-买家清空购物车  普通测试2，清空购物车成功
     * @throws Exception
     * @author yang8miao
     * @date Created in 2020/12/9 16:18
     */
    @Test
    @Order(0)
    public void deleteCarts2() throws Exception {

        // userId = 2
        String token = this.userLogin("36040122840", "123456");

        byte[] responseString = mallClient.delete().uri("/carts").header("authorization",token).exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .returnResult()
                .getResponseBodyContent();

        // 买家查询自己购物车中商品，进行验证
        byte[] queryResponseString = mallClient.get().uri("/carts?page=1&pageSize=10").header("authorization",token).exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .returnResult()
                .getResponseBodyContent();

        String queryExpectedResponse = "{\n" +
                "  \"errno\": 0,\n" +
                "  \"data\": {\n" +
                "    \"page\": 1,\n" +
                "    \"pageSize\": 10,\n" +
                "    \"total\": 0,\n" +
                "    \"pages\": 0,\n" +
                "    \"list\": [\n" +
                "    ]\n" +
                "  }\n" +
                "}";
        JSONAssert.assertEquals(queryExpectedResponse, new String(queryResponseString, StandardCharsets.UTF_8), false);
    }


    /**
     * 购物车服务-买家清空购物车  普通测试3，清空购物车成功
     * @throws Exception
     * @author yang8miao
     * @date Created in 2020/12/9 16:18
     */
    @Test
    @Order(0)
    public void deleteCarts3() throws Exception {

        // userId = 3
        String token = this.userLogin("7306155755", "123456");

        byte[] responseString = mallClient.delete().uri("/carts").header("authorization",token).exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .returnResult()
                .getResponseBodyContent();

        // 买家查询自己购物车中商品，进行验证
        byte[] queryResponseString = mallClient.get().uri("/carts?page=1&pageSize=10").header("authorization",token).exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .returnResult()
                .getResponseBodyContent();

        String queryExpectedResponse = "{\n" +
                "  \"errno\": 0,\n" +
                "  \"data\": {\n" +
                "    \"page\": 1,\n" +
                "    \"pageSize\": 10,\n" +
                "    \"total\": 0,\n" +
                "    \"pages\": 0,\n" +
                "    \"list\": [\n" +
                "    ]\n" +
                "  }\n" +
                "}";
        JSONAssert.assertEquals(queryExpectedResponse, new String(queryResponseString, StandardCharsets.UTF_8), false);
    }


    /**
     * 购物车服务-买家清空购物车  普通测试4，清空购物车成功
     * @throws Exception
     * @author yang8miao
     * @date Created in 2020/12/9 16:18
     */
    @Test
    @Order(0)
    public void deleteCarts4() throws Exception {

        // userId = 4
        String token = this.userLogin("14455881448", "123456");

        byte[] responseString = mallClient.delete().uri("/carts").header("authorization",token).exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .returnResult()
                .getResponseBodyContent();

        // 买家查询自己购物车中商品，进行验证
        byte[] queryResponseString = mallClient.get().uri("/carts?page=1&pageSize=10").header("authorization",token).exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .returnResult()
                .getResponseBodyContent();

        String queryExpectedResponse = "{\n" +
                "  \"errno\": 0,\n" +
                "  \"data\": {\n" +
                "    \"page\": 1,\n" +
                "    \"pageSize\": 10,\n" +
                "    \"total\": 0,\n" +
                "    \"pages\": 0,\n" +
                "    \"list\": [\n" +
                "    ]\n" +
                "  }\n" +
                "}";
        JSONAssert.assertEquals(queryExpectedResponse, new String(queryResponseString, StandardCharsets.UTF_8), false);
    }


    /**
     * 购物车服务-买家清空购物车  普通测试5，清空购物车成功
     * @throws Exception
     * @author yang8miao
     * @date Created in 2020/12/9 16:18
     */
    @Test
    @Order(0)
    public void deleteCarts5() throws Exception {

        // userId = 5
        String token = this.userLogin("8906373389", "123456");

        byte[] responseString = mallClient.delete().uri("/carts").header("authorization",token).exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .returnResult()
                .getResponseBodyContent();

        // 买家查询自己购物车中商品，进行验证
        byte[] queryResponseString = mallClient.get().uri("/carts?page=1&pageSize=10").header("authorization",token).exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .returnResult()
                .getResponseBodyContent();

        String queryExpectedResponse = "{\n" +
                "  \"errno\": 0,\n" +
                "  \"data\": {\n" +
                "    \"page\": 1,\n" +
                "    \"pageSize\": 10,\n" +
                "    \"total\": 0,\n" +
                "    \"pages\": 0,\n" +
                "    \"list\": [\n" +
                "    ]\n" +
                "  }\n" +
                "}";
        JSONAssert.assertEquals(queryExpectedResponse, new String(queryResponseString, StandardCharsets.UTF_8), false);
    }

    /**
     * 购物车服务-买家清空购物车  普通测试6，清空购物车成功
     * @throws Exception
     * @author yang8miao
     * @date Created in 2020/12/9 16:18
     */
    @Test
    @Order(0)
    public void deleteCarts6() throws Exception {

        // userId = 6
        String token = this.userLogin("39118189028", "123456");

        byte[] responseString = mallClient.delete().uri("/carts").header("authorization",token).exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .returnResult()
                .getResponseBodyContent();

        // 买家查询自己购物车中商品，进行验证
        byte[] queryResponseString = mallClient.get().uri("/carts?page=2&pageSize=10").header("authorization",token).exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .returnResult()
                .getResponseBodyContent();

        String queryExpectedResponse = "{\n" +
                "  \"errno\": 0,\n" +
                "  \"data\": {\n" +
                "    \"page\": 2,\n" +
                "    \"pageSize\": 10,\n" +
                "    \"total\": 0,\n" +
                "    \"pages\": 0,\n" +
                "    \"list\": [\n" +
                "    ]\n" +
                "  }\n" +
                "}";
        JSONAssert.assertEquals(queryExpectedResponse, new String(queryResponseString, StandardCharsets.UTF_8), false);
    }

    /**
     * 购物车服务-买家清空购物车  普通测试6，清空购物车成功,查询时使用默认的page和pageSize
     * @throws Exception
     * @author yang8miao
     * @date Created in 2020/12/9 16:18
     */
    @Test
    @Order(0)
    public void deleteCarts7() throws Exception {

        // userId = 7
        String token = this.userLogin("63088258694", "123456");

        byte[] responseString = mallClient.delete().uri("/carts").header("authorization",token).exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .returnResult()
                .getResponseBodyContent();

        // 买家查询自己购物车中商品，进行验证
        byte[] queryResponseString = mallClient.get().uri("/carts?").header("authorization",token).exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .returnResult()
                .getResponseBodyContent();

        String queryExpectedResponse = "{\n" +
                "  \"errno\": 0,\n" +
                "  \"data\": {\n" +
                "    \"page\": 1,\n" +
                "    \"pageSize\": 10,\n" +
                "    \"total\": 0,\n" +
                "    \"pages\": 0,\n" +
                "    \"list\": [\n" +
                "    ]\n" +
                "  }\n" +
                "}";
        JSONAssert.assertEquals(queryExpectedResponse, new String(queryResponseString, StandardCharsets.UTF_8), false);
    }

    /**
     * 购物车服务-买家清空购物车  普通测试6，清空购物车成功,查询时使用默认的page和pageSize
     * @throws Exception
     * @author yang8miao
     * @date Created in 2020/12/9 16:18
     */
    @Test
    @Order(0)
    public void deleteCarts8() throws Exception {

        // userId = 8
        String token = this.userLogin("46613241589", "123456");

        byte[] responseString = mallClient.delete().uri("/carts").header("authorization",token).exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .returnResult()
                .getResponseBodyContent();

        // 买家查询自己购物车中商品，进行验证
        byte[] queryResponseString = mallClient.get().uri("/carts?").header("authorization",token).exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .returnResult()
                .getResponseBodyContent();

        String queryExpectedResponse = "{\n" +
                "  \"errno\": 0,\n" +
                "  \"data\": {\n" +
                "    \"page\": 1,\n" +
                "    \"pageSize\": 10,\n" +
                "    \"total\": 0,\n" +
                "    \"pages\": 0,\n" +
                "    \"list\": [\n" +
                "    ]\n" +
                "  }\n" +
                "}";
        JSONAssert.assertEquals(queryExpectedResponse, new String(queryResponseString, StandardCharsets.UTF_8), false);
    }


    /**
     * 购物车服务-买家修改购物车单个商品的数量或规格  普通测试1，修改成功并查询，此时修改数量
     * @throws Exception
     * @author yang8miao
     * @date Created in 2020/12/9 16:18
     */
    @Test
    @Order(0)
    public void putCartsId1() throws Exception {

        // userId = 10000
        String token = this.userLogin("39288437216", "123456");

        JSONObject body = new JSONObject();
        body.put("goodsSkuId", 367);
        body.put("quantity", 111);
        String requireJson = body.toJSONString();

        byte[] responseString = mallClient.put().uri("/carts/1041").header("authorization",token)
                .bodyValue(requireJson)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .returnResult()
                .getResponseBodyContent();

        // 买家查询自己购物车中商品，进行验证
        byte[] queryResponseString = mallClient.get().uri("/carts?page=1&pageSize=10").header("authorization",token).exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .returnResult()
                .getResponseBodyContent();

        String queryExpectedResponse = "{\n" +
                "  \"errno\": 0,\n" +
                "  \"data\": {\n" +
                "    \"page\": 1,\n" +
                "    \"pageSize\": 10,\n" +
                "    \"total\": 3,\n" +
                "    \"pages\": 1,\n" +
                "    \"list\": [\n" +
                "      {\n" +
                "        \"id\": 1041,\n" +
                "        \"goodsSkuId\": 367,\n" +
                "        \"skuName\": \"+\",\n" +
                "        \"quantity\": 111,\n" +
                "        \"price\": 24120,\n" +
                "        \"gmtCreate\": \"2020-11-24T17:06:28\",\n" +
                "        \"couponActivity\": [\n" +
                "        ]\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": 1042,\n" +
                "        \"goodsSkuId\": 658,\n" +
                "        \"skuName\": \"+\",\n" +
                "        \"quantity\": 100,\n" +
                "        \"price\": 2,\n" +
                "        \"gmtCreate\": \"2020-11-24T17:06:28\",\n" +
                "        \"gmtModified\": \"2020-11-24T17:06:28\",\n" +
                "        \"couponActivity\": [\n" +
                "        ]\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": 1043,\n" +
                "        \"goodsSkuId\": 377,\n" +
                "        \"skuName\": \"+\",\n" +
                "        \"quantity\": 100,\n" +
                "        \"price\": 2,\n" +
                "        \"gmtCreate\": \"2020-11-24T17:06:28\",\n" +
                "        \"gmtModified\": \"2020-11-24T17:06:28\",\n" +
                "        \"couponActivity\": [\n" +
                "        ]\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";
        JSONAssert.assertEquals(queryExpectedResponse, new String(queryResponseString, StandardCharsets.UTF_8), false);
    }


    /**
     * 购物车服务-买家修改购物车单个商品的数量或规格  普通测试2，修改成功并查询，此时修改数量
     * @throws Exception
     * @author yang8miao
     * @date Created in 2020/12/9 16:18
     */
    @Test
    @Order(0)
    public void putCartsId2() throws Exception {

        // userId = 10001
        String token = this.userLogin("41372695510", "123456");

        JSONObject body = new JSONObject();
        body.put("goodsSkuId", 446);
        body.put("quantity", 101);
        String requireJson = body.toJSONString();

        byte[] responseString = mallClient.put().uri("/carts/1044").header("authorization",token)
                .bodyValue(requireJson)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .returnResult()
                .getResponseBodyContent();

        // 买家查询自己购物车中商品，进行验证
        byte[] queryResponseString = mallClient.get().uri("/carts?page=1&pageSize=10").header("authorization",token).exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .returnResult()
                .getResponseBodyContent();

        String queryExpectedResponse = "{\n" +
                "  \"errno\": 0,\n" +
                "  \"data\": {\n" +
                "    \"page\": 1,\n" +
                "    \"pageSize\": 10,\n" +
                "    \"total\": 2,\n" +
                "    \"pages\": 1,\n" +
                "    \"list\": [\n" +
                "      {\n" +
                "        \"id\": 1044,\n" +
                "        \"goodsSkuId\": 446,\n" +
                "        \"skuName\": \"+\",\n" +
                "        \"quantity\": 101,\n" +
                "        \"price\": 1799,\n" +
                "        \"gmtCreate\": \"2020-11-24T17:06:28\",\n" +
                "        \"couponActivity\": [\n" +
                "        ]\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": 1045,\n" +
                "        \"goodsSkuId\": 643,\n" +
                "        \"skuName\": \"+\",\n" +
                "        \"quantity\": 100,\n" +
                "        \"price\": 2,\n" +
                "        \"gmtCreate\": \"2020-11-24T17:06:28\",\n" +
                "        \"gmtModified\": \"2020-11-24T17:06:28\",\n" +
                "        \"couponActivity\": [\n" +
                "        ]\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";
        JSONAssert.assertEquals(queryExpectedResponse, new String(queryResponseString, StandardCharsets.UTF_8), false);
    }



    /**
     * 购物车服务-买家修改购物车单个商品的数量或规格  普通测试3,要修改的skuId与原先不是同一个spuId，字段不合法
     * @throws Exception
     * @author yang8miao
     * @date Created in 2020/12/9 16:18
     */
    @Test
    @Order(0)
    public void putCartsId3() throws Exception {

        // userId = 10002
        String token = this.userLogin("32485307410", "123456");

        JSONObject body = new JSONObject();
        body.put("goodsSkuId", 522);
        body.put("quantity", 100);
        String requireJson = body.toJSONString();

        byte[] responseString = mallClient.put().uri("/carts/1046").header("authorization",token)
                .bodyValue(requireJson)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.FIELD_NOTVALID.getCode())
                .returnResult()
                .getResponseBodyContent();
    }


    /**
     * 购物车服务-买家修改购物车单个商品的数量或规格  普通测试4,要修改的skuId与原先不是同一个spuId，字段不合法
     * @throws Exception
     * @author yang8miao
     * @date Created in 2020/12/9 16:18
     */
    @Test
    @Order(0)
    public void putCartsId4() throws Exception {

        // userId = 10002
        String token = this.userLogin("32485307410", "123456");

        JSONObject body = new JSONObject();
        body.put("goodsSkuId", 322);
        body.put("quantity", 101);
        String requireJson = body.toJSONString();

        byte[] responseString = mallClient.put().uri("/carts/1047").header("authorization",token)
                .bodyValue(requireJson)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.FIELD_NOTVALID.getCode())
                .returnResult()
                .getResponseBodyContent();
    }


    /**
     * 购物车服务-买家修改购物车单个商品的数量或规格  普通测试5，修改成功并查询，此时修改规格，要修改的skuId未加入购物车
     *
     * @throws Exception
     * @author yang8miao
     * @date Created in 2020/12/9 16:18
     */
    @Test
    @Order(0)
    public void putCartsId5() throws Exception {

        // userId = 10003
        String token = this.userLogin("44866608870", "123456");

        JSONObject body = new JSONObject();
        body.put("goodsSkuId", 6810);
        body.put("quantity", 150);
        String requireJson = body.toJSONString();

        byte[] responseString = mallClient.put().uri("/carts/1048").header("authorization",token)
                .bodyValue(requireJson)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .returnResult()
                .getResponseBodyContent();

        // 买家查询自己购物车中商品，进行验证
        byte[] queryResponseString = mallClient.get().uri("/carts?page=1&pageSize=10").header("authorization",token).exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .returnResult()
                .getResponseBodyContent();



        String queryExpectedResponse = "{\n" +
                "  \"errno\": 0,\n" +
                "  \"data\": {\n" +
                "    \"page\": 1,\n" +
                "    \"pageSize\": 10,\n" +
                "    \"total\": 1,\n" +
                "    \"pages\": 1,\n" +
                "    \"list\": [\n" +
                "      {\n" +
                "        \"goodsSkuId\": 6810,\n" +
                "        \"skuName\": \"+\",\n" +
                "        \"quantity\": 150,\n" +
                "        \"price\": 6697,\n" +
                "        \"couponActivity\": [\n" +
                "        ]\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";
        JSONAssert.assertEquals(queryExpectedResponse, new String(queryResponseString, StandardCharsets.UTF_8), false);
    }


    /**
     * 购物车服务-买家删除购物车中商品  普通测试1，删除成功
     * @throws Exception
     * @author yang8miao
     * @date Created in 2020/12/9 16:18
     */
    @Test
    @Order(0)
    public void deleteCartsId1() throws Exception {

        // userId = 1000
        String token = this.userLogin("97142877706", "123456");

        byte[] responseString = mallClient.delete().uri("/carts/1061").header("authorization",token).exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .returnResult()
                .getResponseBodyContent();

        // 买家查询自己购物车中商品，进行验证
        byte[] queryResponseString = mallClient.get().uri("/carts?page=1&pageSize=10").header("authorization",token).exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .returnResult()
                .getResponseBodyContent();

        String queryExpectedResponse = "{\n" +
                "  \"errno\": 0,\n" +
                "  \"data\": {\n" +
                "    \"page\": 1,\n" +
                "    \"pageSize\": 10,\n" +
                "    \"total\": 0,\n" +
                "    \"pages\": 0,\n" +
                "    \"list\": [\n" +
                "    ]\n" +
                "  }\n" +
                "}";
        JSONAssert.assertEquals(queryExpectedResponse, new String(queryResponseString, StandardCharsets.UTF_8), false);
    }

    /**
     * 购物车服务-买家删除购物车中商品  普通测试2，删除成功
     * @throws Exception
     * @author yang8miao
     * @date Created in 2020/12/9 16:18
     */
    @Test
    @Order(0)
    public void deleteCartsId2() throws Exception {

        // userId = 1001
        String token = this.userLogin("10153144607", "123456");

        byte[] responseString = mallClient.delete().uri("/carts/1062").header("authorization",token).exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .returnResult()
                .getResponseBodyContent();

        // 买家查询自己购物车中商品，进行验证
        byte[] queryResponseString = mallClient.get().uri("/carts?page=1&pageSize=10").header("authorization",token).exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .returnResult()
                .getResponseBodyContent();

        String queryExpectedResponse = "{\n" +
                "  \"errno\": 0,\n" +
                "  \"data\": {\n" +
                "    \"page\": 1,\n" +
                "    \"pageSize\": 10,\n" +
                "    \"total\": 1,\n" +
                "    \"pages\": 1,\n" +
                "    \"list\": [\n" +
                "      {\n" +
                "        \"id\": 1063,\n" +
                "        \"goodsSkuId\": 377,\n" +
                "        \"skuName\": \"+\",\n" +
                "        \"quantity\": 100,\n" +
                "        \"price\": 2,\n" +
                "        \"gmtCreate\": \"2020-11-24T17:06:28\",\n" +
                "        \"gmtModified\": \"2020-11-24T17:06:28\",\n" +
                "        \"couponActivity\": [\n" +
                "        ]\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";
        JSONAssert.assertEquals(queryExpectedResponse, new String(queryResponseString, StandardCharsets.UTF_8), false);
    }

    /**
     * 购物车服务-买家删除购物车中商品  普通测试,该购物车id不存在,删除失败
     * @throws Exception
     * @author yang8miao
     * @date Created in 2020/12/9 16:18
     */
    @Test
    @Order(0)
    public void deleteCartsId3() throws Exception {

        // userId = 10
        String token = this.userLogin("19769355952", "123456");

        byte[] responseString = mallClient.delete().uri("/carts/45").header("authorization",token).exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.RESOURCE_ID_NOTEXIST.getCode())
                .returnResult()
                .getResponseBodyContent();
    }


    /**
     * 购物车服务-买家删除购物车中商品  普通测试,该购物车id所属买家与操作用户不一致,删除失败
     * @throws Exception
     * @author yang8miao
     * @date Created in 2020/12/9 16:18
     */
    @Test
    @Order(0)
    public void deleteCartsId4() throws Exception {

        // userId = 11
        String token = this.userLogin("14902184265", "123456");

        byte[] responseString = mallClient.delete().uri("/carts/1080").header("authorization",token).exchange()
                .expectStatus().isForbidden()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.RESOURCE_ID_OUTSCOPE.getCode())
                .returnResult()
                .getResponseBodyContent();
    }

    /**
     * 购物车服务-买家删除购物车中商品  普通测试,该购物车id所属买家与操作用户不一致,删除失败
     * @throws Exception
     * @author yang8miao
     * @date Created in 2020/12/9 16:18
     */
    @Test
    @Order(0)
    public void deleteCartsId5() throws Exception {

        // userId = 12
        String token = this.userLogin("5217325133", "123456");

        byte[] responseString = mallClient.delete().uri("/carts/1080").header("authorization",token).exchange()
                .expectStatus().isForbidden()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.RESOURCE_ID_OUTSCOPE.getCode())
                .returnResult()
                .getResponseBodyContent();
    }


    /**
     * 购物车服务-买家删除购物车中商品  普通测试,该购物车id不存在,删除失败
     * @throws Exception
     * @author yang8miao
     * @date Created in 2020/12/9 16:18
     */
    @Test
    @Order(0)
    public void deleteCartsId6() throws Exception {

        // userId = 10
        String token = this.userLogin("19769355952", "123456");

        byte[] responseString = mallClient.delete().uri("/carts/523").header("authorization",token).exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.RESOURCE_ID_NOTEXIST.getCode())
                .returnResult()
                .getResponseBodyContent();
    }


    /**
     * 购物车服务-买家删除购物车中商品  普通测试,该购物车id不存在,删除失败
     * @throws Exception
     * @author yang8miao
     * @date Created in 2020/12/9 16:18
     */
    @Test
    @Order(0)
    public void deleteCartsId7() throws Exception {

        // userId = 10
        String token = this.userLogin("19769355952", "123456");

        byte[] responseString = mallClient.delete().uri("/carts/544").header("authorization",token).exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.RESOURCE_ID_NOTEXIST.getCode())
                .returnResult()
                .getResponseBodyContent();
    }


}
