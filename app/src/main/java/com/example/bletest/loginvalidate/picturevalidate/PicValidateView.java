package com.example.bletest.loginvalidate.picturevalidate;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Toast;

import com.example.bletest.MainActivity;
import com.example.bletest.loginvalidate.callback.MoveCallback;
import com.example.bletest.loginvalidate.callback.ResultCallback;
import com.example.bletest.loginvalidate.shape.ArrowShape;
import com.example.bletest.loginvalidate.shape.BaseShape;
import com.example.bletest.loginvalidate.shape.BlockShape;
import com.example.bletest.loginvalidate.shape.TextShape;
import java.util.ArrayList;

public class PicValidateView extends View{

    private String TAG="PicValidateView";
    private String bgTips="向右滑动填充图片解锁";
    public int tipColor= 0x77889900;
    private int tipSize= 45;


    private Paint mPaint;
    private float blockWidth=0;
    private int mHeight,mWidth;
    private int status=IDEL; //0静止，1手指滑动，2回弹

    public static int validateStatue ;
    public final static int TOUCH=111;
    public final static int IDEL=110;
    public final static int BACKTO=112;

    public final static int SUCCESS=1111;
    public final static int FAIL=1112;

    private float RandomX;
    private int offsetX=15;
    public static float[] wrongPoints;
    public static float[] rightPoint;
    public static float[] arrowPoint;
    public float[] darrowPoint;

    private  Rect mBounds;

    private MoveCallback moveCallback;
    private ResultCallback resultCallback;
    private ImageFloatView floatView;
    private int[] position;

    private int duringTime=1000;

    public PicValidateView(Context context) {
        super(context);
        Init(null);
    }

    public PicValidateView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        Init(null);
    }

    public PicValidateView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Init(null);
    }

    public void Init(AttributeSet attrs){
        mPaint=new Paint();
        validateStatue=FAIL;
        status=IDEL;
        floatView=new ImageFloatView(getContext());
        this.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                PicValidateView.this.setMoveCallback(floatView);
                position = new int[2];
                PicValidateView.this.getLocationInWindow(position);
                int height=PicValidateView.this.getMeasuredHeight();
                PicValidateView.this.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                floatView.InitView(position[0],position[1]);
            }
        });
    }

    private void hidenImage(){
        floatView.remove();
    }

    private void successImage(){
        floatView.success();
    }

    private void showImage(){
        floatView.add();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mPaint.setTextSize(tipSize);
        mPaint.setColor(tipColor);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        mWidth=widthSize;

        if (heightMode == MeasureSpec.EXACTLY)
        {
            mHeight = heightSize;
        } else
        {
            mBounds=new Rect();
            mPaint.getTextBounds(bgTips, 0, bgTips.length(), mBounds);
            float textHeight = mBounds.height();
            int desired = (int) (getPaddingTop() + textHeight + getPaddingBottom());
            if (desired<textHeight*2){
                desired= (int) (2*textHeight);
            }
            mHeight = desired;
            blockWidth=desired;
        }
        //初始化点集
        initPoints();
        InitShape();
        setMeasuredDimension(widthSize, mHeight);
    }



    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        for (BaseShape shape:baseShapes) {
            shape.drawShap(canvas,status);
        }
        invalidate();//重绘View
    }
    private float rate=0;
    private boolean catchBlock=false;
    private float startX=0;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (validateStatue==FAIL)
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                Log.e("onTouchEvent","ACTION_DOWN:");
                if (checkTouchBlock(event)){
                    catchBlock=true;
                    status=TOUCH;
                    setRandomX();
                    startX=event.getX();
                    showImage();
                }
                return true;
            case MotionEvent.ACTION_UP:
                Log.e("onTouchEvent","ACTION_UP:");
                if (catchBlock && resultCallback!=null) {
                    if (!isFillGap(event)){
                        validateStatue=FAIL;
                        resultCallback.Failed("失败！");
                    }else if (isFillGap(event)) {
                        validateStatue=SUCCESS;
                        status = IDEL;
                        resultCallback.Success("成功！");
                        successImage();
                    }
                }
                if (catchBlock && validateStatue!=SUCCESS) {
                    status = BACKTO;
                    startAnim(event);
                }
                catchBlock = false;
                break;
            case MotionEvent.ACTION_MOVE:
                if (catchBlock) {
                    status = TOUCH;
                    baseShapes.get(2).offset(event.getX(), 0);
                    Log.e("onTouchEvent", "ACTION_MOVE:" + event.getX() + ",offsetX:" + (startX - event.getX()));
                    rate = event.getX()/getWidth();
                    if (moveCallback!=null){
                        moveCallback.move(rate);
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                catchBlock=false;
                Log.e("onTouchEvent","ACTION_CANCEL:");
                status=BACKTO;
            default:break;
        }
        return super.onTouchEvent(event);
    }


    private void startAnim(MotionEvent event){
        status=BACKTO;
        // 步骤1：设置动画属性的初始值 & 结束值
        ValueAnimator anim = ValueAnimator.ofInt((int) event.getX(), 0);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
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
                if (currentValue==0){
                    status=IDEL;
                    hidenImage();
                }else {
                    status=BACKTO;
                }
                // 获得改变后的值
                Log.e(TAG,"当前位置："+currentValue);
                // 输出改变后的值
                baseShapes.get(2).offset(currentValue,0);
                float rate= currentValue*1.0f/getMeasuredWidth();
                if (moveCallback!=null){
                    moveCallback.move(rate);
                }
            }
        });
        anim.start();
    }


    private boolean checkTouchBlock(MotionEvent event){
        if (event.getAction()==MotionEvent.ACTION_DOWN){
            float x = event.getX();
            float y = event.getY();
            if (x>0 && x<mHeight && y>0 && y<mHeight && status==IDEL){
                return true;
            }
        }
        return false;
    }


    private void setRandomX(){
        float x= (float) (2*Math.random()*100/3*1.0f)+1/3;
        RandomX=500;
    }

    private boolean isFillGap(MotionEvent event){
        if (event.getAction()==MotionEvent.ACTION_UP && catchBlock){
            float x = event.getX();
            float y = event.getY();
            if (x>0 && Math.abs(x-RandomX)<offsetX){
                return true;
            }
        }
        return false;
    }

    private void initPoints(){
        float b=blockWidth;
        wrongPoints= new float[]{b / 4, b / 4, 3 * b / 4, 3 * b / 4, b / 4, 3 * b / 4, 3 * b / 4, b / 4};
        rightPoint=new float[]{b/4,b/2,b/2,3*b/4,b/2,3*b/4,b*3/4,b/4};
        arrowPoint=new float[]{b/4,b/2,3*b/4,b/2,3*b/4,b/2,b/2,b/4,3*b/4,b/2,b/2,3*b/4};
        darrowPoint=new float[]{b/4,b/4,b/2,b/2,  b/2,b/2,b/4,3*b/4,   b/2,b/4,3*b/4,b/2,   b/2,3*b/4,3*b/4,b/2 };
    }

    ArrayList<BaseShape> baseShapes=new ArrayList<>();
    private void InitShape() {
        if (baseShapes!=null && baseShapes.size()==0) {
            baseShapes.add(new TextShape(mWidth, mHeight, getContext()));
            baseShapes.add(new ArrowShape(darrowPoint, mWidth, mHeight, getContext()));
            baseShapes.add(new BlockShape(arrowPoint, mWidth, mHeight, getContext()));
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        baseShapes.clear();
    }

    public void setMoveCallback(MoveCallback moveCallback) {
        this.moveCallback = moveCallback;
    }

    public void setResultCallback(ResultCallback resultCallback) {
        this.resultCallback = resultCallback;
    }
}
