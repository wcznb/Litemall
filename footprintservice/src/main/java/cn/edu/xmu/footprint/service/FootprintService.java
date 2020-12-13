package cn.edu.xmu.footprint.service;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ReturnObject;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 陈渝璇
 * @description 足迹服务
 * @date Created in 16:02 2020/11/29
 **/
@Service
public interface FootprintService {

    /**
     * 分页查询买家所有足迹
     *
     * @param did:店铺id
     * @param userId
     * @param beginTime
     * @param endTime
     * @param page
     * @param pageSize
     * @return ReturnObject<PageInfo<VoObject>> 分页返回买家足迹
     */
    ReturnObject<PageInfo<VoObject>> findPageOfFootprints(Long did, Long userId, String beginTime, String endTime, Integer page, Integer pageSize);


    /**
     * 增加足迹
     * @param userId
     * @param skuId
     * @return 返回对象 ReturnObj
     */
    @Transactional
    ReturnObject<Object> insertFootprint(Long userId, Long skuId);
}
