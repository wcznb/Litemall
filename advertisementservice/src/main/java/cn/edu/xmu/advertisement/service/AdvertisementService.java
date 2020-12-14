package cn.edu.xmu.advertisement.service;

import cn.edu.xmu.advertisement.model.vo.AdvertisementMessageVo;
import cn.edu.xmu.advertisement.model.vo.AdvertisementUpdateVo;
import cn.edu.xmu.advertisement.model.vo.NewAdvertisementVo;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ReturnObject;
import com.github.pagehelper.PageInfo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AdvertisementService {

    /**
     * 管理员设置默认广告
     *
     * @param id 广告id
     */
    public ReturnObject<Boolean> becomeDefault(Long id);
    /**
     * 管理员下架广告
     *
     * @param id 广告id
     */
    public ReturnObject<Boolean> adoffshelf(Long id);
    /**
     * 管理员上架广告
     *
     * @param id 广告id
     */
    public ReturnObject<Boolean> adonshelf(Long id);
    /**
     * 管理员删除广告
     *
     * @param id 广告id
     */
    public ReturnObject deleteAd(Long id);
    /**
     * 管理员审核默认广告
     *
     * @param id 广告id
     */
    public ReturnObject <Object> messageAd( Long id, AdvertisementMessageVo vo);
    /**
     * 修改广告信息
     *
     * @param id 广告id
     */
    public ReturnObject <Object> messageUpdate( Long id, AdvertisementUpdateVo vo);
    /**
     * 在广告时段下新建广告
     *
     * @param tid 广告id
     */
    public ReturnObject createUnderSegID(Long tid, NewAdvertisementVo vo);
    /**
     * 管理员在广告时段下增加广告
     *
     * @param tid 广告id
     */
    public ReturnObject addAdBySegID(Long tid,Long id);
    /**
     * 管理员查看某一时段的广告
     *
     * @param id 广告id
     */
    public ReturnObject<PageInfo<VoObject>> getAdBySegId(Long id, Integer page, Integer pageSize,String beginDate,String endDate);
    /**
     * 管理员查看当前时段的广告
     *
     */
    public   ReturnObject<List> getCurrentAd();

    /**
     * 上传图片
     * @param id: 广告id
     * @param multipartFile: 文件
     * @return
     */
    public ReturnObject uploadImg(Long id, MultipartFile multipartFile);




}
