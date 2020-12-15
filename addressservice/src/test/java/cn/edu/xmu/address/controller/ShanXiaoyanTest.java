package cn.edu.xmu.address.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import cn.edu.xmu.ooad.Application;
import cn.edu.xmu.ooad.util.JacksonUtil;
import cn.edu.xmu.ooad.util.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.nio.charset.StandardCharsets;

import com.alibaba.fastjson.JSONObject;

@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
@Slf4j
@SpringBootTest(classes = Application.class)
public class ShanXiaoyanTest {
    //private String token ="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0aGlzIGlzIGEgdG9rZW4iLCJhdWQiOiJNSU5JQVBQIiwidG9rZW5JZCI6IjIwMjAxMjE1MTAzNDA4NVVVIiwiaXNzIjoiT09BRCIsImRlcGFydElkIjoxNjA2MzUzNDc2NDkwLCJleHAiOjE2MDgwMDMyNDgsInVzZXJJZCI6MSwiaWF0IjoxNjA3OTk5NjQ4fQ.H45UXFZf9QnLhFBg6An5Kaj6TQ-C_7J2E0aW1rJIfDE";

    @Value("${public-test.managementgate}")
    private String managementGate;

    @Value("${public-test.mallgate}")
    private String mallGate;

    private WebTestClient manageClient;

    private WebTestClient mallClient;

    @BeforeEach
    public void setUp(){
        this.manageClient = WebTestClient.bindToServer()
                .baseUrl("http://"+managementGate)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8")
                .build();

        this.mallClient = WebTestClient.bindToServer()
                .baseUrl("http://"+mallGate)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8")
                .build();

    }

    /**
     * 买家登录，获取token
     *
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
     * 管理员登录，获取token
     *
     * @param userName
     * @param password
     * @return token
     * createdBy yang8miao 2020/12/12 19:48
     * modifiedBy yang8miao 2020/12/12 19:48
     */
    private String adminLogin(String userName, String password) throws Exception{

        JSONObject body = new JSONObject();
        body.put("userName", userName);
        body.put("password", password);
        String requireJson = body.toJSONString();
        byte[] responseString = manageClient.post().uri("/privileges/login").bodyValue(requireJson).exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.OK.getMessage())
                .returnResult()
                .getResponseBodyContent();
        return JSONObject.parseObject(new String(responseString, StandardCharsets.UTF_8)).getString("data");
    }

    /**
     * addAddress1
     * 新增地址，参数错误，手机号码为空
     * @throws Exception
     */
    @Test
    public void addAddress1() throws Exception {
        String token = this.userLogin("8606245097", "123456");
        String requireJson = "{\n" +
                " \"regionId\": 1,\n" +
                " \"detail\":  \"测试地址1\",\n" +
                " \"consignee\":  \"测试\",\n" +
                " \"mobile\":  \"1232323\"\n" +
                "}";

        byte[] responseString = mallClient.post().uri("localhost:8080/addresses", 1)
                .header("authorization", token)
                .bodyValue(requireJson)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.FIELD_NOTVALID.getCode())
                .returnResult()
                .getResponseBodyContent();
    }
    /**addAddress2
     * 新增地址，参数错误，收件人为空
     * @throws Exception
     */
    @Test
    public void addAddress2() throws Exception {
        String token = this.userLogin("8606245097", "123456");
        String requireJson = "{\n" +
                " \"regionId\": 1,\n" +
                " \"detail\":  \"测试地址1\",\n" +
                " \"consignee\":  \"\",\n" +
                " \"mobile\":  \"18990897878\"\n" +
                "}";

        byte[] responseString = mallClient.post().uri("localhost:8080/addresses")
                .header("authorization", token)
                .bodyValue(requireJson)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.FIELD_NOTVALID.getCode())
                .returnResult()
                .getResponseBodyContent();
    }
    /**
     * addAddress3
     * 新增地址，参数错误，详情为空
     * @throws Exception
     */

    @Test
    public void addAddress3() throws Exception {
        String token = this.userLogin("8606245097", "123456");
        String requireJson = "{\n" +
                " \"regionId\": 1,\n" +
                " \"detail\":  \"\",\n" +
                " \"consignee\":  \"测试\",\n" +
                " \"mobile\":  \"18990897878\"\n" +
                "}";

        byte[] responseString = mallClient.post().uri("localhost:8080/addresses", 1)
                .header("authorization", token)
                .bodyValue(requireJson)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.FIELD_NOTVALID.getCode())
                .returnResult()
                .getResponseBodyContent();
    }
    /**
     * addAddress4
     * 新增地址，参数错误，地区id为空
     * @throws Exception
     */


    @Test
    public void addAddress4() throws Exception {
        String token = this.userLogin("8606245097", "123456");
        String requireJson = "{\n" +
                " \"regionId\": null,\n" +
                " \"detail\":  \"测试地址1\",\n" +
                " \"consignee\":  \"测试\",\n" +
                " \"mobile\":  \"18990897878\" \n" +
                "}";

        byte[] responseString = mallClient.post().uri("localhost:8080/addresses")
                .header("authorization", token)
                .bodyValue(requireJson)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.FIELD_NOTVALID.getCode())
                .returnResult()
                .getResponseBodyContent();
    }


    /**
     * addAddress5
     * 新增地址，地区已废弃
     * 先废弃地区再测试添加地址
     * @throws Exception
     */

    @Test
    public void addAddress5() throws Exception{


        String token = this.userLogin("8606245097", "123456");

        byte[] responseString1 = mallClient.delete().uri("localhost:8080/regions/101")
                .header("authorization", token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .returnResult()
                .getResponseBodyContent();


        String requireJson="{\n"+
                " \"regionId\": 101,\n"+
                " \"detail\":  \"测试地址1\",\n" +
                " \"consignee\":  \"测试\",\n" +
                " \"mobile\":  \"18990897878\"\n"+
                "}";

        byte[] responseString = mallClient.post().uri("localhost:8080/addresses")
                .header("authorization", token)
                .bodyValue(requireJson)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.REGION_OBSOLETE.getCode())
                .returnResult()
                .getResponseBodyContent();



    }
    /**
     * addAddress6
     * 新增地址，地区不存在
     * @throws Exception
     */
    @Test
    public void addAddress6() throws Exception{

        String token = this.userLogin("8606245097", "123456");
        String requireJson="{\n"+
                " \"regionId\": -1,\n"+
                " \"detail\":  \"测试地址1\",\n" +
                " \"consignee\":  \"测试\",\n" +
                " \"mobile\":  \"18990897878\"\n"+
                "}";

        byte[] responseString = mallClient.post().uri("localhost:8080/addresses")
                .header("authorization", token)
                .bodyValue(requireJson)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.RESOURCE_ID_NOTEXIST.getCode())
                .returnResult()
                .getResponseBodyContent();



    }
    /**
     * addAddress7
     * 新增地址
     * @throws Exception
     */
    @Test
    public void addAddress7() throws Exception{

        String token = this.userLogin("8606245097", "123456");
        String requireJson="{\n"+
                " \"regionId\": 1,\n"+
                " \"detail\":  \"测试地址1\",\n" +
                " \"consignee\":  \"测试\",\n" +
                " \"mobile\":  \"18990897878\"\n"+
                "}";

        byte[] responseString = mallClient.post().uri("localhost:8080/addresses")
                .header("authorization", token)
                .bodyValue(requireJson)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .returnResult()
                .getResponseBodyContent();

        String expectedResponse= "{\n" +
                "    \"errno\": 0,\n" +
                "    \"data\": {\n" +
                "        \"id\": 15,\n" +
                "        \"regionId\": 1,\n" +
                "        \"detail\": \"测试地址1\",\n" +
                "        \"consignee\": \"测试\",\n" +
                "        \"mobile\": \"18990897878\",\n" +
                "        \"beDefault\": false\n" +
                "    },\n" +
                "    \"errmsg\": \"成功\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, new String(responseString, StandardCharsets.UTF_8), false);


    }

    /**
     * addRegion1
     * 新增地区 父地区不存在
     */
    @Test
    public void addRegion1() throws Exception{

        String token = this.adminLogin("13088admin", "123456");
        String requireJson="{\n"+
                " \"name\": \"fujian\",\n" +
                " \"postalCode\":  \"100100\"\n"+
                "}";

        byte[] responseString = mallClient.post().uri("localhost:8080/regions/-1/subregions")
                .header("authorization", token)
                .bodyValue(requireJson)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.RESOURCE_ID_NOTEXIST.getCode())
                .returnResult()
                .getResponseBodyContent();



    }
    /**addRegion2
     * 新增地区 父地区已废弃  先废弃id为100的地区，再测试新增地区
     * !!!!!可能需要改api,http设为400
     */
    @Test
    public void addRegion2() throws Exception{

        String token = this.adminLogin("13088admin", "123456");
        byte[] responseString1 = mallClient.delete().uri("localhost:8080/regions/100")
                .header("authorization", token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .returnResult()
                .getResponseBodyContent();


        String requireJson="{\n"+
                " \"name\": \"fujian\",\n" +
                " \"postalCode\":  \"100100\"\n"+
                "}";

        byte[] responseString2 = mallClient.post().uri("localhost:8080/regions/100/subregions")
                .header("authorization", token)
                .bodyValue(requireJson)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.REGION_OBSOLETE.getCode())
                .returnResult()
                .getResponseBodyContent();



    }

    /**
     * addRegion3
     * 增地区
     */
    @Test
    public void addRegion3() throws Exception{


        String token = this.adminLogin("13088admin", "123456");
        String requireJson="{\n"+
                " \"name\": \"test\",\n" +
                " \"postalCode\":  \"100100\"\n"+
                "}";

        byte[] responseString = mallClient.post().uri("localhost:8080/regions/1/subregions")
                .header("authorization", token)
                .bodyValue(requireJson)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .returnResult()
                .getResponseBodyContent();



    }

    /**
     * addRegion4
     * 新增地区，地区名字为空
     */
    @Test
    public void addRegion4() throws Exception{


        String token = this.adminLogin("13088admin", "123456");
        String requireJson="{\n"+
                " \"name\": \"\",\n" +
                " \"postalCode\":  \"100100\"\n"+
                "}";

        byte[] responseString = mallClient.post().uri("localhost:8080/regions/1/subregions")
                .header("authorization", token)
                .bodyValue(requireJson)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.FIELD_NOTVALID.getCode())
                .returnResult()
                .getResponseBodyContent();



    }


    /**
     * diableRegion
     * 废弃地区 地区id不存在
     */

    @Test
    public void diableRegion1() throws Exception {
        String token = this.adminLogin("13088admin", "123456");

        byte[] responseString = mallClient.delete().uri("localhost:8080/regions/-1")
                .header("authorization", token)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.RESOURCE_ID_NOTEXIST.getCode())
                .returnResult()
                .getResponseBodyContent();



    }
    /**
     * diableRegion
     * 废弃地区
     */

    @Test
    public void diableRegion2() throws Exception {
        String token = this.adminLogin("13088admin", "123456");

        byte[] responseString = mallClient.delete().uri("localhost:8080/regions/100")
                .header("authorization", token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .returnResult()
                .getResponseBodyContent();

        //用新增子地区来测试是否将地区成功修改为无效 和addRegion2同理

        String requireJson2="{\n"+
                " \"name\": \"fujian\",\n" +
                " \"postalCode\":  \"100100\"\n"+
                "}";

        byte[] responseString2 = mallClient.post().uri("localhost:8080/regions/100/subregions")
                .header("authorization", token)
                .bodyValue(requireJson2)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.REGION_OBSOLETE.getCode())
                .returnResult()
                .getResponseBodyContent();


    }



    /**
     * setAsDefault1
     * !!!测试点:买家修改的地址id不是自己的地址id 应该是badrequest
     * @throws Exception
     */
    @Test
    public void setAsDefault1() throws Exception {
        String token = this.userLogin("8606245097", "123456");

        byte[] responseString = mallClient.put().uri("localhost:8080/addresses/1/default").header("authorization", token)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .returnResult().getResponseBodyContent();
    }

    /**
     * 地址id不存在
     * setAsDefault1
     * @throws Exception
     */
    @Test
    public void setAsDefault2() throws Exception {
        String token = this.userLogin("8606245097", "123456");

        byte[] responseString = mallClient.put().uri("localhost:8080/addresses/100/default").header("authorization", token)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.RESOURCE_ID_NOTEXIST.getCode())
                .returnResult().getResponseBodyContent();

    }


}
