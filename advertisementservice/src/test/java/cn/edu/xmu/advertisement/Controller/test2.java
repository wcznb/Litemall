package cn.edu.xmu.advertisement.Controller;


import cn.edu.xmu.advertisement.AdvertisementserviceApplication;
import cn.edu.xmu.ooad.Application;
import cn.edu.xmu.ooad.util.JwtHelper;
import cn.edu.xmu.ooad.util.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.File;
import java.io.FileInputStream;




//无直接根据广告id查询广告的api，利用两次状态通过来检测
//查看某一个广告时段的广告列表？？大材小用？？
//审核——》下架--》

/**
 * @author 24320182203293
 * @date 2020-12-08
 */
@SpringBootTest(classes = AdvertisementserviceApplication.class)
@Slf4j
public class test2 {
    private WebTestClient webClient;

    public test2() {
        this.webClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:8080")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8")
                .build();
    }


    /**
     * 测试管理员设置默认广告
     * 资源不存在
     *
     * @throws Exception
     */
    @Test
    public void becomeDefault() throws Exception {

        String token = creatTestToken(1L, 1L, 1000);
        byte[] responseString = webClient.put().uri("/shops/0/advertisement/1000/default").header("authorization",token)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.RESOURCE_ID_NOTEXIST.getCode())
               // .jsonPath("$.errmsg").isEqualTo(ResponseCode.RESOURCE_ID_NOTEXIST.getMessage())
                .returnResult()
                .getResponseBodyContent();

    }

    /**
     * 测试管理员设置默认广告
     * 成功（默认到非默认）
     *
     * @throws Exception
     */
    @Test
    public void becomeDefault1() throws Exception {

        String token = creatTestToken(1L, 1L, 1000);
        byte[] responseString = webClient.put().uri("/shops/0/advertisement/200/default").header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.OK.getMessage())
                .returnResult()
                .getResponseBodyContent();

        byte[] ret = webClient.get().uri("/shops/0/timesegments/100/advertisement").header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.OK.getMessage())
                .returnResult()
                .getResponseBodyContent();

        String responseString1 = new String(ret, "UTF-8");
        String expectedString = "{\"errno\":0,\"data\":" +
                "{\"total\":1,\"pages\":1,\"pageSize\":1,\"page\":1," +
                "\"list\":[{\"id\":200,\"link\":null,\"imagePath\":\"http://47.52.88.176/file/images/201610/1475991949547324589.jpg\","+
                "\"content\":null,\"segId\":100,\"state\":\"4\",\"weight\":null,\"bedefault\":false,\"beginDate\":\"2020-12-15\",\"endDate\":\"2021-10-10\",\"repeats\":true,\"gmt_created\":\"2020-12-06T22:49:31\",\"gmt_modified\":\"2020-12-06T22:49:31\"}]}," +
                "\"errmsg\":\"成功\"}\n";
        JSONAssert.assertEquals(expectedString, responseString1, true);

    }

    /**
     * 测试管理员设置默认广告
     * 成功（非默认到默认）
     *
     * @throws Exception
     */
    @Test
    public void becomeDefault2() throws Exception {

        String token = creatTestToken(1L, 1L, 1000);
        byte[] responseString = webClient.put().uri("/shops/0/advertisement/201/default").header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.OK.getMessage())
                .returnResult()
                .getResponseBodyContent();

        byte[] ret = webClient.get().uri("/shops/0/timesegments/101/advertisement").header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.OK.getMessage())
                .returnResult()
                .getResponseBodyContent();

        String responseString1 = new String(ret, "UTF-8");
        String expectedString = "{\"errno\":0,\"data\":" +
                "{\"total\":1,\"pages\":1,\"pageSize\":1,\"page\":1," +
                "\"list\":[{\"id\":201,\"link\":null,\"imagePath\":\"http://47.52.88.176/file/images/201610/1475991949547324589.jpg\","+
                "\"content\":null,\"segId\":101,\"state\":\"4\",\"weight\":null,\"bedefault\":true,\"beginDate\":\"2020-12-15\",\"endDate\":\"2021-10-10\",\"repeats\":true,\"gmt_created\":\"2020-12-06T22:49:31\",\"gmt_modified\":\"2020-12-06T22:49:31\"}]}," +
                "\"errmsg\":\"成功\"}\n";
        JSONAssert.assertEquals(expectedString, responseString1, true);

    }

    /**
     * 测试管理员上架广告
     * 操作资源的id不存在
     *
     * @throws Exception
     */
    @Test
    public void onshelf0() throws Exception {

        String token = creatTestToken(1L, 1L, 1000);
        byte[] responseString = webClient.put().uri("/shops/0/advertisement/1000/onshelves").header("authorization",token)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.RESOURCE_ID_NOTEXIST.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.RESOURCE_ID_NOTEXIST.getMessage())
                .returnResult()
                .getResponseBodyContent();


    }


    /**
     * 测试管理员上架广告
     * 广告状态禁止
     *
     * @throws Exception
     */
    @Test
    public void onshelf() throws Exception {

        String token = creatTestToken(1L, 1L, 1000);
        byte[] responseString = webClient.put().uri("/shops/0/advertisement/203/onshelves").header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.ADVERTISEMENT_STATENOTALLOW.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.ADVERTISEMENT_STATENOTALLOW.getMessage())
                .returnResult()
                .getResponseBodyContent();
    }

    /**
     * 测试管理员上架广告
     * 成功
     *
     * @throws Exception
     */
    @Test
    public void onshelf1() throws Exception {

        String token = creatTestToken(1L, 1L, 1000);
        byte[] responseString = webClient.put().uri("/shops/0/advertisement/204/onshelves").header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.OK.getMessage())
                .returnResult()
                .getResponseBodyContent();


        byte[] ret = webClient.get().uri("/shops/0/timesegments/104/advertisement").header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.OK.getMessage())
                .jsonPath("$.data.list[?(@.id=='204')].state").isEqualTo("4")
                .returnResult()
                .getResponseBodyContent();

//        String responseString1 = new String(ret, "UTF-8");
//        String expectedString = "{\"errno\":0,\"data\":" +
//                "{\"total\":1,\"pages\":1,\"pageSize\":1,\"page\":1," +
//                "\"list\":[{\"id\":204,\"link\":null,\"imagePath\":\"http://47.52.88.176/file/images/201610/1475991949547324589.jpg\","+
//                "\"content\":null,\"segId\":104,\"state\":\"4\",\"weight\":null,\"bedefault\":false,\"beginDate\":\"2020-12-15\",\"endDate\":\"2021-10-10\",\"repeats\":true,\"gmt_created\":\"2020-12-06T22:49:31\",\"gmt_modified\":\"2020-12-06T22:49:31\"}]}," +
//                "\"errmsg\":\"成功\"}\n";
//        JSONAssert.assertEquals(expectedString, responseString1, true);



    }

    /**
     * 测试管理员下架广告
     * 操作资源的id不存在
     *
     * @throws Exception
     */
    @Test
    public void offshelf0() throws Exception {

        String token = creatTestToken(1L, 1L, 1000);
        byte[] responseString = webClient.put().uri("/shops/0/advertisement/1000/offshelves").header("authorization",token)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.RESOURCE_ID_NOTEXIST.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.RESOURCE_ID_NOTEXIST.getMessage())
                .returnResult()
                .getResponseBodyContent();


    }

    /**
     * 测试管理员下架广告
     * 成功
     *
     * @throws Exception
     */
    @Test
    public void offshelf() throws Exception {



        String token = creatTestToken(1L, 1L, 1000);
        byte[] responseString = webClient.put().uri("/shops/0/advertisement/205/offshelves").header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.OK.getMessage())
                .returnResult()
                .getResponseBodyContent();



        byte[] ret = webClient.get().uri("/shops/0/timesegments/105/advertisement").header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.OK.getMessage())
                .jsonPath("$.data.list[?(@.id=='205')].state").isEqualTo("6")

                .returnResult()
                .getResponseBodyContent();

//        String responseString1 = new String(ret, "UTF-8");
//        String expectedString = "{\"errno\":0,\"data\":" +
//                "{\"total\":0,\"pages\":0,\"pageSize\":0,\"page\":1," +
//                "\"list\":[]}," +
//                "\"errmsg\":\"成功\"}\n";
//        JSONAssert.assertEquals(expectedString, responseString1, true);

    }

    /**
     * 测试管理员下架广告
     * 广告状态禁止（审核态）
     *
     * @throws Exception
     */
    @Test
    public void offshelf1() throws Exception {

        String token = creatTestToken(1L, 1L, 1000);
        byte[] responseString = webClient.put().uri("/shops/0/advertisement/206/offshelves").header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.ADVERTISEMENT_STATENOTALLOW.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.ADVERTISEMENT_STATENOTALLOW.getMessage())
                .returnResult()
                .getResponseBodyContent();

        String expectedResponse = "{\"errno\":608,\"errmsg\":\"广告状态禁止\"}";
        JSONAssert.assertEquals(expectedResponse, new String(responseString, "UTF-8"), false);
    }
    /**
     * 测试管理员删除广告
     * 操作资源的id不存在
     *
     * @throws Exception
     */
    @Test
    public void deleteAd0() throws Exception {

        String token = creatTestToken(1L, 1L, 1000);
        byte[] responseString = webClient.delete().uri("/shops/0/advertisement/1000").header("authorization",token)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.RESOURCE_ID_NOTEXIST.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.RESOURCE_ID_NOTEXIST.getMessage())
                .returnResult()
                .getResponseBodyContent();


    }


    /**
     * 测试管理员删除广告
     * 成功
     *
     * @throws Exception
     */
    @Test
    public void deleteAd() throws Exception {

        String token = creatTestToken(1L, 1L, 1000);
        byte[] responseString = webClient.delete().uri("/shops/0/advertisement/211").header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.OK.getMessage())
                .returnResult()
                .getResponseBodyContent();


        byte[] ret = webClient.get().uri("/shops/0/timesegments/111/advertisement").header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.OK.getMessage())

                .returnResult()
                .getResponseBodyContent();

        String responseString1 = new String(ret, "UTF-8");
        String expectedString = "{\"errno\":0,\"data\":" +
                "{\"total\":0,\"pages\":0,\"pageSize\":0,\"page\":1," +
                "\"list\":[]}," +
                "\"errmsg\":\"成功\"}\n";
        JSONAssert.assertEquals(expectedString, responseString1, true);


    }


    /**
     * 测试管理员删除广告
     * 广告状态禁止
     *
     * @throws Exception
     */
    @Test
    public void deleteAd1() throws Exception {

        String token = creatTestToken(1L, 1L, 1000);
        byte[] responseString = webClient.delete().uri("/shops/0/advertisement/208").header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.ADVERTISEMENT_STATENOTALLOW.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.ADVERTISEMENT_STATENOTALLOW.getMessage())
                .returnResult()
                .getResponseBodyContent();


    }


    /**
     * 测试管理员查看广告所有状态
     * 成功
     *
     * @throws Exception
     */
    @Test
    public void getAllAdStates() throws Exception {

        String token = creatTestToken(1L, 1L, 1000);
        byte[] responseString = webClient.get().uri("advertisement/states").header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.OK.getMessage())
                .returnResult()
                .getResponseBodyContent();

        String ret = new String( responseString, "UTF-8");
        String expectedResponse = "{\"errno\":0,\"errmsg\":\"成功\"\"data\",:[{\"name\":\"待审核\",\"code\":0}," +
                "{\"name\":\"上架\",\"code\":4},{\"name\":\"下架\",\"code\":6}]}\n";
        JSONAssert.assertEquals(expectedResponse, ret, true);

    }


    /**
     * 测试管理员审核广告(审核通过)
     * 成功
     *
     * @throws Exception
     */
    @Test
    public void messageAd() throws Exception {


        String mesJson = "{\"conclusion\": \"true\",\"message\": \"ok\"}";
        String token = creatTestToken(1L, 1L, 1000);
        byte[] responseString = webClient.put().uri("/shops/0/advertisement/206/audit").header("authorization",token)
                .bodyValue(mesJson)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.OK.getMessage())
                .returnResult()
                .getResponseBodyContent();


        byte[] responseString3 =webClient.put().uri("/shops/0/advertisement/206/onshelves").header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.OK.getMessage())
                .returnResult()
                .getResponseBodyContent();



        byte[] ret = webClient.get().uri("/shops/0/timesegments/106/advertisement").header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.OK.getMessage())
                .returnResult()
                .getResponseBodyContent();


        String responseString1 = new String(ret, "UTF-8");
        String expectedString = "{\"errno\":0,\"data\":" +
                "{\"total\":1,\"pages\":1,\"pageSize\":1,\"page\":1," +
                "\"list\":[{\"id\":206,\"link\":null,\"imagePath\":\"http://47.52.88.176/file/images/201610/1475991949547324589.jpg\","+
                "\"content\":null,\"segId\":106,\"state\":\"4\",\"weight\":null,\"bedefault\":false,\"beginDate\":\"2020-12-15\",\"endDate\":\"2021-10-10\",\"repeats\":true,\"gmt_created\":\"2020-12-06T22:49:31\",\"gmt_modified\":\"2020-12-06T22:49:31\"}]}," +
                "\"errmsg\":\"成功\"}\n";
        JSONAssert.assertEquals(expectedString, responseString1, true);



    }


    /**
     * 测试管理员审核广告（审核不通过）
     * 成功
     *
     * @throws Exception
     */
    @Test
    public void messageAd1() throws Exception {


        String mesJson = "{\"conclusion\": \"false\",\"message\": \"notok\"}";
        String token = creatTestToken(1L, 1L, 1000);
        byte[] responseString = webClient.put().uri("/shops/0/advertisement/210/audit").header("authorization",token)
                .bodyValue(mesJson)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.OK.getMessage())
                .returnResult()
                .getResponseBodyContent();







    }

    /**
     * 测试管理员审核广告
     * id不存在
     *
     * @throws Exception
     */
    @Test
    public void messageAd2() throws Exception {
        String mesJson = "{\"conclusion\": \"false\",\"message\": \"hhh\"}";
        String token = creatTestToken(1L, 1L, 1000);
        byte[] responseString = webClient.put().uri("/shops/0/advertisement/300/audit").header("authorization",token)
                .bodyValue(mesJson)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.RESOURCE_ID_NOTEXIST.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.RESOURCE_ID_NOTEXIST.getMessage())
                .returnResult()
                .getResponseBodyContent();


    }



//    /**
//     * @author 24320182203293+wumengchen
//     * 测试管理员设置默认广告
//     * 成功（默认到非默认）
//     */
//    @Test
//    public void becomeDefault1() throws Exception {
//
//        String token = this.login("13088admin", "123456");
//        byte[] responseString = manageClient.put().uri("/shops/0/advertisement/200/default").header("authorization", token)
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody()
//                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
//                .jsonPath("$.errmsg").isEqualTo(ResponseCode.OK.getMessage())
//                .returnResult()
//                .getResponseBodyContent();
//
//
//        byte[] ret = manageClient.get().uri("/shops/0/timesegments/100/advertisement").header("authorization", token)
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody()
//                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
//                .jsonPath("$.errmsg").isEqualTo(ResponseCode.OK.getMessage())
//                .returnResult()
//                .getResponseBodyContent();
//
//        String responseString1 = new String(ret, "UTF-8");
//        String expectedString = "{\"errno\":0,\"data\":" +
//                "{\"total\":1,\"pages\":1,\"pageSize\":1,\"page\":1," +
//                "\"list\":[{\"id\":200,\"link\":null,\"imagePath\":\"http://47.52.88.176/file/images/201610/1475991949547324589.jpg\"," +
//                "\"content\":null,\"segId\":100,\"state\":\"4\",\"weight\":null,\"bedefault\":false,\"beginDate\":\"2020-12-15\",\"endDate\":\"2021-10-10\",\"repeats\":true,\"gmt_created\":\"2020-12-06T22:49:31\",\"gmt_modified\":\"2020-12-06T22:49:31\"}]}," +
//                "\"errmsg\":\"成功\"}\n";
//        JSONAssert.assertEquals(expectedString, responseString1, true);
//
//    }
//
//
//
//    /**
//     * @author 24320182203293+wumengchen
//     * 测试管理员设置默认广告
//     * 成功（非默认到默认）
//     */
//    @Test
//    public void becomeDefault2() throws Exception {
//
//        String token = this.login("13088admin", "123456");
//        byte[] responseString = manageClient.put().uri("/shops/0/advertisement/201/default").header("authorization", token)
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody()
//                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
//                .jsonPath("$.errmsg").isEqualTo(ResponseCode.OK.getMessage())
//                .returnResult()
//                .getResponseBodyContent();
//
//
//        byte[] ret = manageClient.get().uri("/shops/0/timesegments/101/advertisement").header("authorization", token)
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody()
//                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
//                .jsonPath("$.errmsg").isEqualTo(ResponseCode.OK.getMessage())
//                .returnResult()
//                .getResponseBodyContent();
//
//        String responseString1 = new String(ret, "UTF-8");
//        String expectedString = "{\"errno\":0,\"data\":" +
//                "{\"total\":1,\"pages\":1,\"pageSize\":1,\"page\":1," +
//                "\"list\":[{\"id\":201,\"link\":null,\"imagePath\":\"http://47.52.88.176/file/images/201610/1475991949547324589.jpg\"," +
//                "\"content\":null,\"segId\":101,\"state\":\"4\",\"weight\":null,\"bedefault\":true,\"beginDate\":\"2020-12-15\",\"endDate\":\"2021-10-10\",\"repeats\":true,\"gmt_created\":\"2020-12-06T22:49:31\",\"gmt_modified\":\"2020-12-06T22:49:31\"}]}," +
//                "\"errmsg\":\"成功\"}\n";
//        JSONAssert.assertEquals(expectedString, responseString1, true);
//
//    }

//    /**
//     * @author 24320182203293+wumengchen
//     * 测试管理员审核广告（审核不通过）
//     * 成功
//     * @throws Exception
//     */
//    @Test
//    public void messageAd1() throws Exception {
//
//
//        String mesJson = "{\"conclusion\": \"false\",\"message\": \"notok\"}";
//        String token = this.login("13088admin", "123456");
//        byte[] responseString = manageClient.put().uri("/shops/0/advertisement/206/audit").header("authorization",token)
//                .bodyValue(mesJson)
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody()
//                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
//                .jsonPath("$.errmsg").isEqualTo(ResponseCode.OK.getMessage())
//                .returnResult()
//                .getResponseBodyContent();
//
////审核不通过，审核-》上架,608
//        byte[] responseString1 =manageClient.put().uri("/shops/0/advertisement/206/onshelves").header("authorization",token)
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody()
//                .jsonPath("$.errno").isEqualTo(ResponseCode.ADVERTISEMENT_STATENOTALLOW.getCode())
//                .jsonPath("$.errmsg").isEqualTo(ResponseCode.ADVERTISEMENT_STATENOTALLOW.getMessage())
//                .returnResult()
//                .getResponseBodyContent();
//
//
//    }
//


//
//    /**
//     * 测试管理员上传广告图片
//     * 成功
//     *
//     * @throws Exception
//     */
//    @Test
//    public void uploadImg() throws Exception {
//
//        String token = creatTestToken(1L, 1L, 1000);
//        File file = new File("."+File.separator + "src" + File.separator + "test" + File.separator+"resources" + File.separator + "img" + File.separator+"12.jpg");
//       // MockMultipartFile firstFile = new MockMultipartFile("img", "timg.png" , "multipart/form-data", new FileInputStream(file));
//
//
//
//        byte[] responseString = webClient.post().uri("advertisement/200/uploadImg").header("authorization",token)
//               .contentType(MediaType.MULTIPART_FORM_DATA)
//                .body()
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody()
//                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
//                .jsonPath("$.errmsg").isEqualTo(ResponseCode.OK.getMessage())
//                .returnResult()
//                .getResponseBodyContent();
//    }



//    /**
//     * 测试删除某个时段，该时段下的广告segid都置为0
//     * 广告状态禁止
//     *
//     * @throws Exception
//     */
//    @Test
//    public void deleteAd12() throws Exception {
//
//        String token = creatTestToken(1L, 1L, 1000);
//        byte[] responseString = webClient.put().uri("advertisement/2/sd").header("authorization",token)
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody()
//                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
//                .jsonPath("$.errmsg").isEqualTo(ResponseCode.OK.getMessage())
//                .returnResult()
//                .getResponseBodyContent();
//
////        String expectedResponse = "{\"errno\":608,\"errmsg\":\"广告状态禁止\"}";
////        JSONAssert.assertEquals(expectedResponse, new String(responseString, "UTF-8"), false);
//    }





    private final String creatTestToken(Long userId, Long departId, int expireTime) {
        String token = new JwtHelper().createToken(userId, departId, expireTime);
        log.debug(token);
        return token;
    }
}

