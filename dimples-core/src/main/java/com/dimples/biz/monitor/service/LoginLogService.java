package com.dimples.biz.monitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dimples.biz.system.po.LoginLog;

/**
 * @author zhongyj <1126834403@qq.com><br/>
 * @date 2020/1/28
 */
public interface LoginLogService extends IService<LoginLog> {

    /**
     * 保存登陆日志
     *
     * @param username String
     */
    void saveLoginLog(String username);

    /**
     * 统计当日访问IP量
     *
     * @return Integer
     */
    Integer todayIpTotal();

    /**
     * 统计当日访问量
     *
     * @return Integer
     */
    Integer todayTotal();
}














