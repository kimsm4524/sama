package com.example.wlgusdn.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class Loading extends ProgressDialog
{

    private Context c;
    private ImageView imgLogo;



    public Loading(Context context) {
        super(context);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCanceledOnTouchOutside(false);

        c=context;


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ld);
        imgLogo = findViewById(R.id.img_android);
        Animation anim = AnimationUtils.loadAnimation(c,R.anim.loading);
        imgLogo.setAnimation(anim);


    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void dismiss() {



        super.dismiss();
    }
    @Override
    public void onBackPressed()
    {

    }
}
