package cn.edu.xmu.address.model.vo;

import cn.edu.xmu.address.model.bo.Address;
import lombok.Data;

@Data
public class AddressRetVo {

    private Long id;

    private Long regionId;

    private String detail;

    private String consignee;

    private String mobile;

    private Boolean beDefault;

    private String gmtCreate;

    private Byte state;

    public AddressRetVo(Address address){
        this.id=address.getId();
        this.regionId=address.getRegionId();
        this.detail=address.getDetail();
        this.consignee=address.getConsignee();
        this.mobile=address.getMobile();
        if(address.getBeDefault()==0){
            this.beDefault=false;
        }else{
            this.beDefault=true;
        }
        this.gmtCreate=address.getGmtCreate();
        this.state=address.getState();

    }


}
