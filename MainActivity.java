package com.example.appwebview;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.webkit.SafeBrowsingResponseCompat;
import androidx.webkit.WebViewClientCompat;
import androidx.webkit.WebViewFeature;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.onesignal.OneSignal;

public class MainActivity extends AppCompatActivity {
    private static final String ONESIGNAL_APP_ID = "a6b51f72-296f-4595-9271-c0958e8817e5";
    WebView webviewku;
    WebSettings websettingku;
    long exitTime = 0;
    SwipeRefreshLayout refreshLayout;
    BottomNavigationView bottomNavigationView;
    private WebView superSafeWebView;
    private boolean safeBrowsingIsInitialized;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Enable verbose OneSignal logging to debug issues if needed.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        // OneSignal Initialization
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);



        FirebaseMessaging.getInstance().subscribeToTopic("weather")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Done";
                        if (!task.isSuccessful()) {
                            msg = "Failed";
                        }
                    }
                });
        superSafeWebView = new WebView(this);
        safeBrowsingIsInitialized = false;
        webviewku = (WebView) findViewById(R.id.webView);
        bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setSelectedItemId(R.id.home_menu);
        websettingku = webviewku.getSettings();
        websettingku.setJavaScriptEnabled(true);
        webviewku.clearCache(true);
        webviewku.clearHistory();
        webviewku.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        webviewku.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webviewku.getSettings().setAppCacheEnabled(true);
        webviewku.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        websettingku.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        websettingku.setEnableSmoothTransition(true);
        webviewku.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webviewku.setWebChromeClient(new WebChromeClient());
        webviewku.setWebViewClient(new WebViewClient());
        webviewku.getSettings().setDomStorageEnabled(true);
        webviewku.loadUrl("https://fila.co.id/");

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
            getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity.this, R.color.colorAccent));

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
                webviewku.loadUrl("https://fila.co.id/");
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

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(this, "Tekan sekali lagi untuk keluar", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }

        public class MyWebViewClient extends WebViewClientCompat {
            // Automatically go "back to safety" when attempting to load a website that
            // Google has identified as a known threat. An instance of WebView calls
            // this method only after Safe Browsing is initialized, so there's no
            // conditional logic needed here.
            @Override
            public void onSafeBrowsingHit(WebView view, WebResourceRequest request,
                                          int threatType, SafeBrowsingResponseCompat callback) {
                // The "true" argument indicates that your app reports incidents like
                // this one to Safe Browsing.
                if (WebViewFeature.isFeatureSupported(WebViewFeature.SAFE_BROWSING_RESPONSE_BACK_TO_SAFETY)) {
                    callback.backToSafety(true);
                    Toast.makeText(view.getContext(), "Unsafe web page blocked.",
                            Toast.LENGTH_LONG).show();
                }
            }


        }

    }






























































