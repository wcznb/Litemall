package cn.edu.xmu.user.model.vo;

import cn.edu.xmu.ooad.util.encript.AES;
import cn.edu.xmu.user.model.bo.Customer;
import cn.edu.xmu.user.model.po.CustomerPo;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CustomerRetVo {

    Long id;
    String userName;
    String realName;
    String mobile;
    String email;
    String gender;
    String birthday;
    LocalDateTime gmt_create;
    LocalDateTime gmt_modified;


    public CustomerRetVo(Customer customer){

        this.id = customer.getId();
        this.userName = customer.getUserName();
        this.realName = customer.getRealname();
        this.email = customer.getEmail();
        this.mobile = customer.getMobile();
        this.gender = customer.getGender().intValue()>0?"男":"女";
        this.birthday = customer.getBirthday().toString();
        this.gmt_create = customer.getGmt_create();
        this.gmt_modified = customer.getGmt_modified();

    }

    public CustomerRetVo(CustomerPo customerPo){
        this.id = customerPo.getId();
        this.userName = customerPo.getUserName();
        this.realName = customerPo.getRealName();
        this.email = customerPo.getEmail();
        this.mobile = customerPo.getMobile();
        this.gender = customerPo.getGender().intValue()>0?"男":"女";
        this.birthday = customerPo.getBirthday().toString();
        this.gmt_modified = customerPo.getGmtModified();
        this.gmt_create = customerPo.getGmtCreate();
    }


}
