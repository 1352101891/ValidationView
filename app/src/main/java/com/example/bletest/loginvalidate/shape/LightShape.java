package com.example.bletest.loginvalidate.shape;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;

import com.example.bletest.R;
import com.example.bletest.loginvalidate.picturevalidate.PicValidateView;

import static com.example.bletest.loginvalidate.picturevalidate.PicValidateView.IDEL;
import static com.example.bletest.loginvalidate.picturevalidate.PicValidateView.SUCCESS;

public class LightShape  extends BaseShape {
    private int[] mPoints;
    private int recWidth=10;
    private int startX=100;
    private int width,height;
    private Path mPath;
    private Paint mPaint;

    public LightShape(Context mContext,int width,int height) {
        super(mContext);
        this.width=width;
        this.height=height;
        startX =width/5;
        mPoints=new int[]{0,height,recWidth,height,startX+recWidth,0,startX,0};
        Init();
    }

    @Override
    public void Init() {
        mPaint=new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mContext.getResources().getColor(R.color.lighttransparent));
        InitPath();
        middle();
    }

    @Override
    public void drawShap(Canvas canvas, int status) {
        preDo(status);
        if (status==IDEL && PicValidateView.validateStatue==SUCCESS) {
//            mPaint.setShader(new LinearGradient(midx, midy, midx + recWidth, midy, new int[]{ Color.WHITE, Color.BLUE}, null, LinearGradient.TileMode.CLAMP));
            canvas.drawPath(mPath, mPaint);
        }
    }

    @Override
    public void preDo(int status) {

    }

    int totalx=0;
    @Override
    public void offset(float x, float y) {
        if ( x>=width){
            totalx= width;
        }else {
            totalx = (int) x;
        }
        InitPath();
        middle();
    }

    public void InitPath(){
        //path复用之前一定要清空，要不然之前保留的路径都会绘制都canvas上
        mPath=new Path();
        mPath.moveTo(mPoints[0]+totalx,mPoints[1]);
        for (int i=2;i<mPoints.length;i=i+2){
            mPath.lineTo(mPoints[i]+totalx,mPoints[i+1]);
        }
        mPath.close();
    }

    int midx=0,midy=0;
    public void middle(){
        midx=(mPoints[0]+mPoints[6])/2+totalx;
        midy=(mPoints[1]+mPoints[7])/2;
    }
}
