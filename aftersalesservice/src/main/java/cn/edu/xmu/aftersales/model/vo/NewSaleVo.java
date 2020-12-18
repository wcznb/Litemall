package cn.edu.xmu.aftersales.model.vo;

import cn.edu.xmu.aftersales.model.bo.AftersalesBo;
import io.swagger.models.auth.In;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class NewSaleVo {



    @NotNull(message = "售后类型不能为空")
    @Range(message = "只能有0 1 2三种售后", min = 0, max = 3)
    private Byte type;
    @NotNull(message = "数量不能为空")
    @Range(message="数量必须大于0",min=1)
     private Integer quantity;
    @NotBlank(message = "申请理由不能为空")
     private String reason;
    @NotNull(message = "地区id不能为空")
     private Integer regionId;
    @NotBlank(message = "详细地址不能为空")
     private String detail;
    @NotBlank(message = "联系人不能为空")
     private String consignee;
    @NotBlank(message = "电话不能为空")
    @Size(max = 11,min=11)
     private String mobile;

     public AftersalesBo createAfterSale(){
           AftersalesBo aftersalesBo=new AftersalesBo();
           aftersalesBo.setType(AftersalesBo.saleType.getTypeByCode(Integer.valueOf(type)));
           aftersalesBo.setQuantity(this.quantity);
           aftersalesBo.setReason(this.reason);
           aftersalesBo.setRegionId(Long.valueOf(regionId));
           aftersalesBo.setDetail(this.detail);
           aftersalesBo.setConsignee(this.consignee);
           aftersalesBo.setMobile(this.mobile);
           return aftersalesBo;
     }

}
