package com.floats.demo.acts;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.floats.demo.R;
import com.floats.demo.service.FloatService;
import com.floats.demo.util.SingleFloatView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class ChatActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        setTitle("聊天界面");
    }


    private View floatView;
    private FrameLayout contentView;
    private String url = "";

    public void click(View view) {
//        url = "http://poker.fafa163.com";
        url = "https://www.baidu.com";
        showFloat(url);
    }

    private void showFloat(String url) {
        View rootView = getWindow().getDecorView().getRootView();
        contentView = (FrameLayout) rootView.findViewById(android.R.id.content);

        floatView = SingleFloatView.getInstance().getFloatView(this);
        if (floatView == null) {

            floatView = View.inflate(this, R.layout.activity_web_layout, null);
            contentView.addView(floatView);

            floatView.setVisibility(View.VISIBLE);


            WebView webview = (WebView) floatView.findViewById(R.id.webview);
            initWebViewSetting(webview, url);


            SingleFloatView.getInstance().setFloatView(floatView);
            SingleFloatView.getInstance().getFloatView(this);
        } else {
            if (floatView.getParent() == null) {

                contentView.addView(floatView);
            }
        }
        floatView.setVisibility(View.VISIBLE);

        FloatService.hideFloat(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(String messageEvent) {
        Log.d("MainActivity", "messageEvent:" + messageEvent);

        if ("show".equals(messageEvent)) {
            showFloat(url);
        } else if ("hide".equals(messageEvent)) {
            hideFloat();
        }
    }

    @Override
    protected void onDestroy() {
        if (contentView != null && floatView != null) {
            contentView.removeView(floatView);
        }
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }



    @Override
    public void onBackPressed() {
        if (floatView != null &&
                floatView.getParent() != null &&
                floatView.getVisibility() == View.VISIBLE) {
            floatView.setVisibility(View.GONE);
            if (SingleFloatView.showFloat) {
                FloatService.showFloat(this);
            } else {
                SingleFloatView.getInstance().setFloatView(null);
            }
        } else {
            super.onBackPressed();
        }
    }

    public void hideFloat() {
        if (floatView != null &&
                floatView.getParent() != null &&
                floatView.getVisibility() == View.VISIBLE) {
            floatView.setVisibility(View.GONE);
            FloatService.showFloat(this);
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebViewSetting(WebView webView, String url) {
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        WebSettings webSetting = webView.getSettings();
        webSetting.setJavaScriptEnabled(true);
        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(false);
        webSetting.setBuiltInZoomControls(false);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(false);
        webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(true);
        webSetting.setDatabaseEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        webSetting.setAppCachePath(getDir("appCache", Context.MODE_PRIVATE).getPath());
        webSetting.setDatabasePath(getDir("databases", Context.MODE_PRIVATE).getPath());
        webSetting.setGeolocationDatabasePath(getDir("geolocation", Context.MODE_PRIVATE).getPath());
        webSetting.setPluginState(WebSettings.PluginState.ON);
        webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSetting.setTextSize(WebSettings.TextSize.NORMAL);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSetting.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webView.setTag(url);
        webView.loadUrl(url);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(this)) {
                    Toast.makeText(this, "授权失败", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "授权成功", Toast.LENGTH_SHORT).show();
                    hideFloat();
                    FloatService.showFloat(this);
                }
            }
        }
    }

}
