package cn.edu.xmu.user.model.vo;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
public class LoginVo {
    @Length(min=2,message = "用户名长度过短")
    String userName;

    @Length(min=1,message = "密码长度过短")
    String password;
}
