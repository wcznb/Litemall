package cn.edu.xmu.aftersales.model.vo;

import cn.edu.xmu.aftersales.model.bo.AftersalesBo;
import lombok.Data;

@Data
public class StateVo {

    private Long code;
    private String name;

    public StateVo(AftersalesBo.State state){
        this.code=Long.valueOf(state.getCode());
        this.name=state.getDescription();
    }
}
