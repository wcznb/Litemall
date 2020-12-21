package cn.edu.xmu.aftersales.dao;

import cn.edu.xmu.aftersales.mapper.AftersaleServicePoMapper;
import cn.edu.xmu.aftersales.mapper.OrderItemPoMapper;
import cn.edu.xmu.aftersales.mapper.OrderPoMapper;
import cn.edu.xmu.aftersales.mapper.RefundPoMapper;
import cn.edu.xmu.aftersales.model.bo.AftersalesBo;
import cn.edu.xmu.aftersales.model.bo.Order;
import cn.edu.xmu.aftersales.model.bo.Refund;
import cn.edu.xmu.aftersales.model.bo.querySaleBo;
import cn.edu.xmu.aftersales.model.po.*;
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

    @Autowired
    private OrderPoMapper orderPoMapper;

    @Autowired
    private OrderItemPoMapper orderItemPoMapper;

    @Autowired
    private RefundPoMapper refundPoMapper;

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    /**
     * 买家提交售后单 插入到数据库
     */
    public ReturnObject<AftersalesBo> insertSale(AftersalesBo aftersalesBo) {


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
     * 买家查询所有售后单
     */
    public ReturnObject<List<querySaleBo>> getSales(Long userId,LocalDateTime beginTime,LocalDateTime endTime,Integer page,Integer pageSize,Byte type,Byte state){
        AftersaleServicePoExample example=new AftersaleServicePoExample();
        AftersaleServicePoExample.Criteria criteria=example.createCriteria();

        criteria.andBeDeletedNotEqualTo((byte)1);
        criteria.andCustomerIdEqualTo(userId);

        if(beginTime!=null){
            criteria.andGmtCreateGreaterThanOrEqualTo(beginTime);
        }
        if(endTime!=null){
            criteria.andGmtCreateLessThanOrEqualTo(beginTime);
        }
        if(type!=null)
            criteria.andTypeEqualTo(type);
        if(state!=null)
            criteria.andStateEqualTo(state);

        if(page!=null&&pageSize!=null)
        PageHelper.startPage(page,pageSize);
        List<AftersaleServicePo> pos=null;
        ReturnObject<List<querySaleBo>> ret=null;

        try{
            pos=aftersaleServicePoMapper.selectByExample(example);

        }catch (DataAccessException e){
//            logger.error("selectAllRole: DataAccessException:" + e.getMessage());
            ret= new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        }

            List<querySaleBo> bos=new ArrayList<>(pos.size());
            for (AftersaleServicePo sale : pos) {
                querySaleBo bo = new querySaleBo(sale);
                bos.add(bo);
            }
            ret=new ReturnObject<>(bos);

        return ret;
    }


    /**
     * 管理员查看售后单
     */
    public ReturnObject<List<querySaleBo>> getSalesByShopId(Long shopId, LocalDateTime beginTime, LocalDateTime endTime, Integer page, Integer pageSize, Byte type, Byte state){
        AftersaleServicePoExample example=new AftersaleServicePoExample();
        AftersaleServicePoExample.Criteria criteria=example.createCriteria();

        criteria.andBeDeletedNotEqualTo((byte)1);
        if(shopId!=0)
            criteria.andShopIdEqualTo(shopId);

        if(beginTime!=null){
            criteria.andGmtCreateGreaterThanOrEqualTo(beginTime);
        }
        if(endTime!=null){
            criteria.andGmtCreateLessThanOrEqualTo(endTime);
        }
        if(type!=null)
            criteria.andTypeEqualTo(type);
        if(state!=null)
            criteria.andStateEqualTo(state);

        if(page!=null&&pageSize!=null)
            PageHelper.startPage(page,pageSize);

        List<AftersaleServicePo> pos=null;
        ReturnObject<List<querySaleBo>> ret=null;
        try{
            pos=aftersaleServicePoMapper.selectByExample(example);

        }catch (DataAccessException e){
//            logger.error("selectAllRole: DataAccessException:" + e.getMessage());
            ret= new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        }

            List<querySaleBo> bos=new ArrayList<>(pos.size());
            for (AftersaleServicePo sale : pos) {
                querySaleBo bo = new querySaleBo(sale);
                bos.add(bo);
            }
            ret=new ReturnObject<>(bos);

        return ret;
    }

    /**
     * 买家根据id查询售后单(只能查本人的售后单)
     */
    public ReturnObject<AftersalesBo> findById(Long userId, Long id) {

        ReturnObject<AftersalesBo> retObj = null;

        try {
            AftersaleServicePo po = aftersaleServicePoMapper.selectByPrimaryKey(id);
            if (po==null||po.getBeDeleted()==1) {
                return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
            } else {
                if(!po.getCustomerId().equals(userId))
                    return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE);
                else{
                        AftersalesBo bo = new AftersalesBo(po);
                        retObj = new ReturnObject<>(bo);
                }
            }
        } catch (DataAccessException e) {
            //日志
            retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
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
        po.setGmtModified(LocalDateTime.now());
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
                    if(!sale.getCustomerId().equals(userId))
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
            retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
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
                    if(!sale.getCustomerId().equals(userId))
                        return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE);
                    else {
                        if (sale.getState()==(byte)0||sale.getState()==(byte)1) //售后单还没有完成
                        {
                            sale.setState((byte) 7);
                        } else if(sale.getState()==(byte)8||sale.getState()==(byte)7) {
                            sale.setBeDeleted((byte) 1);
                        }else{
                            return new ReturnObject<>(ResponseCode.AFTERSALE_STATENOTALLOW);
                        }
                        sale.setGmtModified(LocalDateTime.now());
                        criteria.andStateIn(List.of((byte)0,(byte)1,(byte)8,(byte)7));
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
            retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
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
                    if(!sale.getCustomerId().equals(userId))
                        return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE);
                    if(sale.getState()!=(byte)1){
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
            retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
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
                    if(!sale.getCustomerId().equals(userId))
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
            retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库发生错误:%s", e.getMessage()));
        } catch (Exception e) {
            retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库发生错误%s", e.getMessage()));
        }
        return retObj;
    }

    /**
     * 管理员根据售后单id和店铺Id查询售后单信息
     */
    public ReturnObject<AftersalesBo> search(Long shopId, Long id) {
        AftersaleServicePoExample example = new AftersaleServicePoExample();
        AftersaleServicePoExample.Criteria criteria = example.createCriteria();

        criteria.andBeDeletedNotEqualTo((byte)1);
        criteria.andIdEqualTo(id);

        ReturnObject<AftersalesBo> retObj = null;
        try {
            List<AftersaleServicePo> po = aftersaleServicePoMapper.selectByExample(example);
            if (po.size() == 0) {
                retObj = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("查询售后单失败，id=%d,shopId=%d", id, shopId));
            } else {
                for (AftersaleServicePo sale : po) {
                    if(sale.getShopId()!=shopId)
                        return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE);
                    AftersalesBo bo = new AftersalesBo(sale);
                    retObj = new ReturnObject<>(bo);
                }
            }
        } catch (DataAccessException e) {
            //日志
            retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
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
        Byte newType=vo.getType();


        AftersaleServicePoExample example = new AftersaleServicePoExample();
        AftersaleServicePoExample.Criteria criteria = example.createCriteria();
        criteria.andBeDeletedNotEqualTo((byte)1);
        criteria.andIdEqualTo(id);
        try{
            List<AftersaleServicePo> pos=aftersaleServicePoMapper.selectByExample(example);
            if(pos.size()==0){
                return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST,String.format("此售后订单不存在"));
            }else{
                for(AftersaleServicePo po:pos){
                    if(po.getShopId()!=shopId)
                        return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE);
                    if(po.getState()!=0)
                        return new ReturnObject<>(ResponseCode.AFTERSALE_STATENOTALLOW);
//                    if(vo.isConfirm()) {

//                        if (((po.getType() == 1 || newType == 1) && (price == null || price >= 0)) || ((po.getType() == 2 || newType == 2) && (price == null || price <= 0))||((po.getType()==0||newType==0)&&(price!=null||price!=0)))//不是修改为维修 价格不对
//                            return new ReturnObject<>(ResponseCode.FIELD_NOTVALID);
//                        }
                    }
                }
        }catch (DataAccessException e) {
            //日志
           return  new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        } catch (Exception e) {
           return  new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        }

        AftersaleServicePo po = new AftersaleServicePo();
        if (confirm == true) {
            if(po.getType()!=null){
                po.setType(vo.getType());
            }
            po.setState((byte) 1);
            po.setConclusion(conclusion);
            if(price!=null){
                po.setRefund(price);
            }
        } else {
            po.setState((byte) 6);
            if(vo.getPrice()!=null){
                po.setRefund(vo.getPrice());
            }
            po.setConclusion(conclusion);
        }

        //防止其他进程在此之前修改
        criteria.andStateEqualTo((byte)0);
        po.setGmtModified(LocalDateTime.now());
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
            retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        } catch (Exception e) {
            retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        }
        return retObj;
    }

    /**
     * 店家确认收到买家的退（换）货
     * 如果是退款，内部接口调用退款单，则退款给用户；如果换货则发货时产生一个新订单，如果是维修则等待下一个动作
     */
    public ReturnObject<AftersaleServicePo> confirmReceive(Long shopId,Long id,confirmVo vo){
        AftersaleServicePoExample example=new AftersaleServicePoExample();
        AftersaleServicePoExample.Criteria criteria=example.createCriteria();

        criteria.andIdEqualTo(id);
        criteria.andBeDeletedNotEqualTo((byte)1);
        ReturnObject<AftersaleServicePo> retObj=null;
        try{
            List<AftersaleServicePo> po=aftersaleServicePoMapper.selectByExample(example);
            if(po.size()==0){
                retObj=new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST,String.format("此售后单不存在，id=%d,shopId=%d",id,shopId));
            }else{
                for(AftersaleServicePo sale:po){
                    if(sale.getShopId()!=shopId)
                        return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE);
                    if(sale.getState()!=2){
                        return new ReturnObject<>(ResponseCode.AFTERSALE_STATENOTALLOW);
                    }
                   return new ReturnObject<>(sale);
                }
            }
        }catch (DataAccessException e) {
            //日志
            retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        } catch (Exception e) {
            retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        }
       return retObj;
    }

    /**
     * 店家寄出维修好（调换）的货物
     */
    public ReturnObject<AftersalesBo> deliverProduct(Long shopId,Long id){


        AftersaleServicePoExample example=new AftersaleServicePoExample();
        AftersaleServicePoExample.Criteria criteria=example.createCriteria();
        criteria.andBeDeletedNotEqualTo((byte)1);
        criteria.andIdEqualTo(id);

        ReturnObject<AftersalesBo> retObj=null;
        try {

            List<AftersaleServicePo> pos = aftersaleServicePoMapper.selectByExample(example);
            if (pos.size() == 0) {
                retObj = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("此售后单不存在，id=%d,shopId=%d", id, shopId));
            } else {
                for (AftersaleServicePo sale : pos) {
                    if(sale.getShopId()!=shopId)
                        return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE);
                    if (sale.getState() != 4) {
                        return new ReturnObject<>(ResponseCode.AFTERSALE_STATENOTALLOW);
                    } else {
                        return new ReturnObject<>(new AftersalesBo(sale));
                        }
                    }
                }
            } catch (DataAccessException e) {
            //日志
            retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        } catch (Exception e) {
            retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        }
        return retObj;
    }

    public ReturnObject<VoObject> updateRecevice(AftersaleServicePo po){

        AftersaleServicePoExample example=new AftersaleServicePoExample();
        AftersaleServicePoExample.Criteria criteria=example.createCriteria();
        criteria.andBeDeletedNotEqualTo((byte)1);
//        criteria.andStateEqualTo((byte)2)

        ReturnObject<VoObject> retObj=null;
        try{
            int ret=aftersaleServicePoMapper.updateByPrimaryKeySelective(po);
            if(ret==0){
                retObj=new ReturnObject<>(ResponseCode.AFTERSALE_STATENOTALLOW,String.format("店家确认收货失败"));
            }else{
                retObj=new ReturnObject<>(ResponseCode.OK);
            }
            return retObj;
        } catch (DataAccessException e) {
            //日志
            retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        } catch (Exception e) {
            retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        }
        return retObj;
    }


    public ReturnObject<VoObject> updateAfterSale(AftersalesBo bo) {
        AftersaleServicePo po=bo.gotSalePo();
        ReturnObject<VoObject> retObj=null;
        try{
            int ret=aftersaleServicePoMapper.updateByPrimaryKeySelective(po);
            if(ret==0){
                retObj=new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
            }
            else retObj=new ReturnObject<>(ResponseCode.OK);
        }catch (DataAccessException e) {
            //日志
            retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        } catch (Exception e) {
            retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        }
        return retObj;
    }

    public ReturnObject<AftersaleServicePo> selectSale(Long id) {
        ReturnObject<AftersaleServicePo> retObj=null;
        try{
            AftersaleServicePo po=aftersaleServicePoMapper.selectByPrimaryKey(id);
            if(po==null){
                retObj=new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
            }
            else retObj=new ReturnObject<>(po);
        }catch (DataAccessException e) {
            //日志
            retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        } catch (Exception e) {
            retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        }
        return retObj;
    }

    public ReturnObject<VoObject> shopFindOrderById(Long shopId, Long id) {
        OrderItemPoExample example = new OrderItemPoExample();
        OrderItemPoExample.Criteria criteria1 = example.createCriteria();
        criteria1.andOrderIdEqualTo(id);
        try {
            OrderPo orderPo = orderPoMapper.selectByPrimaryKey(id);
            if(orderPo==null)
                return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST,String.format("查询的订单id不存在"));
            else if(orderPo.getShopId()==null||(!orderPo.getShopId().equals(shopId)))
                return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE,String.format("查询的订单id不是本店铺的"));
            List<OrderItemPo> orderItemPos = orderItemPoMapper.selectByExample(example);
            Order order = new Order(orderPo, orderItemPos);
            return new ReturnObject<>(order);
        } catch (DataAccessException e) {
//            logger.error("selectOrder: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        } catch (Exception e) {
//            logger.error("other exception:" + e.getMessage());
            e.printStackTrace();
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("严重数据库错误：%s", e.getMessage()));
        }
    }


    public ReturnObject<List> adminGetAftersaleRefunds(Long shopId, Long id) {
        List<VoObject> refunds=null;
        RefundPoExample example = new RefundPoExample();
        RefundPoExample.Criteria criteria1 = example.createCriteria();
        criteria1.andAftersaleIdEqualTo(id);

        try {
            List<RefundPo> refundPo=refundPoMapper.selectByExample(example);
            List<VoObject> refundContents=null;
            if(refundPo.isEmpty()){

                return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("查询id不存在"));
            }
            refundContents=new ArrayList<>(refundPo.size());
            for (RefundPo po:refundPo){
                Refund refund=new Refund(po);

                refundContents.add(refund);
            }
            return new ReturnObject<>(refundContents);
        }
        catch (DataAccessException e){
            // 其他数据库错误
//            logger.debug("other sql exception : " + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));

        }
        catch (Exception e){
//            logger.error("other exception : " + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了严重的数据库错误：%s", e.getMessage()));
        }
    }
}
