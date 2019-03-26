package com.floats.demo.util;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;

import com.floats.demo.R;
import com.floats.demo.dialog.DialogCallBack;
import com.floats.demo.dialog.MyDialog;
import com.floats.demo.service.FloatService;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by a on 2019/3/25.
 */

public class SingleFloatView {
    private SingleFloatView() {}

    public static boolean showFloat = false;

    private static class SingletonInstance {
        private static final SingleFloatView INSTANCE = new SingleFloatView();
    }

    public static SingleFloatView getInstance() {
        return SingletonInstance.INSTANCE;
    }
    private View floatView;

    public View getFloatView(final Activity activity) {
        if (floatView != null && activity != null) {
            ImageView ivMenu = (ImageView) floatView.findViewById(R.id.iv_menu);
            ivMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showMenuDialog(activity);
                }
            });
            ImageView ivClose = (ImageView) floatView.findViewById(R.id.iv_close);
            ivClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    floatView.setVisibility(View.GONE);
                    if (!SingleFloatView.showFloat) {
                        FloatService.hideFloat(activity);
                        SingleFloatView.getInstance().setFloatView(null);
                    }else{
                        FloatService.showFloat(activity);
                    }
                }
            });
        }
        return floatView;
    }

    private void showMenuDialog(final Activity activity) {
//        String status = showFloat ? "关闭浮窗" : "开启浮窗";
//        final String[] items = new String[]{status, "复制", "分享到好友", "系统浏览器打开"};
//        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
//        builder.setTitle("提示").setItems(items, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                if (items[i].equals("关闭浮窗")) {
//                    showFloat = false;
//                    Toast.makeText(activity, "已关闭浮窗", Toast.LENGTH_SHORT).show();
//                    FloatService.hideFloat(activity);
//                }
//                else if (items[i].equals("开启浮窗")) {
//                    showFloat = true;
//                    open(activity);
//                } else if (items[i].equals("复制")) {
//                    copy(activity);
//                } else if (items[i].equals("分享到好友")) {
//                    shareFriends(activity);
//                } else if (items[i].equals("系统浏览器打开")) {
//                    openSystemBrowser(activity);
//                }
//            }
//        });
//        builder.create().show();


        MyDialog myDialog = new MyDialog(activity, new DialogCallBack() {
            @Override
            public void onFloatShow(MyDialog dialog) {
                dialog.dismiss();
                if (showFloat){
                    showFloat = false;
                    Toast.makeText(activity, "已关闭浮窗", Toast.LENGTH_SHORT).show();
                    FloatService.hideFloat(activity);
                }else {
                    showFloat = true;
                    open(activity);
                }
            }

            @Override
            public void onCopyLink() {
                copy(activity);
            }

            @Override
            public void onBrowseBtn() {
                openSystemBrowser(activity);
            }

            @Override
            public void onRefreshBtn() {

                if (floatView != null) {
                    WebView webView = floatView.findViewById(R.id.webview);
                    if (webView != null) {
                        if (webView.getTag() != null) {
                            String tag = (String) webView.getTag();
                            if (tag != null) {
                                Toast.makeText(activity, "已刷新", Toast.LENGTH_SHORT).show();
                                webView.loadUrl(tag);
                            }
                        }
                    }
                }
            }

            @Override
            public void onTransmitBtn() {
                shareFriends(activity);
            }
        });
        myDialog.show();
        if (showFloat) {
            myDialog.setFloatText("取消浮窗");
            myDialog.setFloatStatus(R.mipmap.icon_close_window);
        } else {
            myDialog.setFloatText("浮窗");
            myDialog.setFloatStatus(R.mipmap.float_btn);
        }
    }
    private void shareFriends(Activity activity) {
        Toast.makeText(activity, "分享到好友", Toast.LENGTH_SHORT).show();
    }

    private void openSystemBrowser(Activity activity) {
        try {

            String url = "";
            if (floatView != null) {
                WebView webView = floatView.findViewById(R.id.webview);
                if (webView != null) {
                    url = webView.getUrl();
                }
            }
            Log.d("SingleFloatView", url);
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            activity.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void copy(Activity activity) {
        String url = "";
        if (floatView != null) {
            WebView webView = floatView.findViewById(R.id.webview);
            if (webView != null) {
                url = webView.getUrl();
            }
        }
        Log.d("SingleFloatView", url);

        ClipboardManager cm = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
        cm.setText(url);
        Toast.makeText(activity, "复制成功", Toast.LENGTH_SHORT).show();

        Log.d("WebActivity", url);
    }

    private void open(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(activity)) {
                Toast.makeText(activity, "当前无权限，请授权", Toast.LENGTH_SHORT);
                activity.startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + activity.getPackageName())), 0);
            } else {
                EventBus.getDefault().post("hide");
                FloatService.showFloat(activity);
            }
        }
    }
    public void setFloatView(View floatView) {
        this.floatView = floatView;
    }
}
