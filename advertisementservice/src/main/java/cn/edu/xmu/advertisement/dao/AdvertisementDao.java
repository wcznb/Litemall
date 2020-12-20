package cn.edu.xmu.advertisement.dao;

import cn.edu.xmu.advertisement.mapper.AdvertisementPoMapper;
import cn.edu.xmu.advertisement.mapper.TimeSegmentPoMapper;
import cn.edu.xmu.advertisement.model.bo.Advertisement;
import cn.edu.xmu.advertisement.model.po.AdvertisementPo;
import cn.edu.xmu.advertisement.model.po.AdvertisementPoExample;
import cn.edu.xmu.advertisement.model.po.TimeSegmentPo;
import cn.edu.xmu.advertisement.model.po.TimeSegmentPoExample;
import cn.edu.xmu.advertisement.model.vo.AdvertisementRetVo;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.embedded.netty.NettyWebServer;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static cn.edu.xmu.ooad.util.ResponseCode.RESOURCE_ID_NOTEXIST;


@Repository
public class AdvertisementDao {


    @Autowired
    private AdvertisementPoMapper advertisementPoMapper;

    @Autowired
    private TimeSegmentPoMapper timeSegmentPoMapper;


    private static final Logger logger = LoggerFactory.getLogger(AdvertisementDao.class);







    public ReturnObject getAdByDefault(Long id){

        Byte BeDefault = 1;
        Byte notBeDefault = 0;

        AdvertisementPo advertisementPo = advertisementPoMapper.selectByPrimaryKey(id);
        if (advertisementPo ==null)
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
        if(advertisementPo.getBeDefault().equals(BeDefault)){

            advertisementPo.setBeDefault(notBeDefault);
            advertisementPoMapper.updateByPrimaryKey(advertisementPo);
            return new ReturnObject(ResponseCode.OK);
        }
        AdvertisementPoExample example = new AdvertisementPoExample();
        AdvertisementPoExample.Criteria criteria=example.createCriteria();
        criteria.andBeDefaultEqualTo(BeDefault);

        List<AdvertisementPo> poList = advertisementPoMapper.selectByExample(example);
        for(AdvertisementPo oldDefaultPo:poList){

            oldDefaultPo.setBeDefault(notBeDefault);
            advertisementPoMapper.updateByPrimaryKeySelective(oldDefaultPo);
        }

        advertisementPo.setBeDefault(BeDefault);
        advertisementPoMapper.updateByPrimaryKey(advertisementPo);

        return new ReturnObject(ResponseCode.OK);
    }



    /**
     * 通过广告id更新广告信息
     *
     * @param advertisementPo
     * @return
     */
    public ReturnObject<Object> modifyAdvertiseDefault(AdvertisementPo advertisementPo) {
        try {
            int ret = advertisementPoMapper.updateByPrimaryKeySelective(advertisementPo);
            Long id = advertisementPo.getId();
            //检查更新是否成功
            if (ret == 0) {
                logger.info("广告不存在或已被删除：id = " + id);
                return new ReturnObject<>(RESOURCE_ID_NOTEXIST);
            } else {
                logger.info("广告 id = " + id + " 的资料已更新");
                return new ReturnObject<>();
            }
        } catch (Exception e) {
            logger.error("Internal error Happened:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        }
    }


    public List<AdvertisementPo> getAdByExmple() {

        //没有默认广告时略带一点问题
        AdvertisementPoExample example = new AdvertisementPoExample();
        AdvertisementPoExample.Criteria criteria = example.createCriteria();
        Byte bedefaults = 1;
        criteria.andBeDefaultEqualTo(bedefaults);

        if (advertisementPoMapper.selectByExample(example).get(0) == null)
            return null;

        else {
            List<AdvertisementPo> advertisementPos = advertisementPoMapper.selectByExample(example);
            return advertisementPos;
        }


    }


    /**
     * ID获取广告信息
     *
     * @param id
     * @return
     * @author ww
     */
    public AdvertisementPo findAdById(Long id) {

        AdvertisementPo advertisementPo = advertisementPoMapper.selectByPrimaryKey(id);

        return advertisementPo;
    }


    /**
     * ID删除广告（有状态限制）
     *
     * @param id
     * @return
     */
    public ReturnObject deleteAd(Long id) {

        if (advertisementPoMapper.selectByPrimaryKey(id) == null)
            return new ReturnObject<>(RESOURCE_ID_NOTEXIST);

        //除了上架广告不能删608，其他皆可删除
        //设定上架的广告不能删
        //     if (advertisementPoMapper.selectByPrimaryKey(id).getState() != Advertisement.State.SHELF.getCode().byteValue()) {

        try {
            advertisementPoMapper.deleteByPrimaryKey(id);
            return new ReturnObject();
        } catch (DataAccessException e) {
            // 数据库错误
            logger.error("数据库错误：" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        } catch (Exception e) {
            // 属未知错误
            logger.error("严重错误：" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        }
//        } else
//            return new ReturnObject<>(ResponseCode.ADVERTISEMENT_STATENOTALLOW);    //608 广告状态错误

    }

    /**
     * ID删除广告（无状态限制，直接限制）
     *
     * @param id
     * @return
     */
    public ReturnObject deleteAd1(Long id) {

        try {
            advertisementPoMapper.deleteByPrimaryKey(id);
            return new ReturnObject();
        } catch (DataAccessException e) {
            // 数据库错误
            logger.error("数据库错误：" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        } catch (Exception e) {
            // 属未知错误
            logger.error("严重错误：" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        }
    }


    /**
     * ID查找当前时段下广告的数量
     *
     * @param tid 广告时段id
     * @return
     */
    public int numBySegID(Long tid) {

        AdvertisementPoExample example = new AdvertisementPoExample();
        AdvertisementPoExample.Criteria criteria = example.createCriteria();
        criteria.andSegIdEqualTo(tid);

        List<AdvertisementPo> advertisementPos = null;
        try {
            advertisementPos = advertisementPoMapper.selectByExample(example);
        } catch (DataAccessException e) {
            StringBuilder message = new StringBuilder().append("getSeg_Id: ").append(e.getMessage());
            logger.error(message.toString());
        }
        if (null == advertisementPos || advertisementPos.isEmpty() || advertisementPos.size() < 8) {
            return 1;
        } else
            return 0;

    }

    //
//    /**
//     * 在广告时段下增加广告
//     *
//     * @param tid 广告时段id
//     * @return
//     */
//    public ReturnObject addAdBySegId(Long tid, Long id) {
//
//        AdvertisementPo advertisementPo = new AdvertisementPo();
//        advertisementPo.setId(id);
//        ReturnObject returnObject;
//
//        if (tid != 0) {
//            //判断该广告时段是否存在
//            TimeSegmentPo timeSegmentPo = timeSegmentPoMapper.selectByPrimaryKey(tid);
//            if (timeSegmentPo == null || timeSegmentPo.getType() != 0)
//                return new ReturnObject<>(RESOURCE_ID_NOTEXIST);
//        }
//
//      AdvertisementPo advertisementPo1s = findAdById(id);
//        if (advertisementPo1s==null)
//            return new ReturnObject<>(RESOURCE_ID_NOTEXIST);
//
////           //判断该时段下的广告是否有8个
////        int ret = numBySegID(tid);
////        if (ret==1) {
//
//            //将原有的广告时段覆盖
//            advertisementPo.setSegId(tid);
//            try {
//                int rets = advertisementPoMapper.updateByPrimaryKeySelective(advertisementPo);
//                Long ids = advertisementPo.getId();
//
//                //检查更新是否成功
//                if (rets == 0) {
//                    logger.info("广告不存在或已被删除：id = " + ids);
//                    return new ReturnObject<>(RESOURCE_ID_NOTEXIST);
//                } else {
//                    logger.info("广告 id = " + ids + " 的资料已更新");
//
////                    AdvertisementPoExample example = new AdvertisementPoExample();
////                    AdvertisementPoExample.Criteria criteria = example.createCriteria();
////                    criteria.andIdEqualTo(id);
//
//                    //AdvertisementPo advertisementPo1 = advertisementPoMapper.selectByPrimaryKey(id);
//                    AdvertisementPo advertisementPo1 = findAdById(id);
//                  returnObject   = new ReturnObject<>(new AdvertisementRetVo(advertisementPo1));
//                    logger.info("广告 id = " + ids + " 返回");
//
//                }
//            } catch (Exception e) {
//                logger.error("Internal error Happened:" + e.getMessage());
//                return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
//            }
//        return returnObject;
////        } else {
////            return new ReturnObject(ResponseCode.ADVERTISEMENT_OUTLIMIT);
//
//       // }
//
//        //     }
//
//
//    }
    public ReturnObject addAdBySegId(Long tid, Long id) {

        AdvertisementPo ishave = findAdById(id);
        if (ishave == null)
            return new ReturnObject(RESOURCE_ID_NOTEXIST);

        AdvertisementPo advertisementPo = new AdvertisementPo();
        advertisementPo.setId(id);
        if (tid != 0) {
            //判断该广告时段是否存在
            TimeSegmentPo timeSegmentPo = timeSegmentPoMapper.selectByPrimaryKey(tid);
            if (timeSegmentPo == null || timeSegmentPo.getType() != 0)
                return new ReturnObject<>(RESOURCE_ID_NOTEXIST);
        }
//        else {

//            //判断该时段下的广告是否有8个
//            ReturnObject ret = numBySegID(tid);
//            if (ret.getCode() == ResponseCode.OK) {

        //将原有的广告时段覆盖
        advertisementPo.setSegId(tid);

        try {
            int rets = advertisementPoMapper.updateByPrimaryKeySelective(advertisementPo);
            Long ids = advertisementPo.getId();
            //检查更新是否成功
            if (rets == 0) {
                logger.info("广告不存在或已被删除：id = " + ids);
                return new ReturnObject<>(RESOURCE_ID_NOTEXIST);
            } else {
                logger.info("广告 id = " + ids + " 的资料已更新");

                AdvertisementPoExample example = new AdvertisementPoExample();
                AdvertisementPoExample.Criteria criteria = example.createCriteria();
                criteria.andIdEqualTo(id);

                AdvertisementPo advertisementPo1 = advertisementPoMapper.selectByExample(example).get(0);
                ReturnObject returnObject = new ReturnObject<>(new AdvertisementRetVo(advertisementPo1));
                return returnObject;
            }
        } catch (Exception e) {
            logger.error("Internal error Happened:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        }

//            } else {
//                return ret;
//
//            }

        // }


    }

    /**
     * 管理员获得某一时段下的广告
     *
     * @param tid      时段id
     * @param page     页数
     * @param pageSize 每页大小
     * @return 列表用户
     */
    public ReturnObject<PageInfo<VoObject>> getAdBySegID(Long tid, Integer page, Integer pageSize, String beginDate, String endDate) {

        if (tid != 0) {

            //判断tid存不存在
            TimeSegmentPo timeSegmentPo = getSegIdPos(tid);
            if (timeSegmentPo == null || timeSegmentPo.getType() != 0)
                return new ReturnObject<>(RESOURCE_ID_NOTEXIST);
        }

        if (page <= 0 || pageSize <= 0)
            return new ReturnObject<>(ResponseCode.FIELD_NOTVALID);

        AdvertisementPoExample example = new AdvertisementPoExample();
        AdvertisementPoExample.Criteria criteria = example.createCriteria();
        criteria.andSegIdEqualTo(tid);

        AdvertisementPoExample example2 = new AdvertisementPoExample();
        AdvertisementPoExample.Criteria criteria2 = example2.createCriteria();
        criteria2.andSegIdEqualTo(tid);


        //String->Localdata 赋值广告开始结束时间 类型LocalData
        //查询的广告时间应该位于beginDate和endDate之间
        LocalDate enddata2;
        LocalDate begindata1;

        //结束时间为空
        if (beginDate != null && beginDate != "" && (endDate == null || endDate == "")) {

            try {

                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                begindata1 = LocalDate.parse(beginDate, fmt);

                criteria.andBeginDateGreaterThanOrEqualTo(begindata1);
            } catch (Exception e) {

                return new ReturnObject<>(ResponseCode.FIELD_NOTVALID);
            }

        }

        //开始时间为空
        if ((beginDate == null || beginDate == "") && endDate != null && endDate != "") {

            try {

                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                enddata2 = LocalDate.parse(endDate, fmt);

                criteria.andEndDateLessThanOrEqualTo(enddata2);

            } catch (Exception e) {

                return new ReturnObject<>(ResponseCode.FIELD_NOTVALID);
            }
        }

        //开始和结束时间都不为空
        if (beginDate != null && beginDate != "" && endDate != null && endDate != "") {

            try {
                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                begindata1 = LocalDate.parse(beginDate, fmt);
                enddata2 = LocalDate.parse(endDate, fmt);

                //开始时间大于结束时间
                if (begindata1.isAfter(enddata2))
                    return new ReturnObject<>(ResponseCode.Log_Bigger);

                criteria.andBeginDateGreaterThanOrEqualTo(begindata1);
                criteria.andEndDateLessThanOrEqualTo(enddata2);
            } catch (Exception e) {

                return new ReturnObject<>(ResponseCode.FIELD_NOTVALID);
            }

        }

        //  分页查询
        //按照（数据库）排序字段 倒序 排序
//        String orderBy = "weight desc";
//
//       PageHelper.startPage(page,pageSize,orderBy);
        PageHelper.startPage(page, pageSize, true, true, null);
        // PageHelper.offsetPage(0,8,true);

        List<AdvertisementPo> advertisementPos = null;
        Byte aaa = 1;
        try {

            //按照条件进行查询
            advertisementPos = advertisementPoMapper.selectByExample(example);

        } catch (DataAccessException e) {

            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        }


//        if (advertisementPos.size()==0 && beginDate != null && endDate != null) {
//
//
//            criteria2.andRepeatsEqualTo(aaa);
//            PageHelper.startPage(page, pageSize, true, true, null);
//
//            try {
//                advertisementPos = advertisementPoMapper.selectByExample(example2);
//            }catch  (DataAccessException e) {
//
//                return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
//            }
//
//        }

        List<VoObject> ret = new ArrayList<>(advertisementPos.size());

        for (AdvertisementPo po : advertisementPos) {
            Advertisement advertisement = new Advertisement(po);
            ret.add(advertisement);
        }

        PageInfo<AdvertisementPo> adPoPage = PageInfo.of(advertisementPos);
        PageInfo<VoObject> adPage = new PageInfo<>(ret);
        adPage.setPages(adPoPage.getPages());
        adPage.setPageNum(adPoPage.getPageNum());
        adPage.setPageSize(adPoPage.getPageSize());
        adPage.setTotal(adPoPage.getTotal());
        return new ReturnObject<>(adPage);

    }


    /**
     * 管理员查看当前时段下的广告(在哪个时间段tid内，依据tid找在该tid下有哪些广告)
     *
     * @return
     */
    public ReturnObject<List> getCurrentAd() {

        LocalTime localTime = LocalTime.now();

        TimeSegmentPoExample timeSegmentPoExample = new TimeSegmentPoExample();
        TimeSegmentPoExample.Criteria criteria = timeSegmentPoExample.createCriteria();

        Byte a = 0;
        criteria.andTypeEqualTo(a);

        List<TimeSegmentPo> list = null;

        try {
            list = timeSegmentPoMapper.selectByExample(timeSegmentPoExample);

        } catch (DataAccessException e) {

            logger.error("selectAllRole: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        }

        //   List<TimeSegmentPo> list1 = null;

        Long tid;

        ArrayList<Advertisement> list2 = new ArrayList<>();

        List<Advertisement> advertisementList = new ArrayList<>(8);

        if (list.size() != 0) {

            for (TimeSegmentPo po : list) {

                LocalDateTime begindate = po.getBeginTime();
                LocalDateTime enddate = po.getEndTime();

                LocalTime begintime = begindate.toLocalTime();
                LocalTime endtime = enddate.toLocalTime();

                if (begintime.isBefore(localTime) && endtime.isAfter(localTime)) {

                    tid = po.getId();

                    AdvertisementPoExample advertisementPoExample = new AdvertisementPoExample();
                    AdvertisementPoExample.Criteria criteria1 = advertisementPoExample.createCriteria();

                    criteria1.andSegIdEqualTo(tid);
                    criteria1.andStateEqualTo(Advertisement.State.SHELF.getCode().byteValue());

                    List<AdvertisementPo> advertisementPos = null;

                    try {
                        advertisementPos = advertisementPoMapper.selectByExample(advertisementPoExample);
                    } catch (DataAccessException e) {

                        logger.error("selectAllRole: DataAccessException:" + e.getMessage());
                        return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
                    }

                    for (AdvertisementPo pos : advertisementPos) {
                        Advertisement advertisement = new Advertisement(pos);
                        list2.add(advertisement);
                    }

                }
            }


            if (list2.size() > 8) {
                int aa = 0;
                for (Advertisement poss : list2) {

                    advertisementList.add(poss);
                    a++;
                    if (a == 8) break;
                }
            } else {
                for (Advertisement poss : list2)
                    advertisementList.add(poss);

            }
            return new ReturnObject<List>(advertisementList);
        } else
            return new ReturnObject<>();


        //        criteria.andBeginTimeLessThanOrEqualTo(localDateTime);
//        criteria.andEndTimeGreaterThanOrEqualTo(localDateTime);

//
//            if(list.size()==0)
//                return new ReturnObject<>(RESOURCE_ID_NOTEXIST);
//            else{
//                 TimeSegmentPo timeSegmentPo = list.get(0);
//             Long tid = timeSegmentPo.getId();
//
//            //查找当前时段下的广告列表
//            AdvertisementPoExample advertisementPoExample = new AdvertisementPoExample();
//            AdvertisementPoExample.Criteria criteria1 = advertisementPoExample.createCriteria();
//
//            criteria1.andSegIdEqualTo(tid);
//            criteria1.andStateEqualTo(Advertisement.State.SHELF.getCode().byteValue());//上架态的广告才有资格看
//
//            List<AdvertisementPo> advertisementPos = null;
//
//            try {
//                advertisementPos = advertisementPoMapper.selectByExample(advertisementPoExample);
//            } catch (DataAccessException e) {
//
//                logger.error("selectAllRole: DataAccessException:" + e.getMessage());
//                return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
//            }
//
//            List<Advertisement> ret = new ArrayList<>(advertisementPos.size());
//            for (AdvertisementPo po : advertisementPos) {
//                Advertisement advertisement = new Advertisement(po);
//                ret.add(advertisement);
//            }
//
//            return new ReturnObject<List>(ret);
//
//        }


    }


    /**
     * 获得广告
     *
     * @param id
     * @return advertisement
     */

    public ReturnObject<Advertisement> getAdByID(Long id) {

        AdvertisementPo advertisementPo = advertisementPoMapper.selectByPrimaryKey(id);
        if (advertisementPo == null) {
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        Advertisement advertisement = new Advertisement(advertisementPo);
//        if (!user.authetic()) {
//            StringBuilder message = new StringBuilder().append("getUserById: ").append(ResponseCode.RESOURCE_FALSIFY.getMessage()).append(" id = ")
//                    .append(user.getId()).append(" username =").append(user.getUserName());
//            logger.error(message.toString());
//            return new ReturnObject<>(ResponseCode.RESOURCE_FALSIFY);
//        }
        return new ReturnObject<>(advertisement);
    }

    /**
     * 获得广告
     *
     * @param id
     * @return advertisement
     */
    public ReturnObject getAdByIDAndisBe(Long id) {

        AdvertisementPo advertisementPo = advertisementPoMapper.selectByPrimaryKey(id);
        if (advertisementPo == null)
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
        else
            return new ReturnObject(ResponseCode.OK);

    }


    /**
     * 获得广告时段
     *
     * @param tid
     * @return timeSegmentPo
     */

    public TimeSegmentPo getSegIdPos(Long tid) {

        return timeSegmentPoMapper.selectByPrimaryKey(tid);
    }


    /**
     * 更新广告图片
     *
     * @param advertisement
     * @return Advertisement
     */
    public ReturnObject updateAdImg(Advertisement advertisement) {
        ReturnObject returnObject = new ReturnObject();
        AdvertisementPo newAdvertisementPo = new AdvertisementPo();
        newAdvertisementPo.setId(advertisement.getId());
        newAdvertisementPo.setImageUrl(advertisement.getImageUrl());
        newAdvertisementPo.setState(advertisement.getState().getCode().byteValue());
        //更新广告状态为审核
        int ret = advertisementPoMapper.updateByPrimaryKeySelective(newAdvertisementPo);
        if (ret == 0) {
            logger.debug("updateUserAvatar: update fail. user id: " + advertisement.getId());
            returnObject = new ReturnObject(ResponseCode.FIELD_NOTVALID);
        } else {
            logger.debug("updateUserAvatar: update user success : " + advertisement.toString());
            returnObject = new ReturnObject();
        }
        return returnObject;
    }


    /**
     * 删除广告时段后相应的广告时段id置为0
     *
     * @param tid
     * @return Advertisement
     */
    public ReturnObject deleteSegIDThenZero(Long tid) {


        AdvertisementPoExample example = new AdvertisementPoExample();
        AdvertisementPoExample.Criteria criteria = example.createCriteria();
        criteria.andSegIdEqualTo(tid);
        List<AdvertisementPo> advertisementPos = null;

        try {

            advertisementPos = advertisementPoMapper.selectByExample(example);
        } catch (DataAccessException e) {

            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        }

        ReturnObject retObj = null;
        for (AdvertisementPo po : advertisementPos) {

            po.setSegId(0L);
            retObj = modifyAdvertiseDefault(po);

            if (retObj.getCode() != ResponseCode.OK) {
                break;
            }
        }

        return retObj;

    }


}

