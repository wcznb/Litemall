package cn.edu.xmu.advertisement.model.vo;

import cn.edu.xmu.advertisement.model.bo.Advertisement;
import cn.edu.xmu.advertisement.model.po.AdvertisementPo;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdvertisementRetVo {

Long id;

String link;

String imagePath;

String content;

Long segId;

String state;

String weight;

Boolean bedefault;

String beginDate;

String endDate;

Boolean repeats;

String gmt_created;

String gmt_modified;


public AdvertisementRetVo(Advertisement advertisement){

    this.id = advertisement.getId();
    this.link = advertisement.getLink();
   // this.bedefault = advertisement.getBeDefault().intValue()>0?true:false;
    if(advertisement.getBeDefault()==null)
    this.bedefault = null;
    else
        this.bedefault = advertisement.getBeDefault().intValue()>0?true:false;
    this.segId = advertisement.getSegId();
    this.content = advertisement.getContent();
    this.beginDate = advertisement.getBeginDate().toString();
    this.endDate = advertisement.getEndDate().toString();
    this.repeats = advertisement.getRepeates().intValue()>0?true:false;
    this.imagePath = advertisement.getImageUrl();
    if(advertisement.getWeight()==null)
        this.weight = null;
    else
        this.weight = advertisement.getWeight().toString();
    if(advertisement.getState()==null)
        this.state = null;
    else
        this.state = advertisement.getState().getCode().toString();
    if(advertisement.getGmt_created()==null)
        this.gmt_created = null;
    else
        this.gmt_created = advertisement.getGmt_created().toString();
    if(advertisement.getGmt_modified()==null)
        this.gmt_modified = null;
    else
        this.gmt_modified = advertisement.getGmt_modified().toString();




}

public  AdvertisementRetVo(AdvertisementPo advertisementPo){

    this.id = advertisementPo.getId();
    this.segId = advertisementPo.getSegId();
    if(advertisementPo.getBeDefault()==null)
        this.bedefault = null;
    else
        this.bedefault = advertisementPo.getBeDefault().intValue()>0?true:false;

    //this.bedefault = advertisementPo.getBeDefault().intValue()>0?true:false;
    this.link = advertisementPo.getLink();
    this.content = advertisementPo.getContent();
    this.beginDate =advertisementPo.getBeginDate().toString();
    this.endDate = advertisementPo.getEndDate().toString();
    this.repeats = advertisementPo.getRepeats().intValue()>0?true:false;
    this.imagePath = advertisementPo.getImageUrl();

    if(advertisementPo.getWeight()==null)
        this.weight=null;
    else
         this.weight = advertisementPo.getWeight().toString();

    this.state = advertisementPo.getState().toString();
    this.gmt_created = advertisementPo.getGmtCreate().toString();
    this.gmt_modified = advertisementPo.getGmtModified().toString();

}


}
