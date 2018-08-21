package com.example.bletest.loginvalidate.stringvalidate;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.bletest.loginvalidate.Util;

public class StrvalidateView extends LinearLayout{
    CodeImage validateImage;
    EditText codeEditText;
    TextView title,Tips;

    LinearLayout.LayoutParams codeEditParams;

    public StrvalidateView(Context context) {
        super(context);
        Init();
    }

    public StrvalidateView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        Init();
    }

    public StrvalidateView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Init();
    }

    public void Init(){
        Log.e(TAG,"StrvalidateView  create()");

        this.setClickable(true);
        this.setOrientation(HORIZONTAL);
        this.setLayoutDirection(LAYOUT_DIRECTION_INHERIT);
        this.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        final int px=Util.dp2px(getContext(),5);
        this.setGravity(Gravity.CENTER_VERTICAL);

        title=new TextView(getContext());
        title.setText("验证码");
        title.setTextSize(Util.dp2px(getContext(),10));
        title.setGravity(Gravity.CENTER);
        title.setPadding(px,px,px,px);
        title.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        this.addView(title);


        codeEditText=new EditText(getContext());
        codeEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
        codeEditText.setInputType( InputType.TYPE_CLASS_NUMBER);
        codeEditParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        codeEditText.setLayoutParams(codeEditParams);
        this.addView(codeEditText);

        LinearLayout.LayoutParams imageParams= new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);;
        validateImage = new CodeImage(getContext());
        validateImage.setLayoutParams(imageParams);
        validateImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                validateImage.changeCode();
            }
        });
        postDelayed(new Runnable() {
            @Override
            public void run() {
                validateImage.performClick();
            }
        },500);
        validateImage.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int[] size = new int[2];
                size[0]=validateImage.getMeasuredWidth();
                size[1]=validateImage.getMeasuredHeight();
                validateImage.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                int[] size1 = new int[2];
                size1[0]=title.getMeasuredWidth();
                size1[1]=title.getMeasuredHeight();

                codeEditParams.width = getMeasuredWidth() -size[0]-size1[0]-2*px;
                codeEditText.setLayoutParams(codeEditParams);
            }
        });
        this.addView(validateImage);
    }


    private String TAG="StrvalidateView";
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.e(TAG,"onDetachedFromWindow");
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.e(TAG,"onAttachedToWindow");
    }
}
