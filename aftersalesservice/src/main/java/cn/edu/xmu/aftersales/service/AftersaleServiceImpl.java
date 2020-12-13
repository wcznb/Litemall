package cn.edu.xmu.aftersales.service;

import cn.edu.xmu.aftersales.dao.AftersalesDao;
import cn.edu.xmu.aftersales.model.bo.AftersalesBo;
import cn.edu.xmu.aftersales.model.po.AftersaleServicePoExample;
import cn.edu.xmu.aftersales.model.vo.NewSaleVo;
import cn.edu.xmu.aftersales.model.vo.UpdateVo;
import cn.edu.xmu.aftersales.model.vo.checkVo;
import cn.edu.xmu.aftersales.model.vo.confirmVo;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AftersaleServiceImpl implements AfterSaleService{

    @Autowired
    private AftersalesDao aftersalesDao;
    @Transactional
    @Override
    public ReturnObject<VoObject> newSale(Long userId, Long orderItemId,NewSaleVo newSaleVo){


        AftersalesBo aftersalesBo=newSaleVo.createAfterSale();
        //内部接口

        //如果没有此orderItemId
        if(1==0)
        return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST,String.format("此orderItemId不存在"));

        Long orderId=Long.valueOf(1);
        Long shopId=Long.valueOf(1);//现在是自己假设数据

        Long skuId=Long.valueOf(1);
        String orderSn="1111";
        String skuName="手机";
        String serviceSn= UUID.randomUUID().toString().replace("-", "");

        aftersalesBo.setServiceSn(serviceSn);
        aftersalesBo.setOrderId(orderId);
        aftersalesBo.setOrderItemId(orderItemId);
        aftersalesBo.setSkuId(skuId);
        aftersalesBo.setSkuName(skuName);
        aftersalesBo.setShopId(shopId);
        aftersalesBo.setOrderSn(orderSn);
        aftersalesBo.setCustomerId(userId);
        aftersalesBo.setState(AftersalesBo.State.getTypeByCode(0));
        aftersalesBo.setBeDeleted((byte)0);
        aftersalesBo.setGmtCreated(LocalDateTime.now());
        aftersalesBo.setGmtModified(LocalDateTime.now());
        ReturnObject<AftersalesBo> retObj=aftersalesDao.insertSale(aftersalesBo);

        ReturnObject<VoObject> retSale=null;
        if(retObj.getCode().equals(ResponseCode.OK)){
            retSale=new ReturnObject<>(retObj.getData());
        }else {
            retSale=new ReturnObject<>(retObj.getCode(),retObj.getErrmsg());
        }
        return retSale;
    }


    @Override
    public ReturnObject<PageInfo<VoObject>> getSale(Long userId, Long spuId,Long skuId,String beginTime,String endTime, Integer page, Integer pageSize, Byte type, Byte state) {
        ReturnObject<PageInfo< VoObject>> ret=aftersalesDao.getSales(userId, spuId, skuId, beginTime, endTime, page, pageSize, type, state);
        return ret;
    }

    @Override
    public ReturnObject<PageInfo<VoObject>> getSaleByShop(Long shopId, Long spuId, Long skuId, String beginTime, String endTime, Integer page, Integer pageSize, Byte type, Byte state) {
        ReturnObject<PageInfo<VoObject>> ret=aftersalesDao.getSalesByShopId( shopId, spuId, skuId, beginTime, endTime, page, pageSize, type, state);
        return ret;
    }

    @Override
    public ReturnObject<VoObject> searchSaleById(Long userId,Long id){
        ReturnObject<AftersalesBo> retObj=aftersalesDao.findById(userId,id);

        ReturnObject<VoObject> ret=null;
        if(retObj.getCode().equals(ResponseCode.OK)){
            ret=new ReturnObject<>(retObj.getData());
        }else {
            ret=new ReturnObject<>(retObj.getCode(),retObj.getErrmsg());
        }
        return ret;
    }

    @Transactional
    @Override
    public ReturnObject<VoObject> modifySale(Long userId, Long id, UpdateVo newVo){
        AftersalesBo bo=newVo.createAfterSale();
        bo.setGmtModified(LocalDateTime.now());
        ReturnObject<AftersalesBo> retObj=aftersalesDao.modify(id,userId,bo);

        ReturnObject<VoObject> ret=null;
        if(retObj.getCode().equals(ResponseCode.OK)){
            ret=new ReturnObject<>(retObj.getData());
        }else {
            ret=new ReturnObject<>(retObj.getCode(),retObj.getErrmsg());
        }
        return ret;
    }

    @Transactional
    @Override
    public ReturnObject<VoObject> deleteSale(Long userId,Long id){
        return aftersalesDao.cancelSale(userId,id);
    }

    @Transactional
    @Override
    public ReturnObject<VoObject> customerLogSn(Long userId,Long id,String LogSn){
        ReturnObject<VoObject> retObj=aftersalesDao.CustomerLogSn(userId, id, LogSn);
        return retObj;
    }

    @Override
    public ReturnObject<VoObject> confirmOver(Long userId, Long id) {
        ReturnObject<VoObject> ret=aftersalesDao.confirm(userId,id);
        return ret;
    }

    @Override
    public ReturnObject<VoObject> searchSale(Long userId, Long shopId,Long id) {
        ReturnObject<VoObject> retObj=aftersalesDao.search(userId,shopId,id);
        return retObj;
    }

    @Override
    public ReturnObject<VoObject> agreeRequest(Long shopId, Long id, checkVo vo) {
        ReturnObject<VoObject> ret=aftersalesDao.agreeSale(shopId, id, vo);
        return ret;
    }

    @Override
    public ReturnObject<VoObject> receive(Long shopId, Long id, confirmVo vo) {
        ReturnObject<VoObject> ret=aftersalesDao.confirmReceive(shopId, id, vo);
        return ret;
    }

    @Override
    public ReturnObject<VoObject> deliver(Long shopId, Long id, String logSn) {
        ReturnObject<VoObject> ret=aftersalesDao.deliverProduct(shopId, id, logSn);
        return ret;
    }
}
