package cn.edu.xmu.address.service.impl;


import cn.edu.xmu.address.dao.AddressDao;
import cn.edu.xmu.address.model.vo.AddressRetVo;
import cn.edu.xmu.address.model.vo.NewAddressRetVo;
import cn.edu.xmu.address.model.vo.NewAddressVo;
import cn.edu.xmu.address.service.AddressService;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    AddressDao addressDao;

    @Transactional

    @Override
    public ReturnObject<NewAddressRetVo> addAddress(NewAddressVo vo, Long id){
        return addressDao.addAddress(vo,id);
    }

    @Override
    public ReturnObject<PageInfo<VoObject>> getAllAddressById(Long id, Integer page, Integer pagesize){
        ReturnObject<PageInfo<VoObject>> ret = addressDao.getALlAddressByUserid(id,page, pagesize);
        return ret;
    }

    @Override
    public ReturnObject<Object> updateAddress(Long id , NewAddressVo vo){
        return addressDao.updateAddress(id,vo);
    }

    @Override
    @Transactional
    public ReturnObject<Boolean> setAddressAsDefault(Long id,Long customerId){
        ReturnObject retObj = addressDao.setAddressAsDefault(id,customerId);
        if (retObj.getCode() != ResponseCode.OK) {
            return retObj;
        }
        return new ReturnObject<>(true);
    }

    @Override
    @Transactional
    public ReturnObject deleteAddress(Long id){
        return addressDao.deleteAddress(id);
    }
}
