package com.example.appwebview;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class sale extends AppCompatActivity {

    WebView webviewku;
    WebSettings websettingku;
    long exitTime = 0;
    SwipeRefreshLayout refreshLayout;
    BottomNavigationView bottomNavigationView;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale);
        webviewku = (WebView) findViewById(R.id.sale_webView);
        bottomNavigationView = findViewById(R.id.sale_nav_view);
        bottomNavigationView.setSelectedItemId(R.id.sale);
        websettingku = webviewku.getSettings();
        websettingku.setJavaScriptEnabled(true);
        webviewku.setWebChromeClient(new WebChromeClient());
        webviewku.setWebViewClient(new WebViewClient());
        webviewku.loadUrl("https://fila.co.id/search?q=sale");

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



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(sale.this, R.color.colorAccent));

        }

        webviewku.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                refreshLayout.setRefreshing(true);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                refreshLayout.setRefreshing(false);
            }
        });
        refreshLayout = findViewById(R.id.swipeRefreshLayout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                webviewku.loadUrl("https://fila.co.id/search?q=sale");
            }
        });

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webviewku.canGoBack()) {
            webviewku.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}