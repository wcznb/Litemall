package cn.edu.xmu.address.model.bo;

import cn.edu.xmu.address.model.po.RegionPo;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Region {
    private Long id;

    private Long pid;

    private String name;

    private Long postalCode;

    private Byte state;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

    public Region(RegionPo po){
        this.id=po.getId();
        this.pid=po.getPid();
        this.name=po.getName();
        this.postalCode=po.getPostalCode();
        this.state=po.getState();
        this.gmtCreate=po.getGmtCreate();
        this.gmtModified=po.getGmtModified();
    }

}
