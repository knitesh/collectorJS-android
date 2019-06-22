package cs683.kumarnitesh.collectorjs;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;

public class WebViewActivity extends BaseActivity {

    private android.webkit.WebView mWebView;
    private static final String TAG =  MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        String url = getIntent().getStringExtra("URL");

        mWebView = (android.webkit.WebView) findViewById(R.id.activity_web_view);
        // Enable Javascript
        mWebView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        Log.d(TAG,"Loading url --> "+ url);

        mWebView.loadUrl(url);


    }
}
