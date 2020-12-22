package cn.edu.xmu.footprint.dao;

import cn.edu.xmu.footprint.mapper.FootPrintPoMapper;
import cn.edu.xmu.footprint.model.po.FootPrintPo;
import cn.edu.xmu.footprint.model.po.FootPrintPoExample;
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
    FootPrintPoMapper footprintPoMapper;

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
    public ReturnObject<PageInfo<FootPrintPo>> findPageOfFootprints(Long userId, LocalDateTime beginTime, LocalDateTime endTime, Integer page, Integer pageSize){
        FootPrintPoExample footPrintPoExample = new FootPrintPoExample();
        //添加条件
        FootPrintPoExample.Criteria criteria = footPrintPoExample.createCriteria();
        if(userId!=null)
            criteria.andCustomerIdEqualTo(userId);
        //时间条件处理
        criteria.andGmtCreateBetween(beginTime,endTime);
        //分页查询
        PageHelper.startPage(page, pageSize);
        List<FootPrintPo> footPrintPos = null;
        try {
            footPrintPos = footprintPoMapper.selectByExample(footPrintPoExample);
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
        PageInfo<FootPrintPo> footPrintsPoPage = PageInfo.of(footPrintPos);
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
    public ReturnObject<Object> insertFootprint(FootPrintPo footprintPo){
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
