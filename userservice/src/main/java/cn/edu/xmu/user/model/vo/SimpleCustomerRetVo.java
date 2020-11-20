package cn.edu.xmu.user.model.vo;

import cn.edu.xmu.ooad.util.encript.AES;
import cn.edu.xmu.user.model.bo.Customer;
import cn.edu.xmu.user.model.po.CustomerPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

//        "id": 0,
//                "userName": "string",
//                "realName": "string"

@Data
@ApiModel(description = "简单用户信息")
public class SimpleCustomerRetVo {
    @ApiModelProperty(value = "用户id")
    Long id;
    @ApiModelProperty(value = "用户名")
    String userName;
    @ApiModelProperty(value = "用户真实姓名")
    String realName;

    public SimpleCustomerRetVo(Customer customer){
        this.id = customer.getId();
        this.realName = customer.getRealname();
        this.userName = customer.getUserName();
    }
}
