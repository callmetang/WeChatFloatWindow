package com.floats.demo.core;

import android.app.Application;

import com.floats.demo.util.SingleFloatView;

/**
 * Created by a on 2019/3/20.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        SingleFloatView.showFloat = false;
        SingleFloatView.getInstance().setFloatView(null);
        registerActivityLifecycleCallbacks(new MyActivityLifecycleCallbacks());
    }
}
