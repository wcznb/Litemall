package cn.edu.xmu.footprint.model.bo;

import lombok.Data;

@Data
public class GoodsSku {
    private Long id;
    private String name;
    private String SkuSn;
    private String imageUrl;
    private Integer inventory;
    private Long originalPrice;
    private Long price;
    private Boolean disable;
}
