package cn.edu.xmu.share.model.vo;



import cn.edu.xmu.share.model.bo.ShareActivity;
import lombok.Data;

@Data
public class StateVo {
    private Long Code;
    private String name;

    public StateVo(ShareActivity.State state){
        Code=Long.valueOf(state.getCode());
        name=state.getDescription();
    }

}
