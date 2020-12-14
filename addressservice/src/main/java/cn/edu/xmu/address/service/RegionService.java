package cn.edu.xmu.address.service;

import cn.edu.xmu.address.model.vo.NewRegionVo;
import cn.edu.xmu.ooad.util.ReturnObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public interface RegionService {

    @Transactional
    ReturnObject<Object> addRegion(NewRegionVo vo, Long pid);

    @Transactional
    ReturnObject<Object> updateRegion(NewRegionVo vo, Long id);

    @Transactional
    ReturnObject<Object> disableRegion(Long id);

    ReturnObject<Object> getAncestorRegion(Long id);

}
