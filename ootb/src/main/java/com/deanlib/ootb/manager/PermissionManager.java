package com.deanlib.ootb.manager;

import android.app.Activity;

import com.tbruyelle.rxpermissions.Permission;
import com.tbruyelle.rxpermissions.RxPermissions;

import rx.functions.Action1;

/**
 * 权限管理
 *
 * Created by dean on 2017/7/20.
 */

public class PermissionManager {

    /**
     * 权限是否被允许
     * @param activity
     * @param permission
     * @return
     */
    public static boolean isGranted(Activity activity,String permission){

        RxPermissions rxPermissions = new RxPermissions(activity);

        return rxPermissions.isGranted(permission);

    }

    /**
     * 请求权限
     * @param activity
     * @param action
     * @param permissions
     */
    public static void requstPermission(Activity activity,Action1<Permission> action,String... permissions){

        RxPermissions rxPermissions = new RxPermissions(activity);

        rxPermissions.requestEach(permissions).subscribe(action);

    }



}
