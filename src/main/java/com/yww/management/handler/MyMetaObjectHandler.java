package com.yww.management.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.yww.management.system.entity.User;
import com.yww.management.system.service.IUserService;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * <p>
 *      Mybatis-plus的自动填充处理处理
 *      注意是对象属性名不是表的字段名
 * </p>
 *
 * @ClassName MyMetaObjectHandler
 * @Author yww
 * @Date 2022/10/12 21:14
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    private final IUserService userService;

    @Autowired
    public MyMetaObjectHandler(IUserService userService) {
        this.userService = userService;
    }

    /**
     * 插入数据时填充创建和修改时间
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class , LocalDateTime.now());
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class , LocalDateTime.now());
        User user = userService.getCurrentUser();
        if (null == user) {
            // 匿名账户处理
            this.strictInsertFill(metaObject, "createBy", String.class, "anonymousUser");
            this.strictInsertFill(metaObject, "updateBy", String.class, "anonymousUser");
        } else {
            // 登录账户处理
            this.strictInsertFill(metaObject, "createBy", String.class, user.getUsername());
            this.strictInsertFill(metaObject, "updateBy", String.class, user.getUsername());
        }
    }

    /**
     * 修改数据时更新修改时间
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        User user = userService.getCurrentUser();
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class , LocalDateTime.now());
        if (null == user) {
            // 匿名账户处理
            this.strictInsertFill(metaObject, "updateBy", String.class, "anonymousUser");
        } else {
            // 登录账户处理
            this.strictInsertFill(metaObject, "updateBy", String.class, user.getUsername());
        }
    }

}

