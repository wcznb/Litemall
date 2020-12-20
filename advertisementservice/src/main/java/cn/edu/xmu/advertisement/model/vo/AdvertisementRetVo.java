package cn.edu.xmu.advertisement.model.vo;

import cn.edu.xmu.advertisement.model.bo.Advertisement;
import cn.edu.xmu.advertisement.model.po.AdvertisementPo;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
public class AdvertisementRetVo {

    Long id;

    String link;

    String imagePath;

    String content;

    Long segId;

    Integer state;

    String weight;

    Boolean beDefault;

    String beginDate;

    String endDate;

    Boolean repeat;

    String gmtCreate;
    //
    String gmtModified;


    public AdvertisementRetVo(Advertisement advertisement) {


        this.id = advertisement.getId();
        this.link = advertisement.getLink();
        // this.bedefault = advertisement.getBeDefault().intValue()>0?true:false;
        if (advertisement.getBeDefault() == null)
            this.beDefault = null;
        else
            this.beDefault = advertisement.getBeDefault().intValue() > 0 ? true : false;
        this.segId = advertisement.getSegId();
        this.content = advertisement.getContent();
        this.beginDate = advertisement.getBeginDate().toString();
        this.endDate = advertisement.getEndDate().toString();
        this.repeat = advertisement.getRepeate().intValue() > 0 ? true : false;

        if (advertisement.getImageUrl() == null)
            this.imagePath = null;
        else
            this.imagePath = advertisement.getImageUrl();
        if (advertisement.getWeight() == null)
            this.weight = null;
        else
            this.weight = advertisement.getWeight().toString();
        if (advertisement.getState() == null)
            this.state = null;
        else
            this.state = advertisement.getState().getCode();

        if(advertisement.getGmtCreate()==null)
            this.gmtCreate = null;
        else
            this.gmtCreate= advertisement.getGmtCreate().toString();
        if(advertisement.getGmtModified()==null)
            this.gmtModified = null;
        else
            this.gmtModified = advertisement.getGmtModified().toString();


    }

    public AdvertisementRetVo(AdvertisementPo advertisementPo) {


        this.id = advertisementPo.getId();
        this.segId = advertisementPo.getSegId();
        if (advertisementPo.getBeDefault() == null)
            this.beDefault = null;
        else
            this.beDefault = advertisementPo.getBeDefault().intValue() > 0 ? true : false;

        //this.bedefault = advertisementPo.getBeDefault().intValue()>0?true:false;
        this.link = advertisementPo.getLink();
        this.content = advertisementPo.getContent();
        this.beginDate = advertisementPo.getBeginDate().toString();
        this.endDate = advertisementPo.getEndDate().toString();
        this.repeat = advertisementPo.getRepeats().intValue() > 0 ? true : false;

        if (advertisementPo.getImageUrl() == null)
            this.imagePath = null;
        else
            this.imagePath = advertisementPo.getImageUrl();

        if (advertisementPo.getWeight() == null)
            this.weight = null;
        else
            this.weight = advertisementPo.getWeight().toString();

        this.state = advertisementPo.getState().intValue();

        if(advertisementPo.getGmtCreate()==null)
            this.gmtCreate = null;
        else
            this.gmtCreate= advertisementPo.getGmtCreate().toString();
        if(advertisementPo.getGmtModified()==null)
            this.gmtModified = null;
        else
            this.gmtModified = advertisementPo.getGmtModified().toString();


    }


}
