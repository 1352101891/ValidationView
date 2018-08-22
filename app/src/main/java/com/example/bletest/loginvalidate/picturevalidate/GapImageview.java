package com.example.bletest.loginvalidate.picturevalidate;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;

import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;

import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;

import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.example.bletest.loginvalidate.callback.AnimationEndCallback;
import com.example.bletest.loginvalidate.callback.MoveCallback;
import com.example.bletest.loginvalidate.shape.GapShape;
import com.example.bletest.loginvalidate.shape.LightShape;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.example.bletest.loginvalidate.picturevalidate.PicValidateView.IDEL;
import static com.example.bletest.loginvalidate.picturevalidate.PicValidateView.SUCCESS;


public class GapImageview  extends android.support.v7.widget.AppCompatImageView implements MoveCallback {
    private Paint paint;
    private Bitmap gapBitmap;
    private PorterDuffXfermode  mPorterDuffXfermode ;
    private GapShape shape;
    private Bitmap shadow;
    private int mWidth,mHeight;
    private String TAG="GapImageView";
    private int startX=0,startY=200;
    private int shadowX=500,shadowY=200;
    public static int per=25;
    private LightShape lightShape;
    private boolean isSuccessAnimate=false;
    private int duringTime=2000;
    private int gapWidth,gapHeight;
    private AnimationEndCallback callback;

    public GapImageview(Context context) {
        super(context);
        Init();
    }

    public GapImageview(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        Init();
    }

    public GapImageview(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        InitShape();
    }

    private Paint mMaskShadowPaint,mMaskPaint;
    private void Init(){

        //滑块区域
        mPorterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
        // 实例化阴影画笔
        mMaskShadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mMaskShadowPaint.setColor(Color.BLACK);
/*        mMaskShadowPaint.setStrokeWidth(50);
        mMaskShadowPaint.setTextSize(50);
        mMaskShadowPaint.setStyle(Paint.Style.FILL_AND_STROKE);
     */
        mMaskShadowPaint.setMaskFilter(new BlurMaskFilter(10, BlurMaskFilter.Blur.SOLID));
        mMaskPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        paint= new Paint();
        paint.setAntiAlias(true);
        setDrawingCacheEnabled(true);//设置能否缓存图片信息（drawing cache）
        buildDrawingCache();//如果能够缓存图片，则创建图片缓存

    }

    public void InitShape(){
        if (lightShape == null) {
            lightShape = new LightShape(getContext(), mWidth, mHeight);
        }
        if (shape==null){
            int[] p=new int[]{0,1,0,2,0,3,0,4,1,4,2,4,3,4,3,3,3,2,3,1,2,1,1,1};
            shape=new GapShape(getContext(),p);
            shape.offset(shadowX,shadowY);
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //创建滑块图片
        createBlockBitmap();
        //绘制空缺阴影
        shape.drawShap(canvas,0);

        //绘制滑块阴影部分
        if (shadow!=null){
            Paint paint=new Paint();
            paint.setColor(Color.GRAY);
            canvas.drawBitmap(shadow, startX+5, startY+5, paint);
        }
        //绘制滑块
        if (gapBitmap!=null) {
            canvas.drawBitmap(gapBitmap, startX, startY, paint);
        }

        //成功后绘制流光
        if(PicValidateView.validateStatue==SUCCESS){
            lightShape.drawShap(canvas,IDEL);
        }
    }

    private synchronized void createBlockBitmap() {
        if ( gapBitmap!=null) {
            return;
        }

        //滑块阴影
        Path mPath=createMaskPath(0,0);
        Bitmap temp = getMaskBitmap( getDrawingCache(), mPath);
        if (shadow==null){
            shadow=temp.extractAlpha();
        }
        gapBitmap=temp;
    }

    //抠图
    private Bitmap getMaskBitmap(Bitmap mBitmap, Path mask) {
        if (gapBitmap!= null){
            return gapBitmap;
        }
        //以控件宽高 create一块bitmap
        Bitmap tempBitmap = Bitmap.createBitmap(gapWidth, gapHeight, Bitmap.Config.ARGB_8888);
        Log.e(TAG, " getMaskBitmap: width:" + mBitmap.getWidth() + ",  height:" + mBitmap.getHeight());
        Log.e(TAG, " View: width:" + mWidth + ",  height:" + mHeight);
        //把创建的bitmap作为画板
        Canvas mCanvas = new Canvas(tempBitmap);
        //有锯齿 且无法解决,所以换成XFermode的方法做
        //mCanvas.clipPath(mask);
        // 抗锯齿
        mCanvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        //绘制用于遮罩的圆形
        mCanvas.drawPath(mask, mMaskPaint);
        //设置遮罩模式(图像混合模式)
        mMaskPaint.setXfermode(mPorterDuffXfermode);
        //★考虑到scaleType等因素，要用Matrix对Bitmap进行缩放
        mCanvas.drawBitmap(mBitmap,new Rect(shadowX,shadowY,shadowX+gapWidth , shadowY+gapHeight),new Rect(0,0,gapWidth,gapHeight),mMaskPaint);

        mMaskPaint.setXfermode(null);
        return tempBitmap;
    }



    public String saveBitmap(Context context, Bitmap mBitmap) {

        File filePic;

        try {
            filePic = new File(context.getExternalCacheDir() , "test.jpg");
            if (!filePic.exists()) {
                filePic.getParentFile().mkdirs();
                filePic.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(filePic);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

        return filePic.getAbsolutePath();
    }


    public Path createMaskPath(int offsetX,int offsetY){
        int[] gapPoint=new int[]{0,1,0,2,0,3,0,4,1,4,2,4,3,4,3,3,3,2,3,1,2,1,1,1};
        for(int i=0;i<gapPoint.length;i=i+2){
            gapPoint[i]= gapPoint[i]*per +offsetX; //+  shadowX
            gapPoint[i+1]= gapPoint[i+1]*per +offsetY; //+  shadowY
        }
        gapHeight=gapWidth=gapPoint[7]+per -gapPoint[1];


        Path mPath=new Path();

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

        return mPath;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        paint=null;
        shadow.recycle();
        shadow=null;
        destroyDrawingCache();//释放缓存占用的资源
        gapBitmap.recycle();
        gapBitmap=null;
        lightShape=null;
        shape=null;
    }

    @Override
    public void move(float percent) {
        if ( percent<1&& percent>0 ){
            startX= (int) (getMeasuredWidth()*percent);
        }
        invalidate();
    }


    public void startAnimation(){
        if (isSuccessAnimate){
            return;
        }
        isSuccessAnimate=true;
        // 步骤1：设置动画属性的初始值 & 结束值
        ValueAnimator anim = ValueAnimator.ofInt( 0, mWidth);
        anim.setInterpolator(new LinearInterpolator());
        // 步骤2：设置动画的播放各种属性
        anim.setDuration(duringTime);
        // 设置动画运行的时长
        anim.setRepeatCount(0);
        // 步骤3：将改变的值手动赋值给对象的属性值：通过动画的更新监听器
        // 设置 值的更新监听器
        // 即：值每次改变、变化一次,该方法就会被调用一次
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int currentValue = (Integer) animation.getAnimatedValue();
                if (currentValue== mWidth){
                    isSuccessAnimate=false;
                    if (callback!=null){
                        callback.End();
                    }
                }
                if (lightShape!=null) {
                    lightShape.offset(currentValue, 0);
                    invalidate();
                }
            }
        });
        anim.start();
    }


    public void setCallback(AnimationEndCallback callback) {
        this.callback = callback;
    }

}
