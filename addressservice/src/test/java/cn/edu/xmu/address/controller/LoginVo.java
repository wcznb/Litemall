package cn.edu.xmu.address.controller;


import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginVo {
    @NotBlank(message = "必须输入用户名")
    private String userName;

    @NotBlank(message = "必须输入密码")
    private String password;
}
