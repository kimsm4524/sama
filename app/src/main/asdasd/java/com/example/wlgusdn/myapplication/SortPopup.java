package com.example.wlgusdn.myapplication;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.app.AlertDialog;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class SortPopup extends Activity
{
    ListView lv;
    String username;
    final String[] Sortstr = {"최신순","마감임박순","구매수량순","닫기"};
    final String[] Furniturestr = {"의자","테이블","소파","침대","옷장","기타"};
    final String[] Dishstr = {"접시","그릇","찻잔","컵/텀블러","수저/포크","기타"};

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_popup);

        Intent in = getIntent();
        String type = in.getStringExtra("type");

        if(type.equals("sort")) {
            ArrayList<String> list = new ArrayList<>();

            lv = findViewById(R.id.Popup_Listview);
            for (int i = 0; i < 4; i++) {

                list.add(Sortstr[i]);

            }

            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);


            lv.setAdapter(adapter);


            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {


                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String strText = (String) parent.getItemAtPosition(position);
                    Toast.makeText(SortPopup.this, strText, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.putExtra("result1", strText);
                    setResult(RESULT_OK, intent);

                    finish();
                }
            });
        }

        else if(type.equals("furniture"))//가구를 선택했을 때
        {
            ArrayList<String> list = new ArrayList<>();
            lv = findViewById(R.id.Popup_Listview);
            for (int i = 0; i < 6; i++) {

                list.add(Furniturestr[i]);

            }

            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);


            lv.setAdapter(adapter);


            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {


                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    String strText = (String) parent.getItemAtPosition(position);
                    if(strText.equals("기타"))//기타를 선택시 입력창이 나옴
                    {
                        show();
                        strText = username;


                   }
                   else {
                        Toast.makeText(SortPopup.this, strText, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.putExtra("result", strText);
                        setResult(RESULT_OK, intent);

                        finish();
                    }
                }
            });
        }

        else if(type.equals("dish"))
        {
            ArrayList<String> list = new ArrayList<>();
            lv = findViewById(R.id.Popup_Listview);
            for (int i = 0; i < 6; i++) {

                list.add(Dishstr[i]);

            }

            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);


            lv.setAdapter(adapter);


            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {


                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    String strText = (String) parent.getItemAtPosition(position);
                    if(strText.equals("기타"))
                    {
                        show();
                        strText = username;


                    }
                    else {
                        Toast.makeText(SortPopup.this, strText, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.putExtra("result", strText);
                        setResult(RESULT_OK, intent);

                        finish();
                    }
                }
            });
        }
        else
        {

                show();


        }
        }

        void show()
        {

            AlertDialog.Builder ad = new AlertDialog.Builder(this);

            ad.setTitle("카테고리를 입력하세요.");       // 제목 설정
            ad.setCancelable(false);
// EditText 삽입하기
            final EditText et = new EditText(this);
            ad.setView(et);

// 확인 버튼 설정
            ad.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {


                    // Text 값 받아서 로그 남기기
                   username = et.getText().toString();

                    dialog.dismiss();     //닫기
                    // Event
                    Toast.makeText(SortPopup.this, et.getText().toString(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.putExtra("result", et.getText().toString());
                    setResult(RESULT_OK, intent);

                    finish();
                }
            });



// 창 띄우기
            ad.show();


        }
       // setListViewHeightBasedOnChildren(lv);

    }











