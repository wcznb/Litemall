package cn.edu.xmu.user.model.vo;

import cn.edu.xmu.ooad.util.encript.AES;
import cn.edu.xmu.user.model.bo.Customer;
import cn.edu.xmu.user.model.po.CustomerPo;
import lombok.Data;

@Data
public class CustomerRetVo {

    Long id;
    String userName;
    String realName;
    String mobile;
    String email;
    String gender;
    String birthday;

    public CustomerRetVo(){


    }

    public CustomerRetVo(CustomerPo customerPo){
        this.id = customerPo.getId();
        this.userName = customerPo.getUserName();
        this.realName = AES.decrypt(customerPo.getRealName(), Customer.AESPASS);
        this.email = AES.decrypt(customerPo.getEmail(), Customer.AESPASS);
        this.mobile = AES.decrypt(customerPo.getMobile(), Customer.AESPASS);
        this.gender = customerPo.getGender().intValue()>0?"男":"女";
        this.birthday = customerPo.getBirthday().toString();
    }


}
