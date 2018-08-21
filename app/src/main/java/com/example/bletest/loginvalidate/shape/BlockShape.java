package com.example.bletest.loginvalidate.shape;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import com.example.bletest.R;
import com.example.bletest.loginvalidate.picturevalidate.PicValidateView;


public class BlockShape extends BaseShape {
    private Paint mPaint;
    private int shapecolor,pathColor;
    private int bgColor;
    private float[] mPoints;
    private Rect rect;
    private Rect pathRect;
    public static int errorColor= 0xFA807200;
    private int height,width;

    public BlockShape(float[] mPoints,int width,int height, Context context) {
        super(context);
        this.width=width;
        this.height=height;
        this.mPoints=mPoints;
        Init();
    }

    @Override
    public void Init() {
        if (mPaint==null){
            mPaint=new Paint();
        }
        if (rect==null){
            rect=new Rect(0,0,height,height);
            pathRect=new Rect(0,0,0,0);
        }else {
            rect.right=height;
            pathRect.left=0;
            pathRect.right=0;
            pathRect.bottom=0;
            pathRect.top=0;
        }
        offset(0,0);
        bgColor= mContext.getResources().getColor(R.color.idelBlockColor);
        shapecolor=mContext.getResources().getColor(R.color.idelshapeColor);

    }

    @Override
    public  void drawShap(Canvas canvas,int status) {
        preDo(status);

        if (mPaint==null){
            return;
        }

        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        //画背景
        mPaint.setColor(bgColor);
        canvas.drawRect(rect,mPaint);
        //画路径
        mPaint.setColor(pathColor);
        canvas.drawRect(pathRect,mPaint);

        canvas.save();//锁画布(为了保存之前的画布状态)
        canvas.translate(totalX, totalY);//把当前画布的原点移到(10,10),后面的操作都以(10,10)作为参照点，默认原点为(0,0)
        //画形状
        mPaint.setStrokeWidth(5);
        mPaint.setColor(shapecolor);
        canvas.drawLines(mPoints,mPaint);
        canvas.restore();
    }

    @Override
    public void preDo(int status) {
        if (status== PicValidateView.IDEL){
            if ( PicValidateView.validateStatue==PicValidateView.SUCCESS){
                rect.left=totalX;rect.top=0;rect.right=totalX+height;rect.bottom=height;
                pathRect.left=0;pathRect.top=0;pathRect.right=totalX;pathRect.bottom=height;
                mPoints= PicValidateView.rightPoint;
            } else{
                Init();
                mPoints= PicValidateView.arrowPoint;
            }
        }else if (status== PicValidateView.TOUCH){
            rect.left=totalX;rect.top=0;rect.right=totalX+height;rect.bottom=height;
            pathRect.left=0;pathRect.top=0;pathRect.right=totalX;pathRect.bottom=height;
            bgColor= mContext.getResources().getColor(R.color.sideBlockColor);
            shapecolor=mContext.getResources().getColor(R.color.shapeColor);
            pathColor=mContext.getResources().getColor(R.color.pathBlockkColor);
        }else if (status== PicValidateView.BACKTO && PicValidateView.validateStatue==PicValidateView.FAIL){
            rect.left=totalX;rect.top=0;rect.right=totalX+height;rect.bottom=height;
            pathRect.left=0;pathRect.top=0;pathRect.right=totalX;pathRect.bottom=height;
            bgColor= mContext.getResources().getColor(R.color.errorBlockColor);
            shapecolor=mContext.getResources().getColor(R.color.shapeColor);
            pathColor=mContext.getResources().getColor(R.color.errorPathColor);
            mPoints= PicValidateView.wrongPoints;
        }
    }

    private int totalX=0,totalY=0;

    @Override
    public void offset(float x, float y) {
        totalX= (int) x;
        totalY= (int) y;
        if (width<totalX+height){
            totalX=width-height;
        }
    }

}
