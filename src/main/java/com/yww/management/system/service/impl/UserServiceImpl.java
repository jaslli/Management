package com.yww.management.system.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yww.management.security.AccountUser;
import com.yww.management.system.entity.Menu;
import com.yww.management.system.entity.User;
import com.yww.management.system.entity.UserRole;
import com.yww.management.system.mapper.UserMapper;
import com.yww.management.system.service.IMenuService;
import com.yww.management.system.service.IRoleService;
import com.yww.management.system.service.IUserRoleService;
import com.yww.management.system.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *      用户信息实体类 服务实现类
 * </p>
 *
 * @Author  yww
 * @Date  2022-10-21
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    IUserRoleService userRoleService;
    @Autowired
    IRoleService roleService;
    @Autowired
    IMenuService menuService;

    @Override
    public User getByUsername(String username) {
        return this.getOne(new QueryWrapper<User>().lambda().eq(User::getUsername, username));
    }

    @Override
    public List<GrantedAuthority> getUserAuthorities(String userId) {
        // TODO 权限信息可以先从Redis从获取，或者是从Token里面获取
        StringBuilder authority = new StringBuilder();
        // 目前该系统一个用户只对应一个角色信息
        UserRole userRole = userRoleService
                .getOne(new QueryWrapper<UserRole>().lambda().eq(UserRole::getUserId, userId));
        if (ObjectUtil.isNull(userRole)) {
            return null;
        }
        String roleId = userRole.getRoleId();
        String roleCode = roleService.getById(roleId).getCode();
        if (StrUtil.isNotBlank(roleCode)) {
            authority.append("ROLE_").append(roleCode);
        }
        List<Menu> menus = menuService.getMenusByRoleId(roleId);
        if (menus.size() > 0) {
            for (Menu menu : menus) {
                authority.append(",").append(menu.getCode());
            }
        }
        return AuthorityUtils.commaSeparatedStringToAuthorityList(authority.toString());
    }

    @Override
    @SuppressWarnings("all")
    public User getCurrentUser() {
        Object object = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // 如果是匿名账号，返回空值
        if ("anonymousUser".equals(object)) {
            return null;
        } else {
            AccountUser user = (AccountUser) object;
            // 返回用户信息
            User res = this.getOne(new QueryWrapper<User>().lambda().eq(User::getUsername, user.getUsername()));
            res.setPassword("");
            return res;
        }
    }

}