package cn.edu.xmu.advertisement.Controller;

import cn.edu.xmu.advertisement.AdvertisementserviceApplication;
import cn.edu.xmu.advertisement.model.vo.AdvertisementMessageVo;
import cn.edu.xmu.advertisement.model.vo.AdvertisementUpdateVo;
import cn.edu.xmu.advertisement.model.vo.NewAdvertisementVo;
import cn.edu.xmu.ooad.util.JacksonUtil;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = AdvertisementserviceApplication.class)   //标识本类是一个SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ControllerTest1 {
    @Autowired
    private MockMvc mvc;

    private static final Logger logger = LoggerFactory.getLogger(ControllerTest1.class);

    @Test
    public void becomeDefault() throws Exception {

        ResultActions res = this.mvc.perform(put("/advertisement/125/default")
//                .header("authorization",authorization)
                .contentType("application/json;charset=UTF-8"));

        String responseString = res.andExpect(status().is(200))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println("becomebedefault: "+responseString);
    }


    @Test
    public void offshelf() throws Exception {

        ResultActions res = this.mvc.perform(put("/advertisement/126/offshelves")
//                .header("authorization",authorization)
                .contentType("application/json;charset=UTF-8"));

        String responseString = res.andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println("offshelf: "+responseString);
    }


    @Test
    public void onshelf() throws Exception {

        ResultActions res = this.mvc.perform(put("/advertisement/125/onshelves")
//                .header("authorization",authorization)
                .contentType("application/json;charset=UTF-8"));

        String responseString = res.andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println("onshelf: "+responseString);
    }


    @Test
    public void deleteAd() throws Exception {

        ResultActions res = this.mvc.perform(delete("/advertisement/146"));
//                .header("authorization",authorization)


        String responseString = res.andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println("deleteAd: "+responseString);
    }


    @Test
    public void messageAd() throws Exception {

       AdvertisementMessageVo vo = new AdvertisementMessageVo();
       vo.setConclusion(true);
       vo.setMessage("very good!");

        String contentJson = JacksonUtil.toJson(vo);


        ResultActions res = this.mvc.perform(
                        put("/advertisement/129/audit")
                        .contentType("application/json;charset=UTF-8").content(contentJson)
        );
        String responseString = res.andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println("messagead: "+responseString);

    }

    @Test
    public void messageUpdate() throws Exception {

        AdvertisementUpdateVo advertisementUpdateVo = new AdvertisementUpdateVo();
        advertisementUpdateVo.setContent("ww");
        advertisementUpdateVo.setLink("123546");
     //   advertisementUpdateVo.setSeg_id("3");
        advertisementUpdateVo.setWeight("2");

        String contentJson = JacksonUtil.toJson(advertisementUpdateVo);


        ResultActions res = this.mvc.perform(
                put("/advertisement/134")
                        .contentType("application/json;charset=UTF-8").content(contentJson)
        );
        String responseString = res.andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println("updateAd: "+responseString);

    }

    @Test
    public void getAllAdStates() throws Exception {

        String responseString=this.mvc.perform(get("/advertisement/states"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();

        System.out.println("getAllAdStates: "+responseString);

    }


    @Test
    public void newAdBySegID() throws Exception {


//       NewAdvertisementVo newAdvertisementVo = new NewAdvertisementVo();
//       newAdvertisementVo.setContent("hh");
//       newAdvertisementVo.setLink("123");
//       newAdvertisementVo.setWeight(4);
//
//        String contentJson = JacksonUtil.toJson(newAdvertisementVo);
//
//        String responseString=this.mvc.perform(post("/timesegments/5/advertisement")
//                .contentType("application/json;charset=UTF-8")
//                .content(contentJson))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("application/json;charset=UTF-8"))
//                .andReturn().getResponse().getContentAsString();
//
//        System.out.println("newAdBySegID: "+responseString);

    }
    @Test
    public void AddAdBySegID() throws Exception {

                String responseString=this.mvc.perform(post("/timesegments/1/advertisement/130")
                .contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();

        System.out.println("addAdBySefID: "+responseString);

    }


    //timesegments/{id}/advertisement?page=2&pagesize=3
    @Test
    public void getAdBySegID() throws Exception{

        ResultActions res = this.mvc.perform(get("/timesegments/16/advertisement?page=1&pageSize=4")
                //.header("authorization",authorization)
                .contentType("application/json;charset=UTF-8"));

        String responseString = res.andExpect(status().is(200))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();

        System.out.println("getAdBySegId() "+responseString);

    }


    /*
     * 上传成功
     */
    @Test
    public void uploadFileSutccess() throws Exception{
//
//        File file = new File("."+File.separator + "src" + File.separator + "test" + File.separator+"resources" + File.separator + "img" + File.separator+"12.jpg");
//        MockMultipartFile firstFile = new MockMultipartFile("img", "12.jpg" , "multipart/form-data", new FileInputStream(file));
//        String responseString = mvc.perform(MockMvcRequestBuilders
//                .multipart("/advertisement/134/uploadImg")
//                .file(firstFile)
//                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andReturn()
//                .getResponse()
//                .getContentAsString();
//        String expectedResponse = "{\"errno\":0,\"errmsg\":\"成功\"}";
//        JSONAssert.assertEquals(expectedResponse, responseString, true);
   }


}
