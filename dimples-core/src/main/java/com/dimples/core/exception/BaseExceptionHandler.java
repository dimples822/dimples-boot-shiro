package com.dimples.core.exception;

import com.dimples.core.eunm.CodeAndMessageEnum;
import com.dimples.core.transport.ResponseDTO;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.nio.file.AccessDeniedException;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;

import lombok.extern.slf4j.Slf4j;

/**
 * 基础异常捕捉
 *
 * @author zhongyj <1126834403@qq.com><br/>
 * @date 2019/12/27
 */
@Slf4j
public class BaseExceptionHandler {

    /**
     * BizException
     *
     * @param e BizException
     * @return ResponseDTO
     */
    @ExceptionHandler(value = BizException.class)
    public ResponseDTO handleBizException(BizException e) {
        log.error("业务错误", e);
        return ResponseDTO.failed(e.getMessage());
    }

    /**
     * AccessDeniedException
     *
     * @return ResponseDTO
     */
    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseDTO handleAccessDeniedException() {
        return ResponseDTO.failed(CodeAndMessageEnum.NOT_AUTH);
    }

    /**
     * LockedAccountException
     *
     * @param e LockedAccountException
     * @return ResponseDTO
     */
    @ExceptionHandler(value = LockedAccountException.class)
    public ResponseDTO handleException(LockedAccountException e) {
        log.error("登录验证错误,异常信息: ", e);
        return ResponseDTO.failed(CodeAndMessageEnum.ACCOUNT_LOCK);
    }

    /**
     * UnknownAccountException
     *
     * @param e UnknownAccountException
     * @return ResponseDTO
     */
    @ExceptionHandler(value = UnknownAccountException.class)
    public ResponseDTO handleException(UnknownAccountException e) {
        log.error("登录验证错误,异常信息: ", e);
        return ResponseDTO.failed(CodeAndMessageEnum.USER_NOT_EXISTED);
    }


    /**
     * IncorrectCredentialsException
     *
     * @param e IncorrectCredentialsException
     * @return ResponseDTO
     */
    @ExceptionHandler(value = IncorrectCredentialsException.class)
    public ResponseDTO handleException(IncorrectCredentialsException e) {
        log.error("登录验证错误,异常信息: ", e);
        return ResponseDTO.failed(CodeAndMessageEnum.ACCOUNT_ERROR);
    }

    /**
     * 统一处理请求参数校验(普通传参)
     *
     * @param e ConstraintViolationException
     * @return ResponseDTO
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseDTO handleConstraintViolationException(ConstraintViolationException e) {
        StringBuilder message = new StringBuilder();
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        for (ConstraintViolation<?> violation : violations) {
            Path path = violation.getPropertyPath();
            String[] pathArr = StringUtils.splitByWholeSeparatorPreserveAllTokens(path.toString(), ".");
            message.append(pathArr[1]).append(violation.getMessage()).append(",");
        }
        message = new StringBuilder(message.substring(0, message.length() - 1));
        return ResponseDTO.failed(CodeAndMessageEnum.REQUEST_INVALIDATE.getCode(), message.toString());
    }

    /**
     * Exception
     *
     * @param e Exception
     * @return ResponseDTO
     */
    @ExceptionHandler(value = Exception.class)
    public ResponseDTO handleException(Exception e) {
        log.error("系统内部异常,异常信息: ", e);
        return ResponseDTO.failed(CodeAndMessageEnum.FAIL);
    }

}














