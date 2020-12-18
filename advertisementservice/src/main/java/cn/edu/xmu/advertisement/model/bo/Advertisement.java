package cn.edu.xmu.advertisement.model.bo;

import cn.edu.xmu.advertisement.model.po.AdvertisementPo;
import cn.edu.xmu.advertisement.model.vo.AdvertisementRetVo;
import cn.edu.xmu.ooad.model.VoObject;
import lombok.Data;
import org.hibernate.validator.internal.util.logging.Messages;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
public class Advertisement  implements VoObject {


private Long id;

private Long segId;;

private  String link;

private String imageUrl;

private  String content;

private  State state ;

private Integer weight;

private Byte beDefault;

    private LocalDate beginDate;

    private LocalDate endDate;

    private  Byte repeate;

private String message;

private LocalDateTime gmtCreate;

private LocalDateTime gmtModified;


    @Override
    public Object createVo() { return new AdvertisementRetVo(this);  }

    @Override
    public Object createSimpleVo() { return null; }

    /**
     * 广告状态
     */
    public enum State {
       // NEW(0, "空状态"),
     //   DEFAULT(1, "默认"),
        MESSAGES(0,"待审核"),
        SHELF(4, "上架"),
        OFFSHELF(6,"下架");



//608广告状态禁止：审核态：只有当广告处于审核态时，才能执行该状态。 审核-》下架若成功
        //上架态：只有广告处于下架态时，才能下架  下架-》上架
        //下架态：只有广告处于上架态/审核态才能下架  上架/审核-》下架
        //删除：广告上架态不能删除，只有下架的时候删除




        private static final Map<Integer, Advertisement.State> stateMap;

        static { //由类加载机制，静态块初始加载对应的枚举属性到map中，而不用每次取属性时，遍历一次所有枚举值
            stateMap = new HashMap();
            for (Advertisement.State enum1 : values()) {
                stateMap.put(enum1.code, enum1);
            }
        }

        private int code;
        private String description;

        State(int code, String description) {
            this.code = code;
            this.description = description;
        }

        public static Advertisement.State getTypeByCode(Integer code) {
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
    public Advertisement(AdvertisementPo po){

        this.link = po.getLink();
          this.beDefault = po.getBeDefault();
          this.beginDate = po.getBeginDate();
          this.content = po.getContent();
          this.endDate = po.getEndDate();
          this.id = po.getId();
          this.imageUrl = po.getImageUrl();
          this.message = po.getMessage();
          this.repeate = po.getRepeats();
          this.segId = po.getSegId();
          this.weight = po.getWeight();
          this.gmtCreate = po.getGmtCreate();
          this.gmtModified = po.getGmtModified();
        if (null != po.getState()) {
            this.state = State.getTypeByCode(po.getState().intValue());
        }


    }


}
