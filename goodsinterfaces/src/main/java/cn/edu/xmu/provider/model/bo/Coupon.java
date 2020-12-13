package cn.edu.xmu.provider.model.bo;


import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Feiyan Liu
 * @date Created at 2020/12/3 16:30
 */
@Data
public class Coupon implements Serializable {
    public enum State {
        NOT_CLAIMED(0,"未领取"),
        CLAIMED(1,"已领取"),
        USED(2,"已使用"),
        INVALID(3,"失效");
        private static final Map<Integer, State> stateMap;

        static { //由类加载机制，静态块初始加载对应的枚举属性到map中，而不用每次取属性时，遍历一次所有枚举值
            stateMap = new HashMap();
            for (Coupon.State enum1 : values()) {
                stateMap.put(enum1.code, enum1);
            }
        }

        private int code;
        private String description;

        State(int code, String description) {
            this.code = code;
            this.description = description;
        }

        public static Coupon.State getTypeByCode(Integer code) {
            return stateMap.get(code);
        }

        public int getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }
    }
    public Coupon()
    {

    }
}
