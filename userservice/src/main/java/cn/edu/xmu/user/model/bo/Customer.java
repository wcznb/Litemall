package cn.edu.xmu.user.model.bo;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.ooad.util.encript.AES;
import cn.edu.xmu.ooad.util.encript.SHA256;
import cn.edu.xmu.user.model.po.CustomerPo;
import cn.edu.xmu.user.model.vo.CustomerRetVo;
import cn.edu.xmu.user.model.vo.SimpleCustomerRetVo;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
public class Customer implements VoObject {
    public static String AESPASS = "OOAD2020-11-01";

    private Long id;

    private String userName;

    private String password;

    private String mobile;

    private String email;

    private String realname;

    private Byte gender;

    private LocalDateTime birthday;

    private Integer point;

    private State state = State.NEW;

    private Byte beDeleted;

    @Override
    public Object createVo() { return new SimpleCustomerRetVo(this); }

    @Override
    public Object createSimpleVo() { return new CustomerRetVo(this); }

    /**
     * 后台用户状态
     */
    public enum State {
        NEW(0, "空状态"),
        NORM(1, "正常"),
        FORBID(2, "封禁"),
        DELETE(3, "废弃");

        private static final Map<Integer, Customer.State> stateMap;

        static { //由类加载机制，静态块初始加载对应的枚举属性到map中，而不用每次取属性时，遍历一次所有枚举值
            stateMap = new HashMap();
            for (Customer.State enum1 : values()) {
                stateMap.put(enum1.code, enum1);
            }
        }

        private int code;
        private String description;

        State(int code, String description) {
            this.code = code;
            this.description = description;
        }

        public static Customer.State getTypeByCode(Integer code) {
            return stateMap.get(code);
        }

        public Integer getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }
    }

    /**
     * 构造函数
     * @param po Po对象
     */
    public Customer(CustomerPo po){
        this.id = po.getId();
        this.userName = po.getUserName();
        this.password =po.getPassword();

        this.mobile = AES.decrypt(po.getMobile(),AESPASS);

        this.email = AES.decrypt(po.getEmail(),AESPASS);

        this.realname = AES.decrypt(po.getRealName(), AESPASS);

        this.gender = po.getGender();

        this.birthday = po.getBirthday();

        this.point = po.getPoint();

        this.beDeleted = po.getBeDeleted();

        if (null != po.getState()) {
            this.state = State.getTypeByCode(po.getState().intValue());
        }
    }

}
