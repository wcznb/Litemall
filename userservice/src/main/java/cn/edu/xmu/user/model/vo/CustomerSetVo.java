package cn.edu.xmu.user.model.vo;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDateTime;

@Data
public class CustomerSetVo {
    String realName;

    @Range(min=0,max=1,message = "sex is 0/1")
    Byte gender;
    LocalDateTime birthday;

}
