package cn.edu.xmu.address.service;


import cn.edu.xmu.address.dao.AddressDao;
import cn.edu.xmu.address.model.vo.AddressRetVo;
import cn.edu.xmu.address.model.vo.NewAddressRetVo;
import cn.edu.xmu.address.model.vo.NewAddressVo;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public interface AddressService {
    ReturnObject<NewAddressRetVo> addAddress(NewAddressVo vo, Long id);
    ReturnObject<PageInfo<VoObject>> getAllAddressById(Long id, Integer page, Integer pagesize);
    ReturnObject<Object> updateAddress(Long id , NewAddressVo vo);
    ReturnObject<Boolean> setAddressAsDefault(Long id,Long customerId);
    ReturnObject deleteAddress(Long id);

}
