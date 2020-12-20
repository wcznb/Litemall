package cn.edu.xmu.address.model.vo;

import cn.edu.xmu.address.model.bo.Address;
import cn.edu.xmu.address.model.po.AddressPo;
import lombok.Data;

import java.time.LocalDateTime;
@Data
public class NewAddressRetVo {
    private Long id;

    private Long regionId;

    private String detail;

    private String consignee;

    private String mobile;

    private Boolean beDefault;

    private String gmtCreate;

    private String gmtModified;

    public NewAddressRetVo(Address po){
        this.id=po.getId();
        this.regionId=po.getRegionId();
        this.detail=po.getDetail();
        this.consignee=po.getConsignee();
        this.mobile=po.getMobile();
        if(po.getBeDefault()==0){
            this.beDefault=false;
        }else{
            this.beDefault=true;
        }
        this.gmtCreate= String.valueOf(po.getGmtCreate());
        this.gmtModified= String.valueOf(po.getGmtModified());
    }



}
