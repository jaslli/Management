package com.yww.management.system.service.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yww.management.system.entity.Menu;
import com.yww.management.system.mapper.MenuMapper;
import com.yww.management.system.mapper.UserMapper;
import com.yww.management.system.service.IMenuService;
import com.yww.management.system.service.IRoleService;
import com.yww.management.system.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *      菜单权限实体类 服务实现类
 * </p>
 *
 * @Author  yww
 * @Date  2022-10-21
 */
@Service
@RequiredArgsConstructor(onConstructor_ = {@Lazy})
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    private final UserMapper userMapper;

    @Override
    public List<Menu> getMenusByRoleId(String roleId) {
        return baseMapper.getMenusByRoleId(roleId);
    }

    @Override
    public List<Menu> getMenusByUserId(String userId) {
        String roleId = userMapper.getRoleIdByUserId(userId);
        return baseMapper.getMenusByRoleId(roleId);
    }

}

