package com.fyl.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * Created by zyj on 2016/5/13 0013.
 */
public class PermissionsUtil {
    private static final String TAG = "PermissionsUtil";

    //权限监听回调
    public interface PermissionCallbacks extends
            ActivityCompat.OnRequestPermissionsResultCallback {

        void onPermissionsGranted(int requestCode, List<String> perms);

        void onPermissionsDenied(int requestCode, List<String> perms);

    }

    //是否拥有权限
    public static boolean hasPermissions(Context context, String... perms) {
        for (String perm : perms) {
            boolean hasPerm = (ContextCompat.checkSelfPermission(context, perm) == PackageManager
                    .PERMISSION_GRANTED);
            if (!hasPerm) {
                return false;
            }
        }

        return true;
    }

    // 申请权限,兼容多个权限申请
    public static void requestPermissions(final Object object, String rationale,
                                          String positiveButton,
                                          String negativeButton,
                                          final int requestCode, final String... perms) {

        checkCallingObjectSuitability(object);
        final PermissionCallbacks callbacks = (PermissionCallbacks) object;

        boolean shouldShowRationale = false;
        for (String perm : perms) {
            shouldShowRationale = shouldShowRationale || shouldShowRequestPermissionRationale
                    (object, perm);
        }

        if (shouldShowRationale) {
            AlertDialog dialog = new AlertDialog.Builder(getActivity(object))
                    .setMessage(rationale)
                    .setPositiveButton(positiveButton, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            executePermissionsRequest(object, perms, requestCode);
                        }
                    })
                    .setNegativeButton(negativeButton, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // act as if the permissions were denied
                            callbacks.onPermissionsDenied(requestCode, Arrays.asList(perms));
                        }
                    }).create();
            dialog.show();
        } else {
            executePermissionsRequest(object, perms, requestCode);
        }
    }

    public static void onRequestPermissionsResult(int requestCode, String[] permissions,
                                                  int[] grantResults, Object object) {

        checkCallingObjectSuitability(object);
        PermissionCallbacks callbacks = (PermissionCallbacks) object;

        // Make a collection of granted and denied permissions from the request.
        ArrayList<String> granted = new ArrayList<>();
        ArrayList<String> denied = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            String perm = permissions[i];
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                granted.add(perm);
            } else {
                denied.add(perm);
            }
        }

        // Report granted permissions, if any.
        if (!granted.isEmpty()) {
            // Notify callbacks
            callbacks.onPermissionsGranted(requestCode, granted);
        }

        // Report denied permissions, if any.
        if (!denied.isEmpty()) {
            callbacks.onPermissionsDenied(requestCode, denied);
        }

    }

    //是否需要权限解释对话框
    @SuppressWarnings("SimplifiableIfStatement")
    private static boolean shouldShowRequestPermissionRationale(Object object, String perm) {
        if (object instanceof Activity) {
            return ActivityCompat.shouldShowRequestPermissionRationale((Activity) object, perm);
        } else if (object instanceof Fragment) {
            return ((Fragment) object).shouldShowRequestPermissionRationale(perm);
        } else {
            return false;
        }
    }

    private static Activity getActivity(Object object) {
        if (object instanceof Activity) {
            return ((Activity) object);
        } else if (object instanceof Fragment) {
            return ((Fragment) object).getActivity();
        } else {
            return null;
        }
    }

    private static void executePermissionsRequest(Object object, String[] perms, int requestCode) {
        checkCallingObjectSuitability(object);
        if (object instanceof Activity) {
            ActivityCompat.requestPermissions((Activity) object, perms, requestCode);
        } else if (object instanceof Fragment) {
            ((Fragment) object).requestPermissions(perms, requestCode);
        }
    }

    private static void checkCallingObjectSuitability(Object object) {

        if (!((object instanceof Fragment) || (object instanceof Activity))) {
            throw new IllegalArgumentException("Caller must be an Activity or a Fragment.");
        }


        if (!(object instanceof PermissionCallbacks)) {
            throw new IllegalArgumentException("Caller must implement PermissionCallbacks.");
        }
    }

}
