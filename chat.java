package com.example.appwebview;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class chat extends AppCompatActivity {

    WebView webviewku;
    WebSettings websettingku;
    BottomNavigationView bottomNavigationView;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        webviewku = (WebView) findViewById(R.id.chat_webView);
        bottomNavigationView = findViewById(R.id.chat_nav_view);
        bottomNavigationView.setSelectedItemId(R.id.chat);
        websettingku = webviewku.getSettings();
        websettingku.setJavaScriptEnabled(true);
        webviewku.setWebChromeClient(new WebChromeClient());
        webviewku.getSettings().setDomStorageEnabled(true);
        webviewku.loadUrl("https://api.whatsapp.com/send/?phone=628122026450");

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {

            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.home_menu:
                        startActivity(new Intent(getApplication(), MainActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.sale:
                        startActivity(new Intent(getApplication(), sale.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.chat:
                        startActivity(new Intent(getApplication(), chat.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.account:
                        startActivity(new Intent(getApplication(), account.class));
                        overridePendingTransition(0, 0);
                        return true;
                }

                return false;

            }
        });
            class myWebclient extends WebViewClient {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    if (url.startsWith("whatsapp:")) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        startActivity(intent);
                        return true;
                    }
                    return false;
                }
            }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(chat.this, R.color.colorAccent));

        }


    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webviewku.canGoBack()) {
            webviewku.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }
}







































