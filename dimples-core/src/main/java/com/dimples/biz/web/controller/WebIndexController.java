package com.dimples.biz.web.controller;

import com.dimples.biz.monitor.service.LoginLogService;
import com.dimples.biz.monitor.vo.StatisticVO;
import com.dimples.biz.system.po.User;
import com.dimples.biz.web.constant.PageConstant;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

/**
 * @author zhongyj <1126834403@qq.com><br/>
 * @date 2020/1/26
 */
@Slf4j
@Controller
@RequestMapping("web")
public class WebIndexController {

    private LoginLogService loginLogService;

    @Autowired
    public WebIndexController(LoginLogService loginLogService) {
        this.loginLogService = loginLogService;
    }

    @GetMapping("index")
    public ModelAndView index() {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        // TODO: 2020/1/29  开发时设置，生产环境去除
        user = User.builder().username("zhongyj").userId(4L).build();
        log.info("当前用户登陆：{}", user.getUsername());
        StatisticVO statistic = StatisticVO.builder()
                .loginTotal(loginLogService.count())
                .todayIpTotal(loginLogService.todayIpTotal())
                .todayTotal(loginLogService.todayTotal())
                .build();
        ModelAndView view = new ModelAndView(PageConstant.INDEX);
        view.addObject("user", user);
        view.addObject("statistic", statistic);
        return view;
    }
}















