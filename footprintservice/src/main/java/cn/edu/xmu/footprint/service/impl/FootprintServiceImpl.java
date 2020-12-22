package cn.edu.xmu.footprint.service.impl;

import cn.edu.xmu.footprint.dao.FootprintDao;
import cn.edu.xmu.footprint.model.bo.Footprint;
import cn.edu.xmu.footprint.model.po.FootPrintPo;
import cn.edu.xmu.footprint.service.FootprintService;
import cn.edu.xmu.footprint.util.ListToMap;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.provider.model.vo.GoodsSkuSimpleRetVo;
import cn.edu.xmu.provider.server.GoodsService;
import com.github.pagehelper.PageInfo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 陈渝璇
 * @description 足迹服务
 * @date Created in 16:02 2020/11/29
 **/
@Service
public class FootprintServiceImpl implements FootprintService {

    @Autowired
    private FootprintDao footprintDao;

    @DubboReference(version = "1.1.1")
    GoodsService goodsService;

//    @Resource
//    private RocketMQTemplate rocketMQTemplate;

    @Override
    public ReturnObject<PageInfo<VoObject>> findPageOfFootprints(Long did, Long userId, String beginTime, String endTime, Integer page, Integer pageSize){
        if(did < Long.valueOf(0))
            //无权限
            return new ReturnObject<>(ResponseCode.FIELD_NOTVALID);
        LocalDateTime bgt, et;
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if (beginTime != null) {
            bgt = LocalDateTime.parse(beginTime, fmt);
        } else {
            bgt = LocalDateTime.parse("1900-01-01 00:00:00", fmt);
        }
        if (endTime != null) {
            et = LocalDateTime.parse(endTime, fmt);
        } else {
            et = LocalDateTime.parse("3000-12-31 23:59:59", fmt);
        }
        //开始时间大于结束时间
        if (bgt.isAfter(et)) {
            return new ReturnObject<>(ResponseCode.Log_Bigger);
        }
        ReturnObject<PageInfo<FootPrintPo>> ret = footprintDao.findPageOfFootprints(userId, bgt, et, page, pageSize);
        if (ret.getCode() == ResponseCode.OK) {
            //成功搜索
            PageInfo<FootPrintPo> poPageInfo = ret.getData();
            List<FootPrintPo> footPrintPos = poPageInfo.getList();
            List<VoObject> retObj = new ArrayList<>(footPrintPos.size());

            //调用商品内部api
            List<Long> skuIds = footPrintPos.stream().map(po -> po.getGoodsSkuId()).collect(Collectors.toList());
            ReturnObject<List<GoodsSkuSimpleRetVo>> retGoodsSkus = null;
            if (skuIds.size() != 0){
                if(did == Long.valueOf(0))
                    //平台管理员
                    retGoodsSkus = goodsService.getGoodsSkuListById(skuIds);
                else
                    //店铺管理员
                    retGoodsSkus = goodsService.getGoodsSkuListBySkuIdAndShopId(did,skuIds);
            }
            if (retGoodsSkus == null || retGoodsSkus.getCode() == ResponseCode.OK) {
                if (retGoodsSkus != null) {
                    ListToMap listToMap = new ListToMap();
                    Map<Long, GoodsSkuSimpleRetVo> map = listToMap.GoodsSkuSimpleRetVoListToMap(retGoodsSkus.getData());
                    for (FootPrintPo po : footPrintPos) {
                        Footprint footprint = new Footprint(po);
                        footprint.setGoodsSku(map.get(po.getGoodsSkuId()));
                        retObj.add(footprint);
                    }
                }

                PageInfo<VoObject> footPrintsPage = new PageInfo<>(retObj);
                footPrintsPage.setPages(poPageInfo.getPages());
                footPrintsPage.setPageNum(poPageInfo.getPageNum());
                footPrintsPage.setPageSize(poPageInfo.getPageSize());
                footPrintsPage.setTotal(poPageInfo.getTotal());
                return new ReturnObject<>(footPrintsPage);
            } else {
                return new ReturnObject<>(retGoodsSkus.getCode());
            }
        } else {
            return new ReturnObject<>(ret.getCode());
        }
    }

    @Transactional
    @Override
    public ReturnObject<Object> insertFootprint(Long userId, Long skuId){
        ReturnObject<Object> ret = null;
        LocalDateTime now = LocalDateTime.now();
        FootPrintPo footprintPo = new FootPrintPo();
        footprintPo.setCustomerId(userId);
        footprintPo.setGoodsSkuId(skuId);
        footprintPo.setGmtCreate(now);
        ReturnObject<Object> ret2 = goodsService.checkSkuId(skuId);
        if(ret2.getCode() != ResponseCode.OK)
            return new ReturnObject<>(ret2.getCode());
        ret = footprintDao.insertFootprint(footprintPo);
        return ret;
    }

}
