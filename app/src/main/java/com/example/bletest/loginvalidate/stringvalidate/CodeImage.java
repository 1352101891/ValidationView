package com.example.bletest.loginvalidate.stringvalidate;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.bletest.R;

import java.util.Random;


public class CodeImage extends View {
    private String code="8621";
    int mWidth,mHeight;
    Paint mPaint;
    Rect mBounds;
    int size=45;
    Random random;
    private String TAG="CodeImage";
    private  Path path;

    public CodeImage(Context context) {
        super(context);
        Init();
    }

    public CodeImage(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        Init();
    }

    public CodeImage(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Init();
    }

    public void Init(){
        Log.e(TAG,"CodeImage  create()");

        mPaint=new Paint();
        mPaint.setTextSize(size);
        mPaint.setColor(Color.BLACK);
        random = new Random();
        changeCode();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.e(TAG,"CodeImage  onMeasure()");

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);


        if (heightMode == MeasureSpec.EXACTLY)
        {
            mHeight = heightSize;
        } else
        {
            mBounds=new Rect();
            mPaint.getTextBounds(code, 0, code.length(), mBounds);
            float textHeight = mBounds.height();
            int desired = (int) (getPaddingTop() + textHeight + getPaddingBottom());
            if (desired<textHeight*2){
                desired= (int) (2*textHeight);
            }
            mHeight = desired;
        }

        if (MeasureSpec.EXACTLY==widthMode){

            mWidth=widthSize;
        }else {
            mWidth= (mBounds.width()+getPaddingLeft()+getPaddingRight())+mBounds.height();
        }
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.e(TAG,"CodeImage  onDraw()");

        canvas.drawColor(getContext().getResources().getColor(R.color.codeBgColor));

        //随机设置画笔的颜色
        Paint paint=new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        for (int i=0;i<20;i++) {
            paint.setStrokeWidth(10);
            paint.setARGB(255, random.nextInt(255), random.nextInt(255),
                    random.nextInt(255));
            canvas.drawPoint(random.nextInt(getMeasuredWidth()), random.nextInt(getMeasuredHeight()), mPaint);
        }


        if (path!=null) {
            mPaint.setARGB(argb[0],argb[1],argb[2],argb[3]);
            mPaint.setStrokeWidth(5);
            mPaint.setStyle(Paint.Style.STROKE);
            canvas.drawPath(path, mPaint);


            mPaint.setStrokeWidth(3);
            mPaint.setColor(Color.BLACK);
            canvas.drawTextOnPath(code, path, 0, 15, mPaint);
        }
    }

    public float rh(){
        float rh=random.nextFloat()*(2*getHeight())/3;
        if (rh<getHeight()*0.3){
            rh= (float) (getHeight()*0.3);
        }else if (rh>getHeight()*0.7){
            rh= (float) (getHeight()*0.7);
        }
        return rh;
    }

    public void changeCode(){
        String temp="";
        for (int i=0;i<4;i++) {
            temp+= random.nextInt(9)+" ";
        }
        code=temp ;
        InitPath();
        Log.d(TAG,"code:"+code);
        invalidate();
    }

    int[] argb=new int[4];
    public void InitPath(){
        path=new Path();
        float w=getMeasuredWidth()/5;
        path.moveTo( w*random.nextFloat() ,rh());
        path.lineTo( w+w*random.nextFloat() ,rh());
        path.lineTo( 2*w+w*random.nextFloat(),rh());
        path.lineTo( 3*w+w*random.nextFloat() ,rh());
        path.lineTo( 4*w+w*random.nextFloat() ,rh());

        //保存颜色避免在弹出键盘的时候measure，layout导致颜色改变
        argb[0]=255;
        argb[1]=random.nextInt(255);
        argb[2]=random.nextInt(255);
        argb[3]=random.nextInt(255);
    }

}
