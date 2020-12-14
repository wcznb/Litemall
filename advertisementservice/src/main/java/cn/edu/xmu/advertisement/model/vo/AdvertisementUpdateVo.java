package cn.edu.xmu.advertisement.model.vo;

import lombok.Data;

@Data
public class AdvertisementUpdateVo {

    String content;
    String beginDate;
    String endDate;
    String weight;
    Boolean repeat;
    String link;


}
