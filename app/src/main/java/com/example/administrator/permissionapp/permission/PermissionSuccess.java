package com.example.administrator.permissionapp.permission;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Description:
 * Copyright  : Copyright (c) 2018
 * Company    :
 * Author     : 田高攀
 * Date       : 2018/5/20 11:16
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PermissionSuccess {
    //请求码 .
    public int requestCode();
}
