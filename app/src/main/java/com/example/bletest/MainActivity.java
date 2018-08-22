package com.example.bletest;


import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;
import com.example.bletest.loginvalidate.callback.ResultCallback;
import com.example.bletest.loginvalidate.picturevalidate.PicValidateView;


public class MainActivity extends AppCompatActivity {

    private String TAG="MainActivity";
    PicValidateView validateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        validateView=(PicValidateView) findViewById(R.id.validateview);
        validateView.setResultCallback(new ResultCallback() {
            @Override
            public void Success(String msg) {
                Toast.makeText(MainActivity.this,"验证成功！",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void Failed(String msg) {
                Toast.makeText(MainActivity.this,"验证失败！",Toast.LENGTH_SHORT).show();
            }
        });
        InitOthers();
    }


    public void InitOthers(){

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume : " );
    }



    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop : " );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy : " );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                // 如果请求被取消，则结果数组为空。
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i("tag","同意申请");
                } else {
                    Log.i("tag","拒绝申请");

                }
                return;
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // TODO Auto-generated method stub
        super.onConfigurationChanged(newConfig);
        Log.i(TAG, "onConfigurationChanged : " );
    }
}
