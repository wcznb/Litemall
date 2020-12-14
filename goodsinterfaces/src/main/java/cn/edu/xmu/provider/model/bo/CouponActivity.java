package cn.edu.xmu.provider.model.bo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
public class CouponActivity implements Serializable {
    Long id;
    String name;
    LocalDateTime beginTime;
    LocalDateTime endTime;
    public enum State {
        OFFLINE(0, "已下线"),
        ONLINE(1, "已上线"),
        DELETED(2,"已删除");
        private static final Map<Integer, State> stateMap;

        static { //由类加载机制，静态块初始加载对应的枚举属性到map中，而不用每次取属性时，遍历一次所有枚举值
            stateMap = new HashMap();
            for (CouponActivity.State enum1 : values()) {
                stateMap.put(enum1.code, enum1);
            }
        }
        private int code;
        private String description;
        State(int code, String description) {
            this.code = code;
            this.description = description;
        }
        public static CouponActivity.State getTypeByCode(Integer code) {
            return stateMap.get(code);
        }

        public int getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }
    }
    public CouponActivity(){};
}
