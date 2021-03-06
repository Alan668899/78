package cn.novate.x5webview.widget;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.DownloadListener;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.util.ArrayList;
import java.util.List;

import cn.novate.x5webview.R;
import cn.novate.x5webview.tools.LogTAG;

public class MyX5WebView extends WebView {

    private static final String TAG = LogTAG.x5webview;
    private static final int MAX_LENGTH = 8;

    ProgressBar progressBar;
    private TextView tvTitle;
    private ImageView imageView;
    private List<String> newList;


    public MyX5WebView(Context context) {
        super(context);
        initUI();
    }

    public MyX5WebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initUI();
    }

    public MyX5WebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initUI();
    }

    public void setShowProgress(boolean showProgress) {
        if (showProgress) {
            progressBar.setVisibility(VISIBLE);
        } else {
            progressBar.setVisibility(GONE);
        }
    }


    private void initUI() {

        getX5WebViewExtension().setScrollBarFadingEnabled(false);
        setHorizontalScrollBarEnabled(false);//????????????????????????
        setVerticalScrollBarEnabled(false); //????????????????????????

//      setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);//????????????WebView????????????
//      setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);//????????????WebView????????????


        progressBar = new ProgressBar(getContext(), null, android.R.attr.progressBarStyleHorizontal);
        progressBar.setMax(100);
        progressBar.setProgressDrawable(this.getResources().getDrawable(R.drawable.color_progressbar));

        addView(progressBar, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 6));
        imageView = new ImageView(getContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//      ????????? ????????????????????????????????????
        imageView.setImageResource(R.mipmap.splash);
        imageView.setVisibility(VISIBLE);
        addView(imageView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        initWebViewSettings();
    }

    //   ?????????WebViewSetting
    public void initWebViewSettings() {
        setBackgroundColor(getResources().getColor(android.R.color.white));
        setWebViewClient(client);
        setWebChromeClient(chromeClient);
        setDownloadListener(downloadListener);
        setClickable(true);
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        WebSettings webSetting = getSettings();
        webSetting.setJavaScriptEnabled(true);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(true);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(true);
        webSetting.setAppCacheEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        //android ?????????????????????_bank?????????????????????????????????WebSettings.setSupportMultipleWindows(false)
        //???false????????????_bank?????????????????????????????????
        //???x5???????????????????????????WebSettings.setSupportMultipleWindows(true)???
        // ????????????????????????????????????false??????????????????
        //????????????????????????????????????WebChromeClient.onCreateWindow
        webSetting.setSupportMultipleWindows(false);
//        webSetting.setCacheMode(WebSettings.LOAD_NORMAL);
//        getSettingsExtension().setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);//extension

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && this.canGoBack()) {
            this.goBack(); // goBack()????????????WebView???????????????
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private WebChromeClient chromeClient = new WebChromeClient() {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            if (tvTitle == null || TextUtils.isEmpty(title)) {
                return;
            }
            if (title != null && title.length() > MAX_LENGTH) {
                tvTitle.setText(title.subSequence(0, MAX_LENGTH) + "...");
            } else {
                tvTitle.setText(title);
            }
        }

        //????????????
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            progressBar.setProgress(newProgress);
            if (progressBar != null && newProgress != 100) {

                //Webview?????????????????? ????????????????????????????????????
                progressBar.setVisibility(VISIBLE);

            } else if (progressBar != null) {

                //Webview???????????? ??????????????????,??????Webview
                progressBar.setVisibility(GONE);
                imageView.setVisibility(GONE);
            }
        }
    };

    private WebViewClient client = new WebViewClient() {

        //??????????????????????????????
        @Override
        public void onPageFinished(WebView webView, String url) {
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            String endCookie = cookieManager.getCookie(url);
            Log.i(TAG, "onPageFinished: endCookie : " + endCookie);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                CookieSyncManager.getInstance().sync();//??????cookie
            } else {
                CookieManager.getInstance().flush();
            }
            super.onPageFinished(webView, url);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //????????????true??????????????????WebView?????????
            // ???false??????????????????????????????????????????
            if (url.startsWith("http") || url.startsWith("https") || url.startsWith("ftp")) {
                return false;
            } else {
                try {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    view.getContext().startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(view.getContext(), "??????????????????????????????????????????????????????", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            return super.shouldInterceptRequest(view, request);
        }

        @Override
        public void onLoadResource(WebView webView, String s) {
            super.onLoadResource(webView, s);
            String reUrl = webView.getUrl() + "";
//            Log.i(TAG, "onLoadResource: onLoadResource : " + reUrl);
            List<String> urlList = new ArrayList<>();
            urlList.add(reUrl);
            newList = new ArrayList();
            for (String cd : urlList) {
                if (!newList.contains(cd)) {
                    newList.add(cd);
                }
            }
        }


    };

    public void syncCookie(String url, String cookie) {
        CookieSyncManager.createInstance(getContext());
        if (!TextUtils.isEmpty(url)) {
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.removeSessionCookie();// ??????
            cookieManager.removeAllCookie();

            //?????????????????????????????????
            String[] split = cookie.split(";");
            for (String string : split) {
                //???url??????cookie
                // ajax?????????  cookie????????????????????????
                cookieManager.setCookie(url, string);
            }
            String newCookie = cookieManager.getCookie(url);
            Log.i(TAG, "syncCookie: newCookie == " + newCookie);
            //sdk21??????CookieSyncManager????????????????????????CookieManager??????????????????
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                CookieSyncManager.getInstance().sync();//??????cookie
            } else {
                CookieManager.getInstance().flush();
            }
        } else {

        }

    }

    //??????Cookie
    private void removeCookie() {

        CookieSyncManager.createInstance(getContext());
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeSessionCookie();
        cookieManager.removeAllCookie();
        if (Build.VERSION.SDK_INT < 21) {
            CookieSyncManager.getInstance().sync();
        } else {
            CookieManager.getInstance().flush();
        }

    }

    public String getDoMain(String url) {
        String domain = "";
        int start = url.indexOf(".");
        if (start >= 0) {
            int end = url.indexOf("/", start);
            if (end < 0) {
                domain = url.substring(start);
            } else {
                domain = url.substring(start, end);
            }
        }
        return domain;
    }

    DownloadListener downloadListener = new DownloadListener() {
        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            getContext().startActivity(intent);
        }
    };
}
