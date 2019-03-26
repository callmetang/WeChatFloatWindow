package com.floats.demo.util;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;

import com.floats.demo.acts.ChatActivity;
import com.floats.demo.service.FloatService;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by a on 2019/3/25.
 */

public class FloatUtil {


    private View floatView;
    private FrameLayout contentView;

    public void showFloat(Activity activity) {
        if (activity instanceof ChatActivity) {
            return;
        }
        View rootView = activity.getWindow().getDecorView().getRootView();
        contentView = (FrameLayout) rootView.findViewById(android.R.id.content);

        floatView = SingleFloatView.getInstance().getFloatView(activity);
        if (floatView != null && floatView.getParent() == null) {
            contentView.addView(floatView);
        }
        if (floatView != null) {
            floatView.setVisibility(View.VISIBLE);
        }
        FloatService.hideFloat(activity);
    }

    public void hideFloat(Activity activity) {
        if (activity instanceof ChatActivity) {
            return;
        }
        if (floatView != null && floatView.getParent() != null && floatView.getVisibility() == View.VISIBLE) {
            floatView.setVisibility(View.GONE);
            if (!SingleFloatView.showFloat){
                FloatService.hideFloat(activity);
            }
        }
    }

    public void onStart(Activity activity) {
        if (activity instanceof ChatActivity) {
            return;
        }
        EventBus.getDefault().register(activity);
    }

    public void onPause(Activity activity) {
        if (activity instanceof ChatActivity){
            return;
        }
        if (contentView != null && floatView != null) {
            contentView.removeView(floatView);
        }
        if (EventBus.getDefault().isRegistered(activity)) {
            EventBus.getDefault().unregister(activity);
        }
    }

    public boolean onBackPressed(Activity activity) {
        if (activity instanceof ChatActivity){
            return true;
        }
        if (floatView != null && floatView.getParent() != null && floatView.getVisibility() == View.VISIBLE) {
            floatView.setVisibility(View.GONE);
            if (SingleFloatView.showFloat) {
                FloatService.showFloat(activity);
            } else {
                SingleFloatView.getInstance().setFloatView(null);
            }
            return false;
        } else {
            return true;
        }
    }

}
