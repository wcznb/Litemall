package cn.edu.xmu.advertisement.dao;

import cn.edu.xmu.advertisement.mapper.AdvertisementPoMapper;
import cn.edu.xmu.advertisement.mapper.TimeSegmentPoMapper;
import cn.edu.xmu.advertisement.model.bo.Advertisement;
import cn.edu.xmu.advertisement.model.po.AdvertisementPo;
import cn.edu.xmu.advertisement.model.po.AdvertisementPoExample;
import cn.edu.xmu.advertisement.model.po.TimeSegmentPo;
import cn.edu.xmu.advertisement.model.vo.AdvertisementRetVo;
import cn.edu.xmu.advertisement.model.vo.NewAdvertisementVo;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Repository
public class NewAdvertisementDao {

    private static final Logger logger = LoggerFactory.getLogger(NewAdvertisementDao.class);

    @Autowired
    AdvertisementPoMapper advertisementPoMapper;

    @Autowired
    private AdvertisementDao advertisementDao;

    @Autowired
    private TimeSegmentPoMapper timeSegmentPoMapper;

    /**
     * 由vo创建newAd
     *
     * @param vo
     * @return ReturnObject
     */
    public ReturnObject createNewAdByVo(Long tid, NewAdvertisementVo vo) {

        if (tid != 0) {
            //获取该时间段下，该时间段是否存在
            TimeSegmentPo timeSegmentPo = timeSegmentPoMapper.selectByPrimaryKey(tid);

            if (timeSegmentPo == null || timeSegmentPo.getType() != 0) {  //判断时间段存不存在

                return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);

            }

            //判断广告时段是否已经达到上线
            int returnObject1 = advertisementDao.numBySegID(tid);
            if (returnObject1 == 0)
                return new ReturnObject(ResponseCode.FIELD_NOTVALID);
        }
        //else {

        AdvertisementPo advertisementPo = new AdvertisementPo();
        ReturnObject returnObject;

        //创建时有的信息
        if (vo.getLink() != null)
            advertisementPo.setLink(vo.getLink());
        else
            advertisementPo.setLink(null);

        //传入的权重能否转换为数字
        if (vo.getWeight() != null) {
            if (vo.getWeight() > 0)
                advertisementPo.setWeight(vo.getWeight());
            else
                return new ReturnObject(ResponseCode.FIELD_NOTVALID);

        } else
            advertisementPo.setWeight(null);

        if (vo.getContent() != null)
            advertisementPo.setContent(vo.getContent());      //是否要查重复？
        else
            advertisementPo.setContent(null);

        //默认值为0
        Byte xx = 0;
        advertisementPo.setBeDefault(xx);

        //Boolean->Byte 传入的repeat能否转化为Boolean
        if (vo.getRepeat() == true || vo.getRepeat() == false) {
            Byte a = 1;
            Byte b = 0;
            Byte c;
            c = vo.getRepeat() ? a : b;
            advertisementPo.setRepeats(c);
        } else
            return new ReturnObject(ResponseCode.FIELD_NOTVALID);

        try {


            //String->Localdata 赋值广告开始结束时间 类型LocalData
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate begindata1 = LocalDate.parse(vo.getBeginDate(), fmt);
            LocalDate enddata1 = LocalDate.parse(vo.getEndDate(), fmt);

            // 取本月第1天：begin
            LocalDate firstDayOfThisMonth = begindata1.with(TemporalAdjusters.firstDayOfMonth());
            // 取本月最后一天，再也不用计算是28，29，30还是31：
            LocalDate lastDayOfThisMonth = begindata1.with(TemporalAdjusters.lastDayOfMonth());

            // 取本月第1天：end
            LocalDate firstDayOfThisMonth1 = enddata1.with(TemporalAdjusters.firstDayOfMonth());
            // 取本月最后一天，再也不用计算是28，29，30还是31：
            LocalDate lastDayOfThisMonth1 = enddata1.with(TemporalAdjusters.lastDayOfMonth());

            //开始日期晚于结束日期
            if (begindata1.isAfter(enddata1))
                return new ReturnObject<>(ResponseCode.Log_Bigger);

            if (!(begindata1.isAfter(firstDayOfThisMonth) && begindata1.isBefore(lastDayOfThisMonth) || begindata1.isEqual(firstDayOfThisMonth) || begindata1.isEqual(lastDayOfThisMonth)))
                return new ReturnObject<>(ResponseCode.FIELD_NOTVALID);

            if (!(enddata1.isAfter(firstDayOfThisMonth1) && enddata1.isBefore(lastDayOfThisMonth1) || enddata1.isEqual(firstDayOfThisMonth1) || enddata1.isEqual(lastDayOfThisMonth1)))
                return new ReturnObject<>(ResponseCode.FIELD_NOTVALID);

            advertisementPo.setBeginDate(begindata1);
            advertisementPo.setEndDate(enddata1);

            String str = "2020-11-30";
            LocalDate str1 = LocalDate.parse(str, fmt);

            if (enddata1.isBefore(str1))
                return new ReturnObject<>(ResponseCode.FIELD_NOTVALID);

        } catch (Exception e) {
            return new ReturnObject<>(ResponseCode.FIELD_NOTVALID);
        }
        //设置时间段的id
        advertisementPo.setSegId(tid);

        //管理员在时间段下新建广告时候，广告置于审核态，准备审核。  新建-》待审核
        advertisementPo.setState(Advertisement.State.MESSAGES.getCode().byteValue());

        //获取当前创建时间
        LocalDateTime localDateTime = LocalDateTime.now();
        advertisementPo.setGmtCreate(localDateTime);
        advertisementPo.setGmtModified(localDateTime);


        //图片，审核附言 null
        advertisementPo.setImageUrl(null);
        advertisementPo.setMessage(null);
        ReturnObject returnObject1 ;

        try {
            advertisementPoMapper.insert(advertisementPo);

        }catch (Exception e){

            System.out.println("1");
//
            return new ReturnObject(ResponseCode.INTERNAL_SERVER_ERR);
        }


        returnObject = new ReturnObject<>(new AdvertisementRetVo(advertisementPo));

        return  returnObject;


    }


    /**
     * 检查内容重复？？？
     *
     * @param content 需要检查的用户名
     * @return boolean
     */
    public boolean isContentExist(String content) {
        logger.debug("is checking userName in user table");
        AdvertisementPoExample example = new AdvertisementPoExample();
        AdvertisementPoExample.Criteria criteria = example.createCriteria();
        criteria.andContentEqualTo(content);
        List<AdvertisementPo> userPos = advertisementPoMapper.selectByExample(example);
        return !userPos.isEmpty();
    }


}
