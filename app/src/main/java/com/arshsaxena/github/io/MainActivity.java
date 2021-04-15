package com.arshsaxena.github.io;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.webkit.WebSettings;
import android.webkit.WebView;

import android.webkit.WebViewClient;

import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {
    private WebView myWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseMessaging.getInstance().subscribeToTopic("notifications");

        if (savedInstanceState != null){
            ((WebView) findViewById(R.id.webView)).restoreState(savedInstanceState.getBundle("webViewState"));
        }
        else {
            myWebView = (WebView)findViewById(R.id.webView);
            myWebView = (WebView)findViewById(R.id.webView);
            WebSettings webSettings = myWebView.getSettings();
            webSettings.setLoadsImagesAutomatically(true);
            webSettings.setJavaScriptEnabled(true);
            myWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            webSettings.setAllowContentAccess(true);
            webSettings.setAllowFileAccess(true);
            webSettings.setAppCacheEnabled(true);
            webSettings.setDomStorageEnabled(true);
            webSettings.setUseWideViewPort(true);
            myWebView.loadUrl("https://arshsaxena.github.io/");
            myWebView.setWebViewClient(new MyBrowser(){
                public void onReceivedError(WebView myWebView, int i, String s, String s1){
                    myWebView.loadUrl("file:///android_asset/404.html");
                }

                @Override
                public boolean shouldOverrideUrlLoading(WebView myWebView, String url) {
                    if (url == null || url.startsWith("http://") || url.startsWith("https://"))
                        return false;
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        myWebView.getContext().startActivity(intent);
                        return true;
                    }
                    catch (Exception e) {
                        Log.i(TAG, "shouldOverrideUrlLoading Exception:" + e);
                        return true;
                    }
                }
            });
        }
    }

    private static final String TAG = "MainActivity";

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Bundle bundle = new Bundle();
        myWebView.saveState(bundle);
        outState.putBundle("webViewState", bundle);
    }

    @Override
    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);
        Bundle bundle = new Bundle();
        myWebView.saveState(bundle);
        state.putBundle("webViewState", bundle);
    }

    @Override
    public void onBackPressed() {
        if (myWebView.canGoBack()) {
            myWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}