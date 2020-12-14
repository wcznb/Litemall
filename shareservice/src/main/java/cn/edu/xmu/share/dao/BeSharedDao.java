package cn.edu.xmu.share.dao;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.share.mapper.BeSharePoMapper;
import cn.edu.xmu.share.model.bo.BeShared;
import cn.edu.xmu.share.model.bo.ShareActivity;
import cn.edu.xmu.share.model.po.*;
import cn.edu.xmu.share.model.vo.BeSharedRetVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class BeSharedDao {
    @Autowired
    BeSharePoMapper beSharePoMapper;

    public List<BeSharePo> getBesharedBySpuidlist(List<Long> spuIds, LocalDateTime beginTime, LocalDateTime endTime, Integer page, Integer pageSize){
        BeSharePoExample example = new BeSharePoExample();
        BeSharePoExample.Criteria criteria = example.createCriteria();
        if(spuIds!=null)criteria.andGoodsSkuIdIn(spuIds);
        if(beginTime!=null) criteria.andGmtCreateGreaterThanOrEqualTo(beginTime);
        if(endTime!=null) criteria.andGmtCreateLessThanOrEqualTo(endTime);
        PageHelper.startPage(page, pageSize);

        return beSharePoMapper.selectByExample(example);
    }

    public List<BeSharePo> getOwnBeshared(Long customerId, Long spuid, LocalDateTime beginTime, LocalDateTime endTime, Integer page, Integer pageSize){
        BeSharePoExample example = new BeSharePoExample();
        //        分页查询
        PageHelper.startPage(page, pageSize);

        BeSharePoExample.Criteria criteria = example.createCriteria();
        if(customerId!=null) criteria.andSharerIdEqualTo(customerId);
        if(spuid!=null) criteria.andGoodsSkuIdEqualTo(spuid);
        if(beginTime!=null) criteria.andGmtCreateGreaterThanOrEqualTo(beginTime);
        if(endTime!=null) criteria.andGmtCreateLessThanOrEqualTo(endTime);


        return beSharePoMapper.selectByExample(example);
    }



    public ReturnObject<Object> addBeShared(Long spuId, Long sharerId, Long shareId, Long shareActivityId, Long customerid){

        BeSharePoExample example = new BeSharePoExample();
        BeSharePoExample.Criteria criteria = example.createCriteria();

        criteria.andGoodsSkuIdEqualTo(spuId);
        criteria.andSharerIdEqualTo(sharerId);
        criteria.andShareIdEqualTo(shareId);

        if(shareActivityId!=null) criteria.andShareActivityIdEqualTo(shareActivityId);
        criteria.andCustomerIdEqualTo(customerid);

        List<BeSharePo> beSharePos = beSharePoMapper.selectByExample(example);

        BeSharePo beSharePo = null;
        ReturnObject<Object> retObj = null;


        if(!beSharePos.isEmpty()){
            beSharePo = beSharePos.get(0);
            retObj = new ReturnObject<>(new BeSharedRetVo(beSharePo));
        }else{
            beSharePo = new BeSharePo();
            beSharePo.setGoodsSkuId(spuId);
            beSharePo.setSharerId(sharerId);
            beSharePo.setShareId(shareId);
            beSharePo.setShareActivityId(shareActivityId);
            beSharePo.setCustomerId(customerid);
            beSharePo.setRebate(0);
            beSharePo.setGmtCreate(LocalDateTime.now());
            beSharePo.setOrderId(0L);

            try{
                int ret = beSharePoMapper.insertSelective(beSharePo);
                if (ret == 0) {
                    //插入失败
                    retObj = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("新增失败"));
                } else {
                    retObj = new ReturnObject<>(new BeSharedRetVo(beSharePo));
                }
            }
            catch (DataAccessException e) {
                // 其他数据库错误
                retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
            }
            catch (Exception e) {
                // 其他Exception错误
                retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了其他错误：%s", e.getMessage()));
            }
        }

        return retObj;
    }
}
