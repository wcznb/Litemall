package cn.edu.xmu.share.service.impl;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.share.dao.BeSharedDao;
import cn.edu.xmu.share.dao.SharesDao;
import cn.edu.xmu.share.model.bo.BeShared;
import cn.edu.xmu.share.model.po.BeSharePo;
import cn.edu.xmu.share.model.po.SharePo;
import cn.edu.xmu.share.service.BeSharedService;
import cn.edu.xmu.share.util.ExchangeDate;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class BeSharedServiceimpl implements BeSharedService {
    @Autowired
    BeSharedDao beSharedDao;

    @Autowired
    SharesDao sharesDao;

    @Transactional
    @Override
    public ReturnObject<Object> addBeshared(Long sharerId, Long spuId, Long customerId) {
        SharePo sharePo = sharesDao.getSharePoBySpuIdAndSharerId(spuId, sharerId);
        ReturnObject<Object> ret=null;
        if(sharePo!=null) ret = beSharedDao.addBeShared(spuId, sharerId, sharePo.getId(), sharePo.getShareActivityId(), customerId);
        else ret = new ReturnObject<>(ResponseCode.FIELD_NOTVALID);
        return ret;
    }

    @Override
    public ReturnObject<PageInfo<VoObject>> getOwnBeshared(Long custormerId, Long skuId, String beginTime, String endTime, Integer page, Integer pageSize){

        LocalDateTime begintime=null;
        LocalDateTime endtime=null;
        if(!beginTime.equals("")) begintime = ExchangeDate.StringToDateTime(beginTime).get(true);
        if(!endTime.equals("")) endtime = ExchangeDate.StringToDateTime(endTime).get(true);

        //通过skuId列表获取sku，内部接口

        List<BeSharePo> beSharePos = beSharedDao.getOwnBeshared(custormerId, skuId, begintime, endtime, page, pageSize);

        List<VoObject> ret = new ArrayList<>(beSharePos.size());
        for (BeSharePo po : beSharePos) {
            BeShared beShared = new BeShared(po);
            ret.add(beShared);
        }
//        PageInfo<VoObject> rolePage = PageInfo.of(ret);

//        List<VoObject> ret = null;
//        if(beSharePos!=null){
//            ret = new ArrayList<>(beSharePos.size());
//            for(BeSharePo po:beSharePos){
//                BeShared beShared = new BeShared(po);
//                ret.add(beShared);
//            }
//        }else {
//            ret = new ArrayList<>();
//        }
        PageInfo<VoObject> pageInfo = PageInfo.of(ret);
        return new ReturnObject<>(pageInfo);
    }

    @Override
    public ReturnObject<PageInfo<VoObject>> getShopBeshared(Long id, Long skuId, String beginTime, String endTime, Integer page, Integer pageSize) {

        //通过店铺id获取sku的列表

        //调用内部接口通过商品id获取spuid列表
        List<Long> spuIds = new ArrayList<>();
        spuIds.add(306L);
        spuIds.add(577L);
        spuIds.add(397L);
        //-----------------------------------------------------------



        List<BeSharePo> beSharePos = null;
        LocalDateTime begintime=null;
        LocalDateTime endtime=null;
        if(!beginTime.equals("")) begintime = ExchangeDate.StringToDateTime(beginTime).get(true);
        if(!endTime.equals("")) endtime = ExchangeDate.StringToDateTime(endTime).get(true);

        if(skuId!=null){
            if(spuIds.contains(skuId)){
                spuIds = new ArrayList<>();
                spuIds.add(skuId);
                beSharePos = beSharedDao.getBesharedBySpuidlist(spuIds, begintime, endtime, page, pageSize);
            }
        }else{
            beSharePos = beSharedDao.getBesharedBySpuidlist(spuIds, begintime, endtime, page, pageSize);
        }

        List<VoObject> ret = new ArrayList<>(beSharePos.size());
        for (BeSharePo po : beSharePos) {
            BeShared beShared = new BeShared(po);
            ret.add(beShared);
        }

//        List<VoObject> ret = null;
//        if(beSharePos!=null){
//            ret = new ArrayList<>(beSharePos.size());
//            for(BeSharePo po:beSharePos){
//                BeShared beShared = new BeShared(po);
//                ret.add(beShared);
//            }
//        }else {
//            ret = new ArrayList<>();
//        }
        PageInfo<VoObject> pageInfo = PageInfo.of(ret);
        return new ReturnObject<>(pageInfo);
    }
}
