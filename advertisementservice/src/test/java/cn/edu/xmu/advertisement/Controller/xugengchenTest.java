package cn.edu.xmu.advertisement.Controller;


import cn.edu.xmu.advertisement.AdvertisementserviceApplication;
import cn.edu.xmu.ooad.util.JacksonUtil;
import cn.edu.xmu.ooad.util.ResponseCode;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.nio.charset.StandardCharsets;

/**
 * 其他模块测试-广告、时间段服务
 * @author  24320182203305 徐庚辰
 * @date 2020/12/14 10:19
 */
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(classes = AdvertisementserviceApplication.class)
public class xugengchenTest {

    private WebTestClient manageClient;

    private WebTestClient mallClient;
    private WebTestClient temp;


    @BeforeEach
    public void setUp() {
        this.manageClient = WebTestClient.bindToServer()
                .baseUrl("http://127.0.0.1:8084")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8")
                .build();

        this.mallClient = WebTestClient.bindToServer()
                .baseUrl("http://127.0.0.1:8084")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8")
                .build();

        this.temp = WebTestClient.bindToServer()
                .baseUrl("http://114.215.198.238:4522")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8")
                .build();
    }
    /**
     * 管理员登录，获取token
     *
     * @param userName
     * @param password
     * @return token
     */
    private String adminlogin(String userName, String password) throws Exception{

        JSONObject body = new JSONObject();
        body.put("userName", userName);
        body.put("password", password);
        String requireJson = body.toJSONString();
        byte[] responseString = temp.post().uri("/adminusers/login").bodyValue(requireJson).exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.OK.getMessage())
                .returnResult()
                .getResponseBodyContent();
        //return JSONObject.parseObject(new String(responseString, StandardCharsets.UTF_8)).getString("data");

        return "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0aGlzIGlzIGEgdG9rZW4iLCJhdWQiOiJNSU5JQVBQIiwidG9rZW5JZCI6IjIwMjAxMjIwMTkyMzE2MlJMIiwiaXNzIjoiT09BRCIsImRlcGFydElkIjotMiwiZXhwIjoxNjA4NDY2OTk2LCJ1c2VySWQiOjEsImlhdCI6MTYwODQ2MzM5Nn0.o9DRY9s8GQe6yRSX4JT8mYP8PhQfN9QuGL8GwLs0tkI";

    }

    /**
     * 买家登录，获取token
     *
     * @param userName
     * @param password
     * @return token
     */
    private String userLogin(String userName, String password) throws Exception{

        JSONObject body = new JSONObject();
        body.put("userName", userName);
        body.put("password", password);
        String requireJson = body.toJSONString();
        byte[] responseString = mallClient.post().uri("/users/login").bodyValue(requireJson).exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.OK.getMessage())
                .returnResult()
                .getResponseBodyContent();

        return JSONObject.parseObject(new String(responseString, StandardCharsets.UTF_8)).getString("data");
    }




    /**
     * 获得广告的所有状态
     *@throws Exception
     * @author 徐庚辰
     **/
    @Order(7)
    @Test
    public void getAdStateTest1() throws Exception {
        String admintoken = this.adminlogin("13088admin", "123456");
        byte[] responseString = manageClient.get().uri("/advertisement/states")
                .header("authorization", admintoken)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .returnResult().getResponseBodyContent();
        String expectedResponse = "{\"errno\":0,\"errmsg\":\"成功\"," +
                "\"data\":" +
                "[{\"code\":0,\"name\":\"待审核\"}," +
                "{\"code\":4,\"name\":\"上架\"}," +
                "{\"code\":6,\"name\":\"下架\"}]}";
        JSONAssert.assertEquals(expectedResponse, new String(responseString, "UTF-8"), false);
    }

    /**
     * 获得广告的所有状态-管理员未登录
     *@throws Exception
     * @author 徐庚辰
     **/
    @Order(8)
    @Test
    public void getAdStateTest2() throws Exception {
        byte[] responseString = manageClient.get().uri("/advertisement/states")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .returnResult().getResponseBodyContent();
        String expectedResponse = "{\"errno\":0,\"errmsg\":\"成功\"," +
                "\"data\":" +
                "[{\"code\":0,\"name\":\"待审核\"}," +
                "{\"code\":4,\"name\":\"上架\"}," +
                "{\"code\":6,\"name\":\"下架\"}]}";
        JSONAssert.assertEquals(expectedResponse, new String(responseString, "UTF-8"), false);
    }

    /**
     * 管理员查看某一个时间段的广告,输入均合法，没有符合的条件,返回值为空
     *@throws Exception
     * @author 徐庚辰
     */
    @Order(9)
    @Test
    public void GetAdTest4() throws Exception {
        String admintoken = this.adminlogin("13088admin", "123456");
        byte[] responseString2 = manageClient.get().uri("/shops/0/timesegments/4/advertisement?endDate=1990-10-11")
                .header("authorization", admintoken)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .returnResult()
                .getResponseBodyContent();
    }

    /**
     * 管理员查看某一个时间段的广告
     *@throws Exception
     * @author 徐庚辰
     */
    @Order(10)
    @Test
    public void GetAdTest1() throws Exception {
        String admintoken = this.adminlogin("13088admin", "123456");
        byte[] responseString2 = manageClient.get().uri("/shops/0/timesegments/4/advertisement")
                .header("authorization", admintoken)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .returnResult().getResponseBodyContent();
        log.info(new String(responseString2,"UTF-8"));
        String expectedResponse2 = "{\"errno\":0,\"data\":{\"total\":2,\"pages\":1,\"pageSize\":10,\"page\":1," +
                "\"list\":[" +
                "{\"id\":124,\"link\":null,\"imagePath\":\"http://47.52.88.176/file/images/201610/1475992167803037996.jpg\"," +
                "\"content\":null,\"segId\":4,\"state\":4,\"weight\":\"1\",\"beDefault\":false,\"beginDate\":\"2020-12-15\"," +
                "\"endDate\":\"2021-10-10\",\"repeat\":true}," +
                "{\"id\":434,\"link\":null,\"imagePath\":\"http://47.52.88.176/file/images/201707/1500430319599204956.jpg\"," +
                "\"content\":null,\"segId\":4,\"state\":4,\"weight\":\"4\",\"beDefault\":false,\"beginDate\":\"2020-12-15\"," +
                "\"endDate\":\"2021-10-10\",\"repeat\":true}]}}";
        JSONAssert.assertEquals(expectedResponse2, new String(responseString2, "UTF-8"), false);
    }

    /**
     * 管理员查看某一个时间段的广告-未登录
     *@throws Exception
     * @author 徐庚辰
     */
    @Order(11)
    @Test
    public void GetAdTest5() throws Exception {
        byte[] responseString2 = manageClient.get().uri("/shops/0/timesegments/4/advertisement")
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.AUTH_NEED_LOGIN.getCode())
                .returnResult()
                .getResponseBodyContent();
    }

    /**
     * 管理员查看某一个时间段的广告-时间段不存在
     *@throws Exception
     * @author 徐庚辰
     */
    @Order(12)
    @Test
    public void GetAdTest2() throws Exception {
        String admintoken = this.adminlogin("13088admin", "123456");
        byte[] responseString2 = manageClient.get().uri("/shops/0/timesegments/87264/advertisement")
                .header("authorization", admintoken)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.RESOURCE_ID_NOTEXIST.getCode())
                .returnResult()
                .getResponseBodyContent();
    }

    /**
     * 管理员设置默认广告
     *@throws Exception
     * @author 徐庚辰
     */
    @Order(13)
    @Test
    public void modifyAdTest3() throws Exception {
        String admintoken = this.adminlogin("13088admin", "123456");
        byte[] responseString1 = manageClient.put().uri("/shops/0/advertisement/125/default")
                .header("authorization", admintoken)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .returnResult()
                .getResponseBodyContent();

        byte[] responseString2 = manageClient.get().uri("/shops/0/timesegments/5/advertisement")
                .header("authorization", admintoken)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .returnResult().getResponseBodyContent();
        log.info(new String(responseString2,"UTF-8"));
        String expectedResponse2 = "{\"errno\":0,\"errmsg\":\"成功\"," +
                "\"data\":" +
                "{\"page\":1," +
                "\"pageSize\":10," +
                "\"total\":2," +
                "\"pages\":1," +
                "\"list\":" +
                "[{\"id\":125," +
                "\"link\":null,"+
                "\"imagePath\":\"http://47.52.88.176/file/images/201610/1476498085799000890.jpg\","+
                "\"content\":null,"+
                "\"segId\":5,"+
                "\"state\":4,"+
                "\"weight\":\"1\","+
                "\"beDefault\":true,"+
                "\"beginDate\":\"2020-12-15\","+
                "\"endDate\":\"2021-10-10\","+
                "\"repeat\":true}," +
                "{\"id\":435," +
                "\"link\":null,"+
                "\"imagePath\":\"http://47.52.88.176/file/images/201707/1500428162371550836.jpg\","+
                "\"content\":null,"+
                "\"segId\":5,"+
                "\"state\":0,"+
                "\"weight\":\"5\","+
                "\"beDefault\":false,"+
                "\"beginDate\":\"2020-12-15\","+
                "\"endDate\":\"2021-10-10\","+
                "\"repeat\":true}]}}";
        JSONAssert.assertEquals(expectedResponse2, new String(responseString2, "UTF-8"), false);
    }


    /**
     * 管理员设置默认广告-管理员密码错误，登录失败
     *@throws Exception
     * @author 徐庚辰
     */
    @Order(14)
    @Test
    public void modifyAdTest4() throws Exception {
        byte[] responseString1 = manageClient.put().uri("/shops/0/advertisement/122/default")
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody()
                .returnResult()
                .getResponseBodyContent();
    }

    /**
     * 管理员设置默认广告-不存在该广告
     *@throws Exception
     * @author 徐庚辰
     */
    @Order(15)
    @Test
    public void modifyAdTest5() throws Exception {
        String admintoken = this.adminlogin("13088admin", "123456");
        byte[] responseString1 = manageClient.put().uri("/shops/0/advertisement/872642/default")
                .header("authorization", admintoken)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .returnResult()
                .getResponseBodyContent();
    }

    /**
     * 管理员删除某一个广告-管理员用户名密码错误
     *@throws Exception
     * @author 徐庚辰
     */
    @Order(16)
    @Test
    public void deleteTimeTest2() throws Exception {
        byte[] responseString1 = manageClient.delete().uri("/shops/0/advertisement/147")
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody()
                .returnResult()
                .getResponseBodyContent();
    }

    /**
     * 管理员删除某一个广告
     *@throws Exception
     * @author 徐庚辰
     */
    @Order(18)
    @Test
    public void deleteTimeTest1() throws Exception {
        String admintoken = this.adminlogin("13088admin", "123456");
        byte[] responseString1 = manageClient.delete().uri("/shops/0/advertisement/147")
                .header("authorization", admintoken)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .returnResult()
                .getResponseBodyContent();

    }


    /**
     * 管理员删除某一个广告-广告不存在
     *@throws Exception
     * @author 徐庚辰
     */
    @Order(19)
    @Test
    public void deleteTimeTest4() throws Exception {
        String admintoken = this.adminlogin("13088admin", "123456");
        byte[] responseString1 = manageClient.delete().uri("/shops/0/advertisement/23421")
                .header("authorization", admintoken)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .returnResult()
                .getResponseBodyContent();
    }

    /**
     * 管理员删除某一个广告-未登录
     *@throws Exception
     * @author 徐庚辰
     */
    @Order(20)
    @Test
    public void deleteTimeTest5() throws Exception {
        byte[] responseString1 = manageClient.delete().uri("/shops/0/advertisement/147")
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody()
                .returnResult()
                .getResponseBodyContent();
    }

    /**
     * 管理员修改广告内容-未登录
     *@throws Exception
     * @author 徐庚辰
     **/
    @Order(21)
    @Test
    public void modifyAuthUserTest10() throws Exception {
        String roleJson = "{\"content\": \"加油\",\"beginDate\": \"2019-12-14\",\"endDate\": \"2021-10-10\",\"weight\": \"1\",\"repeat\": true,\"link\": \"\"}";
        byte[] responseString1 = manageClient.put().uri("/shops/0/advertisement/123")
                .bodyValue(roleJson)
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody()
                .returnResult()
                .getResponseBodyContent();
    }

    /**
     * 管理员设置默认广告-管理员密码错误，登录失败
     *@throws Exception
     * @author 徐庚辰
     */
    @Order(22)
    @Test
    public void modifyAdTest04() throws Exception {
        byte[] responseString1 = manageClient.put().uri("/shops/0/advertisement/124/default")
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody()
                .returnResult()
                .getResponseBodyContent();
    }

    /**
     * 管理员设置默认广告-不存在该广告
     *@throws Exception
     * @author 徐庚辰
     */
    @Order(23)
    @Test
    public void modifyAdTest05() throws Exception {
        String admintoken = this.adminlogin("13088admin", "123456");
        byte[] responseString1 = manageClient.put().uri("/shops/0/advertisement/124357/default")
                .header("authorization", admintoken)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .returnResult()
                .getResponseBodyContent();
    }


    /**
     * 管理员修改广告内容-未登录
     *@throws Exception
     * @author 徐庚辰
     **/
    @Order(24)
    @Test
    public void modifyAuthUserTest01() throws Exception {
        String roleJson = "{\"content\": \"加油\",\"beginDate\": \"2020-12-15\",\"endDate\": \"2021-10-10\",\"weight\": \"1\",\"repeat\": true,\"link\": \"\"}";
        byte[] responseString1 = manageClient.put().uri("/shops/0/advertisement/123")
                .bodyValue(roleJson)
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody()
                .returnResult()
                .getResponseBodyContent();
    }


    /**
     * 管理员查看某一个时间段的广告-未登录
     *@throws Exception
     * @author 徐庚辰
     */
    @Order(25)
    @Test
    public void GetAdTest05() throws Exception {
        byte[] responseString2 = manageClient.get().uri("/shops/0/timesegments/4/advertisement")
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody()
                .returnResult()
                .getResponseBodyContent();
    }

    /**
     * 管理员查看某一个时间段的广告,输入均合法，没有符合的条件,返回值为空
     *@throws Exception
     * @author 徐庚辰
     */
    @Order(26)
    @Test
    public void GetAdTest04() throws Exception {
        String admintoken = this.adminlogin("13088admin", "123456");
        byte[] responseString2 = manageClient.get().uri("/shops/0/timesegments/4/advertisement?beginDate=2180-12-13")
                .header("authorization", admintoken)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .returnResult()
                .getResponseBodyContent();
    }

    /**
     * 管理员查看某一个时间段的广告,输入均合法，没有符合的条件,返回值为空
     *@throws Exception
     * @author 徐庚辰
     */
    @Order(27)
    @Test
    public void GetAdTest8() throws Exception {
        String admintoken = this.adminlogin("13088admin", "123456");
        byte[] responseString2 = manageClient.get().uri("/shops/0/timesegments/4/advertisement?endDate=1879-12-31")
                .header("authorization", admintoken)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .returnResult()
                .getResponseBodyContent();
    }



    /**
     * 平台管理员删除广告时间段-时间段不存在
     *@throws Exception
     * @author 徐庚辰
     */
    @Order(28)
    @Test
    public void modifyTimeTest02() throws Exception {
        String admintoken = this.adminlogin("13088admin", "123456");
        byte[] responseString1 = manageClient.delete().uri("/shops/0/advertisement/timesegments/456742")
                .header("authorization", admintoken)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .returnResult()
                .getResponseBodyContent();
    }



}
