package cn.edu.xmu.user.model.bo;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.user.model.po.CustomerPo;
import cn.edu.xmu.user.model.vo.CustomerRetVo;
import cn.edu.xmu.user.model.vo.SimpleCustomerRetVo;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
public class Customer implements VoObject {
    private Long id;

    private String userName;

    private String password;

    private String mobile;

    private String email;

    private String realname;

    private Byte gender;

    private LocalDateTime birthday;

    private Integer point;

    private State state;

    private Byte beDeleted;

    private LocalDateTime gmt_create;

    private LocalDateTime gmt_modified;

    @Override
    public Object createVo() { return new SimpleCustomerRetVo(this); }

    @Override
    public Object createSimpleVo() {
        CustomerRetVo customerRetVo = new CustomerRetVo();

        customerRetVo.setId(this.id);
        customerRetVo.setUserName(this.userName);
        customerRetVo.setRealName(this.realname);
        customerRetVo.setEmail(this.email);
        customerRetVo.setMobile(this.mobile);
        customerRetVo.setGender(this.gender.intValue()>0?"男":"女");
        customerRetVo.setBirthday(this.birthday.toString());

        return customerRetVo;
    }

    /**
     * 后台用户状态
     */
    public enum State {
        BACKGROUND(0, "后台用户"),
        NORM(4, "正常用户"),
        FORBID(6, "被封禁用户");

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

        this.mobile = po.getMobile();

        this.email = po.getEmail();

        this.realname = po.getRealName();

        this.gender = po.getGender();

        this.birthday = po.getBirthday();

        this.point = po.getPoint();

        this.beDeleted = po.getBeDeleted();

        if (null != po.getState()) {
            this.state = State.getTypeByCode(po.getState().intValue());
        }

        this.gmt_create = po.getGmtCreate();

        this.gmt_modified = po.getGmtModified();
    }

}
