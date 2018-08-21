package com.example.bletest.loginvalidate.shape;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import com.example.bletest.loginvalidate.picturevalidate.GapImageview;
import com.example.bletest.loginvalidate.picturevalidate.PicValidateView;

public class GapShape extends BaseShape {
    private int gapPoint[];
    private Paint mPaint;
    private int per;
    private Path mPath;


    public GapShape(Context mContext,int[] gapPoint) {
        super(mContext);
        this.gapPoint =gapPoint;
        per= GapImageview.per;
        Init();
    }

    @Override
    public void Init() {
        for(int i=0;i<gapPoint.length;i++){
            gapPoint[i]=gapPoint[i]*per;
        }
        InitPath();
    }

    public void InitPath(){
        mPaint=new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);//实心矩形框

        mPath=new Path();

        mPath.moveTo(gapPoint[0], gapPoint[1]);
        mPath.lineTo(gapPoint[2], gapPoint[3]);
        mPath.quadTo(gapPoint[2]+per, (gapPoint[3]+gapPoint[5])/2,gapPoint[4], gapPoint[5]);
        mPath.lineTo(gapPoint[6], gapPoint[7]);

        mPath.lineTo(gapPoint[8], gapPoint[9]);
        mPath.quadTo((gapPoint[8]+gapPoint[10])/2, gapPoint[9]-per,gapPoint[10], gapPoint[11]);
        mPath.lineTo(gapPoint[12], gapPoint[13]);

        mPath.lineTo(gapPoint[14], gapPoint[15]);
        mPath.quadTo(gapPoint[14]+per, (gapPoint[3]+gapPoint[5])/2,gapPoint[16], gapPoint[17]);
        mPath.lineTo(gapPoint[18], gapPoint[19]);

        mPath.lineTo(gapPoint[20], gapPoint[21]);
        mPath.quadTo((gapPoint[20]+gapPoint[22])/2, gapPoint[21]-per,gapPoint[22], gapPoint[23]);
        mPath.lineTo(gapPoint[0], gapPoint[1]);
        mPath.close();
    }

    @Override
    public void drawShap(Canvas canvas, int status) {
        canvas.drawPath(mPath, mPaint);
    }

    @Override
    public void preDo(int status) {

    }


    @Override
    public void offset(float x, float y) {
        for(int i=0;i<gapPoint.length;i=i+2){
            gapPoint[i]+=x;
            gapPoint[i+1]+=y;
        }
        InitPath();
    }
}
