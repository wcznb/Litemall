package cn.edu.xmu.aftersales.model.vo;


import cn.edu.xmu.aftersales.model.bo.AftersalesBo;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class UpdateVo {
//每个属性必须有值吗
    //@NotNull(message = "数量不能为空")
    @Range(message = "数量必须是正数",min=1)
    private Integer quantity;
   // @NotBlank(message = "申请理由不能为空")
    private String reason;
    //@NotNull(message = "地区id不能为空")
    private Integer regionId;
   // @NotBlank(message = "详细地址不能为空")
    private String detail;
   // @NotBlank(message = "联系人不能为空")
    private String consignee;
   // @NotBlank(message = "电话不能为空")
   @Size(max = 11,min=11)
    private String mobile;

    public AftersalesBo createAfterSale(){
        AftersalesBo aftersalesBo=new AftersalesBo();
        if(quantity!=null)
        aftersalesBo.setQuantity(this.quantity);
        if(this.reason!=null)
        aftersalesBo.setReason(this.reason);
        if(this.regionId!=null)
        aftersalesBo.setRegionId(Long.valueOf(regionId));
        if(this.detail!=null)
        aftersalesBo.setDetail(this.detail);
        if(this.consignee!=null)
        aftersalesBo.setConsignee(this.consignee);
        if(this.mobile!=null)
        aftersalesBo.setMobile(this.mobile);
        return aftersalesBo;
    }
}
