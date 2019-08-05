package com.example.wlgusdn.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Login extends AppCompatActivity {

    EditText Id;
    EditText Password;
    String Login = "login";
    String idtext;
    UserData1 userData;
    public SharedPreferences sf;
    private FirebaseDatabase mFirebaseDatabase;



    private DatabaseReference mDatabaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();

        sf = getSharedPreferences(Login,0);


       /* if(sf.getString("id","")!="")
        {
            Intent i = new Intent(this,MainActivity.class);
            startActivity(i);
        }*/
        Id= findViewById(R.id.Id);

        Password = findViewById(R.id.Password);



        //Loading.imgAndroid = (ImageView) findViewById(R.id.Login_img_android);
        // Loading.anim = AnimationUtils.loadAnimation(this, R.anim.loading);
        // Loading.back = findViewById(R.id.Login_back);








    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }

    public void accountclick(View view)
    {
        Intent it = new Intent(this,Account.class);
        startActivity(it);

    }


    public void Login(View view)
    {
        // Loading.StartLoad();







        LoginAsync data = new LoginAsync();
        data.execute();

    }



    public void Login_Search(View view)
    {

    }

    class LoginAsync extends AsyncTask<Void, String, Void> {
        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            Toast.makeText(Login.this, values[0], Toast.LENGTH_SHORT).show();
            if(values[0]=="로그인 실패"){
                Id.setText("");
                Password.setText("");
                //    Loading.CloseLoad();
            } else if(values[0]=="로그인 성공") {

                MainActivity.Myname = Id.getText().toString();
                mFirebaseDatabase.getReference("users").child(Id.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        userData = dataSnapshot.getValue(UserData1.class);

                        userData.fcmToken = FirebaseInstanceId.getInstance().getToken();

                        mDatabaseReference.child("users").child(userData.userEmailID).setValue(userData);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                Intent t = new Intent(Login.this, MainActivity.class);
                idtext = Id.getText().toString();

                final SharedPreferences sf = getSharedPreferences(Login,0);
                final SharedPreferences.Editor editor = sf.edit();
                editor.putString("id",idtext);

                editor.commit();

                mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        Log.d("checkkk",dataSnapshot.toString());
                        editor.putString("Token",dataSnapshot.child("users").child(idtext).child("fcmToken").getValue(true).toString());

                        Log.d("checkkk",dataSnapshot.child("users").child(idtext).child("fcmToken").getValue(true).toString());

                        int temp = Integer.parseInt(dataSnapshot.child("Login").getValue(true).toString());
                        temp++;
                        mDatabaseReference.child("Login").setValue(temp);






                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });




                //    Loading.CloseLoad();
                Intent intent = new Intent(Login.this,MainActivity.class);
                startActivity(intent);
            }
        }


        protected Void doInBackground(Void... voids) {//user thread
            // 70.12.244.133
            String url = "http://52.79.255.160:8080/login.jsp";
            String param = "?id=" + Id.getText().toString() + "&pass=" + SHA256(Password.getText().toString());
            Document xml = null;
            String u = url + param;
            Log.i("zzzzzzzzzzzzz",u);
            try {
                xml = Jsoup.connect(u).get();//url에 접속해서 xml파일을 받아옴
            } catch (IOException e) {
                e.printStackTrace();
            }

            Elements result = xml.select("data");

            for (Element e : result) {
                if (e.select("result").text().equals("true"))
                    publishProgress("로그인 성공");
                else
                    publishProgress("로그인 실패");
            }

            return null;
        }
    }
    public String SHA256(String st){

        String SHA = "";
        String str = "nidfmkasof"+st;
        try{

            MessageDigest sh = MessageDigest.getInstance("SHA-256");

            sh.update(str.getBytes());

            byte byteData[] = sh.digest();

            StringBuffer sb = new StringBuffer();

            for(int i = 0 ; i < byteData.length ; i++){

                sb.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));

            }

            SHA = sb.toString();



        }catch(NoSuchAlgorithmException e){

            e.printStackTrace();

            SHA = null;

        }

        return SHA;

    }
}