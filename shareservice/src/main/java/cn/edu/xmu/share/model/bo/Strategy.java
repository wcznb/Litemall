package cn.edu.xmu.share.model.bo;

import lombok.Data;

import java.util.List;

@Data
public class Strategy {
    List<ShareRule> rules;
    Byte firstOrAvg;
    public Strategy(){}

    public Strategy(List<ShareRule> rules, Byte firstOrAvg){
        this.rules = rules;
        this.firstOrAvg = firstOrAvg;
    }
}
