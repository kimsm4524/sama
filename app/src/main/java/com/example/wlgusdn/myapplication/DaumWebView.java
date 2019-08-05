package com.example.wlgusdn.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class DaumWebView extends AppCompatActivity {

    private WebView webView;
    private TextView result;
    private EditText Detail;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daum_web_view);

        result = (TextView) findViewById(R.id.result);
        Detail = findViewById(R.id.DaumWeb_Detail);
        // WebView 초기화
        new webView().execute();

        // 핸들러를 통한 JavaScript 이벤트 반응
        handler = new Handler();
    }

    class webView extends AsyncTask<Void, String, Void>{
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            webView = (WebView) findViewById(R.id.webView);
            // JavaScript 허용
            webView.getSettings().setJavaScriptEnabled(true);
            // JavaScript의 window.open 허용
            webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
            // JavaScript이벤트에 대응할 함수를 정의 한 클래스를 붙여줌
            // 두 번째 파라미터는 사용될 php에도 동일하게 사용해야함
            // web client 를 chrome 으로 설정
            webView.setWebChromeClient(new WebChromeClient());
            webView.addJavascriptInterface(new MyJavaScriptInterface(), "TestApp");
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {

                    webView.loadUrl("javascript:sample2_execDaumPostcode();");
                }
            });
            webView.loadUrl("http://52.79.255.160/daum.html");
            Log.i("ZZZZZZZZZZZZ","http://52.79.255.160/daum.html");
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }
    class MyJavaScriptInterface
    {
        @JavascriptInterface
        @SuppressWarnings("unused")
        public void processDATA(String data) {
            result.setText(data);
        }
    }

    public void Cancel_Map(View view)
    {
        Toast.makeText(DaumWebView.this,"취소",Toast.LENGTH_SHORT).show();
        finish();
    }

    public void Complete_Map(View view)
    {
        //Toast.makeText(DaumWebView.this,result.getText().toString()+Detail.getText().toString(),Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        intent.putExtra("Adress", result.getText().toString()+"\n"+Detail.getText().toString());
        setResult(RESULT_OK, intent);

        finish();
    }

    private class AndroidBridge {
        @JavascriptInterface
        public void setAddress(final String arg1, final String arg2, final String arg3) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    result.setText(String.format("(%s) %s %s", arg1, arg2, arg3));
                    // WebView를 초기화 하지않으면 재사용할 수 없음
                    new webView().execute();
                }
            });
        }
    }

}

