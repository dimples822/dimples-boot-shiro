package com.dimples.biz.system.controller;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.dimples.biz.common.dto.DeptTreeDTO;
import com.dimples.biz.system.po.Dept;
import com.dimples.biz.system.service.DeptService;
import com.dimples.core.annotation.OpsLog;
import com.dimples.core.eunm.OpsLogTypeEnum;
import com.dimples.core.transport.QueryRequest;
import com.dimples.core.transport.ResponseDTO;
import com.wuwenze.poi.ExcelKit;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhongyj <1126834403@qq.com><br/>
 * @date 2020/2/5
 */
@Slf4j
@Api(value = "部门管理模块", tags = "部门管理模块")
@RestController
@RequestMapping("dept")
public class DeptController {

    private DeptService deptService;

    @Autowired
    public DeptController(DeptService deptService) {
        this.deptService = deptService;
    }

    @ApiOperation(value = "获取部门树", notes = "获取部门树")
    @OpsLog(value = "获取部门树", type = OpsLogTypeEnum.SELECT)
    @GetMapping("select/tree")
    public ResponseDTO getDeptTree() {
        List<DeptTreeDTO<Dept>> deptList = this.deptService.findDeptList();
        return ResponseDTO.success(deptList);
    }

    @ApiOperation(value = "根据条件获取部门树", notes = "根据条件获取部门树")
    @OpsLog(value = "根据条件获取部门树", type = OpsLogTypeEnum.SELECT)
    @GetMapping("tree")
    public ResponseDTO getDeptTree(Dept dept) {
        List<DeptTreeDTO<Dept>> deptList = this.deptService.findDeptList(dept);
        return ResponseDTO.success(deptList);
    }

    @ApiOperation(value = "新增部门", notes = "新增部门")
    @OpsLog(value = "新增部门", type = OpsLogTypeEnum.ADD)
    @PostMapping
    @RequiresPermissions("dept:add")
    public ResponseDTO addDept(@Valid Dept dept) {
        this.deptService.insertDept(dept);
        return ResponseDTO.success();
    }

    @ApiOperation(value = "删除部门", notes = "删除部门")
    @ApiImplicitParam(name = "deptIds", value = "部门字符串，以,分隔", paramType = "string", dataType = "path", required = true)
    @OpsLog(value = "删除部门", type = OpsLogTypeEnum.DELETE)
    @DeleteMapping("delete/{deptIds}")
    @RequiresPermissions("dept:delete")
    public ResponseDTO deleteDepts(@NotBlank(message = "{required}") @PathVariable String deptIds) {
        String[] ids = deptIds.split(StringPool.COMMA);
        this.deptService.deleteDeptList(ids);
        return ResponseDTO.success();
    }

    @ApiOperation(value = "修改部门", notes = "修改部门")
    @OpsLog(value = "修改部门", type = OpsLogTypeEnum.UPDATE)
    @PostMapping("update")
    @RequiresPermissions("dept:update")
    public ResponseDTO updateDept(@Valid Dept dept) {
        this.deptService.updateDept(dept);
        return ResponseDTO.success();
    }

    @ApiOperation(value = "导出部门Excel", notes = "导出部门Excel")
    @OpsLog(value = "导出部门Excel", type = OpsLogTypeEnum.EXPORT)
    @GetMapping("excel")
    @RequiresPermissions("dept:export")
    public void export(Dept dept, QueryRequest request, HttpServletResponse response) {
        List<Dept> depts = this.deptService.findDeptList(dept, request);
        ExcelKit.$Export(Dept.class, response).downXlsx(depts, false);
    }

}













