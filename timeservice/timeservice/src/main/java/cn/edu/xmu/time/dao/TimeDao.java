package cn.edu.xmu.time.dao;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.time.mapper.TimeSegmentPoMapper;
import cn.edu.xmu.time.model.bo.TimeSegment;
import cn.edu.xmu.time.model.po.TimeSegmentPo;
import cn.edu.xmu.time.model.po.TimeSegmentPoExample;
import cn.edu.xmu.time.model.vo.NewTimeSegmentVo;
import cn.edu.xmu.time.model.vo.TimeSegmentRetVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.lettuce.core.StrAlgoArgs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.format.datetime.joda.LocalTimeParser;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author shyanne 3184
 */

@Repository
public class TimeDao {
    @Autowired
    TimeSegmentPoMapper timeSegmentPoMapper;

    /**
     * 广告时段
     * @param newTimeSegmentVo
     * @return
     */
    public ReturnObject<TimeSegmentRetVo> addTimeSegment(NewTimeSegmentVo newTimeSegmentVo, Byte type){
        TimeSegmentPo timeSegmentPo = new TimeSegmentPo();
        ReturnObject returnObject;

        timeSegmentPo.setBeginTime(LocalDateTime.parse(newTimeSegmentVo.getBeginTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        timeSegmentPo.setEndTime(LocalDateTime.parse(newTimeSegmentVo.getEndTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        LocalDateTime localDateTime = LocalDateTime.now();
        timeSegmentPo.setGmtCreate(localDateTime);
        timeSegmentPo.setGmtModified(localDateTime);
        timeSegmentPo.setType(type);

        if(timeSegmentPo.getBeginTime().isAfter(timeSegmentPo.getEndTime())){
            return new ReturnObject<>(ResponseCode.Log_Bigger);
        }
        if(isTimeSegmentExist(timeSegmentPo)){
            return new ReturnObject<>(ResponseCode.TIMESEG_CONFLICT);

        }else{
            try{
                int ret = timeSegmentPoMapper.insertSelective(timeSegmentPo);
                if (ret == 0) {
                    //插入失败
                    returnObject = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("新增失败：" + timeSegmentPo.getBeginTime()));
                } else {
                    //插入成功
                    TimeSegment timeSegment=new TimeSegment(timeSegmentPo);
                    TimeSegmentRetVo retVo=new TimeSegmentRetVo(timeSegment);
                    retVo.setId(timeSegmentPo.getId());
                    returnObject = new ReturnObject<>(retVo);
                }
            }catch (DataAccessException e) {
                // 其他数据库错误
                returnObject = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
            }
            catch (Exception e) {
                // 其他Exception错误
                returnObject = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了严重的数据库错误：%s", e.getMessage()));
            }
            return returnObject;

        }



    }

    /**
     * 检查重复
     * @param
     * @return boolean
     */
    public boolean isTimeSegmentExist(TimeSegmentPo newPo) {
        TimeSegmentPoExample example=new TimeSegmentPoExample();
        TimeSegmentPoExample.Criteria criteria=example.createCriteria();

        //新的时段的开始时间晚于原时段的结束时间 或 新的结束时间早于原时段的开始时间，则不冲突

        criteria.andTypeEqualTo(newPo.getType());

        LocalTime newB = LocalTime.of(newPo.getBeginTime().getHour(), newPo.getBeginTime().getMinute(), newPo.getBeginTime().getSecond());
        LocalTime newE = LocalTime.of(newPo.getEndTime().getHour(), newPo.getEndTime().getMinute(), newPo.getEndTime().getSecond());

        //如果开始时间和结束时间一模一样，还需要单独判断一下

        List<TimeSegmentPo> pos=timeSegmentPoMapper.selectByExample(example);
        for(TimeSegmentPo po:pos){
            LocalTime oldB = LocalTime.of(po.getBeginTime().getHour(), po.getBeginTime().getMinute(), po.getBeginTime().getSecond());
            LocalTime oldE = LocalTime.of(po.getEndTime().getHour(), po.getEndTime().getMinute(), po.getEndTime().getSecond());

            if(!newB.isBefore(oldE) || !newE.isAfter(oldB))continue;
            else return true;
        }
        return false;



    }

    /**
      这是写给商品模块的内部api,返回type值为1的所有秒杀时段
     */
    public List<TimeSegmentPo> getAllFlashsaleTimeSegmentsInternal(){
        TimeSegmentPoExample example=new TimeSegmentPoExample();
        TimeSegmentPoExample.Criteria criteria=example.createCriteria();

        criteria.andTypeEqualTo((byte)1);
        List<TimeSegmentPo> pos=timeSegmentPoMapper.selectByExample(example);
        return pos;
    }

    /**
     * advertisement
     * @param id
     * @return
     * need fix
     */
    public ReturnObject<Object> deleteTimeSegmentById0(Long id) {
        ReturnObject<Object> retObj = null;
        TimeSegmentPo timeSegmentPo = timeSegmentPoMapper.selectByPrimaryKey(id);
        if(timeSegmentPo!=null){
            if (timeSegmentPo.getType() == 0 ) {
                int ret = timeSegmentPoMapper.deleteByPrimaryKey(id);
                if (ret == 0) {
                    retObj = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("时间段id不存在：" + id));
                } else {
                    retObj = new ReturnObject<>();

                    //import advertisement set all segid 0
                }
            } else {
                retObj = new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE, String.format("非广告时段，不可操作: " + id));
            }

        }else retObj= new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("时间段id不存在：" + id));




        return retObj;
    }

    /**
     *flashsale
     * @param id
     * @return
     */
    public ReturnObject<Object> deleteTimeSegmentById1(Long id) {
        ReturnObject<Object> retObj = null;
        TimeSegmentPo timeSegmentPo = timeSegmentPoMapper.selectByPrimaryKey(id);
        if(timeSegmentPo!=null){
            if (timeSegmentPo.getType() == 1 ) {
                int ret = timeSegmentPoMapper.deleteByPrimaryKey(id);
                if (ret == 0) {
                    retObj = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("时间段id不存在：" + id));
                } else {
                    retObj = new ReturnObject<>();
                }
            } else {
                retObj = new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE, String.format("非flashsale时段，不可操作: " + id));
            }

        }else retObj= new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("时间段id不存在：" + id));



        return retObj;
    }

//    public ReturnObject<PageInfo<VoObject>> getAllTimeSegments(Byte type,Integer page,Integer pagesize){
//
//        TimeSegmentPoExample example = new TimeSegmentPoExample();
//        TimeSegmentPoExample.Criteria criteria = example.createCriteria();
//        if(type==0||type==1) {
//            criteria.andTypeEqualTo(type);
//             }
//
////        分页查询
//        PageHelper.startPage(page, pagesize);
//        List<TimeSegmentPo> pos = null;
//
//        try {
//            //按照条件进行查询
//            pos = timeSegmentPoMapper.selectByExample(example);
//        }catch (DataAccessException e){
//            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
//        }
//
//        List<VoObject> ret = new ArrayList<>(pos.size());
//        for (TimeSegmentPo po : pos) {
//            TimeSegment retVo = new TimeSegment(po);
//            ret.add(retVo);
//        }
//
//        PageInfo<VoObject> rolePage = PageInfo.of(ret);
//
//        return new ReturnObject<>(rolePage);
//    }

    public ReturnObject<PageInfo<TimeSegmentPo>> getAllTimeSegments(Byte type,Integer page, Integer pagesize){


        TimeSegmentPoExample example = new TimeSegmentPoExample();
        TimeSegmentPoExample.Criteria criteria = example.createCriteria();
        if(type==0||type==1) {
            criteria.andTypeEqualTo(type);
             }

        PageHelper.startPage(page, pagesize);
        List<TimeSegmentPo> pos = null;

        try {

            pos = timeSegmentPoMapper.selectByExample(example);

        }catch (DataAccessException e){

            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));

        }

        PageInfo<TimeSegmentPo> footPrintsPoPage = PageInfo.of(pos);
        return new ReturnObject<>(footPrintsPoPage);

    }

}
