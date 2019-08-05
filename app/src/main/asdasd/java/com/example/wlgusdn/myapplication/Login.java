package com.example.wlgusdn.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    EditText Id;
    EditText Password;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

         Id= findViewById(R.id.Id);

        Password = findViewById(R.id.Password);

    }
    public void accountclick(View view)
    {

        Intent it = new Intent(this,Account.class);
        startActivity(it);


    }


    public void Login(View view)
    {

        String idtext = Id.getText().toString();


        String Passwordtext = Password.getText().toString();

        if(idtext.equals("1"))
        {
            if (Passwordtext.equals("1"))
            {

                Intent it = new Intent(this, MainActivity.class);
                startActivity(it);

            }
            else
            {

                Toast.makeText(this,"비밀번호 틀림",Toast.LENGTH_LONG).show();;

            }
        }
        else
        {

            Toast.makeText(this,"아이디가 틀림",Toast.LENGTH_LONG).show();;

        }
    }

    public void Login_Search(View view)
    {
        if(view.getId()==R.id.Login_Search_Id)
        {
            //아이디 찾기
        }
        else
        {
            //비밀번호 찾기
        }
    }
}
