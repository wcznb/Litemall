package cn.edu.xmu.address.model.bo;

import cn.edu.xmu.address.model.po.AddressPo;
import cn.edu.xmu.address.model.vo.AddressRetVo;
import cn.edu.xmu.ooad.model.VoObject;
import lombok.Data;

@Data
public class Address implements VoObject {
    private Long id;

    private Long regionId;

    private String detail;

    private String consignee;

    private String mobile;

    private Byte beDefault;

    private String gmtCreate;

    private String gmtModified;

    public Byte state; //get from region part ??

    public Address(AddressPo po){
        this.id=po.getId();
        this.regionId=po.getRegionId();
        this.detail=po.getDetail();
        this.consignee=po.getConsignee();
        this.mobile=po.getMobile();
        this.beDefault=po.getBeDefault();
        this.gmtCreate= String.valueOf(po.getGmtCreate());
        this.gmtModified=String.valueOf(po.getGmtModified());
        //build state by region apis getstateByRegionId
    }


    @Override
    public Object createVo(){
        return new AddressRetVo(this);
    }

    @Override
    public Object createSimpleVo(){
        return new AddressRetVo(this);
    }

}
