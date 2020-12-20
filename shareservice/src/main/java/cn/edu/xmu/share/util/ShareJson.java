package cn.edu.xmu.share.util;

import cn.edu.xmu.share.model.bo.ShareRule;
import cn.edu.xmu.share.model.bo.Strategy;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.util.List;

public class ShareJson {
    public static Strategy StringToJson(String strategt){
        try {
            JSONObject jsonObject = JSONObject.parseObject(strategt);
            Byte a = jsonObject.getByteValue("firstOrAvg");
            JSONArray jsonArray = jsonObject.getJSONArray("rule");
            List<ShareRule> shareRules = jsonArray.toJavaList(ShareRule.class);
            return new Strategy(shareRules,a);
        }catch (Exception e){
            return null;
        }
    }

    public static Boolean JudgeJson(String strategy){
        Strategy strategie = StringToJson(strategy);
        if(strategie==null){
            return false;
        }else {
            for(ShareRule shareRule:strategie.getRules()){
                if(shareRule.getNum()==null||shareRule.getRate()==null){
                    return false;
                }
            }
            return true;
        }
    }
}
