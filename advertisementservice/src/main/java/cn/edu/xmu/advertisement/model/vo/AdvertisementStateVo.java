package cn.edu.xmu.advertisement.model.vo;

import cn.edu.xmu.advertisement.model.bo.Advertisement;
import lombok.Data;

@Data
public class AdvertisementStateVo {

    private Long Code;
    private String name;

    public AdvertisementStateVo(Advertisement.State state){
        Code=Long.valueOf(state.getCode());
        name=state.getDescription();
    }
}
