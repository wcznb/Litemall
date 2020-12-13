package cn.edu.xmu.share.service.impl;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.share.dao.ShareActivityDao;
import cn.edu.xmu.share.dao.SharesDao;
import cn.edu.xmu.share.model.bo.Share;
import cn.edu.xmu.share.model.po.SharePo;
import cn.edu.xmu.share.util.ExchangeDate;
import cn.edu.xmu.share.model.vo.SharesVo;
import cn.edu.xmu.share.service.SharesService;
import com.github.pagehelper.PageInfo;
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

    @Override
    @Transactional
    public ReturnObject<Object> addShareService(Long id, Long userId) {
        Long shareActivateId = shareActivityDao.getShareActivityByspuId(id);
        return sharesDao.addShares(id, userId, shareActivateId);
    }

    @Override
    public ReturnObject<PageInfo<VoObject>> getOwnSharesService(Long userId, Long goodsId, String beginTime, String endTime, Integer page, Integer pageSize) {

        LocalDateTime begintime=null;
        LocalDateTime endtime=null;
        if(!beginTime.equals(""))  begintime = ExchangeDate.StringToDateTime(beginTime).get(true);
        if(!endTime.equals("")) endtime = ExchangeDate.StringToDateTime(endTime).get(true);
        List<SharePo> sharePos =  sharesDao.getOwnShares(userId, goodsId, begintime, endtime, page, pageSize);

        List<VoObject> ret = new ArrayList<>(sharePos.size());
        //通过skuid列表获取sku
        for (SharePo po : sharePos) {
            Share share = new Share(po);
            ret.add(share);
        }
        PageInfo<VoObject> sharesPage = PageInfo.of(ret);

        return  new ReturnObject<>(sharesPage);
    }

    @Override
    public ReturnObject<PageInfo<VoObject>> getSharesBySpuIdService(Long id,Long skuId,Long userId, Integer page, Integer pageSize){
        //通过shopId获取商品信息，，，，，调用接口获取店铺商品sku列表
        List<SharePo> sharePos = null;
        if(skuId==null){
            List<Long> goodsSkuId = new ArrayList<>();
            goodsSkuId.add(521L);
            goodsSkuId.add(442L);
            goodsSkuId.add(443L);
            sharePos = sharesDao.getSharesBySkuIdList(goodsSkuId, page, pageSize);
        }else{
            sharePos = sharesDao.getShareBySkuId(skuId, page, pageSize);
        }
        List<VoObject> ret = new ArrayList<>(sharePos.size());
        for (SharePo po : sharePos) {
            Share share = new Share(po);
            ret.add(share);
        }
        PageInfo<VoObject> sharesPage = PageInfo.of(ret);
        return new ReturnObject<>(sharesPage);
    }
}
