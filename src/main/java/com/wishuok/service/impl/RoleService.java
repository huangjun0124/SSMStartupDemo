package com.wishuok.service.impl;

import com.wishuok.mapper.RoleMapper;
import com.wishuok.pojo.Role;
import com.wishuok.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


//添加Service注解，让Spring进行实例化管理，并给该实例化对象取一个名字，后面使用
@Service("roleService")
public class RoleService implements IRoleService {

    //注入需要的Mapper实例，用于方法调用
    @Autowired
    private RoleMapper roleMapper;

    @Override
    public Role findRoleById(String id) {
        return roleMapper.selectByPrimaryKey(id);
    }
}
