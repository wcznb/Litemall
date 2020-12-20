package cn.edu.xmu.share.service.impl;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.provider.model.vo.GoodsSkuSimpleRetVo;
import cn.edu.xmu.provider.server.GoodsService;
import cn.edu.xmu.share.dao.BeSharedDao;
import cn.edu.xmu.share.dao.SharesDao;
import cn.edu.xmu.share.model.bo.BeShared;
import cn.edu.xmu.share.model.po.BeSharePo;
import cn.edu.xmu.share.model.po.SharePo;
import cn.edu.xmu.share.service.BeSharedService;
import cn.edu.xmu.share.util.ExchangeDate;
import cn.edu.xmu.share.util.ListToMap;
import com.github.pagehelper.PageInfo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class BeSharedServiceimpl implements BeSharedService {
    @Autowired
    BeSharedDao beSharedDao;

    @Autowired
    SharesDao sharesDao;

    @DubboReference(version = "1.2.1")
    GoodsService goodsService;

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

        ListToMap listToMap = new ListToMap();
        LocalDateTime begintime=null;
        LocalDateTime endtime=null;

        if(!beginTime.equals(""))  {
            begintime = ExchangeDate.StringToDateTime(beginTime).get(true);
            if(begintime==null){
                List<VoObject> ret = new ArrayList<>(0);

                PageInfo<VoObject> sharesPage = PageInfo.of(ret);
                sharesPage.setPageSize(pageSize);
                sharesPage.setPageNum(page);
                return new ReturnObject<>(sharesPage);
            }
        }
        if(!endTime.equals("")) {
            endtime = ExchangeDate.StringToDateTime(endTime).get(true);
            if(endtime==null){
                List<VoObject> ret = new ArrayList<>(0);

                PageInfo<VoObject> sharesPage = PageInfo.of(ret);
                sharesPage.setPageSize(pageSize);
                sharesPage.setPageNum(page);
                return new ReturnObject<>(sharesPage);
            }
        }
        if(begintime!=null&&endtime!=null&&begintime.isAfter(endtime)){
            List<VoObject> ret = new ArrayList<>(0);

            PageInfo<VoObject> sharesPage = PageInfo.of(ret);
            sharesPage.setPageSize(pageSize);
            sharesPage.setPageNum(page);
            return new ReturnObject<>(sharesPage);
        }


        //通过skuId列表获取sku，内部接口
        List<BeSharePo> beSharePos = beSharedDao.getOwnBeshared(custormerId, skuId, begintime, endtime, page, pageSize);

        List<Long> ids = new ArrayList<>(beSharePos.size());
        for(BeSharePo beSharePo:beSharePos){
            ids.add(beSharePo.getGoodsSkuId());
        }
        if(ids.size()!=0){
            ReturnObject<List<GoodsSkuSimpleRetVo>> listReturnObject = goodsService.getGoodsSkuListById(ids);
            if(listReturnObject.getCode()==ResponseCode.OK){
                Map<Long, GoodsSkuSimpleRetVo> idMapGoods = listToMap.GoodsSkuSimpleRetVoListToMap(listReturnObject.getData());
                List<VoObject> ret = new ArrayList<>(beSharePos.size());
                for (BeSharePo po : beSharePos) {
                    BeShared beShared = new BeShared(po, idMapGoods.get(po.getGoodsSkuId()));
                    ret.add(beShared);
                }
                PageInfo<VoObject> pageInfo = PageInfo.of(ret);
                pageInfo.setPageSize(pageSize);
                pageInfo.setPageNum(page);
                return new ReturnObject<>(pageInfo);
            }else{
                return new ReturnObject<>(listReturnObject.getCode(), listReturnObject.getErrmsg());
            }
        }else{
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }


    }

    @Override
    public ReturnObject<PageInfo<VoObject>> getShopBeshared(Long id, Long skuId, String beginTime, String endTime, Integer page, Integer pageSize) {

        LocalDateTime begintime=null;
        LocalDateTime endtime=null;
        if(!beginTime.equals("")){
            begintime = ExchangeDate.StringToDateTime(beginTime).get(true);
            if(begintime==null){
                List<VoObject> ret = new ArrayList<>(0);
                PageInfo<VoObject> pageInfo = PageInfo.of(ret);
                pageInfo.setPageSize(pageSize);
                return new ReturnObject<>(pageInfo);
            }
        }
        if(!endTime.equals("")) {
            endtime = ExchangeDate.StringToDateTime(endTime).get(true);
            if(endtime==null){
                List<VoObject> ret = new ArrayList<>(0);
                PageInfo<VoObject> pageInfo = PageInfo.of(ret);
                pageInfo.setPageSize(pageSize);
                return new ReturnObject<>(pageInfo);
            }
        }
        if(begintime!=null&&endtime!=null&&begintime.isAfter(endtime)){
            List<VoObject> ret = new ArrayList<>(0);
            PageInfo<VoObject> pageInfo = PageInfo.of(ret);
            pageInfo.setPageSize(pageSize);
            return new ReturnObject<>(pageInfo);
        }


        if(id==0||goodsService.checkSkuIdByShopId(id, skuId)){
            List<BeSharePo> beSharePos = beSharedDao.getBesharedBySkuId(skuId, begintime, endtime, page, pageSize);

            List<VoObject> ret = new ArrayList<>(beSharePos.size());
            if(beSharePos.isEmpty()){

                for (BeSharePo po : beSharePos) {
                    BeShared beShared = new BeShared(po);
                    ret.add(beShared);
                }
                PageInfo<VoObject> pageInfo = PageInfo.of(ret);
                return new ReturnObject<>(pageInfo);
            }else{
                ReturnObject<GoodsSkuSimpleRetVo> goodsSkuSimpleRetVoReturnObject = goodsService.getGoodsSkuById(skuId);
                if(goodsSkuSimpleRetVoReturnObject.getCode()==ResponseCode.OK){
                    for (BeSharePo po : beSharePos) {
                        BeShared beShared = new BeShared(po, goodsSkuSimpleRetVoReturnObject.getData());
                        ret.add(beShared);
                    }
                    PageInfo<VoObject> pageInfo = PageInfo.of(ret);
                    return new ReturnObject<>(pageInfo);
                }else{
                    return new ReturnObject<>(goodsSkuSimpleRetVoReturnObject.getCode(), goodsSkuSimpleRetVoReturnObject.getErrmsg());
                }

            }

        }else{
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE);
        }
    }
}
