package cn.edu.xmu.share.dao;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.share.mapper.ShareActivityPoMapper;
import cn.edu.xmu.share.model.bo.ShareActivity;
import cn.edu.xmu.share.model.po.ShareActivityPo;
import cn.edu.xmu.share.model.po.ShareActivityPoExample;
import cn.edu.xmu.share.model.po.SharePo;
import cn.edu.xmu.share.model.vo.ShareActivityRetVo;
import cn.edu.xmu.share.model.vo.ShareActivityVo;
import cn.edu.xmu.share.util.ExchangeDate;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ShareActivityDao {
    @Autowired
    ShareActivityPoMapper shareActivityPoMapper;


    private Boolean getStateOnline(ShareActivityPo shareActivityPo){
        if((shareActivityPo.getBeginTime().isBefore(LocalDateTime.now()))&&(shareActivityPo.getEndTime().isAfter(LocalDateTime.now()))){
            if(shareActivityPo.getState()!=null&&shareActivityPo.getState().equals(0)){
                return false;
            }else{
                return true;
            }
        }else {
            return false;
        }
    }


    /**
     * 获取当前时间spuId参加的分享活动
     *@param
     * @param id  spuID
     * @return ReturnObject
     */
    public Long getShareActivityByspuId(Long id){
        LocalDateTime localDateTime = LocalDateTime.now();

        ShareActivityPoExample example = new ShareActivityPoExample();
        ShareActivityPoExample.Criteria criteria = example.createCriteria();
        criteria.andGoodsSkuIdEqualTo(id);
        criteria.andBeginTimeLessThan(localDateTime);
        criteria.andEndTimeGreaterThan(localDateTime);

        List<ShareActivityPo> shareActivityPos = shareActivityPoMapper.selectByExample(example);

        for(ShareActivityPo shareActivityPo:shareActivityPos){
            if(getStateOnline(shareActivityPo)){
                return shareActivityPo.getId();
            }
        }

        return null;
    }
    /**
     * 上线指定的分享活动
     *
     * @param shopId
     * @param id
     * @return ReturnObject
     */
    public ReturnObject<Object> onlineShareActivity(Long shopId, Long id){
        ReturnObject<Object> returnObject=null;
        ShareActivityPo shareActivityPo = shareActivityPoMapper.selectByPrimaryKey(id);
        if(shareActivityPo == null){
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        //shopId和skuId不匹配，无权修改
        if(!shareActivityPo.getShopId().equals(shopId)){
            return new ReturnObject<>(ResponseCode.FIELD_NOTVALID, String.format("shopId, id不匹配"));
        }

        shareActivityPo.setState((byte)1);
        int ret = shareActivityPoMapper.updateByPrimaryKeySelective(shareActivityPo);
        if (ret == 0) {
            returnObject = new ReturnObject(ResponseCode.FIELD_NOTVALID);
        } else {
            returnObject = new ReturnObject();
        }

        return returnObject;
    }


    /**
     * 终止指定活动的分享活动
     *
     * @param shopId
     * @param id
     * @return ReturnObject
     */
    public ReturnObject<Object> deleteShareActivity(Long shopId, Long id){
        ReturnObject<Object> returnObject=null;
        ShareActivityPo shareActivityPo = shareActivityPoMapper.selectByPrimaryKey(id);
        if(shareActivityPo == null){
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        //shopId和skuId不匹配，无权修改
        if(!shareActivityPo.getShopId().equals(shopId)){
            return new ReturnObject<>(ResponseCode.FIELD_NOTVALID, String.format("shopId, id不匹配"));
        }

        shareActivityPo.setState((byte)0);
        int ret = shareActivityPoMapper.updateByPrimaryKeySelective(shareActivityPo);
        if (ret == 0) {
            returnObject = new ReturnObject(ResponseCode.FIELD_NOTVALID);
        } else {
            returnObject = new ReturnObject();
        }

        return returnObject;
    }



    /**
     * 修改获取分享活动
     *
     * @param shopId 店铺id
     * @param id 分享活动id
     * @param shareActivityVo 修改分享活动vo
     * @return ReturnObject<Object>
     */
    public ReturnObject<Object> modifyShareActivity(Long shopId, Long id, ShareActivityVo shareActivityVo){
        //检查时间是否冲突
        LocalDateTime begin= ExchangeDate.StringToDateTime(shareActivityVo.getBeginTime()).get(true);
        LocalDateTime end = ExchangeDate.StringToDateTime(shareActivityVo.getEndTime()).get(true);
        //开始时间再结束时间之后
        if(begin==null||end==null||end.isBefore(begin)){
            return new ReturnObject<>(ResponseCode.TIMESEG_CONFLICT, String.format("时间不合法"));
        }

        ShareActivityPo shareActivityPo = shareActivityPoMapper.selectByPrimaryKey(id);
        if(shareActivityPo == null){
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }

        if(getStateOnline(shareActivityPo)){
            return new ReturnObject<>(ResponseCode.FILE_NO_WRITE_PERMISSION, String.format("活动处于上线不能修改"));
        }

        //shopId和skuId不匹配，无权修改
        if(!shareActivityPo.getShopId().equals(shopId)){
            return new ReturnObject<>(ResponseCode.FIELD_NOTVALID, String.format("shopId, id不匹配"));
        }

        ReturnObject returnObject=null;
        List<ShareActivityPo> shareActivityPos = getShareActivityByTime(shareActivityPo.getGoodsSkuId(),begin, end);
        if(shareActivityPos.isEmpty()||(shareActivityPos.size()==1&&shareActivityPos.contains(shareActivityPo))){
            shareActivityPo.setGmtModified(LocalDateTime.now());
            if(shareActivityVo.getBeginTime()!=null) shareActivityPo.setBeginTime(begin);
            if(shareActivityVo.getEndTime()!=null) shareActivityPo.setEndTime(end);
            if(shareActivityVo.getStrategy()!=null) shareActivityPo.setStrategy(shareActivityVo.getStrategy());
            int ret = shareActivityPoMapper.updateByPrimaryKeySelective(shareActivityPo);
            if (ret == 0) {
                returnObject = new ReturnObject(ResponseCode.FIELD_NOTVALID);
            } else {
                returnObject = new ReturnObject();
            }

        }else{
            returnObject = new ReturnObject(ResponseCode.SHAREACT_CONFLICT);
        }

        return returnObject;
    }

    /**
     * 获取分享活动列表
     *
     * @param shopId
     * @param skuId
     * @return List<ShareActivityPo>
     */
    private List<ShareActivityPo> getShareActivity(Long shopId, Long skuId){
        ShareActivityPoExample example = new ShareActivityPoExample();
        ShareActivityPoExample.Criteria criteria = example.createCriteria();
        if(shopId!=null) criteria.andShopIdEqualTo(shopId);
        if(skuId!=null) criteria.andGoodsSkuIdEqualTo(skuId);
        List<ShareActivityPo> shareActivityPos = shareActivityPoMapper.selectByExample(example);
        return shareActivityPos;
    }

    /**
     * 分页获取分享活动列表
     *
     * @param shopId
     * @param spuId
     * @param page
     * @param pageSize
     * @return ReturnObject<PageInfo<VoObject>>
     */
    public ReturnObject<PageInfo<VoObject>> pageShareActivities(Long shopId, Long spuId, Integer page, Integer pageSize){
        //        分页查询
        PageHelper.startPage(page, pageSize);
        List<ShareActivityPo> shareActivityPos = getShareActivity(shopId, spuId);
        List<VoObject> ret = new ArrayList<>(shareActivityPos.size());
        for (ShareActivityPo po : shareActivityPos) {
            ShareActivity shareActivity = new ShareActivity((po));
            ret.add(shareActivity);
        }
        PageInfo<VoObject> rolePage = PageInfo.of(ret);

        return new ReturnObject<>(rolePage);
    }


    /**
     * 获取分享活动列表
     *
     * @param begin
     * @param skuId
     * @parm end
     * @return List<ShareActivityPo>
     */

    private List<ShareActivityPo> getShareActivityByTime(Long skuId,LocalDateTime begin, LocalDateTime end ){
        ShareActivityPoExample example = new ShareActivityPoExample();

        ShareActivityPoExample.Criteria criteria = example.createCriteria();
        criteria.andGoodsSkuIdEqualTo(skuId);
        criteria.andBeginTimeLessThan(begin);
        criteria.andEndTimeGreaterThan(begin);
        criteria.andStateIsNull();
        example.or(criteria);

        ShareActivityPoExample.Criteria criteria2 = example.createCriteria();
        criteria2.andGoodsSkuIdEqualTo(skuId);
        criteria2.andBeginTimeLessThan(end);
        criteria2.andEndTimeGreaterThan(end);
        criteria2.andStateIsNull();
        example.or(criteria2);

        ShareActivityPoExample.Criteria criteria3 = example.createCriteria();
        criteria3.andGoodsSkuIdEqualTo(skuId);
        criteria3.andBeginTimeEqualTo(begin);
        criteria3.andEndTimeEqualTo(end);
        criteria3.andStateIsNull();
        example.or(criteria3);

        return shareActivityPoMapper.selectByExample(example);
    }

    /**
     * 新增分享活动
     *
     * @param shopId
     * @param skuId
     * @param shareActivityVo
     * @return ReturnObject<Object>
     */

    public ReturnObject<Object> addShareActivity(Long shopId, Long skuId, ShareActivityVo shareActivityVo){

        //检查时间是否冲突

        LocalDateTime begin= ExchangeDate.StringToDateTime(shareActivityVo.getBeginTime()).get(true);
        LocalDateTime end = ExchangeDate.StringToDateTime(shareActivityVo.getEndTime()).get(true);
        //开始时间再结束时间之后
        if(begin==null||end==null||end.isBefore(begin)){
            return new ReturnObject<>(ResponseCode.TIMESEG_CONFLICT, String.format("时间不合法"));
        }

        //判断时间段是否存在冲突

        List<ShareActivityPo> shareActivityPos = getShareActivityByTime(skuId, begin, end);
        if(!shareActivityPos.isEmpty()){
            return new ReturnObject<>(ResponseCode.TIMESEG_CONFLICT, String.format("时间段冲突"));
        }

        //进行插入
        ShareActivityPo shareActivityPo = new ShareActivityPo();
        ReturnObject<Object> retObj=null;
        shareActivityPo.setShopId(shopId);
        shareActivityPo.setGoodsSkuId(skuId);
        shareActivityPo.setBeginTime(begin);
        shareActivityPo.setEndTime(end);
        shareActivityPo.setStrategy(shareActivityVo.getStrategy());
        shareActivityPo.setGmtModified(LocalDateTime.now());

        try{
            int ret = shareActivityPoMapper.insertSelective(shareActivityPo);
            if (ret == 0) {
                //插入失败
                retObj = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("新增失败：" + shareActivityPo.getClass()));
            } else {
                ShareActivity shareActivity = new ShareActivity(shareActivityPo);
                retObj = new ReturnObject<>(shareActivity.createVo());
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
        return retObj;
    }
}
