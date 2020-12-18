package cn.edu.xmu.footprint.publicTest;

import cn.edu.xmu.footprint.FootprintserviceApplication;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;
import cn.edu.xmu.ooad.util.ResponseCode;

import java.nio.charset.StandardCharsets;

@SpringBootTest(classes = FootprintserviceApplication.class)   //标识本类是一个SpringBootTest
@Slf4j
public class test {

////    @Value("${public-test.managementgate}")
//    private String managementGate;
//
////    @Value("${public-test.mallgate}")
//    private String mallGate;

    private WebTestClient manageClient;

    private WebTestClient mallClient;

    @BeforeEach
    public void setUp(){

        this.mallClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:8090")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8")
                .build();

        this.manageClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:8080")
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
        byte[] responseString = mallClient.post().uri("/users/login").bodyValue(requireJson).exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.OK.getMessage())
                .returnResult()
                .getResponseBodyContent();
        return JSONObject.parseObject(new String(responseString, StandardCharsets.UTF_8)).getString("data");
    }



    /**
     * 足迹服务-管理员查看浏览记录  普通测试1，查询成功
     * @throws Exception
     * @author yang8miao
     * @date Created in 2020/12/9 15:28
     */
    @Test
    @Order(0)
    public void getFootprints1() throws Exception {

        String token = this.userLogin("8606245097", "123456");

        byte[] responseString = manageClient.get().uri("/shops/0/footprints?userId=220&page=1&pageSize=10").header("authorization",token).exchange()
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
                "    \"total\": 1,\n" +
                "    \"pages\": 1,\n" +
                "    \"list\": [\n" +
                "      {\n" +
                "        \"id\": 1212599,\n" +
                "        \"goodsSku\": {\n" +
                "          \"id\": 291,\n" +
                "          \"name\": \"+\",\n" +
                "          \"skuSn\": null,\n" +
                "          \"imageUrl\": \"http://47.52.88.176/file/images/201612/file_586227f3cd5c9.jpg\",\n" +
                "          \"inventory\": 1,\n" +
                "          \"originalPrice\": 130000,\n" +
                "          \"price\": 130000,\n" +
                "          \"disable\": 0\n" +
                "        },\n" +
                "        \"gmtCreate\": \"2020-12-07T21:47:22\"\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, new String(responseString, StandardCharsets.UTF_8), false);
    }


    /**
     * 足迹服务-管理员查看浏览记录  普通测试2，查询成功
     * @throws Exception
     * @author yang8miao
     * @date Created in 2020/12/9 15:28
     */
    @Test
    @Order(0)
    public void getFootprints2() throws Exception {

        String token = this.userLogin("8606245097", "123456");

        byte[] responseString = manageClient.get().uri("/shops/0/footprints?userId=134&page=1&pageSize=1").header("authorization",token).exchange()
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
                "    \"total\": 1,\n" +
                "    \"pages\": 1,\n" +
                "    \"list\": [\n" +
                "      {\n" +
                "        \"id\": 1212513,\n" +
                "        \"goodsSku\": {\n" +
                "          \"id\": 537,\n" +
                "          \"name\": \"+\",\n" +
                "          \"skuSn\": null,\n" +
                "          \"imageUrl\": \"http://47.52.88.176/file/images/201711/file_5a10e5d95d038.jpg\",\n" +
                "          \"inventory\": 1000,\n" +
                "          \"originalPrice\": 699,\n" +
                "          \"price\": 699,\n" +
                "          \"disable\": 0\n" +
                "        },\n" +
                "        \"gmtCreate\": \"2020-12-07T21:47:22\"\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, new String(responseString, StandardCharsets.UTF_8), false);
    }


    /**
     * 足迹服务-管理员查看浏览记录 普通测试，查询成功，但未查到任何足迹
     * @throws Exception
     * @author yang8miao
     * @date Created in 2020/12/9 15:28
     */
    @Test
    @Order(0)
    public void getFootprints3() throws Exception {

        String token = this.userLogin("8606245097", "123456");

        byte[] responseString = manageClient.get().uri("/shops/0/footprints?userId=17320&endTime=2019-11-11 12:00:00&page=10&pageSize=10").header("authorization",token).exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .returnResult()
                .getResponseBodyContent();

        String expectedResponse = "{\n" +
                "  \"errno\": 0,\n" +
                "  \"data\": {\n" +
                "    \"page\": 10,\n" +
                "    \"pageSize\": 10,\n" +
                "    \"total\": 0,\n" +
                "    \"pages\": 0,\n" +
                "    \"list\": [\n" +
                "    ]\n" +
                "  }\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, new String(responseString, StandardCharsets.UTF_8), false);
    }


    /**
     * 足迹服务-管理员查看浏览记录 普通测试，查询成功，但未查到任何足迹
     * @throws Exception
     * @author yang8miao
     * @date Created in 2020/12/9 15:28
     */
    @Test
    @Order(0)
    public void getFootprints4() throws Exception {

        String token = this.userLogin("8606245097", "123456");

        byte[] responseString = manageClient.get().uri("/shops/0/footprints?userId=17320&beginTime=2022-11-24 12:00:00&page=10&pageSize=10").header("authorization",token).exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .returnResult()
                .getResponseBodyContent();

        String expectedResponse = "{\n" +
                "  \"errno\": 0,\n" +
                "  \"data\": {\n" +
                "    \"page\": 10,\n" +
                "    \"pageSize\": 10,\n" +
                "    \"total\": 0,\n" +
                "    \"pages\": 0,\n" +
                "    \"list\": [\n" +
                "    ]\n" +
                "  }\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, new String(responseString, StandardCharsets.UTF_8), false);
    }

    /**
     * 足迹服务-管理员查看浏览记录 普通测试，开始时间大于结束时间,返回错误码
     * @throws Exception
     * @author yang8miao
     * @date Created in 2020/12/9 15:28
     */
    @Test
    @Order(0)
    public void getFootprints5() throws Exception {

        String token = this.userLogin("8606245097", "123456");

        byte[] responseString = manageClient.get().uri("/shops/0/footprints?beginTime=2022-11-24 12:00:00&endTime=2020-11-11 12:00:00")
                .header("authorization",token).exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.Log_Bigger.getCode())
                .returnResult()
                .getResponseBodyContent();

    }

    /**
     * 足迹服务-管理员查看浏览记录 普通测试，开始时间大于结束时间,返回错误码
     * @throws Exception
     * @author yang8miao
     * @date Created in 2020/12/9 15:28
     */
    @Test
    @Order(0)
    public void getFootprints6() throws Exception {

        String token = this.userLogin("8606245097", "123456");

        byte[] responseString = manageClient.get().uri("/shops/0/footprints?userId=233&beginTime=2022-11-23 12:00:00&endTime=2020-11-11 12:00:00")
                .header("authorization",token).exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.Log_Bigger.getCode())
                .returnResult()
                .getResponseBodyContent();

    }


}
