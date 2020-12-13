package cn.edu.xmu.address.service.impl;

import cn.edu.xmu.address.dao.RegionDao;
import cn.edu.xmu.address.model.vo.NewRegionVo;
import cn.edu.xmu.address.service.RegionService;
import cn.edu.xmu.ooad.util.ReturnObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegionServiceImpl implements RegionService {
    @Autowired
    RegionDao regionDao;

    @Override
    @Transactional
    public ReturnObject<Object> addRegion(NewRegionVo vo, Long pid){
        return regionDao.addRegion(vo,pid);
    }

    @Override
    @Transactional
    public ReturnObject<Object> updateRegion(NewRegionVo vo, Long id){
        return regionDao.updateRegion(vo,id);
    }

    @Override
    @Transactional
    public ReturnObject<Object> disableRegion(Long id){return regionDao.disableRegion(id);}

    @Override
    public ReturnObject<Object> getAncestorRegion(Long id){return regionDao.getAncestorRegion(id);}

}
