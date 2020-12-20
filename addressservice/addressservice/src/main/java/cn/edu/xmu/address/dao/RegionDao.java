package cn.edu.xmu.address.dao;

import cn.edu.xmu.address.mapper.RegionPoMapper;
import cn.edu.xmu.address.model.bo.Region;
import cn.edu.xmu.address.model.po.AddressPo;
import cn.edu.xmu.address.model.po.AddressPoExample;
import cn.edu.xmu.address.model.po.RegionPo;
import cn.edu.xmu.address.model.po.RegionPoExample;
import cn.edu.xmu.address.model.vo.NewRegionVo;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class RegionDao {

    @Autowired
    RegionPoMapper regionPoMapper;

    public ReturnObject<Object> addRegion(NewRegionVo vo,Long pid){

        //这里检查上级地区是否存在或废弃
        RegionPo checkPo = new RegionPo();
        checkPo = regionPoMapper.selectByPrimaryKey(pid);

        if(checkPo==null){

            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);

        }else if(checkPo.getState()==1){

            return new ReturnObject<>(ResponseCode.REGION_OBSOLETE);

        }else{

            RegionPo regionPo=new RegionPo();
            ReturnObject returnObject;
            regionPo.setName(vo.getName());
            regionPo.setPid(pid);
            regionPo.setPostalCode(Long.valueOf(vo.getPostalCode()));
            regionPo.setState((byte)0);
            LocalDateTime localDateTime=LocalDateTime.now();
            regionPo.setGmtCreate(localDateTime);
            regionPo.setGmtModified(localDateTime);

            try{

                int ret = regionPoMapper.insertSelective(regionPo);

                if (ret == 0) {
                    //插入失败
                    returnObject = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
                } else {
                    //插入成功
                    returnObject = new ReturnObject<>();
                }
            }catch (DataAccessException e) {

                returnObject = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
            }
            catch (Exception e) {

                returnObject = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了严重的数据库错误：%s", e.getMessage()));
            }
            return returnObject;
        }
    }

    public ReturnObject<Object> updateRegion(NewRegionVo vo,Long id){

        //这里检查本地区是否存在或废弃
        RegionPo checkPo=regionPoMapper.selectByPrimaryKey(id);

        if(checkPo==null){

            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);

        }else if(checkPo.getState()==1){

            return new ReturnObject<>(ResponseCode.REGION_OBSOLETE);

        }else{

            RegionPo regionPo= new RegionPo();
            regionPo.setName(vo.getName());
            regionPo.setPostalCode(Long.valueOf(vo.getPostalCode()));
            LocalDateTime localDateTime=LocalDateTime.now();
            regionPo.setGmtModified(localDateTime);
            regionPo.setId(id);

            try{
                int ret = regionPoMapper.updateByPrimaryKeySelective(regionPo);

                if(ret==0){
                    return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
                }else {
                    return new ReturnObject<>();
                }

            }catch (Exception e){
                return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
            }

        }

    }
    public ReturnObject<Object> disableRegion(Long id){

        RegionPo checkPo=regionPoMapper.selectByPrimaryKey(id);

        if(checkPo==null){

            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);

        }else if(checkPo.getState()==1){

            return new ReturnObject<>(ResponseCode.REGION_OBSOLETE);

        }else{

            RegionPo regionPo= new RegionPo();
            LocalDateTime localDateTime=LocalDateTime.now();
            regionPo.setGmtModified(localDateTime);
            regionPo.setId(id);
            regionPo.setState((byte) 1);
            disableChildren(id);

            try{

                int ret = regionPoMapper.updateByPrimaryKeySelective(regionPo);

                if(ret==0){
                    return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
                }else {
                    return new ReturnObject<>();
                }

            }catch (Exception e){
                return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
            }

        }
    }

    public void disableChildren(Long pid){

        RegionPoExample example = new RegionPoExample();
        RegionPoExample.Criteria criteria = example.createCriteria();
        criteria.andPidEqualTo(pid);
        List<RegionPo> pos = null;
        pos = regionPoMapper.selectByExample(example);
        if(pos.size()==0){
            return;
        }else{
            for(RegionPo po : pos){
                po.setState((byte) 1);
                regionPoMapper.updateByPrimaryKeySelective(po);
                Long thisPid = po.getId();
                disableChildren(thisPid);
            }
        }

    }



    public ReturnObject<Object> getAncestorRegion(Long id){

        RegionPo checkPo=regionPoMapper.selectByPrimaryKey(id);

        if(checkPo==null){

            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);

        }else if(checkPo.getState()==1){

            return new ReturnObject<>(ResponseCode.REGION_OBSOLETE);

        }else{

            List<RegionPo> pos=null;
            try{

                if (checkPo.getPid()!=null){
                    pos = getAncestors(checkPo.getPid());
                }


            }catch(DataAccessException e){

                return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));

            }

            return new ReturnObject<>(pos);
        }
    }

    public List<RegionPo> getAncestors(Long pid){

        List<RegionPo> pos=null;
        List<RegionPo> res=new ArrayList<RegionPo>();

        while(pid!=0){

            RegionPoExample example=new RegionPoExample();
            RegionPoExample.Criteria criteria=example.createCriteria();
            criteria.andIdEqualTo(pid);

            pos=regionPoMapper.selectByExample(example);

            if(pos.size()!=0&&pid!=null){

                res.add(pos.get(0));
                pid= pos.get(0).getPid();


            }else{
                break;
            }

        }
        return res;
    }

    public Boolean isExist(Long id){

        RegionPo checkPo=regionPoMapper.selectByPrimaryKey(id);

        if(checkPo==null){
            return false;
        }else {
            return true;
        }

    }

    public Boolean isAble(Long id){

        RegionPo checkPo=regionPoMapper.selectByPrimaryKey(id);

        if(checkPo.getState()==1){
            return false;
        }else{
            return true;
        }

    }

    public Byte getStateByRegionId(Long id){

        return regionPoMapper.selectByPrimaryKey(id).getState();

    }

}
