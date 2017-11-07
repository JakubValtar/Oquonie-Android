package com.hundredrabbits.oquonie;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class MainActivity extends AppCompatActivity {

  private WebView webView;

  @SuppressLint("SetJavaScriptEnabled")
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_main);

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      WebView.setWebContentsDebuggingEnabled(true);
    }

    webView = findViewById(R.id.web_view);
    WebSettings webSettings = webView.getSettings();
    webSettings.setJavaScriptEnabled(true);
    webSettings.setDomStorageEnabled(true);
    webSettings.setSupportZoom(false);
    webSettings.setUseWideViewPort(false);
    webSettings.setLoadWithOverviewMode(true);

    webView.setOverScrollMode(View.OVER_SCROLL_NEVER);
    webView.setHorizontalScrollBarEnabled(false);
    webView.setVerticalScrollBarEnabled(false);

    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
      webSettings.setMediaPlaybackRequiresUserGesture(false);
    }

    webView.loadUrl("file:///android_asset/index.html");
  }

  @Override
  protected void onResume() {
    super.onResume();
    webView.onResume();
    webView.resumeTimers();
    String js = "if (typeof oquonie != 'undefined' && oquonie.music.is_muted == false){oquonie.music.resume_ambience();}";
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      webView.evaluateJavascript(js, null);
    } else {
      webView.loadUrl("javascript:" + js);
    }
  }

  @Override
  protected void onPause() {
    super.onPause();
    String js = "oquonie.music.track_ambient.pause();" +
        "oquonie.music.track_dialog.pause();" +
        "oquonie.music.track_effect.pause();" +
        "oquonie.music.track_interface.pause();";
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      webView.evaluateJavascript(js, null);
    } else {
      webView.loadUrl("javascript:" + js);
    }
    webView.pauseTimers();
    webView.onPause();
  }

  @Override
  protected void onDestroy() {
    webView.destroy();
    webView = null;
    super.onDestroy();
  }

  @SuppressLint("InlinedApi")
  @Override
  public void onWindowFocusChanged(boolean hasFocus) {
    super.onWindowFocusChanged(hasFocus);
    if (hasFocus) {
      getWindow().getDecorView().setSystemUiVisibility(
          View.SYSTEM_UI_FLAG_LOW_PROFILE
              | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
              | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
              | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
              | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
              | View.SYSTEM_UI_FLAG_FULLSCREEN
              | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }
  }
}
