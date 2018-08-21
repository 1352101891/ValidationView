package com.example.bletest.loginvalidate.shape;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.animation.LinearInterpolator;

import com.example.bletest.R;
import com.example.bletest.loginvalidate.picturevalidate.PicValidateView;

import static com.example.bletest.loginvalidate.picturevalidate.PicValidateView.IDEL;
import static com.example.bletest.loginvalidate.picturevalidate.PicValidateView.SUCCESS;

public class ArrowShape extends BaseShape {
    private float[] mPoints;
    private Paint mPaint;
    private int shapecolor;
    private Rect rect;
    private int height,width;

    public ArrowShape(float[] mPoints,int width,int height, Context context) {
        super(context);
        this.height=height;
        this.width=width;
        this.mPoints=mPoints;
        Init();
    }

    @Override
    public void Init() {
        if (mPaint==null){
            mPaint=new Paint();
            shapecolor= mContext.getResources().getColor(R.color.greenArrow);
        }
    }

    @Override
    public void drawShap(Canvas canvas,int status) {
        preDo(status);
        if (mPaint==null){
            return;
        }
        canvas.save();//锁画布(为了保存之前的画布状态)
        canvas.translate(totalX, totalY);//把当前画布的原点移到(10,10),后面的操作都以(10,10)作为参照点，默认原点为(0,0)
        //画形状
        mPaint.setStrokeWidth(5);
        mPaint.setColor(shapecolor);
        canvas.drawLines(mPoints,mPaint);
        canvas.restore();//把当前画布返回（调整）到上一个save()状态之前
    }

    @Override
    public void preDo(int status) {
        if (status==IDEL){
            offset(width/120,0);
            Init();
        }else {
            mPaint=null;
        }
        if (PicValidateView.validateStatue==SUCCESS){
            mPaint=null;
        }
    }

    private int totalX=0,totalY=0;

    @Override
    public void offset(float x, float y) {
        totalX = (int) ((totalX+x)%width);
        totalY =  (int) ((totalY+y)%height);
    }

}
