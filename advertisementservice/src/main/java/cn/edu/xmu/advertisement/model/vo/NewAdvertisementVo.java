package cn.edu.xmu.advertisement.model.vo;

import lombok.Data;

@Data
public class NewAdvertisementVo {


    String content;
    Integer weight;
    String beginDate;
    String endDate;
    Boolean repeat;
    String link;


}
