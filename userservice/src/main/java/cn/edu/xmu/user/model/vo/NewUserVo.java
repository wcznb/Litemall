package cn.edu.xmu.user.model.vo;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Data
public class NewUserVo {
    @Length(min=2,message = "用户名长度过短")
    private String userName;

    @Length(min=1,message = "密码长度过短")
    private String password;

    @Length(min=1,message = "真实名字不能为空")
    private String realName;

    @Email(message="邮箱格式不正确")
    @Length(min=1,message = "邮箱不能为空")
    private String email;

    @Pattern(regexp="[+]?[0-9*#]+",message="手机号格式不正确")
    @Length(min=11,max=11,message = "手机号长度不合法")
    private String mobile;

    @NotNull
    private Byte gender;

    //出生时间
    @Pattern(regexp = "(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})" +
            "-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))" +
            "|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)", message = "时间格式错误")
    @Length(min=1,message = "生日不能为空")
    private String birthday;
}