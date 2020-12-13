package cn.edu.xmu.footprint.service.impl;

import cn.edu.xmu.footprint.dao.FootprintDao;
import cn.edu.xmu.footprint.model.bo.Footprint;
import cn.edu.xmu.footprint.model.bo.GoodsSku;
import cn.edu.xmu.footprint.model.po.FootprintPo;
import cn.edu.xmu.footprint.rpcclient.GoodsClient;
import cn.edu.xmu.footprint.service.FootprintService;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 陈渝璇
 * @description 足迹服务
 * @date Created in 16:02 2020/11/29
 **/
@Service
public class FootprintServiceImpl implements FootprintService {

    @Autowired
    private FootprintDao footprintDao;

    @Autowired
    private GoodsClient goodsClient;

//    @Resource
//    private RocketMQTemplate rocketMQTemplate;

    @Override
    public ReturnObject<PageInfo<VoObject>> findPageOfFootprints(Long did, Long userId, String beginTime, String endTime, Integer page, Integer pageSize){
        ReturnObject<PageInfo<FootprintPo>> ret = footprintDao.findPageOfFootprints(userId, beginTime, endTime, page, pageSize);
        if(ret.getCode()== ResponseCode.OK){
            //成功搜索
            PageInfo<FootprintPo> poPageInfo = ret.getData();
            List<FootprintPo> footprintPos = poPageInfo.getList();
            List<VoObject> retObj = new ArrayList<>(footprintPos.size());
            for(FootprintPo po:footprintPos){
                Footprint footprint = new Footprint(po);
                GoodsSku goodsSku = goodsClient.getGoodSku(did,po.getGoodsSkuId());
                footprint.setGoodsSku(goodsSku);
                retObj.add(footprint);
            }
            PageInfo<VoObject> footPrintsPage = new PageInfo<>(retObj);
            footPrintsPage.setPages(poPageInfo.getPages());
            footPrintsPage.setPageNum(poPageInfo.getPageNum());
            footPrintsPage.setPageSize(poPageInfo.getPageSize());
            footPrintsPage.setTotal(poPageInfo.getTotal());
            return new ReturnObject<>(footPrintsPage);
        } else{
            return new ReturnObject<>(ret.getCode());
        }
    }

    @Transactional
    @Override
    public ReturnObject<Object> insertFootprint(Long userId, Long skuId){
        ReturnObject<Object> ret = null;
        LocalDateTime now = LocalDateTime.now();
        FootprintPo footprintPo = new FootprintPo();
        footprintPo.setCustomerId(userId);
        footprintPo.setGoodsSkuId(skuId);
        footprintPo.setGmtCreate(now);
        ret = footprintDao.insertFootprint(footprintPo);
        return ret;
    }

}
