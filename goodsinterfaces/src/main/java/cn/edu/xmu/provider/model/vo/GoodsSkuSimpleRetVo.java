package cn.edu.xmu.provider.model.vo;

import cn.edu.xmu.provider.model.po.GoodsSkuPo;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Yancheng Lai
 * createdBy Yancheng Lai 2020/12/1 21:40
 * modifiedBy Yancheng Lai 21:40
 **/
@Data
public class GoodsSkuSimpleRetVo implements Serializable {

    private Long id;

    private String skuSn;

    private String name;

    private Long originalPrice;

    private Long price;

    private String imageUrl;

    private Integer inventory;

    private Boolean disable;

    /**
    * @Description: SKU返回简单视图
    * @Param: [goodSku]
    * @return:  GoodsSkuSimpleRetVo
    * @Author: Yancheng Lai
    * @Date: 2020/12/1 21:47
    */

    public GoodsSkuSimpleRetVo(GoodsSkuPo goodsSkuPo){
        this.setDisable((goodsSkuPo.getDisabled()==0)?false:true);
        this.setId (goodsSkuPo.getId());
        this.setImageUrl(goodsSkuPo.getImageUrl());
        this.setInventory( goodsSkuPo.getInventory());
        this.setName(goodsSkuPo.getName());
        this.setOriginalPrice ( goodsSkuPo.getOriginalPrice());
        this.setSkuSn ( goodsSkuPo.getSkuSn());
    }

    public GoodsSkuSimpleRetVo(){}
}
