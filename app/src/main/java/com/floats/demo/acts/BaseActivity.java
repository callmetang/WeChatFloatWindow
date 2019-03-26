package com.floats.demo.acts;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.floats.demo.util.FloatUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    private FloatUtil floatUtil = new FloatUtil();


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(String messageEvent) {
        Log.d("OtherActivity", "messageEvent:" + messageEvent);
        if ("show".equals(messageEvent)) {
            floatUtil.showFloat(this);
        } else if ("hide".equals(messageEvent)) {
            floatUtil.hideFloat(this);
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        floatUtil.onStart(this);
    }
    @Override
    protected void onPause() {
        super.onPause();
        floatUtil.onPause(this);
    }
    @Override
    public void onBackPressed() {
        if (floatUtil.onBackPressed(this)) {
            super.onBackPressed();
        }
    }

}
