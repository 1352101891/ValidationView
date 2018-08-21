package com.example.bletest.loginvalidate.picturevalidate;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bletest.R;
import com.example.bletest.loginvalidate.callback.MoveCallback;

import static com.example.bletest.loginvalidate.Util.dp2px;

public class ImageFloatView implements MoveCallback {
    Context mContext;
    WindowManager wm;
    GapImageview gapImageview;
    private int PX,PY;
    WindowManager.LayoutParams params;
    boolean attach=false;

    public ImageFloatView(Context context) {
        this.mContext=context;
        wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
    }


    public void InitView(int positionX,int positionY){

        int height=dp2px(mContext,300);

        //创建WindowManager的布局参数对象
        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT, height, 0, 0, PixelFormat.TRANSPARENT);

        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        //params.type = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
        params.type = WindowManager.LayoutParams.TYPE_APPLICATION;

        params.gravity = Gravity.TOP| Gravity.START;
        params.x = positionX;
        params.y = positionY-height-50;
        PX=positionX;
        PY=positionY-height-50;
        //创建一个View
        gapImageview = new GapImageview(mContext);
        gapImageview.setScaleType(ImageView.ScaleType.CENTER_CROP);
        gapImageview.setImageResource(R.drawable.timg);
    }

    public void remove(){
        if (!attach){
            return;
        }
        wm.removeViewImmediate(gapImageview);
        attach=false;
    }

    public void add(){
        if (attach){
            return;
        }
        wm.addView(gapImageview,params);
        attach=true;
    }

    @Override
    public void move(float percent) {
        if (percent>1){
            percent=1;
        }
        if (percent<0){
            percent=0;
        }
        gapImageview.move(percent);
    }
}
