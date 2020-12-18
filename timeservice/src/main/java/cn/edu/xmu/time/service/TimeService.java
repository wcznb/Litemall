package cn.edu.xmu.time.service;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.time.dao.TimeDao;
import cn.edu.xmu.time.model.po.TimeSegmentPo;
import cn.edu.xmu.time.model.vo.NewTimeSegmentVo;
import cn.edu.xmu.time.model.vo.TimeSegmentRetVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.lettuce.core.StrAlgoArgs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public interface TimeService {

    //need dubbo
    ReturnObject<TimeSegmentRetVo> addTimeSegment(NewTimeSegmentVo newTimeSegmentVo,Byte type);

    public ReturnObject deleteTimeSegment0(Long id);


    @Transactional
    public ReturnObject deleteTimeSegment1(Long id);


    public ReturnObject<PageInfo<VoObject>> getAllTimeSegments(Byte type,Integer page,Integer pagesize);

    public List<TimeSegmentPo> getAllFlashsaleTimeSegmentsInternal();
}
