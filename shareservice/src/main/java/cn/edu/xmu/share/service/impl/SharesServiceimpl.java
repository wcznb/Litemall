package cn.edu.xmu.share.service.impl;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.provider.model.vo.GoodsSkuSimpleRetVo;
import cn.edu.xmu.provider.server.GoodsService;
import cn.edu.xmu.share.dao.ShareActivityDao;
import cn.edu.xmu.share.dao.SharesDao;
import cn.edu.xmu.share.model.bo.Share;
import cn.edu.xmu.share.model.po.SharePo;
import cn.edu.xmu.share.model.vo.SharesRetVo;
import cn.edu.xmu.share.model.vo.SharesSimpleRetVo;
import cn.edu.xmu.share.util.ExchangeDate;
import cn.edu.xmu.share.model.vo.SharesVo;
import cn.edu.xmu.share.service.SharesService;
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
public class SharesServiceimpl implements SharesService {
    @Autowired
    SharesDao sharesDao;

    @Autowired
    ShareActivityDao shareActivityDao;

    @DubboReference(version = "1.1.1")
    GoodsService goodsService;

    @Override
    @Transactional
    public ReturnObject<SharesRetVo> addShareService(Long id, Long userId) {

        Long shareActivateId = shareActivityDao.getShareActivityByspuId(id);
        ReturnObject<Share> retVoReturnObject = sharesDao.addShares(id, userId, shareActivateId);

        if(retVoReturnObject.getCode()== ResponseCode.OK){
            Share share = retVoReturnObject.getData();
            ReturnObject<GoodsSkuSimpleRetVo> skuSimpleRetVoReturnObject = goodsService.getGoodsSkuById(share.getGoodsSkuId());
            if(skuSimpleRetVoReturnObject.getCode()== ResponseCode.OK){
                share.setSku(skuSimpleRetVoReturnObject.getData());
                return new ReturnObject<>(share.createVo());
            }else{
                return new ReturnObject<>(skuSimpleRetVoReturnObject.getCode(),skuSimpleRetVoReturnObject.getErrmsg());
            }
        }

        return new ReturnObject<>(retVoReturnObject.getCode(),retVoReturnObject.getErrmsg());

    }

    @Override
    public ReturnObject<PageInfo<VoObject>> getOwnSharesService(Long userId, Long goodsId, String beginTime, String endTime, Integer page, Integer pageSize) {

        ListToMap listToMap = new ListToMap();
        LocalDateTime begintime=null;
        LocalDateTime endtime=null;
        if(!beginTime.equals(""))  begintime = ExchangeDate.StringToDateTime(beginTime).get(true);
        if(!endTime.equals("")) endtime = ExchangeDate.StringToDateTime(endTime).get(true);
        List<SharePo> sharePos =  sharesDao.getOwnShares(userId, goodsId, begintime, endtime, page, pageSize);

        List<Long> ids = new ArrayList<>(sharePos.size());
        //通过skuid列表获取sku
        for(SharePo sharePo:sharePos){
            ids.add(sharePo.getGoodsSkuId());
        }

        ReturnObject<List<GoodsSkuSimpleRetVo>> skuSimpleRetVos = goodsService.getGoodsSkuListById(ids);
        if(skuSimpleRetVos.getCode()==ResponseCode.OK){
            List<VoObject> ret = new ArrayList<>(sharePos.size());
            Map<Long, GoodsSkuSimpleRetVo>  idMapGoodsSku = listToMap.GoodsSkuSimpleRetVoListToMap(skuSimpleRetVos.getData());
            for (SharePo po : sharePos) {
                Share share = new Share(po,idMapGoodsSku.get(po.getGoodsSkuId()));
                ret.add(share);
            }
            PageInfo<VoObject> sharesPage = PageInfo.of(ret);

            return  new ReturnObject<>(sharesPage);
        }
        return new ReturnObject<>(skuSimpleRetVos.getCode(), skuSimpleRetVos.getErrmsg());
    }

    @Override
    public ReturnObject<PageInfo<VoObject>> getSharesBySpuIdService(Long did,Long skuId, Integer page, Integer pageSize){
        List<SharePo> sharePos = null;
        if(did==0||goodsService.checkSkuIdByShopId(did, skuId)){
            sharePos = sharesDao.getShareBySkuId(skuId, page, pageSize);
            List<VoObject> ret = new ArrayList<>(sharePos.size());
            ReturnObject<GoodsSkuSimpleRetVo> skuSimpleRetVoReturnObject = goodsService.getGoodsSkuById(skuId);
            if(skuSimpleRetVoReturnObject.getCode()==ResponseCode.OK){
                GoodsSkuSimpleRetVo skuSimpleRetVo = skuSimpleRetVoReturnObject.getData();
                for (SharePo po : sharePos) {
                    Share share = new Share(po, skuSimpleRetVo);
                    ret.add(share);
                }
                PageInfo<VoObject> sharesPage = PageInfo.of(ret);
                return new ReturnObject<>(sharesPage);
            }else{
                return new ReturnObject<>(skuSimpleRetVoReturnObject.getCode(), skuSimpleRetVoReturnObject.getErrmsg());
            }

        }
        return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE);
    }
}
