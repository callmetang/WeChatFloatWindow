package com.floats.demo.service;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.floats.demo.R;
import com.floats.demo.util.ScreenUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by a on 2019/3/20.
 */

public class FloatService extends Service {


    public static boolean isOpen;

    public static void showFloat(Activity activity) {
        if (activity != null) {
            hideFloat(activity);
            isOpen = true;
            activity.startService(new Intent(activity, FloatService.class));
        }
    }

    public static void hideFloat(Activity activity) {
        if (activity != null) {
            activity.stopService(new Intent(activity, FloatService.class));
        }
        isOpen = false;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        showFloatingWindow();
        return super.onStartCommand(intent, flags, startId);
    }

    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;
    private View view;

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (windowManager != null && view != null) {
            windowManager.removeView(view);
        }
    }


    private void showFloatingWindow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.canDrawOverlays(this)) {
                // 获取WindowManager服务
                windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

                view = View.inflate(getApplicationContext(), R.layout.float_view, null);
                if (view != null) {

                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            EventBus.getDefault().post("show");

//                            BaseActivity.sendBroadcast(getApplicationContext(), true);
//                            BaseActivity.baseActivity.showFloat();
//                            startActivity(new Intent(getApplicationContext(), WebActivity.class));
                        }
                    });
                    view.setOnTouchListener(new FloatingOnTouchListener());
                    // 设置LayoutParam
                    layoutParams = new WindowManager.LayoutParams();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
                    } else {
                        layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
                    }
                    layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                            | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
                    layoutParams.format = PixelFormat.RGBA_8888;
                    // 设置悬浮窗口长宽数据
                    layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
                    layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

                    int x = ScreenUtils.getScreenWidth(getApplicationContext()) / 3;
                    int y = ScreenUtils.getScreenHeight(getApplicationContext()) / 4;
                    Log.d("FloatService2", "x:" + x);
                    layoutParams.x = x;
                    layoutParams.y = -y;
                    // 参考系为左上
//                    layoutParams.gravity = Gravity.RIGHT | Gravity.TOP;
                    // 将悬浮窗控件添加到WindowManager
                    windowManager.addView(view, layoutParams);
                }


            }
        }
    }


    private class FloatingOnTouchListener implements View.OnTouchListener {
        private int x;
        private int y;

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    x = (int) event.getRawX();
                    y = (int) event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    int nowX = (int) event.getRawX();
                    int nowY = (int) event.getRawY();
                    int movedX = nowX - x;
                    int movedY = nowY - y;
                    x = nowX;
                    y = nowY;
                    if (layoutParams != null) {
                        layoutParams.x = layoutParams.x + movedX;
                        layoutParams.y = layoutParams.y + movedY;
                    }
                    if (windowManager != null) {

                        // 更新悬浮窗控件布局
                        windowManager.updateViewLayout(view, layoutParams);
                    }
                    break;
                default:
                    break;
            }
            return false;
        }
    }

}
