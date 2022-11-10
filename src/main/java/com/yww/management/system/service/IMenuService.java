package com.yww.management.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yww.management.system.entity.Menu;

import java.util.List;

/**
 * <p>
 *      菜单权限实体类 服务类
 * </p>
 *
 * @Author yww
 * @Date  2022-10-21
 */
public interface IMenuService extends IService<Menu> {

    /**
     * 根据角色ID查询菜单信息
     *
     * @param roleId    角色ID
     * @return          菜单信息
     */
    List<Menu> getMenusByRoleId(String roleId);

    /**
     * 根据用户ID查询菜单信息
     *
     * @param userId    用户ID
     * @return          菜单信息
     */
    List<Menu> getMenusByUserId(String userId);

}
