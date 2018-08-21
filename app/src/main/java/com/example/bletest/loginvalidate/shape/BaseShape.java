package com.example.bletest.loginvalidate.shape;

import android.content.Context;
import android.graphics.Canvas;

public abstract class BaseShape {
    public Context mContext;

    public BaseShape(Context mContext) {
        this.mContext = mContext;
    }

    public abstract void Init();
    public abstract void drawShap(Canvas canvas,int status);
    public abstract void preDo(int status);
    public abstract void offset(float x,float y);
}
