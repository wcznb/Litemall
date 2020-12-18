package cn.edu.xmu.user.model.vo;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Data
public class NewUserVo {
    @Length(min=6,message = "用户名长度过短")
    private String userName;

//    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",message = "密码格式不正确，请包含大小写字母数字及特殊符号")
    private String password;

    private String realName;

    @Email(message="邮箱格式不正确")
    private String email;

    @Pattern(regexp="[+]?[0-9*#]+",message="手机号格式不正确")
    private String mobile;

    private Byte gender;

    //出生时间
    @Pattern(regexp = "(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})" +
            "-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))" +
            "|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)", message = "时间格式错误")
    private String birthday;
}