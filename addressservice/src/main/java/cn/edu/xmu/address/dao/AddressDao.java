package cn.edu.xmu.address.dao;

import cn.edu.xmu.address.mapper.AddressPoMapper;
import cn.edu.xmu.address.mapper.RegionPoMapper;
import cn.edu.xmu.address.model.bo.Address;
import cn.edu.xmu.address.model.po.AddressPo;
import cn.edu.xmu.address.model.po.AddressPoExample;
import cn.edu.xmu.address.model.po.RegionPo;
import cn.edu.xmu.address.model.vo.AddressRetVo;
import cn.edu.xmu.address.model.vo.NewAddressRetVo;
import cn.edu.xmu.address.model.vo.NewAddressVo;
import cn.edu.xmu.address.model.vo.NewRegionVo;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


/**
 * @author shyanne 3184
 */
@Repository
public class AddressDao {
    @Autowired
    AddressPoMapper addressPoMapper;

    @Autowired
    RegionDao regionDao;

    public ReturnObject<NewAddressRetVo> addAddress(NewAddressVo vo, Long id) {
        //检查地址是否已经超过上限
        AddressPoExample example = new AddressPoExample();
        AddressPoExample.Criteria criteria = example.createCriteria();
        criteria.andCustomerIdEqualTo(id);
        List<AddressPo> pos = null;
        pos = addressPoMapper.selectByExample(example);

        ReturnObject returnObject = null;

        if (pos.size() < 20) {

            Long regionId = vo.getRegionId();

            if (!regionDao.isExist(regionId)) {

                return new ReturnObject<>(ResponseCode.FIELD_NOTVALID);

            } else if (!regionDao.isAble(regionId)) {

                return new ReturnObject<>(ResponseCode.REGION_OBSOLETE);

            } else {

                AddressPo po = new AddressPo();
                po.setRegionId(vo.getRegionId());
                po.setDetail(vo.getDetail());
                po.setConsignee(vo.getConsignee());
                po.setMobile(vo.getMobile());
                LocalDateTime localDateTime = LocalDateTime.now();
                po.setGmtCreate(localDateTime);
                po.setGmtModified(localDateTime);
                po.setCustomerId(id);
                po.setBeDefault((byte) 0);

                try {

                    int ret = addressPoMapper.insertSelective(po);
                    if (ret == 0) {

                        returnObject = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);

                    } else {

                        Address address = new Address(po);
                        NewAddressRetVo retVo = new NewAddressRetVo(address);
                        retVo.setId(po.getId());
                        returnObject = new ReturnObject<>(retVo);

                    }
                } catch (DataAccessException e) {

                    returnObject = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误："));

                } catch (Exception e) {

                    returnObject = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了严重的数据库错误：%s", e.getMessage()));

                }
            }
        } else {

            returnObject = new ReturnObject<>(ResponseCode.ADDRESS_OUTLIMIT);

        }

        return returnObject;

    }


    public ReturnObject<PageInfo<AddressPo>> getAllAddressByUserid(Long customerId, Integer page, Integer pagesize) {

        AddressPoExample example = new AddressPoExample();
        AddressPoExample.Criteria criteria = example.createCriteria();
        criteria.andCustomerIdEqualTo(customerId);

        PageHelper.startPage(page, pagesize);
        List<AddressPo> pos = null;

        try {

            pos = addressPoMapper.selectByExample(example);

        } catch (DataAccessException e) {

            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));

        }

        PageInfo<AddressPo> footPrintsPoPage = PageInfo.of(pos);
        return new ReturnObject<>(footPrintsPoPage);

    }

    public ReturnObject<Object> updateAddress(Long userId, Long id, NewAddressVo vo) {

        AddressPo check = addressPoMapper.selectByPrimaryKey(id);
        //addressid not exist
        if (check == null) {
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        //not user's address
        if (!check.getCustomerId().equals(userId)) {
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE);
        }
        //regionid not exist
        if (!regionDao.isExist(vo.getRegionId())) {

            return new ReturnObject<>(ResponseCode.FIELD_NOTVALID);

        } else if (!regionDao.isAble(vo.getRegionId())) {

            return new ReturnObject<>(ResponseCode.REGION_OBSOLETE);
        }
        AddressPo addressPo = new AddressPo();
        addressPo = addressPoMapper.selectByPrimaryKey(id);


        addressPo.setRegionId(vo.getRegionId());
        addressPo.setDetail(vo.getDetail());
        addressPo.setConsignee(vo.getConsignee());
        addressPo.setMobile(vo.getMobile());
        LocalDateTime localDateTime = LocalDateTime.now();
        addressPo.setGmtModified(localDateTime);

        try {

            int ret = addressPoMapper.updateByPrimaryKeySelective(addressPo);

            if (ret == 0) {

                return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);

            } else {

                return new ReturnObject<>();

            }
        } catch (Exception e) {

            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);

        }


    }


    public ReturnObject<Object> setAddressAsDefault(Long id, Long customerId) {


        AddressPo check = addressPoMapper.selectByPrimaryKey(id);
        if (check == null) {
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        if (!(check.getCustomerId().equals(customerId))) {
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE);
        }

        AddressPo addressPo = new AddressPo();
        addressPo.setId(id);
        addressPo.setBeDefault((byte) 1);

        AddressPoExample example = new AddressPoExample();
        AddressPoExample.Criteria criteria = example.createCriteria();
        criteria.andCustomerIdEqualTo(customerId);
        List<AddressPo> pos = null;

        //将之前的默认地址设为非默认
        try {

            pos = addressPoMapper.selectByExample(example);

        } catch (DataAccessException e) {

            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));

        }
        for (AddressPo po : pos) {

            po.setBeDefault((byte) 0);

            try {
                int ret = addressPoMapper.updateByPrimaryKeySelective(po);
                if (ret == 0) {
                    return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
                }
            } catch (Exception e) {
                return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
            }

        }
        //设为默认
        try {

            int ret = addressPoMapper.updateByPrimaryKeySelective(addressPo);

            if (ret == 0) {

                return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);

            } else {

                return new ReturnObject<>();

            }
        } catch (Exception e) {

            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        }


    }


    public ReturnObject<Object> deleteAddress(Long id) {

        ReturnObject<Object> retObj = null;
        AddressPo addressPo = addressPoMapper.selectByPrimaryKey(id);

        if (addressPo != null) {

            int ret = addressPoMapper.deleteByPrimaryKey(id);

            if (ret == 0) {

                retObj = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);

            } else {

                retObj = new ReturnObject<>();

            }


        } else {
            retObj = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }

        return retObj;
    }


}
