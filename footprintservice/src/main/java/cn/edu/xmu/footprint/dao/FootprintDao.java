package cn.edu.xmu.footprint.dao;

import cn.edu.xmu.footprint.mapper.FootprintPoMapper;
import cn.edu.xmu.footprint.model.bo.Footprint;
import cn.edu.xmu.footprint.model.po.FootprintPo;
import cn.edu.xmu.footprint.model.po.FootprintPoExample;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 陈渝璇
 * createdBy 陈渝璇 2020-11-25
 * modifiedBy 陈渝璇 2020-11-27
 */
@Repository
public class FootprintDao {
    private static final Logger logger = LoggerFactory.getLogger(FootprintDao.class);

    @Autowired
    FootprintPoMapper footprintPoMapper;

    /**
     * 分页查询买家所有足迹
     *
     * @param userId
     * @param beginTime
     * @param endTime
     * @param page
     * @param pageSize
     * @return ReturnObject<PageInfo<VoObject>> 分页返回买家足迹
     * createdBy 陈渝璇 2020-11-25
     * modifiedBy 陈渝璇 2020-11-26
     */
    public ReturnObject<PageInfo<FootprintPo>> findPageOfFootprints(Long userId, String beginTime, String endTime, Integer page, Integer pageSize){
        FootprintPoExample footPrintPoExample = new FootprintPoExample();
        LocalDateTime bgt,et;

        //添加条件
        FootprintPoExample.Criteria criteria = footPrintPoExample.createCriteria();
        if(userId!=null)
            criteria.andCustomerIdEqualTo(userId);
        //时间条件处理
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if(beginTime!=null){
            bgt= LocalDateTime.parse(beginTime, fmt);
        } else{
            bgt = LocalDateTime.parse("1900-1-1 00:00:00", fmt);
        }
        if(endTime!=null){
            et = LocalDateTime.parse(endTime, fmt);
        } else{
            et = LocalDateTime.parse("3000-12-31 23:59:59", fmt);
        }
        criteria.andGmtCreateLessThanOrEqualTo(et);
        criteria.andGmtCreateGreaterThanOrEqualTo(bgt);
        //分页查询
        PageHelper.startPage(page, pageSize);
        List<FootprintPo> footprintPos = null;
        try {
            footprintPos = footprintPoMapper.selectByExample(footPrintPoExample);
        } catch (DataAccessException e){
            logger.error("selectAllRole: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR,
                    String.format("数据库错误：%s", e.getMessage()));
        } catch (Exception e) {
            // 属未知错误
            logger.error("严重错误：" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR,
                    String.format("发生了严重的未知错误：%s", e.getMessage()));
        }
        PageInfo<FootprintPo> footPrintsPoPage = PageInfo.of(footprintPos);
        return new ReturnObject<>(footPrintsPoPage);
    }

    /**
     * 增加足迹
     *
     * @param footprintPo
     * @return 返回对象 ReturnObj
     * createdBy 陈渝璇 2020-11-26
     * modifiedBy 陈渝璇 2020-11-27
     */
    public ReturnObject<Object> insertFootprint(FootprintPo footprintPo){
        ReturnObject<Object> retObj = null;
        try {
            //还应该判断这个spu_id是否存在？
            int ret = footprintPoMapper.insert(footprintPo);
            if (ret == 0) {
                //新增失败
                retObj = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST,
                        String.format("新增失败：" + footprintPo.getClass()));
            } else {
                retObj = new ReturnObject<>();
            }
        }
        catch (DataAccessException e){
            //数据库错误
            retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR,
                    String.format("数据库错误：%s", e.getMessage()));
        }
        catch (Exception e) {
            // 属未知错误
            retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR,
                    String.format("发生了严重的未知错误：%s", e.getMessage()));
        }
        return retObj;
    }
}
