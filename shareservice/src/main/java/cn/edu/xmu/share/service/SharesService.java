package cn.edu.xmu.share.service;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.share.model.vo.SharesVo;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Repository
public interface SharesService {

    @Transactional
    public ReturnObject<Object> addShareService(Long id, Long userId);

    public ReturnObject<PageInfo<VoObject>> getOwnSharesService(Long userId, Long goodsId, String beginTime, String endTime, Integer page, Integer pageSize);

    public ReturnObject<PageInfo<VoObject>> getSharesBySpuIdService(Long id,Long skuId,Long userId, Integer page, Integer pageSize);


}
