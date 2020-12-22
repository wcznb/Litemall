package cn.edu.xmu.favorite.publicTest;

import cn.edu.xmu.favorite.FavoriteserviceApplication;
import cn.edu.xmu.ooad.util.ResponseCode;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.nio.charset.StandardCharsets;

@SpringBootTest(classes = FavoriteserviceApplication.class)   //标识本类是一个SpringBootTest
@Slf4j
public class test {
    private WebTestClient manageClient;

    private WebTestClient mallClient;

    @BeforeEach
    public void setUp(){

        this.mallClient = WebTestClient.bindToServer()
                .baseUrl("http://114.215.198.238:4600")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8")
                .build();

        this.manageClient = WebTestClient.bindToServer()
                .baseUrl("http://114.215.198.238:4600")
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
     * 商品收藏服务-买家查看所有收藏的商品  普通测试1，查询成功 page=1&pageSize=1
     * @throws Exception
     * @author yang8miao
     * @date Created in 2020/12/9 16:18
     */
    @Test
    @Order(0)
    public void getFavorites1() throws Exception {

        // userId = 20
        String token = this.userLogin("10101113105", "123456");

        byte[] responseString = mallClient.get().uri("/favorites?page=1&pageSize=1").header("authorization",token).exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .returnResult()
                .getResponseBodyContent();

        String expectedResponse = "{\n" +
                "  \"errno\": 0,\n" +
                "  \"data\": {\n" +
                "    \"page\": 1,\n" +
                "    \"pageSize\": 1,\n" +
                "    \"total\": 3,\n" +
                "    \"pages\": 3,\n" +
                "    \"list\": [\n" +
                "      {\n" +
                "        \"id\": 3735458,\n" +
                "        \"goodsSku\": {\n" +
                "          \"id\": 428,\n" +
                "          \"name\": \"+\",\n" +
                "          \"skuSn\": null,\n" +
                "          \"imageUrl\": \"http://47.52.88.176/file/images/201707/file_5967a3fed2cf4.jpg\",\n" +
                "          \"inventory\": 9972,\n" +
                "          \"originalPrice\": 299,\n" +
                "          \"price\": 299,\n" +
                "          \"disable\":  false\n" +
                "        },\n" +
                "        \"gmtCreate\": \"2020-12-07T21:47:23\"\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, new String(responseString, StandardCharsets.UTF_8), false);
    }

    /**
     * 商品收藏服务-买家查看所有收藏的商品  普通测试2，查询成功 page=1&pageSize=5
     * @throws Exception
     * @author yang8miao
     * @date Created in 2020/12/9 16:18
     */
    @Test
    @Order(0)
    public void getFavorites2() throws Exception {

        // userId = 20
        String token = this.userLogin("10101113105", "123456");

        byte[] responseString = mallClient.get().uri("/favorites?page=1&pageSize=5").header("authorization",token).exchange()
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
                "        \"id\": 3735458,\n" +
                "        \"goodsSku\": {\n" +
                "          \"id\": 428,\n" +
                "          \"name\": \"+\",\n" +
                "          \"skuSn\": null,\n" +
                "          \"imageUrl\": \"http://47.52.88.176/file/images/201707/file_5967a3fed2cf4.jpg\",\n" +
                "          \"inventory\": 9972,\n" +
                "          \"originalPrice\": 299,\n" +
                "          \"price\": 299,\n" +
                "          \"disable\":  false\n" +
                "        },\n" +
                "        \"gmtCreate\": \"2020-12-07T21:47:23\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": 3768225,\n" +
                "        \"goodsSku\": {\n" +
                "          \"id\": 420,\n" +
                "          \"name\": \"+\",\n" +
                "          \"skuSn\": null,\n" +
                "          \"imageUrl\": \"http://47.52.88.176/file/images/201707/file_5967792535e80.jpg\",\n" +
                "          \"inventory\": 150,\n" +
                "          \"originalPrice\": 118000,\n" +
                "          \"price\": 118000,\n" +
                "          \"disable\":  false\n" +
                "        },\n" +
                "        \"gmtCreate\": \"2020-12-07T21:47:24\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": 3800992,\n" +
                "        \"goodsSku\": {\n" +
                "          \"id\": 347,\n" +
                "          \"name\": \"+\",\n" +
                "          \"skuSn\": null,\n" +
                "          \"imageUrl\": \"http://47.52.88.176/file/images/201612/file_58405c09ea4f3.jpg\",\n" +
                "          \"inventory\": 100,\n" +
                "          \"originalPrice\": 4028,\n" +
                "          \"price\": 4028,\n" +
                "          \"disable\":  false\n" +
                "        },\n" +
                "        \"gmtCreate\": \"2020-12-07T21:47:24\"\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, new String(responseString, StandardCharsets.UTF_8), false);
    }

    /**
     * 商品收藏服务-买家查看所有收藏的商品  普通测试3，查询成功 page=1&pageSize=2
     * @throws Exception
     * @author yang8miao
     * @date Created in 2020/12/9 16:18
     */
    @Test
    @Order(0)
    public void getFavorites3() throws Exception {

        // userId = 11026
        String token = this.userLogin("30674268147", "123456");

        byte[] responseString = mallClient.get().uri("/favorites?page=1&pageSize=2").header("authorization",token).exchange()
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
                "    \"total\": 3,\n" +
                "    \"pages\": 2,\n" +
                "    \"list\": [\n" +
                "      {\n" +
                "        \"id\": 3746375,\n" +
                "        \"goodsSku\": {\n" +
                "          \"id\": 573,\n" +
                "          \"name\": \"+\",\n" +
                "          \"skuSn\": null,\n" +
                "          \"imageUrl\": \"http://47.52.88.176/file/images/201807/file_5b3acd8f6d384.png\",\n" +
                "          \"inventory\": 1000,\n" +
                "          \"originalPrice\": 480,\n" +
                "          \"price\": 480,\n" +
                "          \"disable\":  false\n" +
                "        },\n" +
                "        \"gmtCreate\": \"2020-12-07T21:47:23\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": 3779142,\n" +
                "        \"goodsSku\": {\n" +
                "          \"id\": 460,\n" +
                "          \"name\": \"+\",\n" +
                "          \"skuSn\": null,\n" +
                "          \"imageUrl\": \"http://47.52.88.176/file/images/201707/file_59707b5f08a0e.jpg\",\n" +
                "          \"inventory\": 1,\n" +
                "          \"originalPrice\": 15000,\n" +
                "          \"price\": 15000,\n" +
                "          \"disable\":  false\n" +
                "        },\n" +
                "        \"gmtCreate\": \"2020-12-07T21:47:24\"\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, new String(responseString, StandardCharsets.UTF_8), false);
    }


    /**
     * 商品收藏服务-买家查看所有收藏的商品  普通测试4，查询成功 page=1&pageSize=10
     * @throws Exception
     * @author yang8miao
     * @date Created in 2020/12/9 16:18
     */
    @Test
    @Order(0)
    public void getFavorites4() throws Exception {

        // userId = 11026
        String token = this.userLogin("30674268147", "123456");

        byte[] responseString = mallClient.get().uri("/favorites?page=1&pageSize=10").header("authorization",token).exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .returnResult()
                .getResponseBodyContent();

        String expectedResponse = "{\n" +
                "  \"errno\": 0,\n" +
                "  \"data\": {\n" +
                "    \"page\": 1,\n" +
                "    \"pageSize\": 10,\n" +
                "    \"total\": 3,\n" +
                "    \"pages\": 1,\n" +
                "    \"list\": [\n" +
                "      {\n" +
                "        \"id\": 3746375,\n" +
                "        \"goodsSku\": {\n" +
                "          \"id\": 573,\n" +
                "          \"name\": \"+\",\n" +
                "          \"skuSn\": null,\n" +
                "          \"imageUrl\": \"http://47.52.88.176/file/images/201807/file_5b3acd8f6d384.png\",\n" +
                "          \"inventory\": 1000,\n" +
                "          \"originalPrice\": 480,\n" +
                "          \"price\": 480,\n" +
                "          \"disable\":  false\n" +
                "        },\n" +
                "        \"gmtCreate\": \"2020-12-07T21:47:23\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": 3779142,\n" +
                "        \"goodsSku\": {\n" +
                "          \"id\": 460,\n" +
                "          \"name\": \"+\",\n" +
                "          \"skuSn\": null,\n" +
                "          \"imageUrl\": \"http://47.52.88.176/file/images/201707/file_59707b5f08a0e.jpg\",\n" +
                "          \"inventory\": 1,\n" +
                "          \"originalPrice\": 15000,\n" +
                "          \"price\": 15000,\n" +
                "          \"disable\":  false\n" +
                "        },\n" +
                "        \"gmtCreate\": \"2020-12-07T21:47:24\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": 3811909,\n" +
                "        \"goodsSku\": {\n" +
                "          \"id\": 322,\n" +
                "          \"name\": \"+\",\n" +
                "          \"skuSn\": null,\n" +
                "          \"imageUrl\": \"http://47.52.88.176/file/images/201612/file_586652b49d1a7.jpg\",\n" +
                "          \"inventory\": 100,\n" +
                "          \"originalPrice\": 2070,\n" +
                "          \"price\": 2070,\n" +
                "          \"disable\":  false\n" +
                "        },\n" +
                "        \"gmtCreate\": \"2020-12-07T21:47:24\"\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, new String(responseString, StandardCharsets.UTF_8), false);
    }


    /**
     * 商品收藏服务-买家收藏商品  普通测试1，收藏成功
     * @throws Exception
     * @author yang8miao
     * @date Created in 2020/12/9 16:18
     */
    @Test
    @Order(0)
    public void postFavoritesGoodsSpuId1() throws Exception {

        // userId = 11027
        String token = this.userLogin("73559977368", "123456");

        byte[] responseString = mallClient.post().uri("/favorites/goods/371").header("authorization",token).exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .returnResult()
                .getResponseBodyContent();

        String expectedResponse = "{\n" +
                "  \"errno\": 0,\n" +
                "  \"data\": {\n" +
                "      \"goodsSku\": {\n" +
                "        \"id\": 371,\n" +
                "        \"name\": \"+\",\n" +
                "        \"skuSn\": null,\n" +
                "        \"imageUrl\": \"http://47.52.88.176/file/images/201612/file_58406071f1fc7.jpg\",\n" +
                "        \"inventory\": 1,\n" +
                "        \"originalPrice\": 650000,\n" +
                "        \"price\": 650000,\n" +
                "        \"disable\":  false\n" +
                "        }\n" +
                "  }\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, new String(responseString, StandardCharsets.UTF_8), false);

        // 买家查询自身收藏商品，进行验证
        byte[] queryResponseString = mallClient.get().uri("/favorites?page=1&pageSize=10").header("authorization",token).exchange()
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
                "    \"total\": 4,\n" +
                "    \"pages\": 1,\n" +
                "    \"list\": [\n" +
                "      {\n" +
                "        \"id\": 3779143,\n" +
                "        \"goodsSku\": {\n" +
                "          \"id\": 546,\n" +
                "          \"name\": \"+\",\n" +
                "          \"skuSn\": null,\n" +
                "          \"imageUrl\": \"http://47.52.88.176/file/images/201711/file_5a11852d4524d.jpg\",\n" +
                "          \"inventory\": 1000,\n" +
                "          \"originalPrice\": 999,\n" +
                "          \"price\": 999,\n" +
                "          \"disable\":  false\n" +
                "        },\n" +
                "        \"gmtCreate\": \"2020-12-07T21:47:24\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": 3746376,\n" +
                "        \"goodsSku\": {\n" +
                "          \"id\": 476,\n" +
                "          \"name\": \"+\",\n" +
                "          \"skuSn\": null,\n" +
                "          \"imageUrl\": \"http://47.52.88.176/file/images/201707/file_59708e3d5e3b3.jpg\",\n" +
                "          \"inventory\": 1,\n" +
                "          \"originalPrice\": 18000,\n" +
                "          \"price\": 18000,\n" +
                "          \"disable\":  false\n" +
                "        },\n" +
                "        \"gmtCreate\": \"2020-12-07T21:47:23\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": 3811910,\n" +
                "        \"goodsSku\": {\n" +
                "          \"id\": 351,\n" +
                "          \"name\": \"+\",\n" +
                "          \"skuSn\": null,\n" +
                "          \"imageUrl\": \"http://47.52.88.176/file/images/201612/file_58405caec994c.jpg\",\n" +
                "          \"inventory\": 100,\n" +
                "          \"originalPrice\": 2688,\n" +
                "          \"price\": 2688,\n" +
                "          \"disable\":  false\n" +
                "        },\n" +
                "        \"gmtCreate\": \"2020-12-07T21:47:24\"\n" +
                "      },\n" +
                "      {\n" +
                "      \"goodsSku\": {\n" +
                "        \"id\": 371,\n" +
                "        \"name\": \"+\",\n" +
                "        \"skuSn\": null,\n" +
                "        \"imageUrl\": \"http://47.52.88.176/file/images/201612/file_58406071f1fc7.jpg\",\n" +
                "        \"inventory\": 1,\n" +
                "        \"originalPrice\": 650000,\n" +
                "        \"price\": 650000,\n" +
                "        \"disable\":  false\n" +
                "        }\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";
        JSONAssert.assertEquals(queryExpectedResponse, new String(queryResponseString, StandardCharsets.UTF_8), false);
    }

    /**
     * 商品收藏服务-买家收藏商品  普通测试2，收藏成功
     * @throws Exception
     * @author yang8miao
     * @date Created in 2020/12/9 16:18
     */
    @Test
    @Order(0)
    public void postFavoritesGoodsSpuId2() throws Exception {

        // userId = 11028
        String token = this.userLogin("21571184342", "123456");

        byte[] responseString = mallClient.post().uri("/favorites/goods/277").header("authorization",token).exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .returnResult()
                .getResponseBodyContent();

        String expectedResponse = "{\n" +
                "  \"errno\": 0,\n" +
                "  \"data\": {\n" +
                "        \"goodsSku\": {\n" +
                "          \"id\": 277,\n" +
                "          \"name\": \"+\",\n" +
                "          \"skuSn\": null,\n" +
                "          \"imageUrl\": \"http://47.52.88.176/file/images/201612/file_5861c5848ffc4.jpg\",\n" +
                "          \"inventory\": 10,\n" +
                "          \"originalPrice\": 16200,\n" +
                "          \"price\": 16200,\n" +
                "          \"disable\":  false\n" +
                "        }\n" +
                "  }\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, new String(responseString, StandardCharsets.UTF_8), false);


        // 买家查询自身收藏商品，进行验证
        byte[] queryResponseString = mallClient.get().uri("/favorites?page=1&pageSize=10").header("authorization",token).exchange()
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
                "    \"total\": 4,\n" +
                "    \"pages\": 1,\n" +
                "    \"list\": [\n" +
                "      {\n" +
                "        \"id\": 3811911,\n" +
                "        \"goodsSku\": {\n" +
                "          \"id\": 519,\n" +
                "          \"name\": \"+\",\n" +
                "          \"skuSn\": null,\n" +
                "          \"imageUrl\": \"http://47.52.88.176/file/images/201711/file_5a00545f9a1e5.png\",\n" +
                "          \"inventory\": 10000,\n" +
                "          \"originalPrice\": 299,\n" +
                "          \"price\": 299,\n" +
                "          \"disable\":  false\n" +
                "        },\n" +
                "        \"gmtCreate\": \"2020-12-07T21:47:24\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": 3779144,\n" +
                "        \"goodsSku\": {\n" +
                "          \"id\": 668,\n" +
                "          \"name\": \"+\",\n" +
                "          \"skuSn\": null,\n" +
                "          \"imageUrl\": \"http://47.52.88.176/file/images/201903/file_5c789a75c34c7.jpg\",\n" +
                "          \"inventory\": 30,\n" +
                "          \"originalPrice\": 1380,\n" +
                "          \"price\": 1380,\n" +
                "          \"disable\":  false\n" +
                "        },\n" +
                "        \"gmtCreate\": \"2020-12-07T21:47:24\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": 3746377,\n" +
                "        \"goodsSku\": {\n" +
                "          \"id\": 385,\n" +
                "          \"name\": \"+\",\n" +
                "          \"skuSn\": null,\n" +
                "          \"imageUrl\": \"http://47.52.88.176/file/images/201612/file_586664347dae0.jpg\",\n" +
                "          \"inventory\": 996,\n" +
                "          \"originalPrice\": 299,\n" +
                "          \"price\": 299,\n" +
                "          \"disable\":  false\n" +
                "        },\n" +
                "        \"gmtCreate\": \"2020-12-07T21:47:23\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"goodsSku\": {\n" +
                "          \"id\": 277,\n" +
                "          \"name\": \"+\",\n" +
                "          \"skuSn\": null,\n" +
                "          \"imageUrl\": \"http://47.52.88.176/file/images/201612/file_5861c5848ffc4.jpg\",\n" +
                "          \"inventory\": 10,\n" +
                "          \"originalPrice\": 16200,\n" +
                "          \"price\": 16200,\n" +
                "          \"disable\":  false\n" +
                "        }\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";
        JSONAssert.assertEquals(queryExpectedResponse, new String(queryResponseString, StandardCharsets.UTF_8), false);
    }

    /**
     * 商品收藏服务-买家收藏商品  普通测试3，收藏成功
     * @throws Exception
     * @author yang8miao
     * @date Created in 2020/12/9 16:18
     */
    @Test
    @Order(0)
    public void postFavoritesGoodsSpuId3() throws Exception {

        // userId = 11029
        String token = this.userLogin("47812733843", "123456");

        byte[] responseString = mallClient.post().uri("/favorites/goods/447").header("authorization",token).exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .returnResult()
                .getResponseBodyContent();

        String expectedResponse = "{\n" +
                "  \"errno\": 0,\n" +
                "  \"data\": {\n" +
                "        \"goodsSku\": {\n" +
                "          \"id\": 447,\n" +
                "          \"name\": \"+\",\n" +
                "          \"skuSn\": null,\n" +
                "          \"imageUrl\": \"http://47.52.88.176/file/images/201808/file_5b74f0e3a396d.jpg\",\n" +
                "          \"inventory\": 93,\n" +
                "          \"originalPrice\": 669,\n" +
                "          \"price\": 669,\n" +
                "          \"disable\":  false\n" +
                "        }\n" +
                "  }\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, new String(responseString, StandardCharsets.UTF_8), false);

        // 买家查询自身收藏商品，进行验证
        byte[] queryResponseString = mallClient.get().uri("/favorites?page=1&pageSize=10").header("authorization",token).exchange()
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
                "    \"total\": 4,\n" +
                "    \"pages\": 1,\n" +
                "    \"list\": [\n" +
                "      {\n" +
                "        \"id\": 3746378,\n" +
                "        \"goodsSku\": {\n" +
                "          \"id\": 631,\n" +
                "          \"name\": \"+\",\n" +
                "          \"skuSn\": null,\n" +
                "          \"imageUrl\": \"http://47.52.88.176/file/images/201807/file_5b56d79642c94.jpg\",\n" +
                "          \"inventory\": 998,\n" +
                "          \"originalPrice\": 2280,\n" +
                "          \"price\": 2280,\n" +
                "          \"disable\":  false\n" +
                "        },\n" +
                "        \"gmtCreate\": \"2020-12-07T21:47:23\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": 3811912,\n" +
                "        \"goodsSku\": {\n" +
                "          \"id\": 458,\n" +
                "          \"name\": \"+\",\n" +
                "          \"skuSn\": null,\n" +
                "          \"imageUrl\": \"http://47.52.88.176/file/images/201707/file_597028d78c0a3.jpg\",\n" +
                "          \"inventory\": 1,\n" +
                "          \"originalPrice\": 30000,\n" +
                "          \"price\": 30000,\n" +
                "          \"disable\":  false\n" +
                "        },\n" +
                "        \"gmtCreate\": \"2020-12-07T21:47:24\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": 3779145,\n" +
                "        \"goodsSku\": {\n" +
                "          \"id\": 620,\n" +
                "          \"name\": \"+\",\n" +
                "          \"skuSn\": null,\n" +
                "          \"imageUrl\": \"http://47.52.88.176/file/images/201807/file_5b5925a20bbb7.jpg\",\n" +
                "          \"inventory\": 997,\n" +
                "          \"originalPrice\": 1980,\n" +
                "          \"price\": 1980,\n" +
                "          \"disable\":  false\n" +
                "        },\n" +
                "        \"gmtCreate\": \"2020-12-07T21:47:24\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"goodsSku\": {\n" +
                "          \"id\": 447,\n" +
                "          \"name\": \"+\",\n" +
                "          \"skuSn\": null,\n" +
                "          \"imageUrl\": \"http://47.52.88.176/file/images/201808/file_5b74f0e3a396d.jpg\",\n" +
                "          \"inventory\": 93,\n" +
                "          \"originalPrice\": 669,\n" +
                "          \"price\": 669,\n" +
                "          \"disable\":  false\n" +
                "        }\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";
        JSONAssert.assertEquals(queryExpectedResponse, new String(queryResponseString, StandardCharsets.UTF_8), false);
    }

    /**
     * 商品收藏服务-买家收藏商品  普通测试5，收藏成功
     * @throws Exception
     * @author yang8miao
     * @date Created in 2020/12/9 16:18
     */
    @Test
    @Order(0)
    public void postFavoritesGoodsSpuId5() throws Exception {

        // userId = 11031
        String token = this.userLogin("41410343666", "123456");

        byte[] responseString = mallClient.post().uri("/favorites/goods/447").header("authorization",token).exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .returnResult()
                .getResponseBodyContent();

        String expectedResponse = "{\n" +
                "  \"errno\": 0,\n" +
                "  \"data\": {\n" +
                "        \"goodsSku\": {\n" +
                "          \"id\": 447,\n" +
                "          \"name\": \"+\",\n" +
                "          \"skuSn\": null,\n" +
                "          \"imageUrl\": \"http://47.52.88.176/file/images/201808/file_5b74f0e3a396d.jpg\",\n" +
                "          \"inventory\": 93,\n" +
                "          \"originalPrice\": 669,\n" +
                "          \"price\": 669,\n" +
                "          \"disable\":  false\n" +
                "        }\n" +
                "  }\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, new String(responseString, StandardCharsets.UTF_8), false);

        // 买家查询自身收藏商品，进行验证
        byte[] queryResponseString = mallClient.get().uri("/favorites?page=1&pageSize=10").header("authorization",token).exchange()
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
                "    \"total\": 4,\n" +
                "    \"pages\": 1,\n" +
                "    \"list\": [\n" +
                "      {\n" +
                "        \"id\": 3811914,\n" +
                "        \"goodsSku\": {\n" +
                "          \"id\": 646,\n" +
                "          \"name\": \"+\",\n" +
                "          \"skuSn\": null,\n" +
                "          \"imageUrl\": \"http://47.52.88.176/file/images/201807/file_5b57defeb136d.png\",\n" +
                "          \"inventory\": 971,\n" +
                "          \"originalPrice\": 498,\n" +
                "          \"price\": 498,\n" +
                "          \"disable\":  false\n" +
                "        },\n" +
                "        \"gmtCreate\": \"2020-12-07T21:47:24\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": 3779147,\n" +
                "        \"goodsSku\": {\n" +
                "          \"id\": 366,\n" +
                "          \"name\": \"+\",\n" +
                "          \"skuSn\": null,\n" +
                "          \"imageUrl\": \"http://47.52.88.176/file/images/201612/file_58405f8517095.jpg\",\n" +
                "          \"inventory\": 10,\n" +
                "          \"originalPrice\": 3600,\n" +
                "          \"price\": 3600,\n" +
                "          \"disable\":  false\n" +
                "        },\n" +
                "        \"gmtCreate\": \"2020-12-07T21:47:24\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": 3746380,\n" +
                "        \"goodsSku\": {\n" +
                "          \"id\": 383,\n" +
                "          \"name\": \"+\",\n" +
                "          \"skuSn\": null,\n" +
                "          \"imageUrl\": \"http://47.52.88.176/file/images/201612/file_5840622c2337f.jpg\",\n" +
                "          \"inventory\": 1,\n" +
                "          \"originalPrice\": 42000,\n" +
                "          \"price\": 42000,\n" +
                "          \"disable\":  false\n" +
                "        },\n" +
                "        \"gmtCreate\": \"2020-12-07T21:47:23\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"goodsSku\": {\n" +
                "          \"id\": 447,\n" +
                "          \"name\": \"+\",\n" +
                "          \"skuSn\": null,\n" +
                "          \"imageUrl\": \"http://47.52.88.176/file/images/201808/file_5b74f0e3a396d.jpg\",\n" +
                "          \"inventory\": 93,\n" +
                "          \"originalPrice\": 669,\n" +
                "          \"price\": 669,\n" +
                "          \"disable\":  false\n" +
                "        }\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";
        JSONAssert.assertEquals(queryExpectedResponse, new String(queryResponseString, StandardCharsets.UTF_8), false);
    }

    /**
     * 商品收藏服务-买家删除某个收藏的商品  普通测试1，连续删除3件商品，删除收藏成功
     * @throws Exception
     * @author yang8miao
     * @date Created in 2020/12/9 16:18
     */
    @Test
    @Order(0)
    public void deleteFavoritesId1() throws Exception {

        // userId = 11032
        String token = this.userLogin("58084752837", "123456");

        byte[] responseString = mallClient.delete().uri("/favorites/3746381").header("authorization",token).exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .returnResult()
                .getResponseBodyContent();

        responseString = mallClient.delete().uri("/favorites/3811915").header("authorization",token).exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .returnResult()
                .getResponseBodyContent();

        responseString = mallClient.delete().uri("/favorites/3779148").header("authorization",token).exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .returnResult()
                .getResponseBodyContent();

        // 买家查询自身收藏商品，进行验证
        byte[] queryResponseString = mallClient.get().uri("/favorites?page=1&pageSize=10").header("authorization",token).exchange()
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
     * 商品收藏服务-买家删除某个收藏的商品  普通测试2，删除收藏成功
     * @throws Exception
     * @author yang8miao
     * @date Created in 2020/12/9 16:18
     */
    @Test
    @Order(0)
    public void deleteFavoritesId2() throws Exception {

        // userId = 11033
        String token = this.userLogin("30917238566", "123456");

        byte[] responseString = mallClient.delete().uri("/favorites/3779149").header("authorization",token).exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .returnResult()
                .getResponseBodyContent();

        // 买家查询自身收藏商品，进行验证
        byte[] queryResponseString = mallClient.get().uri("/favorites?page=1&pageSize=10").header("authorization",token).exchange()
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
                "        \"id\": 3746382,\n" +
                "        \"goodsSku\": {\n" +
                "          \"id\": 362,\n" +
                "          \"name\": \"+\",\n" +
                "          \"skuSn\": null,\n" +
                "          \"imageUrl\": \"http://47.52.88.176/file/images/201612/file_58405eb2c2268.jpg\",\n" +
                "          \"inventory\": 10,\n" +
                "          \"originalPrice\": 17800,\n" +
                "          \"price\": 17800,\n" +
                "          \"disable\":  false\n" +
                "        },\n" +
                "        \"gmtCreate\": \"2020-12-07T21:47:23\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": 3811916,\n" +
                "        \"goodsSku\": {\n" +
                "          \"id\": 431,\n" +
                "          \"name\": \"+\",\n" +
                "          \"skuSn\": null,\n" +
                "          \"imageUrl\": \"http://47.52.88.176/file/images/201707/file_5967a77908428.jpg\",\n" +
                "          \"inventory\": 9831,\n" +
                "          \"originalPrice\": 299,\n" +
                "          \"price\": 299,\n" +
                "          \"disable\":  false\n" +
                "        },\n" +
                "        \"gmtCreate\": \"2020-12-07T21:47:24\"\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";
        JSONAssert.assertEquals(queryExpectedResponse, new String(queryResponseString, StandardCharsets.UTF_8), false);

    }

    /**
     * 商品收藏服务-买家删除某个收藏的商品  普通测试3，删除收藏失败，该用户未收藏该商品
     * @throws Exception
     * @author yang8miao
     * @date Created in 2020/12/9 16:18
     */
    @Test
    @Order(0)
    public void deleteFavoritesId3() throws Exception {

        // userId = 14712
        String token = this.userLogin("45209106845", "123456");

        byte[] responseString = mallClient.delete().uri("/favorites/3782810").header("authorization",token).exchange()
                .expectStatus().isForbidden()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.RESOURCE_ID_OUTSCOPE.getCode())
                .returnResult()
                .getResponseBodyContent();
    }

    /**
     * 商品收藏服务-买家删除某个收藏的商品  普通测试4，删除收藏失败，该收藏id不存在
     * @throws Exception
     * @author yang8miao
     * @date Created in 2020/12/9 16:18
     */
    @Test
    @Order(0)
    public void deleteFavoritesId4() throws Exception {

        // userId = 14712
        String token = this.userLogin("45209106845", "123456");

        byte[] responseString = mallClient.delete().uri("/favorites/37").header("authorization",token).exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.RESOURCE_ID_NOTEXIST.getCode())
                .returnResult()
                .getResponseBodyContent();
    }

    /**
     * 商品收藏服务-买家删除某个收藏的商品  普通测试5，删除收藏失败，该收藏id不存在
     * @throws Exception
     * @author yang8miao
     * @date Created in 2020/12/9 16:18
     */
    @Test
    @Order(0)
    public void deleteFavoritesId5() throws Exception {

        // userId = 14712
        String token = this.userLogin("45209106845", "123456");

        byte[] responseString = mallClient.delete().uri("/favorites/3227").header("authorization",token).exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.RESOURCE_ID_NOTEXIST.getCode())
                .returnResult()
                .getResponseBodyContent();
    }

    /**
     * 商品收藏服务-买家删除某个收藏的商品  普通测试6，删除收藏失败，该收藏id不存在
     * @throws Exception
     * @author yang8miao
     * @date Created in 2020/12/9 16:18
     */
    @Test
    @Order(0)
    public void deleteFavoritesId6() throws Exception {

        // userId = 14712
        String token = this.userLogin("45209106845", "123456");

        byte[] responseString = mallClient.delete().uri("/favorites/233").header("authorization",token).exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.RESOURCE_ID_NOTEXIST.getCode())
                .returnResult()
                .getResponseBodyContent();
    }

    /**
     * 商品收藏服务-买家删除某个收藏的商品  普通测试7，删除收藏失败，该用户未收藏该商品
     * @throws Exception
     * @author yang8miao
     * @date Created in 2020/12/9 16:18
     */
    @Test
    @Order(0)
    public void deleteFavoritesId7() throws Exception {

        // userId = 14712
        String token = this.userLogin("45209106845", "123456");

        byte[] responseString = mallClient.delete().uri("/favorites/3782808").header("authorization",token).exchange()
                .expectStatus().isForbidden()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.RESOURCE_ID_OUTSCOPE.getCode())
                .returnResult()
                .getResponseBodyContent();
    }


}
