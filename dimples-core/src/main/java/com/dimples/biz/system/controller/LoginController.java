package com.dimples.biz.system.controller;

import com.dimples.biz.system.service.impl.ValidateCodeServiceImpl;
import com.dimples.core.annotation.OpsLog;
import com.dimples.core.eunm.OpsLogTypeEnum;
import com.dimples.core.transport.ResponseDTO;
import com.dimples.core.util.MD5Util;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 用户登陆
 *
 * @author zhongyj <1126834403@qq.com><br/>
 * @date 2019/11/14
 */
@Slf4j
@Api(tags = "用户登录模块")
@RestController
@RequestMapping("sys")
public class LoginController {

    private ValidateCodeServiceImpl validateCodeService;

    @Autowired
    public LoginController(ValidateCodeServiceImpl validateCodeService) {
        this.validateCodeService = validateCodeService;
    }

    private static Subject getSubject() {
        return SecurityUtils.getSubject();
    }

    @ApiOperation(value = "用户登陆", notes = "用户登陆")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true),
            @ApiImplicitParam(name = "password", value = "密码", required = true),
            @ApiImplicitParam(name = "verifyCode", value = "验证码", required = true),
            @ApiImplicitParam(name = "remember", value = "记住我", defaultValue = "false")
    })
    @OpsLog(value = "用户登陆", type = OpsLogTypeEnum.LOGIN)
    @PostMapping("/login")
    public ResponseDTO login(String username, String password, String verifyCode, @RequestParam(defaultValue = "false") Boolean remember, HttpServletRequest request) {
        password = MD5Util.encrypt(username, password);
        UsernamePasswordToken token = new UsernamePasswordToken(username, password, remember);
        // 验证码验证
        String key = request.getSession().getId();
        validateCodeService.check(key, verifyCode);
        // 用户登陆验证
        getSubject().login(token);
        log.info("{} 是否登录==>{}", getSubject(), getSubject().isAuthenticated());
        return ResponseDTO.success();
    }

    @ApiOperation(value = "退出登录", notes = "退出登录")
    @OpsLog(value = "退出登录", type = OpsLogTypeEnum.LOGOUT)
    @GetMapping("/logout")
    public ResponseDTO logout() {
        getSubject().logout();
        return ResponseDTO.success();
    }

    @OpsLog(value = "获取图形验证码", type = OpsLogTypeEnum.CAPTCHA)
    @GetMapping("/captcha")
    public void captcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        validateCodeService.create(request, response);
    }

}















