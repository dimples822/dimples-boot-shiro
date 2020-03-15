package com.dimples.system.service;

import com.dimples.system.po.UserInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author zhongyj <1126834403@qq.com><br/>
 * @date 2020/2/21
 */
public interface UserInfoService extends IService<UserInfo> {


    /**
     * 根据用户id更新用户信息
     *
     * @param userInfo UserInfo
     */
    void updateByUserId(UserInfo userInfo);
}
