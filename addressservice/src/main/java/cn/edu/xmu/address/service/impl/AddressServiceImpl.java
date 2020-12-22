package cn.edu.xmu.address.service.impl;


import cn.edu.xmu.address.dao.AddressDao;
import cn.edu.xmu.address.model.bo.Address;
import cn.edu.xmu.address.model.po.AddressPo;
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

import java.util.ArrayList;
import java.util.List;

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
        ReturnObject<PageInfo<AddressPo>> ret = addressDao.getAllAddressByUserid(id,page, pagesize);

        if(ret.getCode()== ResponseCode.OK){
            //成功搜索
            PageInfo<AddressPo> poPageInfo = ret.getData();
            List<AddressPo> footprintPos = poPageInfo.getList();
            List<VoObject> retObj = new ArrayList<>(footprintPos.size());
            for(AddressPo po:footprintPos){
                Address footprint = new Address(po);

                retObj.add(footprint);
            }
            PageInfo<VoObject> footPrintsPage = new PageInfo<>(retObj);
            footPrintsPage.setPages(poPageInfo.getPages());
            footPrintsPage.setPageNum(poPageInfo.getPageNum());
            footPrintsPage.setPageSize(poPageInfo.getPageSize());
            footPrintsPage.setTotal(poPageInfo.getTotal());
            return new ReturnObject<>(footPrintsPage);
        } else{
            return new ReturnObject<>(ret.getCode());
        }

    }

    @Override
    public ReturnObject<Object> updateAddress(Long userId, Long id , NewAddressVo vo){
        return addressDao.updateAddress(userId,id,vo);
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
