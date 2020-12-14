package cn.edu.xmu.share.dao;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.share.mapper.SharePoMapper;
import cn.edu.xmu.share.model.bo.Share;
import cn.edu.xmu.share.model.bo.ShareActivity;
import cn.edu.xmu.share.model.po.ShareActivityPo;
import cn.edu.xmu.share.model.po.SharePo;
import cn.edu.xmu.share.model.po.SharePoExample;
import cn.edu.xmu.share.model.vo.SharesRetVo;
import cn.edu.xmu.share.model.vo.SharesSimpleRetVo;
import cn.edu.xmu.share.model.vo.SharesVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class SharesDao {
    @Autowired
    private SharePoMapper sharePoMapper;

    public SharePo getSharePoBySpuIdAndSharerId(Long spuId, Long sharerId){
        SharePoExample example = new SharePoExample();
        SharePoExample.Criteria criteria = example.createCriteria();
        criteria.andGoodsSkuIdEqualTo(spuId);
        criteria.andSharerIdEqualTo(sharerId);
        List<SharePo> sharePos = sharePoMapper.selectByExample(example);
        SharePo ret=null;
        if(!sharePos.isEmpty()){
            for(SharePo po:sharePos){
                if(ret==null){
                    ret = po;
                }else if(ret.getGmtCreate().isBefore(po.getGmtCreate())){
                    ret = po;
                }
            }
        }

        return ret;
    }


    public List<SharePo> getSharesBySkuIdList(List<Long> goodsSkuId,Integer page, Integer pageSize){

        PageHelper.startPage(page, pageSize);

        SharePoExample example = new SharePoExample();
        SharePoExample.Criteria criteria = example.createCriteria();
        criteria.andGoodsSkuIdIn(goodsSkuId);


        List<SharePo> sharePos = sharePoMapper.selectByExample(example);

        return sharePos;
    }

    public List<SharePo> getShareBySkuId(Long goodsSpuId, Integer page, Integer pageSize){
        PageHelper.startPage(page, pageSize);
        SharePoExample example = new SharePoExample();
        SharePoExample.Criteria criteria = example.createCriteria();
        criteria.andGoodsSkuIdEqualTo(goodsSpuId);

        List<SharePo> sharePos = sharePoMapper.selectByExample(example);
        return sharePos;
    }


    public List<SharePo> getOwnShares(Long userId, Long goodsId, LocalDateTime beginTime, LocalDateTime endTime, Integer page, Integer pageSize){
        PageHelper.startPage(page, pageSize);

        SharePoExample example = new SharePoExample();
        SharePoExample.Criteria criteria = example.createCriteria();
        criteria.andSharerIdEqualTo(userId);
        if(goodsId!=null) criteria.andGoodsSkuIdEqualTo(goodsId);
        if(beginTime!=null) criteria.andGmtCreateGreaterThanOrEqualTo(beginTime);
        if(endTime!=null) criteria.andGmtCreateLessThanOrEqualTo(endTime);

        List<SharePo> sharePos = sharePoMapper.selectByExample(example);
        return sharePos;
    }



    public ReturnObject<Share> addShares(Long id, Long userId, Long shareActivateId){
        ReturnObject<Share> retObj=null;
        SharePoExample example = new SharePoExample();
        SharePoExample.Criteria criteria = example.createCriteria();
        criteria.andGoodsSkuIdEqualTo(id);
        criteria.andSharerIdEqualTo(userId);
        if(shareActivateId!=null) criteria.andShareActivityIdEqualTo(shareActivateId);

        List<SharePo> sharePos = sharePoMapper.selectByExample(example);
        if(!sharePos.isEmpty()){
            Share share = new Share(sharePos.get(0));
            retObj = new ReturnObject<>(share);
        }else{

            SharePo sharePo = new SharePo();
            sharePo.setGoodsSkuId(id);
            sharePo.setSharerId(userId);
            sharePo.setShareActivityId(shareActivateId);
            sharePo.setQuantity(0);
            sharePo.setGmtModified(LocalDateTime.now());
            try{
                int ret = sharePoMapper.insertSelective(sharePo);
                if (ret == 0) {
                    //插入失败
                    retObj = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("新增失败：" + sharePo.getClass()));
                } else {
                    Share share = new Share(sharePo);
                    retObj = new ReturnObject<Share>(share);
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
