package com.floats.demo.core;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import com.floats.demo.util.SingleFloatView;
import com.floats.demo.service.FloatService;


public class MyActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {


    private boolean showFloat;
    private int activityCount;

    public MyActivityLifecycleCallbacks() {
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
    }

    @Override
    public void onActivityStarted(Activity activity) {

        activityCount++;
        if (activityCount == 1) {
            //说明从后台回到了前台
            Log.d("MyActivityLifecycleCall", "前后台状态: 后台回到了前台");
            if (showFloat) {
                FloatService.showFloat(activity);
            }
        }
    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {
        Log.d("MyActivityLifecycleCall", "onActivityStopped:" + activity.getClass());
        activityCount--;
        if (activityCount == 0) {
            showFloat = false;

            boolean isOpen = SingleFloatView.showFloat;
            if (isOpen) {
                showFloat = true;
                FloatService.hideFloat(activity);
            }
            Log.d("MyActivityLifecycleCall", "前后台状态: 前台回到了后台");
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        Log.d("MyActivityLifecycleCall", "onActivityDestroyed:" + activity.getClass());
    }
}
