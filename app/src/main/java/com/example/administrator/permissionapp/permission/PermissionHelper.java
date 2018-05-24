package com.example.administrator.permissionapp.permission;

import android.app.Activity;
import android.app.Fragment;
import android.support.v4.app.ActivityCompat;

import java.util.List;

/**
 * Description:
 * Copyright  : Copyright (c) 2018
 * Company    :
 * Author     : 田高攀
 * Date       : 2018/5/19 22:02
 */

public class PermissionHelper {

    /**
     *  需要传递
     *  1 object 如activity，fragment
     *  2请求码
     *  3需要的权限 new string[]
     */
    private Object mObject;
    private int mRequestCode;
    private String[] mPermissions;

    private PermissionHelper(Object object) {
         this.mObject = object;
    }

    /**
     *
     * @param activity .
     * @param requestCode .
     * @param permissions .
     */
    public static void requestPermission(Activity activity, int requestCode, String[] permissions) {
        PermissionHelper.with(activity)
                .requestCode(requestCode)
                .requestPermissions(permissions)
                .request();
    }

    /**
     * 链式传递 .
     * @param activity .
     * @return .
     */
    public static PermissionHelper with(Activity activity) {
       return new PermissionHelper(activity);
    }

    public static PermissionHelper with(Fragment fragment) {
        return new PermissionHelper(fragment);
    }

    public PermissionHelper requestCode(int requestCode) {
        this.mRequestCode = requestCode;
        return this;
    }

    public PermissionHelper requestPermissions(String... permissions) {
         this.mPermissions = permissions;
         return this;
    }

    /**
     * 根据传递进来的真正发起请求和判断 .
     */
    public void request() {
        //先判断版本是不是6.0及以上
        //如果不是，直接执行方法 用反射方式 .
        if (!PermissionUtils.isOverMarshmallow()) {
            //直接执行
            PermissionUtils.executeSucceedMethod(mObject, mRequestCode);
            return;
        }
        //如果是，先判断权限是否授予，如果是，则直接执行方法
         //判断是否有没授予过得权限 .
        List<String> deniedPermissions = PermissionUtils.getDeniedPermissions(mObject, mPermissions);
        //如果不是，就申请权限 .
        if (deniedPermissions.isEmpty()) {
            //表示全都授予过了.
            PermissionUtils.executeSucceedMethod(mObject, mRequestCode);
        } else {
            //有没授予过得，需要去申请权限 .
            ActivityCompat.requestPermissions(PermissionUtils.getActivity(mObject)
                    , deniedPermissions.toArray(new String[deniedPermissions.size()])
                    , mRequestCode);
        }
    }

    public static void requestPermissionsResult(Object object, int requestCode, String[] permissions) {
        List<String> deniedPermissions = PermissionUtils.getDeniedPermissions(object, permissions);
        if (deniedPermissions.isEmpty()) {
            //表示用户都同意权限授予 .
            PermissionUtils.executeSucceedMethod(object, requestCode);
        } else {
            //表示申请的权限中有用户不同意的 .
            PermissionUtils.executeFailMethod(object, requestCode);
        }
    }
}
