package cn.edu.xmu.user.service;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.user.model.vo.CustomerSetVo;
import cn.edu.xmu.user.model.vo.NewUserVo;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public interface UserService {
    @Transactional
    ReturnObject register(NewUserVo vo);

    @Transactional
    ReturnObject<String> login(String userName, String password, String ipAddr);

    void banJwt(String jwt);

    ReturnObject Logout(Long userId);

    ReturnObject<PageInfo<VoObject>> getallusers(String userName, String email, String mobile, Integer page, Integer pageSize);

    @Transactional
    ReturnObject<Object> banCustomer(Long did,Long userId);

    @Transactional
    ReturnObject<Object> releaseCustomer(Long did,Long userId);

    @Transactional
    ReturnObject <Object> getCustomer(Long id);

    ReturnObject <Object> modifyCustomer(Long id, CustomerSetVo vo);

    @Transactional
    ReturnObject<VoObject> findUserById(Long id);
}
