package cn.edu.xmu.aftersales.dao;

import cn.edu.xmu.aftersales.mapper.AftersaleServicePoMapper;
import cn.edu.xmu.aftersales.model.bo.AftersalesBo;
import cn.edu.xmu.aftersales.model.bo.adminSaleBo;
import cn.edu.xmu.aftersales.model.po.AftersaleServicePo;
import cn.edu.xmu.aftersales.model.po.AftersaleServicePoExample;
import cn.edu.xmu.aftersales.model.vo.checkVo;
import cn.edu.xmu.aftersales.model.vo.confirmVo;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AftersalesDao implements InitializingBean {


    @Autowired
    private AftersaleServicePoMapper aftersaleServicePoMapper;

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    /**
     * 买家提交售后单 插入到数据库
     */
    public ReturnObject<AftersalesBo> insertSale(AftersalesBo aftersalesBo) {

        //调订单模块 看此orderitemId是否存在
        AftersaleServicePo po = aftersalesBo.gotSalePo();
        ReturnObject<AftersalesBo> retObj = null;
        try {
            int ret = aftersaleServicePoMapper.insertSelective(po);
            if (ret == 0) {
                //失败了
                //日志
                retObj = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("新增售后单失败" + po.getOrderItemId()));

            } else {
                //日志
                aftersalesBo.setId(po.getId());
                retObj = new ReturnObject<>(aftersalesBo);

            }
        } catch (DataAccessException e) {
            //日志
            retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));

        } catch (Exception e) {
            //日志
            retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        }
        return retObj;
    }


    /**
     * 买家查询所有售后单 调用订单模块
     */
    public ReturnObject<PageInfo<VoObject>> getSales(Long userId,Long spuId,Long skuId,String beginTime,String endTime,Integer page,Integer pageSize,Byte type,Byte state){
        AftersaleServicePoExample example=new AftersaleServicePoExample();
        AftersaleServicePoExample.Criteria criteria=example.createCriteria();


        criteria.andBeDeletedNotEqualTo((byte)1);

        criteria.andCustomerIdEqualTo(Long.valueOf(userId));

        List<Long> orderItemIds=new ArrayList<>();
        if(spuId!=null){
            //根据spuId查询orderItemId 订单模块

            orderItemIds.add(Long.valueOf(1));
            orderItemIds.add(Long.valueOf(2));
            orderItemIds.add(Long.valueOf(3));
            criteria.andOrderItemIdIn(orderItemIds);
        }if(skuId!=null){
            //根据skuId查询orderItemId  订单模块

            orderItemIds.add(Long.valueOf(4));
            criteria.andOrderItemIdIn(orderItemIds);
        }


        if(beginTime!=null&&!beginTime.equals(" ")){
            criteria.andGmtCreateGreaterThanOrEqualTo(LocalDateTime.parse(beginTime));
        }
        if(endTime!=null&&!endTime.equals(" ")){
            criteria.andGmtCreateLessThanOrEqualTo(LocalDateTime.parse(endTime));
        }
        if(type!=null)
            criteria.andTypeEqualTo(type);
        if(state!=null)
            criteria.andStateEqualTo(state);

        if(page!=null&&pageSize!=null)
        PageHelper.startPage(page,pageSize);
        List<AftersaleServicePo> pos=null;

        try{
            pos=aftersaleServicePoMapper.selectByExample(example);
        }catch (DataAccessException e){
//            logger.error("selectAllRole: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        }

        List<VoObject> ret=new ArrayList<>(pos.size());
        if(pos.size()==0){
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST,"不存在满足条件的售后单");
        }
        for(AftersaleServicePo sale:pos){
            Long orderItemId=sale.getOrderItemId();
            //订单模块 根据orderItemId查询spuId skuId orderId skuName
            Long skuid=Long.valueOf(1);
            Long orderId=Long.valueOf(1);
            String skuName="华为";

            AftersalesBo bo=new AftersalesBo(sale);
            bo.setOrderId(orderId);
            bo.setSkuId(skuid);
            bo.setSkuName(skuName);
            ret.add(bo);
        }
        PageInfo<VoObject> salePage=PageInfo.of(ret);
        return new ReturnObject<>(salePage);
    }


    /**
     * 管理员查看售后单
     */
    public ReturnObject<PageInfo<VoObject>> getSalesByShopId( Long shopId, Long spuId, Long skuId, String beginTime, String endTime, Integer page, Integer pageSize, Byte type, Byte state){
        AftersaleServicePoExample example=new AftersaleServicePoExample();
        AftersaleServicePoExample.Criteria criteria=example.createCriteria();

        criteria.andBeDeletedNotEqualTo((byte)1);

        criteria.andShopIdEqualTo(shopId);

        List<Long> orderItemIds=new ArrayList<>();
        if(spuId!=null){
            //根据spuId查询orderItemId 订单模块

            orderItemIds.add(Long.valueOf(1));
            orderItemIds.add(Long.valueOf(2));
            orderItemIds.add(Long.valueOf(3));
            criteria.andOrderItemIdIn(orderItemIds);
        }if(skuId!=null){
            //根据skuId查询orderItemId  订单模块

            orderItemIds.add(Long.valueOf(4));
            criteria.andOrderItemIdIn(orderItemIds);
        }


        if(beginTime!=null&&!beginTime.equals(" ")){
            criteria.andGmtCreateGreaterThanOrEqualTo(LocalDateTime.parse(beginTime));
        }
        if(endTime!=null&&!endTime.equals(" ")){
            criteria.andGmtCreateLessThanOrEqualTo(LocalDateTime.parse(endTime));
        }
        if(type!=null)
            criteria.andTypeEqualTo(type);
        if(state!=null)
            criteria.andStateEqualTo(state);

        if(page!=null&&pageSize!=null)
            PageHelper.startPage(page,pageSize);
        List<AftersaleServicePo> pos=null;

        try{
            pos=aftersaleServicePoMapper.selectByExample(example);
        }catch (DataAccessException e){
//            logger.error("selectAllRole: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        }

        if(pos.size()==0){
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST,"不存在满足条件的售后单");
        }
        List<VoObject> ret=new ArrayList<>(pos.size());
        for(AftersaleServicePo sale:pos){
            Long orderItemId=sale.getOrderItemId();
            //订单模块 根据orderItemId查询spuId skuId orderId skuName

            Long orderId=Long.valueOf(1);

           adminSaleBo bo=new adminSaleBo(sale);
            bo.setOrderId(orderId);
            ret.add(bo);
        }
        PageInfo<VoObject> salePage=PageInfo.of(ret);
        return new ReturnObject<>(salePage);
    }

    /**
     * 买家根据id查询售后单(只能查本人的售后单)
     */
    public ReturnObject<AftersalesBo> findById(Long userId, Long id) {

        ReturnObject<AftersalesBo> retObj = null;
        AftersaleServicePoExample example = new AftersaleServicePoExample();
        AftersaleServicePoExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(id);
        criteria.andBeDeletedEqualTo((byte) 0);
        try {
            List<AftersaleServicePo> po = aftersaleServicePoMapper.selectByExample(example);
            if (po.size() == 0) {
                return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
            } else {
                criteria.andCustomerIdEqualTo(userId);
                List<AftersaleServicePo> pos = aftersaleServicePoMapper.selectByExample(example);
                if(pos.size()==0){
                    return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE);
                }else{
                    for (AftersaleServicePo servicePo : pos) {
                        AftersalesBo bo = new AftersalesBo(servicePo);
                        //订单模块内部接口
                        Long orderItemId = servicePo.getOrderItemId();
                        Long orderId = Long.valueOf(1);
                        Long skuId = Long.valueOf(1);
                        String skuName = "手机";
                        String orderSn = "1111";
                        //组装bo
                        bo.setSkuId(skuId);
                        bo.setSkuName(skuName);
                        bo.setOrderId(orderId);
                        bo.setOrderSn(orderSn);
                        retObj = new ReturnObject<>(bo);
                    }
                }
            }
        } catch (DataAccessException e) {
            //日志
            retObj = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("数据库错误：%s", e.getMessage()));
        } catch (Exception e) {
            retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        }
        return retObj;
    }

    /**
     * 买家修改售后单信息
     */
    public ReturnObject<AftersalesBo> modify(Long id, Long userId, AftersalesBo bo) {
        AftersaleServicePo po = bo.gotSalePo();
        po.setState((byte) 0);
        ReturnObject<AftersalesBo> retObj = null;
        try {
            AftersaleServicePoExample example = new AftersaleServicePoExample();
            AftersaleServicePoExample.Criteria criteria = example.createCriteria();
            criteria.andBeDeletedNotEqualTo((byte)1);
            criteria.andIdEqualTo(id);

            List<AftersaleServicePo> pos=aftersaleServicePoMapper.selectByExample(example);
            if(pos.size()==0){
                return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
            }else{
                for(AftersaleServicePo sale:pos){
                    if(sale.getCustomerId()!=userId)
                        return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE);
                    else if(sale.getState()!=0&&sale.getState()!=1)
                        return new ReturnObject<>(ResponseCode.AFTERSALE_STATENOTALLOW);
                }
            }

            criteria.andCustomerIdEqualTo(userId);
            criteria.andStateBetween((byte)0,(byte)1);
            int ret = aftersaleServicePoMapper.updateByExampleSelective(po, example);
            if (ret == 0) {
                //日志
                retObj = new ReturnObject<>(ResponseCode.AFTERSALE_STATENOTALLOW);
            } else {
                retObj = new ReturnObject<>(ResponseCode.OK);
            }
        } catch (DataAccessException e) {
            retObj = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("数据库错误：%s", e.getMessage()));
        } catch (Exception e) {
            retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        }
        return retObj;
    }

    /**
     * 买家取消售后单或逻辑删除售后单
     */
    public ReturnObject<VoObject> cancelSale(Long userId, Long id) {
        //先根据条件查询 判断是否完成
        ReturnObject<VoObject> retObj = null;
        AftersaleServicePoExample example = new AftersaleServicePoExample();
        AftersaleServicePoExample.Criteria criteria = example.createCriteria();
        criteria.andBeDeletedNotEqualTo((byte)1);
        criteria.andIdEqualTo(id);
        try {
            List<AftersaleServicePo> pos = aftersaleServicePoMapper.selectByExample(example);
            if (pos.size() == 0) {
                retObj = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
                return retObj;
            } else {
                for (AftersaleServicePo sale : pos) {
                    //感觉不会有这种情况
                    if(sale.getCustomerId()!=userId)
                        return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE);
                    else {
                        if (sale.getState()==(byte)0||sale.getState()==(byte)1) //售后单还没有完成
                        {
                            sale.setState((byte) 7);
                        } else if(sale.getState()==(byte)8) {
                            sale.setBeDeleted((byte) 1);
                        }else{
                            return new ReturnObject<>(ResponseCode.AFTERSALE_STATENOTALLOW);
                        }
                        sale.setGmtModified(LocalDateTime.now());
                        criteria.andStateIn(List.of((byte)0,(byte)1,(byte)8));
                        int ret = aftersaleServicePoMapper.updateByExampleSelective(sale, example);
                        if (ret == 0) {
                            //日志
                            retObj = new ReturnObject<>(ResponseCode.AFTERSALE_STATENOTALLOW);
                        } else {
                            retObj = new ReturnObject<>(ResponseCode.OK);
                        }
                    }
                }
            }
        } catch (DataAccessException e) {
            retObj = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("数据库错误：%s", e.getMessage()));
        } catch (Exception e) {
            retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库发生错误：%s", e.getMessage()));
        }
        return retObj;
    }

    /**
     * 买家填写售后的运单信息
     */
    public ReturnObject<VoObject> CustomerLogSn(Long userId, Long id, String customerLogSn) {
        AftersaleServicePo po = new AftersaleServicePo();
        po.setCustomerLogSn(customerLogSn);
        po.setState((byte) 2);
        po.setGmtModified(LocalDateTime.now());

        ReturnObject<VoObject> retObj = null;
        AftersaleServicePoExample example = new AftersaleServicePoExample();
        AftersaleServicePoExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(id);
        criteria.andBeDeletedNotEqualTo((byte)1);
        try {
            List<AftersaleServicePo> pos = aftersaleServicePoMapper.selectByExample(example);
            if (pos.size() == 0) {
                retObj = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
                return retObj;
            } else{
                for(AftersaleServicePo sale:pos){
                    if(sale.getCustomerId()!=userId)
                        return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE);
                    if(sale.getBeDeleted()==(byte)1||sale.getState()!=(byte)1){
                        retObj = new ReturnObject<>(ResponseCode.AFTERSALE_STATENOTALLOW);
                        return retObj;
                    }
                }
            }

            criteria.andStateEqualTo((byte)1);
            int ret = aftersaleServicePoMapper.updateByExampleSelective(po, example);
            if (ret == 0) {
                //日志
                retObj = new ReturnObject<>(ResponseCode.AFTERSALE_STATENOTALLOW);
            } else {
                //日志
                retObj = new ReturnObject<>(ResponseCode.OK);
            }
        }catch (DataAccessException e) {
            retObj = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("数据库错误：%s", e.getMessage()));
        } catch (Exception e) {
            retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        }
        return retObj;

    }

    /**
     * 买家确认售后单结束
     */
    public ReturnObject<VoObject> confirm(Long userId, Long id) {


        AftersaleServicePoExample example = new AftersaleServicePoExample();
        AftersaleServicePoExample.Criteria criteria = example.createCriteria();
        criteria.andBeDeletedNotEqualTo((byte)1);
        criteria.andIdEqualTo(id);
        ReturnObject<VoObject> retObj = null;
        AftersaleServicePo po = new AftersaleServicePo();

        try {
            List<AftersaleServicePo> pos = aftersaleServicePoMapper.selectByExample(example);
            if (pos.size() == 0) {
                retObj = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
                return retObj;
            } else{
                for(AftersaleServicePo sale:pos){
                    if(sale.getCustomerId()!=userId)
                        return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE);
                    if(sale.getState()==(byte)3&&sale.getType()==(byte)1||sale.getState()==(byte)5&&sale.getType()!=(byte)1){
                        po.setState((byte) 8);
                        po.setGmtModified(LocalDateTime.now());
                    }else return new ReturnObject<>(ResponseCode.AFTERSALE_STATENOTALLOW);
                }
            }

            criteria.andStateBetween((byte)3,(byte)5);
            int ret = aftersaleServicePoMapper.updateByExampleSelective(po, example);
            if (ret == 0) {
                retObj = new ReturnObject<>(ResponseCode.AFTERSALE_STATENOTALLOW);
            } else {
                retObj = new ReturnObject<>(ResponseCode.OK);
            }
        } catch (DataAccessException e) {
            retObj = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("数据库发生错误:%s", e.getMessage()));
        } catch (Exception e) {
            retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库发生错误%s", e.getMessage()));
        }
        return retObj;
    }

    /**
     * 买家根据售后单id和店铺Id查询售后单信息
     */
    public ReturnObject<VoObject> search(Long userId, Long shopId, Long id) {
        AftersaleServicePoExample example = new AftersaleServicePoExample();
        AftersaleServicePoExample.Criteria criteria = example.createCriteria();


        criteria.andCustomerIdEqualTo(userId);
        criteria.andShopIdEqualTo(shopId);
        criteria.andIdEqualTo(id);

        ReturnObject<VoObject> retObj = null;
        try {
            List<AftersaleServicePo> po = aftersaleServicePoMapper.selectByExample(example);
            if (po.size() == 0) {
                retObj = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("查询售后单失败，id=%d,shopId=%d,customerId=%d", id, shopId, userId));
            } else {
                for (AftersaleServicePo sale : po) {
                    AftersalesBo bo = new AftersalesBo(sale);
                    //调用订单模块
                    Long orderItemId = sale.getOrderItemId();
                    //.......
                    String skuName = "手机";
                    Long skuId = Long.valueOf(1);
                    String orderSn = "1231212";
                    Long orderId = Long.valueOf(1);
                    bo.setSkuName(skuName);
                    bo.setSkuId(skuId);
                    bo.setOrderSn(orderSn);
                    bo.setOrderId(orderId);

                    retObj = new ReturnObject<>(bo);
                }
            }
        } catch (DataAccessException e) {
            //日志
            retObj = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("数据库错误：%s", e.getMessage()));
        } catch (Exception e) {
            retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        }
        return retObj;
    }

    /**
     * 管理员同意或不同意买家的售后申请
     */
    public ReturnObject<VoObject> agreeSale(Long shopId, Long id, checkVo vo) {
        Boolean confirm = vo.isConfirm();
        String conclusion = vo.getConclusion();
        Long price=vo.getPrice();

        AftersaleServicePoExample example = new AftersaleServicePoExample();
        AftersaleServicePoExample.Criteria criteria = example.createCriteria();
        criteria.andShopIdEqualTo(shopId);
        criteria.andIdEqualTo(id);
        try{
            List<AftersaleServicePo> pos=aftersaleServicePoMapper.selectByExample(example);
            if(pos.size()==0){
                return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST,String.format("此售后订单不存在"));
            }else{
                for(AftersaleServicePo po:pos){
                    if(po.getBeDeleted()==(byte)1||po.getState()!=0){
                        return new ReturnObject<>(ResponseCode.AFTERSALE_STATENOTALLOW);
                    }
                }
            }
        }catch (DataAccessException e) {
            //日志
           return  new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("数据库错误：%s", e.getMessage()));
        } catch (Exception e) {
           return  new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        }

        AftersaleServicePo po = new AftersaleServicePo();
        if (confirm == true) {
            po.setState((byte) 1);
            po.setConclusion(conclusion);
            if(price!=null){
                po.setRefund(price);
            }
        } else {
            po.setState((byte) 6);
            po.setConclusion(conclusion);
        }

        //防止其他进程在此之前修改
        criteria.andBeDeletedNotEqualTo((byte)1);
        criteria.andStateEqualTo((byte)0);

        ReturnObject<VoObject> retObj = null;
        try {
            int ret = aftersaleServicePoMapper.updateByExampleSelective(po, example);
            if (ret == 0) {
                retObj = new ReturnObject<>(ResponseCode.AFTERSALE_STATENOTALLOW, String.format("管理员审核售后申请失败，id=%d,conclusion=%s", id, conclusion));
            } else {
                retObj = new ReturnObject<>(ResponseCode.OK);
            }
        } catch (DataAccessException e) {
            //日志
            retObj = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("数据库错误：%s", e.getMessage()));
        } catch (Exception e) {
            retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        }
        return retObj;
    }

    /**
     * 店家确认收到买家的退（换）货
     * 如果是退款，则退款给用户，如果换货则产生一个新订单，如果是维修则等待下一个动作
     */
    public ReturnObject<VoObject> confirmReceive(Long shopId,Long id,confirmVo vo){
        AftersaleServicePoExample example=new AftersaleServicePoExample();
        AftersaleServicePoExample.Criteria criteria=example.createCriteria();

        criteria.andShopIdEqualTo(shopId);
        criteria.andIdEqualTo(id);
        criteria.andBeDeletedNotEqualTo((byte)1);
        ReturnObject<VoObject> retObj=null;
        try{
            List<AftersaleServicePo> po=aftersaleServicePoMapper.selectByExample(example);
            if(po.size()==0){
                retObj=new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST,String.format("此售后单不存在，id=%d,shopId=%d",id,shopId));
            }else{
                for(AftersaleServicePo sale:po){
                    if(sale.getState()!=2){
                        return new ReturnObject<>(ResponseCode.AFTERSALE_STATENOTALLOW);
                    }
                    if(vo.isConfirm()) {
                        if (sale.getType() == (byte) 0) {
                            sale.setState((byte) 4);
                            sale.setConclusion(vo.getConclusion());
                        } else {
                            sale.setState((byte) 3);
                            sale.setConclusion(vo.getConclusion());
                        }
                        sale.setGmtModified(LocalDateTime.now());
                    }else {
                        sale.setConclusion(vo.getConclusion());
                        sale.setState((byte)1);
                    }
                    criteria.andStateEqualTo((byte)2);
                    int ret=aftersaleServicePoMapper.updateByExampleSelective(sale,example);
                    if(ret==0){
                        retObj=new ReturnObject<>(ResponseCode.AFTERSALE_STATENOTALLOW,String.format("店家确认收货失败，id=%d,shopId=%d",id,shopId));
                    }else{
                        retObj=new ReturnObject<>(ResponseCode.OK);
                    }
                }
            }
        }catch (DataAccessException e) {
            //日志
            retObj = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("数据库错误：%s", e.getMessage()));
        } catch (Exception e) {
            retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        }
        return retObj;
    }

    /**
     * 店家寄出维修好（调换）的货物
     */
    public ReturnObject<VoObject> deliverProduct(Long shopId,Long id,String logSn){
        AftersaleServicePo po=new AftersaleServicePo();
        po.setShopLogSn(logSn);
        po.setState((byte)5);

        AftersaleServicePoExample example=new AftersaleServicePoExample();
        AftersaleServicePoExample.Criteria criteria=example.createCriteria();

        criteria.andBeDeletedNotEqualTo((byte)1);
        criteria.andIdEqualTo(id);
        criteria.andShopIdEqualTo(shopId);

        ReturnObject<VoObject> retObj=null;
        try {

            List<AftersaleServicePo> pos = aftersaleServicePoMapper.selectByExample(example);
            if (pos.size() == 0) {
                retObj = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("此售后单不存在，id=%d,shopId=%d", id, shopId));
            } else {
                for (AftersaleServicePo sale : pos) {
                    if (sale.getState() != 4) {
                        return new ReturnObject<>(ResponseCode.AFTERSALE_STATENOTALLOW);
                    } else {
                        criteria.andStateEqualTo((byte) 4);
                        int ret = aftersaleServicePoMapper.updateByExampleSelective(po, example);
                        if (ret == 0) {
                            retObj = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("店家寄出商品失败，id=%d,logSn=%s", id, logSn));
                        } else {
                            retObj = new ReturnObject<>(ResponseCode.OK);
                        }
                    }
                }
            }
        } catch (DataAccessException e) {
            //日志
            retObj = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("数据库错误：%s", e.getMessage()));
        } catch (Exception e) {
            retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        }
        return retObj;
    }
}
