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
import java.util.List;

@Repository
public class NewAdvertisementDao {

    private  static  final Logger logger = LoggerFactory.getLogger(NewAdvertisementDao.class);

    @Autowired
    AdvertisementPoMapper advertisementPoMapper;

    @Autowired
    private AdvertisementDao advertisementDao;

    @Autowired
    private TimeSegmentPoMapper timeSegmentPoMapper;

    /**
     * 由vo创建newAd
     * @param vo
     * @return ReturnObject
     */
    public  ReturnObject createNewAdByVo(Long tid, NewAdvertisementVo vo){

        //获取该时间段下，该时间段是否存在
        TimeSegmentPo timeSegmentPo = timeSegmentPoMapper.selectByPrimaryKey(tid);

        if (timeSegmentPo ==null) {  //时间段不存在

            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);

        }

        //判断广告时段是否已经达到上线
        ReturnObject returnObject1=advertisementDao.numBySegID(tid);
        if(returnObject1.getCode()!=ResponseCode.OK)
            return returnObject1;

        else {

            AdvertisementPo advertisementPo = new AdvertisementPo();
            ReturnObject returnObject;

            //创建时有的信息
            if(vo.getLink()!=null)
            advertisementPo.setLink(vo.getLink());
             else
                advertisementPo.setLink(null);

             if(vo.getWeight()!=null)
            advertisementPo.setWeight(Integer.valueOf(vo.getWeight()));
             else
                 advertisementPo.setWeight(null);

            if(vo.getContent()!=null)
            advertisementPo.setContent(vo.getContent());      //是否要查重复？
             else
                advertisementPo.setContent(null);

            //默认类型default 重复类型 repeats:true

            advertisementPo.setBeDefault(null);

            //Boolean->Byte
             Byte a = 1;
            Byte b = 0;
            Byte c = vo.getRepeat() ? a : b;
            advertisementPo.setRepeats(c);

            //String->Localdata 赋值广告开始结束时间 类型LocalData
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate begindata1 = LocalDate.parse(vo.getBeginDate(), fmt);
            LocalDate enddata1 = LocalDate.parse(vo.getEndDate(), fmt);
            advertisementPo.setBeginDate(begindata1);
            advertisementPo.setEndDate(enddata1);

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

                int ret = advertisementPoMapper.insert(advertisementPo);

            if (ret != 0) {

                AdvertisementPoExample example = new AdvertisementPoExample();
                AdvertisementPoExample.Criteria criteria = example.createCriteria();

              //  criteria.andLinkEqualTo(advertisementPo.getContent());
                criteria.andContentEqualTo(advertisementPo.getContent());
                criteria.andSegIdEqualTo(advertisementPo.getSegId());

               AdvertisementPo advertisementPo1 = advertisementPoMapper.selectByExample(example).get(0);
                returnObject = new ReturnObject<>(new AdvertisementRetVo(advertisementPo1));
            }
            else
                return new ReturnObject(ResponseCode.INTERNAL_SERVER_ERR);

            return returnObject;
        }

    }




    /**
     * 检查内容重复
     * @param content 需要检查的用户名
     * @return boolean
     */
    public boolean isContentExist(String content){
        logger.debug("is checking userName in user table");
       AdvertisementPoExample example=new AdvertisementPoExample();
        AdvertisementPoExample.Criteria criteria=example.createCriteria();
        criteria.andContentEqualTo(content);
        List<AdvertisementPo> userPos=advertisementPoMapper.selectByExample(example);
        return !userPos.isEmpty();
    }




}
