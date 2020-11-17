package cn.edu.xmu.user.service;

import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.user.dao.UserDao;
import cn.edu.xmu.user.model.vo.NewUserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    @Autowired
    UserDao userDao;

    /**
     * @param vo 注册的vo对象
     * @return ReturnObject
     * @author LiangJi3229
     */
    @Transactional
    public ReturnObject register(NewUserVo vo) {
        return userDao.createNewUserByVo(vo);
    }
}
