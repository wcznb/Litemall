package cn.edu.xmu.time.service.impl;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.time.dao.TimeDao;
import cn.edu.xmu.time.model.po.TimeSegmentPo;
import cn.edu.xmu.time.model.vo.NewTimeSegmentVo;
import cn.edu.xmu.time.model.vo.TimeSegmentRetVo;
import cn.edu.xmu.time.service.TimeService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TimeServiceImpl implements TimeService {
    @Autowired
    private TimeDao timeDao;
    @Transactional
    @Override
    public ReturnObject<TimeSegmentRetVo> addTimeSegment(NewTimeSegmentVo newTimeSegmentVo, Byte type) {
        return timeDao.addTimeSegment(newTimeSegmentVo,type);
    }

    @Transactional
    @Override
    public ReturnObject deleteTimeSegment0(Long id){
        return timeDao.deleteTimeSegmentById0(id);
    }
    @Transactional
    @Override
    public ReturnObject deleteTimeSegment1(Long id){
        return timeDao.deleteTimeSegmentById1(id);
    }

    @Override
    public ReturnObject<PageInfo<VoObject>> getAllTimeSegments(Byte type, Integer page, Integer pagesize){

        ReturnObject<PageInfo<VoObject>> ret = timeDao.getAllTimeSegments(type,page, pagesize);
        return ret;
    }

    @Override
    public List<TimeSegmentPo> getAllFlashsaleTimeSegmentsInternal(){
        return timeDao.getAllFlashsaleTimeSegmentsInternal();
    }
}
