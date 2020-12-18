package cn.edu.xmu.share.model.bo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ShareRule {

    Integer num;

    Integer rate;
    public ShareRule(){}
}
