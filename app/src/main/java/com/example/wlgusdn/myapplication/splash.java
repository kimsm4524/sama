package com.example.wlgusdn.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

public class splash extends Activity
{
Handler hd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splash);

        hd = new Handler();





        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            /** * 사용자 단말기의 권한 중 "전화걸기" 권한이 허용되어 있는지 확인한다.
             *
             *
             *
             Android는 C언어 기반으로 만들어졌기 때문에 Boolean 타입보다 Int 타입을 사용한다. */
            int permissionResult = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            /** * 패키지는 안드로이드 어플리케이션의 아이디이다.
             *

             * 현재 어플리케이션이 CALL_PHONE에 대해 거부되어있는지 확인한다.
             */
            if (permissionResult == PackageManager.PERMISSION_DENIED)
            { /** * 사용자가 WRITE_EXTERNAL_STORAGE 권한을 거부한 적이 있는지 확인한다.
             * 거부한적이 있으면 True를 리턴하고 * 거부한적이 없으면 False를 리턴한다. */
                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE))
                {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(splash.this);
                    dialog.setTitle("권한이 필요합니다.")
                            .setMessage("이 기능을 사용하기 위해서는 단말기의 \"저장공간\" 권한이 필요합니다. 계속 하시겠습니까?")
                            .setPositiveButton("네", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) { /** * 새로운 인스턴스(onClickListener)를 생성했기 때문에 * 버전체크를 다시 해준다. */
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        // WRITE_EXTERNAL_STORAGE 권한을 Android OS에 요청한다.
                                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
                                    }
                                }
                            })
                            .setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(splash.this, "기능을 취소했습니다", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .create().show();
                } // 최초로 권한을 요청할 때
                else {
                    // WRITE_EXTERNAL_STORAGE 권한을 Android OS에 요청한다.
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
                }
            }
            // CALL_PHONE의 권한이 있을 때
            else {
                // 즉시 실행
               Toast.makeText(splash.this,"권한이 이미 존재합니다.",Toast.LENGTH_SHORT).show();
                hd.postDelayed(new splashhandler(), 1000); // 1초 후에 hd handler 실행  3000ms = 3초

            }
        } // 마시멜로우 미만의 버전일 때
         else

             {
                 // 즉시 실행

                 hd.postDelayed(new splashhandler(), 1000); // 1초 후에 hd handler 실행  3000ms = 3초
             }






    }

    private class splashhandler implements Runnable{
        public void run(){
            startActivity(new Intent(getApplication(), MainActivity.class)); //로딩이 끝난 후, ChoiceFunction 이동
            splash.this.finish(); // 로딩페이지 Activity stack에서 제거
        }
    }

    @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        if (requestCode == 1000)
        {
            // 요청한 권한을 사용자가 "허용" 했다면...
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {

                // Add Check Permission
                if (ActivityCompat.checkSelfPermission(splash.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(splash.this,"저장공간 접근권한을 동의했습니다.",Toast.LENGTH_SHORT).show();
                }
                hd.postDelayed(new splashhandler(), 1000); // 1초 후에 hd handler 실행  3000ms = 3초
            }
            else
                {
                    Toast.makeText(splash.this, "권한요청을 거부했습니다.", Toast.LENGTH_SHORT).show();
                }
        }
    }




    @Override
    public void onBackPressed() {
        //초반 플래시 화면에서 넘어갈때 뒤로가기 버튼 못누르게 함
    }




}
