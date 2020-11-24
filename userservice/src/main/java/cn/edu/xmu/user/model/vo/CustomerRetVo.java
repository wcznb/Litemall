package cn.edu.xmu.user.model.vo;

import cn.edu.xmu.ooad.util.encript.AES;
import cn.edu.xmu.user.model.bo.Customer;
import cn.edu.xmu.user.model.po.CustomerPo;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CustomerRetVo {

    Long id;
    String userName;
    String realName;
    String mobile;
    String email;
    String gender;
    String birthday;
    LocalDateTime gmt_created;
    LocalDateTime gmt_modified;


    public CustomerRetVo(Customer customer){

        this.id = customer.getId();
        this.userName = customer.getUserName();
        this.realName = customer.getRealname();
        this.email = customer.getEmail();
        this.mobile = customer.getMobile();
        this.gender = customer.getGender().intValue()>0?"男":"女";
        this.birthday = customer.getBirthday().toString();
        this.gmt_created = customer.getGmt_created();
        this.gmt_modified = customer.getGmt_created();

    }

    public CustomerRetVo(CustomerPo customerPo){
        this.id = customerPo.getId();
        this.userName = customerPo.getUserName();
        this.realName = AES.decrypt(customerPo.getRealName(), Customer.AESPASS);
        this.email = AES.decrypt(customerPo.getEmail(), Customer.AESPASS);
        this.mobile = AES.decrypt(customerPo.getMobile(), Customer.AESPASS);
        this.gender = customerPo.getGender().intValue()>0?"男":"女";
        this.birthday = customerPo.getBirthday().toString();
        this.gmt_modified = customerPo.getGmtModified();
        this.gmt_created = customerPo.getGmtCreated();
    }


}
