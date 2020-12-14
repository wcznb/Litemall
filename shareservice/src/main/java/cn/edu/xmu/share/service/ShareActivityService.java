package cn.edu.xmu.share.service;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.share.dao.ShareActivityDao;
import cn.edu.xmu.share.model.vo.ShareActivityVo;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Service
public interface ShareActivityService {
    @Transactional
    public ReturnObject<Object> addShareActivityService(Long shopId, Long skuId, ShareActivityVo shareActivityVo);

    public ReturnObject<PageInfo<VoObject>> getShareActivitiesService(Long shopId, Long skuId, Integer page, Integer pageSize);

    @Transactional
    public ReturnObject<Object> modifyShareActivityService(Long shopId, Long id, ShareActivityVo shareActivityVo);

    @Transactional
    public ReturnObject<Object> deleteShareActivityService(Long shopId, Long id);

    @Transactional
    public ReturnObject<Object> onlineShareActivityService(Long shoId, Long id);

}
