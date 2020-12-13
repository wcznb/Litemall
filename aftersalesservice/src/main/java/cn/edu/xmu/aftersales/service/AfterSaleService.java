package cn.edu.xmu.aftersales.service;

import cn.edu.xmu.aftersales.model.vo.NewSaleVo;
import cn.edu.xmu.aftersales.model.vo.UpdateVo;
import cn.edu.xmu.aftersales.model.vo.checkVo;
import cn.edu.xmu.aftersales.model.vo.confirmVo;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ReturnObject;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public interface AfterSaleService {
    @Transactional
    ReturnObject<VoObject> newSale(Long userId, Long orderItemId, NewSaleVo newSaleVo);

    ReturnObject<VoObject> searchSaleById(Long userId,Long id);

    ReturnObject<PageInfo<VoObject>> getSale(Long userId, Long spuId,Long skuId, String beginTime,String endTime, Integer page, Integer pageSize, Byte type, Byte state);

    ReturnObject<PageInfo<VoObject>> getSaleByShop(Long shopId,Long spuId,Long skuId, String beginTime,String endTime, Integer page, Integer pageSize, Byte type, Byte state);
    @Transactional
    ReturnObject<VoObject> modifySale(Long userId, Long id, UpdateVo newVo);
    @Transactional
    ReturnObject<VoObject> deleteSale(Long userId,Long id);
    @Transactional
    ReturnObject<VoObject> customerLogSn(Long userId,Long id,String LogSn);
    @Transactional
    ReturnObject<VoObject> confirmOver(Long userId,Long id);

    ReturnObject<VoObject> searchSale(Long userId,Long shopId,Long id);
    @Transactional
    ReturnObject<VoObject> agreeRequest(Long shopId, Long id, checkVo vo);
    @Transactional
    ReturnObject<VoObject> receive(Long shopId,Long id,confirmVo vo);
    @Transactional
    ReturnObject<VoObject> deliver(Long shopId,Long id,String logSn);




}
