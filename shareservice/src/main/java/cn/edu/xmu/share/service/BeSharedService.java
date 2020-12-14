package cn.edu.xmu.share.service;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ReturnObject;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Service
public interface BeSharedService {
    @Transactional
    public ReturnObject<Object> addBeshared(Long sharerId, Long spuId, Long customerId);

    public ReturnObject<PageInfo<VoObject>> getOwnBeshared(Long custormerId, Long skuId, String beginTime, String endTime, Integer page, Integer pageSize);

    public ReturnObject<PageInfo<VoObject>> getShopBeshared(Long id, Long  skuId, String beginTime, String endTime, Integer page, Integer pageSize);
}
