package cn.edu.xmu.share.model.vo;

import lombok.Data;
import lombok.NonNull;

@Data
public class SharesVo {
    @NonNull
    Long shareId;
    @NonNull
    Long goodsSpuId;
}
