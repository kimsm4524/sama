package com.example.wlgusdn.myapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

public class Declaration extends Activity
{

    EditText Content;
    ArrayAdapter<String> spinadapter;
    Spinner spinner;
    String Kind;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_popup_declaration);

        spinner = findViewById(R.id.Declaration_Spinner);
        Content = findViewById(R.id.Declaration_Content);


        spinadapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_item,
                (String[])getResources().getStringArray(R.array.kind_of_declaration));
        spinadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(spinadapter);


    }


    public void GoDeclaration(View view)
    {

        Kind = spinner.getSelectedItem().toString();
        Toast.makeText(Declaration.this,Kind,Toast.LENGTH_SHORT).show();


    }

    public void GoCancel(View view)
    {

        finish();

    }
}
