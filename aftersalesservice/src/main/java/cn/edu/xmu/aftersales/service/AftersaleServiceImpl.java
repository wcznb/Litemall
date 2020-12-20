package cn.edu.xmu.aftersales.service;

import cn.edu.xmu.aftersales.dao.AftersalesDao;
import cn.edu.xmu.aftersales.model.bo.AftersalesBo;
import cn.edu.xmu.aftersales.model.bo.querySaleBo;
import cn.edu.xmu.aftersales.model.po.AftersaleServicePo;
import cn.edu.xmu.aftersales.model.vo.*;
import cn.edu.xmu.aftersales.util.ExchangeDate;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.provider.server.OrderService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AftersaleServiceImpl implements AfterSaleService{

    @Autowired
    private AftersalesDao aftersalesDao;

    @DubboReference(version = "1.1.2")
    OrderService orderService;
    @Transactional
    @Override
    public ReturnObject<VoObject> newSale(Long userId, Long orderItemId,NewSaleVo newSaleVo) {


        AftersalesBo aftersalesBo = newSaleVo.createAfterSale();
        //内部接口
        ReturnObject<Boolean> validItem = orderService.validItem(orderItemId);

        if (validItem.getCode() != ResponseCode.OK) {
            return new ReturnObject(validItem.getCode(), validItem.getErrmsg());
        }
        if(validItem.getData()==false)
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST,String.format("orderItemId不存在"));
        else {

            Byte orderState=orderService.checkState(orderItemId);
            if (orderState==null) {
                return new ReturnObject(ResponseCode.INTERNAL_SERVER_ERR,String.format("内部接口出错"));
            }
            if(orderState!=(byte)3)
                return new ReturnObject<>(ResponseCode.ORDER_STATENOTALLOW,String.format("order未完成"));

            ReturnObject<Long> cusId = orderService.getUserIdByOrderItemId(orderItemId);
            if (cusId.getCode() != ResponseCode.OK)
                return new ReturnObject(cusId.getCode(), cusId.getErrmsg());
            else {
                Long customerId = cusId.getData();
                if (!customerId.equals(userId))
                    return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE);
                else {
//                    ReturnObject<Long> quantity=orderService.getnumber(orderItemId);
                    ReturnObject<Long> orderId = orderService.getOrderIdByOrderItemId(orderItemId);
                    ReturnObject<Long> shopId = orderService.getShopIdByOrderId(orderId.getData());
                    ReturnObject<Long> skuId = orderService.getGoodsSkuIdByOrderItemId(orderItemId);
                    ReturnObject<String> skuName = orderService.getSkuName(orderItemId);
                    if (orderId.getCode() != ResponseCode.OK || shopId.getCode() != ResponseCode.OK || skuId.getCode() != ResponseCode.OK || skuName.getCode() != ResponseCode.OK)
                        return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR,String.format("内部接口出错"));///?
//                    if(newSaleVo.getQuantity()>quantity.getData())
//                        return new ReturnObject<>(ResponseCode.FIELD_NOTVALID,String.format("数量超出！"));
                    String serviceSn = UUID.randomUUID().toString().replace("-", "");

                    aftersalesBo.setServiceSn(serviceSn);
                    aftersalesBo.setOrderItemId(orderItemId);
                    aftersalesBo.setSkuId(skuId.getData());
                    aftersalesBo.setSkuName(skuName.getData());
                    aftersalesBo.setShopId(shopId.getData());
                    aftersalesBo.setCustomerId(userId);
                    aftersalesBo.setState(AftersalesBo.State.getTypeByCode(0));
                    aftersalesBo.setBeDeleted((byte) 0);
                    aftersalesBo.setGmtCreated(LocalDateTime.now());
                    aftersalesBo.setGmtModified(LocalDateTime.now());
                    ReturnObject<AftersalesBo> retObj = aftersalesDao.insertSale(aftersalesBo);

                    ReturnObject<VoObject> retSale = null;
                    if (retObj.getCode().equals(ResponseCode.OK)) {
                        retSale = new ReturnObject<>(retObj.getData());
                    } else {
                        retSale = new ReturnObject<>(retObj.getCode(), retObj.getErrmsg());
                    }
                    return retSale;
                }
            }
        }
    }

    @Override
    public ReturnObject<PageInfo<VoObject>> getSale(Long userId,String beginTime,String endTime, Integer page, Integer pageSize, Byte type, Byte state) {

        LocalDateTime begintime=null;
        LocalDateTime endtime=null;
        ReturnObject<PageInfo<VoObject>> rett=null;
        if(!beginTime.equals(""))  {
            begintime = ExchangeDate.StringToDateTime(beginTime).get(true);
            if(ExchangeDate.StringToDateTime(beginTime).containsKey(false))
                return new ReturnObject<>(ResponseCode.FIELD_NOTVALID);
        }
        if(!endTime.equals("")){
            endtime = ExchangeDate.StringToDateTime(endTime).get(true);
            if(ExchangeDate.StringToDateTime(endTime).containsKey(false))
                return new ReturnObject<>(ResponseCode.FIELD_NOTVALID);
        }


//        if(!beginTime.equals("")&&!endTime.equals("")&&begintime.isAfter(endtime))
//            return new ReturnObject<>(ResponseCode.FIELD_NOTVALID);

        ReturnObject<List<querySaleBo>> afterSaleBos=aftersalesDao.getSales(userId, begintime, endtime, page, pageSize, type, state);
        if(afterSaleBos.getCode()== ResponseCode.OK){
            List<querySaleBo> bos = afterSaleBos.getData();
            List<VoObject> ret=new ArrayList<>(bos.size());
            for(querySaleBo bo:bos){
                ret.add(bo);
            }
            PageInfo<VoObject> salePage=PageInfo.of(ret);
            salePage.setPageNum(page);
            salePage.setPageSize(pageSize);
            return new ReturnObject<>(salePage);
        }else{
            return new ReturnObject<>(afterSaleBos.getCode());
        }
    }

    @Override
    public ReturnObject<PageInfo<VoObject>> getSaleByShop(Long shopId,  String beginTime, String endTime, Integer page, Integer pageSize, Byte type, Byte state) {

        LocalDateTime begintime=null;
        LocalDateTime endtime=null;
        if(!beginTime.equals(""))  {
            begintime = ExchangeDate.StringToDateTime(beginTime).get(true);
            if(ExchangeDate.StringToDateTime(beginTime).containsKey(false))
                return new ReturnObject<>(ResponseCode.FIELD_NOTVALID);
        }
        if(!endTime.equals("")){
            endtime = ExchangeDate.StringToDateTime(endTime).get(true);
            if(ExchangeDate.StringToDateTime(endTime).containsKey(false))
                return new ReturnObject<>(ResponseCode.FIELD_NOTVALID);
        }
//        if(!beginTime.equals("")&&!endTime.equals("")&&begintime.isAfter(endtime))
//            return new ReturnObject<>(ResponseCode.FIELD_NOTVALID);

        ReturnObject<List<querySaleBo>> afterSaleBos=aftersalesDao.getSalesByShopId(shopId,begintime, endtime, page, pageSize, type, state);
        if(afterSaleBos.getCode()== ResponseCode.OK){
            List<querySaleBo> bos = afterSaleBos.getData();
            List<VoObject> ret=new ArrayList<>(bos.size());
            for(querySaleBo bo:bos){
                ret.add(bo);
            }
            PageInfo<VoObject> salePage=PageInfo.of(ret);
            salePage.setPageNum(page);
            salePage.setPageSize(pageSize);
            return new ReturnObject<>(salePage);
        }else{
            return new ReturnObject<>(afterSaleBos.getCode());
        }
    }

    @Override
    public ReturnObject<VoObject> searchSaleById(Long userId,Long id){
        ReturnObject<AftersalesBo> afterSaleBo=aftersalesDao.findById(userId,id);

        ReturnObject<VoObject> ret=null;
        if(afterSaleBo.getCode().equals(ResponseCode.OK)){
            AftersalesBo sale=afterSaleBo.getData();
            ReturnObject<Long> skuId=orderService.getGoodsSkuIdByOrderItemId(sale.getOrderItemId());
            ReturnObject<String> skuName=orderService.getSkuName(sale.getOrderItemId());
            if (skuId.getCode() != ResponseCode.OK || skuName.getCode() != ResponseCode.OK)
                return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR,"内部接口出错");///?

            sale.setSkuId(skuId.getData());
            sale.setSkuName(skuName.getData());
            ret=new ReturnObject<>(sale);

        }else {
            ret=new ReturnObject<>(afterSaleBo.getCode(),afterSaleBo.getErrmsg());
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
    public ReturnObject<VoObject> searchSale(Long shopId,Long id) {
        ReturnObject<AftersalesBo> retObj=aftersalesDao.search(shopId,id);
        if(retObj.getCode()!=ResponseCode.OK){
            return new ReturnObject<>(retObj.getCode(),retObj.getErrmsg());
        }else{
            AftersalesBo sale=retObj.getData();
            ReturnObject<Long> skuId=orderService.getGoodsSkuIdByOrderItemId(sale.getOrderItemId());
            ReturnObject<String> skuName=orderService.getSkuName(sale.getOrderItemId());
            if (skuId.getCode() != ResponseCode.OK || skuName.getCode() != ResponseCode.OK)
                return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR,String.format("内部接口出错"));///?
            sale.setSkuId(skuId.getData());
            sale.setSkuName(skuName.getData());
            return new ReturnObject<>(sale);
        }
    }

    @Override
    public ReturnObject<VoObject> agreeRequest(Long shopId, Long id, checkVo vo) {
//        Boolean confirm=vo.isConfirm();
//        if(confirm==false){
//            if(vo.getType()!=null){
//                return new ReturnObject<>(ResponseCode.FIELD_NOTVALID);
//            }
//        }
        ReturnObject<AftersaleServicePo> po=aftersalesDao.selectSale(id);
        if(po.getCode()!=ResponseCode.OK)
            return new ReturnObject<>(po.getCode(),po.getErrmsg());
        else {
            AftersaleServicePo sale = po.getData();
            ReturnObject<Long> cusId = orderService.getUserIdByOrderItemId(sale.getOrderItemId());
            if (cusId.getCode() != ResponseCode.OK)
                return new ReturnObject(cusId.getCode(),"内部接口出错惹");
            else {
                Long customerId = cusId.getData();
                if (!customerId.equals(sale.getCustomerId()))
                    return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE);
                ReturnObject<VoObject> ret = aftersalesDao.agreeSale(shopId, id, vo);
                return ret;
            }
        }
    }

    @Override
    public ReturnObject<VoObject> receive(Long shopId, Long id, confirmVo vo) {
        ReturnObject<AftersaleServicePo> ret=aftersalesDao.confirmReceive(shopId, id, vo);
        ReturnObject<VoObject> returnObject=null;
        if(ret.getCode()!=ResponseCode.OK)
            return new ReturnObject<>(ret.getCode(),ret.getErrmsg());
        else{
            AftersaleServicePo sale=ret.getData();
            if(vo.isConfirm()) {
                if (sale.getType() == (byte) 0||sale.getType()==(byte)2) {
                    sale.setState((byte) 4);
                    sale.setConclusion(vo.getConclusion());
                } else {
                    sale.setState((byte) 3);
                    sale.setConclusion(vo.getConclusion());
                    boolean innner=orderService.createRefund(sale.getId(),sale.getOrderItemId(),sale.getQuantity(),sale.getShopId());
                    if (innner==false)
                        return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR,String.format("内部接口出错"));///?

                }
                sale.setGmtModified(LocalDateTime.now());
            }else {
                sale.setConclusion(vo.getConclusion());
                sale.setState((byte)1);
            }
            returnObject=aftersalesDao.updateRecevice(sale);
        }
        return returnObject;
    }

    @Override
    public ReturnObject<VoObject> deliver(Long shopId, Long id, shopLogSnVo vo) {
        ReturnObject<AftersalesBo> ret=aftersalesDao.deliverProduct(shopId, id);
        if(ret.getCode()!=ResponseCode.OK){
            return new ReturnObject<>(ret.getCode(),ret.getErrmsg());
        }else{
            AftersalesBo bo=ret.getData();
            if(bo.getType().getCode()==0){//换货
                Long orderId=orderService.NewShopOrder(bo.getConsignee(),bo.getRegionId(),bo.getDetail(),bo.getMobile(),bo.getOrderItemId(),bo.getQuantity());
                if(orderId==null)
                    return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR,String.format("内部接口出错"));
                else{
//                    if(vo.getShopLogSn()!=null)
//                        return new ReturnObject<>(ResponseCode.FIELD_NOTVALID);
                    bo.setOrderId(orderId);
                    bo.setState(AftersalesBo.State.getTypeByCode(5));
                    bo.setGmtModified(LocalDateTime.now());
                }
            }else if(bo.getType().getCode()==2){

                boolean innerPay=orderService.createPay(bo.getId(),bo.getRefund());
                if(innerPay==false)
                    return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR,"内部接口出错");
                if(vo.getShopLogSn()==null)
                    return new ReturnObject<>(ResponseCode.FIELD_NOTVALID);
                bo.setShopLogSn(vo.getShopLogSn());
                bo.setState(AftersalesBo.State.getTypeByCode(5));
                bo.setGmtModified(LocalDateTime.now());
            }
            ReturnObject<VoObject> retObj=aftersalesDao.updateAfterSale(bo);
            return retObj;
        }
    }
}
