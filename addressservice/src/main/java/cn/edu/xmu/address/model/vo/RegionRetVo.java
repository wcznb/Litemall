package cn.edu.xmu.address.model.vo;

import cn.edu.xmu.address.model.bo.Region;

import java.time.LocalDateTime;

public class RegionRetVo {
    private Long id;

    private Long pid;

    private String name;

    private Long postalCode;

    private Byte state;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

    public RegionRetVo(Region region){
        this.id=region.getId();
        this.pid=region.getPid();
        this.name=region.getName();
        this.postalCode=region.getPostalCode();
        this.state=region.getState();
        this.gmtCreate=region.getGmtCreate();
        this.gmtModified=region.getGmtModified();
    }

}
