package cn.edu.xmu.user.model.vo;

import cn.edu.xmu.ooad.util.encript.AES;
import cn.edu.xmu.user.model.bo.Customer;
import cn.edu.xmu.user.model.po.CustomerPo;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CustomerRetVo {

    Long id;
    String userName;
    String name;
    String mobile;
    String email;
    Byte gender;
    LocalDate birthday;
    Byte state;
    LocalDateTime gmt_create;
    LocalDateTime gmt_modified;

    public CustomerRetVo(CustomerPo customerPo){
        this.id = customerPo.getId();
        this.userName = customerPo.getUserName();
        this.name = customerPo.getRealName();
        this.email = customerPo.getEmail();
        this.mobile = customerPo.getMobile();
        this.gender = customerPo.getGender();
        this.birthday = customerPo.getBirthday();
        this.gmt_modified = customerPo.getGmtModified();
        this.gmt_create = customerPo.getGmtCreate();
        this.state = customerPo.getState();
    }


}
