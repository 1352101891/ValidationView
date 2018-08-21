package com.example.bletest.loginvalidate.shape;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.example.bletest.R;
import com.example.bletest.loginvalidate.picturevalidate.PicValidateView;

import static com.example.bletest.loginvalidate.picturevalidate.PicValidateView.SUCCESS;

public class TextShape extends BaseShape {
    private String bgTips="向右滑动填充图片解锁";
    private int tipSize= 45;
    private Paint mPaint;
    private Rect mBounds;
    private int mWidth,mHeight;

    /**
     *  视图宽高
     * @param width
     * @param height
     */
    public TextShape(int width, int height, Context context) {
        super(context);
        this.mWidth=width;
        this.mHeight=height;

        Init();
    }

    public void Init() {
        if (mPaint == null) {
            mPaint = new Paint();
            mPaint.setTextSize(tipSize);
            mPaint.setColor(mContext.getResources().getColor(R.color.idelshapeColor));
            mBounds = new Rect();
            mPaint.getTextBounds(bgTips, 0, bgTips.length(), mBounds);
        }
    }

    @Override
    public void drawShap(Canvas canvas, int status) {
        preDo(status);
        if (mPaint!=null) {
            mPaint.setTextSize(tipSize);
            canvas.drawText(bgTips, (mWidth - mBounds.width()) / 2, (mHeight + mBounds.height()) / 2, mPaint);
        }
    }

    @Override
    public void preDo(int status) {
        switch (status){
            case PicValidateView.IDEL:
                Init();
                break;
            case PicValidateView.BACKTO:
                Init();
                break;
            case PicValidateView.TOUCH:
                mPaint=null;
        }
        if (PicValidateView.validateStatue==SUCCESS){
            Init();
            bgTips="解锁成功！";
        }
    }

    @Override
    public void offset(float x, float y) {

    }
}
