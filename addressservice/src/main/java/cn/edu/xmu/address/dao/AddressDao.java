package cn.edu.xmu.address.dao;

import cn.edu.xmu.address.mapper.AddressPoMapper;
import cn.edu.xmu.address.model.po.AddressPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class AddressDao {
    @Autowired
    AddressPoMapper addressPoMapper;
    public void test(){
        AddressPo e = addressPoMapper.selectByPrimaryKey(1L);
        System.out.println("asdasd:  "+ e.getCustomerId());
    }
}
