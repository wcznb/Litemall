package cn.edu.xmu.advertisement.service.impl;

import cn.edu.xmu.advertisement.dao.AdvertisementDao;
import cn.edu.xmu.advertisement.dao.NewAdvertisementDao;
import cn.edu.xmu.advertisement.model.bo.Advertisement;
import cn.edu.xmu.advertisement.model.po.AdvertisementPo;

import cn.edu.xmu.advertisement.model.po.TimeSegmentPo;
import cn.edu.xmu.advertisement.model.vo.AdvertisementMessageVo;
import cn.edu.xmu.advertisement.model.vo.AdvertisementRetVo;
import cn.edu.xmu.advertisement.model.vo.AdvertisementUpdateVo;
import cn.edu.xmu.advertisement.model.vo.NewAdvertisementVo;
import cn.edu.xmu.advertisement.service.AdvertisementService;
import cn.edu.xmu.advertisement.util.ImgUploads;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ImgHelper;

import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import com.github.pagehelper.PageInfo;
import io.lettuce.core.StrAlgoArgs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class AdvertisementServiceImpl implements AdvertisementService {

    private Logger logger = LoggerFactory.getLogger(AdvertisementServiceImpl.class);

    @Autowired
    private AdvertisementDao advertisementDao;

    @Autowired
    private NewAdvertisementDao newAdvertisementDao;

    //dev配置的服务器密码和姓名，存放图片的位置
//    @Value("${privilegeservice.imglocation}")
//    private String imgLocation;

//    @Value("${privilegeservice.dav.username}")
//    private String davUsername;
//
//    @Value("${privilegeservice.dav.password}")
//    private String davPassword;
//
//    @Value("${privilegeservice.dav.baseUrl}")
//    private String baseUrl;
@Value("${minio.endpoint}")
private  String ENDPOINT;
    @Value("${minio.bucketName}")
    private  String BUCKETNAME;
    @Value("${minio.accessKey}")
    private  String ACCESSKEY;
    @Value("${minio.secretKey}")
    private  String SECRETKEY;




    /**
     * 管理员设置默认广告
     *
     * @param id 广告id
     */
    @Override
    @Transactional
    public ReturnObject<Boolean> becomeDefault(Long id) {

        AdvertisementPo advertisementPo1 = advertisementDao.findAdById(id);

        if(advertisementPo1==null)
            return  new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);

        AdvertisementPo advertisementPo = new AdvertisementPo();
        advertisementPo.setId(id);
        Byte bedefaults = 1;
        Byte notbedefaults = 0;

        //该广告一开始是非默认广告或未设置广告是否默认
        if(advertisementPo1.getBeDefault()==bedefaults){

            advertisementPo.setBeDefault(notbedefaults);  //默认-》非默认
        }
        else{
            advertisementPo.setBeDefault(bedefaults); //非默认-》默认

        }

        ReturnObject retObj = advertisementDao.modifyAdvertiseDefault(advertisementPo);

        if (retObj.getCode() != ResponseCode.OK) {
            return retObj;
        }
        return new ReturnObject<>(true);
    }


    /**
     * 管理员下架广告
     *
     * @param id 广告id
     */
    @Override
    @Transactional
    public ReturnObject<Boolean> adoffshelf(Long id) {
        AdvertisementPo advertisementPo = new AdvertisementPo();

        advertisementPo.setId(id);

         AdvertisementPo advertisementPo1 = advertisementDao.findAdById(id);

         if(advertisementPo1==null){

             return  new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
         }
        //只有上架态才能转为下架态  上架-》下架
        if(advertisementPo1.getState()!=Advertisement.State.SHELF.getCode().byteValue()){

            return new ReturnObject<>(ResponseCode.ADVERTISEMENT_STATENOTALLOW);   //608 广告状态禁止
        }

        else {

            advertisementPo.setState(Advertisement.State.OFFSHELF.getCode().byteValue());//设置状态设为下架

            ReturnObject retObj = advertisementDao.modifyAdvertiseDefault(advertisementPo);

            if (retObj.getCode() != ResponseCode.OK) {
                return retObj;

            }
            return new ReturnObject<>(true);
        }
    }


    /**
     * 管理员上架广告
     *
     * @param id 广告id
     */
    @Override
    @Transactional
    public ReturnObject<Boolean> adonshelf(Long id) {

        AdvertisementPo advertisementPo = new AdvertisementPo();

        advertisementPo.setId(id);

         AdvertisementPo advertisementPo1 = advertisementDao.findAdById(id);

        if(advertisementPo1==null){

            return  new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        //只有下架态才能转为上架态  下架-》上架
        //上架态的广告？？上架-》上架
        if(advertisementPo1.getState()!=Advertisement.State.OFFSHELF.getCode().byteValue()){

            return new ReturnObject<>(ResponseCode.ADVERTISEMENT_STATENOTALLOW);   //608 广告状态禁止
        }

        else {

            advertisementPo.setState(Advertisement.State.SHELF.getCode().byteValue());//设置状态设为下架

            ReturnObject retObj = advertisementDao.modifyAdvertiseDefault(advertisementPo);

            if (retObj.getCode() != ResponseCode.OK) {
                return retObj;

            }
            return new ReturnObject<>(true);
        }

    }

    /**
     * 管理员删除广告
     *
     * @param id 广告id
     */
    @Override
    @Transactional
    public ReturnObject deleteAd(Long id) {
        return advertisementDao.deleteAd(id);
    }


    /**
     * 管理员审核默认广告
     *
     * @param id 广告id
     */
    @Override
    @Transactional
    public ReturnObject <Object> messageAd( Long id, AdvertisementMessageVo vo){

        AdvertisementPo advertisementPo = new AdvertisementPo();

        advertisementPo.setId(id);

        AdvertisementPo advertisementPo1 = advertisementDao.findAdById(id);

        //判断id是否存在 404 504
        if(advertisementPo1==null){

            return  new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
//只有审核态才能被审核 否则 608
        if(advertisementPo1.getState()!=Advertisement.State.MESSAGES.getCode().byteValue()){

            return new ReturnObject<>(ResponseCode.ADVERTISEMENT_STATENOTALLOW);   //608 广告状态禁止
        }

        else{
//审核通过，则将审核附加的话传到相应的广告上，并且将审核通过的广告置为下架态
            if(vo.getConclusion()) {

                advertisementPo.setMessage(vo.getMessage());
                advertisementPo.setState(Advertisement.State.OFFSHELF.getCode().byteValue());
                ReturnObject retObj = advertisementDao.modifyAdvertiseDefault(advertisementPo);

                if (retObj.getCode() != ResponseCode.OK) {
                    return retObj;
                }
                return new ReturnObject<>(true);
            }
            else{

                //广告审核不通过，返回ok，但不会修改广告
                return new ReturnObject<>(true);

            }


            }

    }

    /**
     * 修改广告信息
     *
     * @param id 广告id
     */
    @Override
    @Transactional
    public ReturnObject <Object> messageUpdate( Long id, AdvertisementUpdateVo vo){


        //判断id存不存在，返回404.路径id不存在 504
        //处于待审核态的广告允许修改吗

        ReturnObject<Advertisement> r = advertisementDao.getAdByID(id);
        if(r.getCode()==ResponseCode.RESOURCE_ID_NOTEXIST)
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);


        AdvertisementPo advertisementPo = new AdvertisementPo();
        advertisementPo.setId(id);


        //String->Localdata 赋值广告开始结束时间 类型LocalData
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate begindata1 = LocalDate.parse(vo.getBeginDate(), fmt);
        LocalDate enddata1 = LocalDate.parse(vo.getEndDate(), fmt);
        advertisementPo.setBeginDate(begindata1);
        advertisementPo.setEndDate(enddata1);

        //Boolean->Byte
        Byte a = 1;
        Byte b = 0;
        Byte c = vo.getRepeat() ? a : b;
        advertisementPo.setRepeats(c);

        if(vo.getLink()!=null)
        advertisementPo.setLink(vo.getLink());
        if(vo.getWeight()!=null)  //判别非空 进行类型转换   String->Integer
        advertisementPo.setWeight(Integer.valueOf(vo.getWeight()));
        if(vo.getContent()!=null)
            advertisementPo.setContent(vo.getContent());

        //更新广告状态为待审核
        advertisementPo.setState(Advertisement.State.MESSAGES.getCode().byteValue());

        ReturnObject<Object> retObj = advertisementDao.modifyAdvertiseDefault(advertisementPo);

        if (retObj.getCode() != ResponseCode.OK) {
            return retObj;
        }
        return new ReturnObject<>(true);

        }

    /**
     * 在广告时段下新建广告
     *
     * @param tid 广告id
     */
    @Override
    @Transactional
    public ReturnObject createUnderSegID(Long tid,NewAdvertisementVo vo) {

        return newAdvertisementDao.createNewAdByVo(tid,vo);
    }



    /**
     * 管理员在广告时段下增加广告
     *
     * @param tid 广告id
     */
    @Override
    @Transactional
    public ReturnObject addAdBySegID(Long tid,Long id) {


        return advertisementDao.addAdBySegId(tid,id);
    }

    /**
     * 管理员查看某一时段的广告
     *
     * @param id 广告id
     */
    @Override
    @Transactional
    public ReturnObject<PageInfo<VoObject>> getAdBySegId(Long id, Integer page, Integer pageSize,String beginDate,String endDate){
        ReturnObject<PageInfo<VoObject>> ret = advertisementDao.getAdBySegID(id, page, pageSize,beginDate,endDate);
        return ret;
    }

    /**
     * 管理员查看当前时段的广告
     *
     */
    @Override
    @Transactional
    public   ReturnObject<List> getCurrentAd() {

        return advertisementDao.getCurrentAd();
    }


    /**
     * 上传图片
     * @param id: 广告id
     * @param multipartFile: 文件
     * @return
     */
    @Override
    @Transactional
    public ReturnObject uploadImg(Long id, MultipartFile multipartFile){

        ReturnObject<Advertisement> advertisementReturnObject = advertisementDao.getAdByID(id);

        if(advertisementReturnObject.getCode() == ResponseCode.RESOURCE_ID_NOTEXIST) {
            return advertisementReturnObject;
        }
        Advertisement advertisement = advertisementReturnObject.getData();

        ReturnObject returnObject = new ReturnObject();
        try{
            returnObject = ImgUploads.remoteSaveImg(multipartFile,2,ACCESSKEY, SECRETKEY,BUCKETNAME,ENDPOINT);

            //文件上传错误
            if(returnObject.getCode()!=ResponseCode.OK){
                logger.debug(returnObject.getErrmsg());
                return returnObject;
            }

            String oldFilename =advertisement.getImageUrl();
            advertisement.setImageUrl(returnObject.getData().toString());

            advertisement.setState(Advertisement.State.MESSAGES);//更新广告状态为审核

            ReturnObject updateReturnObject = advertisementDao.updateAdImg(advertisement);//插入新的图片

            //数据库更新失败，需删除新增的图片
            if(updateReturnObject.getCode()==ResponseCode.FIELD_NOTVALID){
               ImgUploads.deleteRemoteImg(returnObject.getData().toString(),ACCESSKEY, SECRETKEY,BUCKETNAME,ENDPOINT);
                return updateReturnObject;
            }

            //数据库更新失败，广告状态还需要变成审核态吗？
            //广告更新成功后，将广告的状态变为审核态？？？？？


            //数据库更新成功需删除旧图片，未设置则不删除
            if(oldFilename!=null) {
                ImgUploads.deleteRemoteImg(oldFilename, ACCESSKEY, SECRETKEY,BUCKETNAME,ENDPOINT);
            }
        }
        catch (IOException e){
            logger.debug("uploadImg: I/O Error:" + ENDPOINT+BUCKETNAME);
            return new ReturnObject(ResponseCode.FILE_NO_WRITE_PERMISSION);
        }
        return returnObject;
    }


    /**
     * 时段删除，将删除时段的广告seg_id置为0
     * @param seg_id: 时段id
     * @return
     */
    @Transactional
    public ReturnObject deleteSegIDThenZero(Long seg_id){

        return advertisementDao.deleteSegIDThenZero(seg_id);

    }



}
