package com.yww.management.system.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yww.management.security.AccountUser;
import com.yww.management.system.entity.Menu;
import com.yww.management.system.entity.User;
import com.yww.management.system.mapper.UserMapper;
import com.yww.management.system.service.IMenuService;
import com.yww.management.system.service.IRoleService;
import com.yww.management.system.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户信息实体类 服务实现类
 * </p>
 *
 * @Author yww
 * @Date 2022-10-21
 */
@Service
@RequiredArgsConstructor(onConstructor_ = {@Lazy})
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    private static final String ANONYMOUS_USER = "anonymousUser";
    private final IRoleService roleService;
    private final IMenuService menuService;

    @Override
    public User getByUsername(String username) {
        return this.getOne(new QueryWrapper<User>().lambda().eq(User::getUsername, username));
    }

    @Override
    public List<GrantedAuthority> getUserAuthorities(String userId) {
        // TODO 权限信息可以先从Redis从获取，或者是从Token里面获取
        StringBuilder authority = new StringBuilder();
        // 目前该系统一个用户只对应一个角色信息
        String roleId = baseMapper.getRoleIdByUserId(userId);
        String roleCode = roleService.getById(roleId).getCode();
        if (StrUtil.isNotBlank(roleCode)) {
            authority.append(roleCode);
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
    public User getCurrentUser() {
        Object object = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // 如果是匿名账号，返回空值
        if (ANONYMOUS_USER.equals(object)) {
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
