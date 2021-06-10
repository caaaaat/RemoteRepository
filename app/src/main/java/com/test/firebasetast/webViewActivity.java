package com.test.firebasetast;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class webViewActivity extends AppCompatActivity {

    //談出視窗的webView
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_layout);
        WebView webView = findViewById(R.id.webView);
        String url=getIntent().getExtras().getString("url");

        webView.loadUrl(url);
        // 覆蓋WebView預設通過第三方或者是系統瀏覽器開啟網頁的行為，使得網頁可以在WebVIew中開啟
        webView.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                
                //返回值是true的時候控制網頁在WebView中去開啟，如果為false呼叫系統瀏覽器或第三方瀏覽器去開啟
                view.loadUrl(url);
                return true;
            }
            //WebViewClient幫助WebView去處理一些頁面控制和請求通知

        });
        //啟用支援JavaScript
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        //WebView載入頁面優先使用快取載入
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
    }
}
