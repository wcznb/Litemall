package cn.edu.xmu.provider.model.vo;

import cn.edu.xmu.provider.model.bo.GoodsSKU;
import cn.edu.xmu.provider.model.po.GoodsSkuPo;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

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
    private Integer price;
    private String imageUrl;
    private Integer inventory;
    private Byte disabled;

    /**
    * @Description: SKU返回简单视图
    * @Param: [goodSku]
    * @return:  GoodsSkuSimpleRetVo
    * @Author: Yancheng Lai
    * @Date: 2020/12/1 21:47
    */

    public GoodsSkuSimpleRetVo(GoodsSkuPo goodsSkuPo){
        this.setDisabled( goodsSkuPo.getDisabled());
        this.setId (goodsSkuPo.getId());
        this.setImageUrl(goodsSkuPo.getImageUrl());
        this.setInventory( goodsSkuPo.getInventory());
        this.setName(goodsSkuPo.getName());
        this.setOriginalPrice ( goodsSkuPo.getOriginalPrice());
        this.setSkuSn ( goodsSkuPo.getSkuSn());
    }

    public GoodsSkuSimpleRetVo(){}
}
