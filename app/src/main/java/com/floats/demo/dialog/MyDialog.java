package com.floats.demo.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.floats.demo.R;


public class MyDialog extends Dialog implements View.OnClickListener {
    private DialogCallBack callBack;
    public MyDialog(@NonNull Context context, DialogCallBack callBack) {
        super(context, R.style.MyDialog);
        this.callBack = callBack;
    }
    private ImageView mIvFloatStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window dialogWindow = getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        dialogWindow.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setContentView(R.layout.dialog_commom);
        dialogWindow.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager windowManager = dialogWindow.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (display.getWidth()); //设置宽度
        dialogWindow.setAttributes(lp);

        mIvFloatStatus = (ImageView) findViewById(R.id.iv_float_status);

        this.findViewById(R.id.float_btn).setOnClickListener(this);
        this.findViewById(R.id.copy_link).setOnClickListener(this);
        this.findViewById(R.id.browser_btn).setOnClickListener(this);
        this.findViewById(R.id.refresh_btn).setOnClickListener(this);
        this.findViewById(R.id.transmit_btn).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.float_btn:
                callBack.onFloatShow(this);
                break;
            case R.id.copy_link:
                callBack.onCopyLink();
                break;
            case R.id.browser_btn:
                callBack.onBrowseBtn();
                break;
            case R.id.refresh_btn:
                callBack.onRefreshBtn();
                dismiss();
                break;
            case R.id.transmit_btn:
                callBack.onTransmitBtn();
                break;
            default:
                break;
        }
    }

    public void setFloatText(String text){
        ((TextView)findViewById(R.id.float_text)).setText(text);
    }

    public void setFloatStatus(int resId) {
        if (mIvFloatStatus != null) {
            mIvFloatStatus.setImageResource(resId);
        }
    }

}
