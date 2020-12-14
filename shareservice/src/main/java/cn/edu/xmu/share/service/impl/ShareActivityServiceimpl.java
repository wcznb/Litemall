package cn.edu.xmu.share.service.impl;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.share.dao.ShareActivityDao;
import cn.edu.xmu.share.model.vo.ShareActivityVo;
import cn.edu.xmu.share.service.ShareActivityService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ShareActivityServiceimpl implements ShareActivityService {
    @Autowired
    ShareActivityDao shareActivityDao;

    @Override
    @Transactional
    public ReturnObject<Object> addShareActivityService(Long shopId, Long skuId, ShareActivityVo shareActivityVo){
        //验证skuId、shopId是否存在、通过调用其他组的api
        return shareActivityDao.addShareActivity(shopId, skuId, shareActivityVo);
    }

    @Override
    public ReturnObject<PageInfo<VoObject>> getShareActivitiesService(Long shopId, Long skuId, Integer page, Integer pageSize){
        return shareActivityDao.pageShareActivities(shopId, skuId, page, pageSize);
    }

    @Override
    @Transactional
    public ReturnObject<Object> modifyShareActivityService(Long shopId, Long id, ShareActivityVo shareActivityVo){
        return shareActivityDao.modifyShareActivity(shopId, id, shareActivityVo);
    }

    @Override
    @Transactional
    public ReturnObject<Object> deleteShareActivityService(Long shopId, Long id){
        return shareActivityDao.deleteShareActivity(shopId, id);
    }

    @Override
    public ReturnObject<Object> onlineShareActivityService(Long shoId, Long id) {
        return shareActivityDao.onlineShareActivity(shoId, id);
    }


}
