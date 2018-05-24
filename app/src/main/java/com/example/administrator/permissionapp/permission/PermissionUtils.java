package com.example.administrator.permissionapp.permission;

import android.app.Activity;
import android.app.Fragment;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Description: 处理权限请求的工具类 .
 * Copyright  : Copyright (c) 2018
 * Company    :
 * Author     : 田高攀
 * Date       : 2018/5/20 11:04
 */

public class PermissionUtils {

    private PermissionUtils() {
        throw new UnsupportedOperationException("不能实例化对象");
    }

    /**
     * 判断是不是6.0及以上的版本 .
     * @return .
     */
    public static boolean isOverMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    /**
     * 执行成功方法 .
     * @param object .
     * @param requestCode .
     */
    public static void executeSucceedMethod(Object object, int requestCode) {
        //获取class中的方法,去找到标记的
        Method[] declaredMethods = object.getClass().getDeclaredMethods();
        for (Method method : declaredMethods) {
            //获取该方法上面有没有这个标记 有的话肯定不为空 .
            PermissionSuccess succeedMethod = method.getAnnotation(PermissionSuccess.class);
            if (succeedMethod != null && requestCode == succeedMethod.requestCode()) {
                //反射执行该方法 .
                executeMethod(object, method);
                break;
            }
        }
    }

    /**
     * 反射执行该方法 .
     * @param object .
     * @param method .
     */
    private static void executeMethod(Object object, Method method) {
        //第一个参数 是方法属于哪个类
        //第二个是传 参数 .
        try {
            //允许执行私有方法 .
            method.setAccessible(true);
            method.invoke(object, new Object[]{});
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取所有没有授予过得权限 .
     * @param object .
     * @param permissions .
     * @return .
     */
    public static List<String> getDeniedPermissions(Object object, String[] permissions) {
        List<String> deniedPermissions = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(getActivity(object), permission)
                    == PackageManager.PERMISSION_DENIED) {
                deniedPermissions.add(permission);
            }
        }
        return deniedPermissions;
    }

    public static Activity getActivity(Object object) {
        if (object instanceof Activity) {
           return (Activity) object;
        }
        if (object instanceof Fragment) {
            return ((Fragment) object).getActivity();
        }
        return null;
    }


    public static void executeFailMethod(Object object, int requestCode) {
        //获取class中的方法,去找到标记的
        Method[] declaredMethods = object.getClass().getDeclaredMethods();
        for (Method method : declaredMethods) {
            //获取该方法上面有没有这个标记 有的话肯定不为空 .
            PermissionFail succeedMethod = method.getAnnotation(PermissionFail.class);
            if (succeedMethod != null && requestCode == succeedMethod.requestCode()) {
                //反射执行该方法 .
                executeMethod(object, method);
                break;
            }
        }
    }
}
