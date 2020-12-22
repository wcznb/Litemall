package cn.edu.xmu.aftersales.service;

import cn.edu.xmu.aftersales.model.bo.AftersalesBo;
import cn.edu.xmu.aftersales.model.vo.*;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ReturnObject;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public interface AfterSaleService {
    @Transactional
    ReturnObject<VoObject> newSale(Long userId, Long orderItemId, NewSaleVo newSaleVo);

    ReturnObject<VoObject> searchSaleById(Long userId,Long id);

    ReturnObject<PageInfo<VoObject>> getSale(Long userId, String beginTime,String endTime, Integer page, Integer pageSize, Byte type, Byte state);

    ReturnObject<PageInfo<VoObject>> getSaleByShop(Long shopId, String beginTime,String endTime, Integer page, Integer pageSize, Byte type, Byte state);
    @Transactional
    ReturnObject<VoObject> modifySale(Long userId, Long id, UpdateVo newVo);
    @Transactional
    ReturnObject<VoObject> deleteSale(Long userId,Long id);
    @Transactional
    ReturnObject<VoObject> customerLogSn(Long userId,Long id,String LogSn);
    @Transactional
    ReturnObject<VoObject> confirmOver(Long userId,Long id);

    ReturnObject<VoObject> searchSale(Long shopId, Long id);
    @Transactional
    ReturnObject<VoObject> agreeRequest(Long shopId, Long id, checkVo vo);
    @Transactional
    ReturnObject<VoObject> receive(Long shopId,Long id,confirmVo vo);
    @Transactional
    ReturnObject<VoObject> deliver(Long shopId, Long id, shopLogSnVo vo);


}
