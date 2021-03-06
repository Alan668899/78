package com.dl7.playerdemo;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bumptech.glide.Glide;
import com.dl7.player.danmaku.OnDanmakuListener;
import com.dl7.player.media.IjkPlayerView;
import com.dl7.player.utils.SoftInputUtils;
import com.dl7.playerdemo.danmaku.DanmakuConverter;
import com.dl7.playerdemo.danmaku.DanmakuData;
import com.dl7.playerdemo.danmaku.DanmakuLoader;
import com.dl7.playerdemo.danmaku.DanmakuParser;
import com.dl7.playerdemo.utils.GsonHelper;

import java.io.IOException;
import java.io.InputStream;

public class CustomDanmakuActivity extends AppCompatActivity {

    private static final String VIDEO_URL = "http://flv2.bn.netease.com/videolib3/1611/28/GbgsL3639/SD/movie_index.m3u8";
    private static final String IMAGE_URL = "http://vimg2.ws.126.net/image/snapshot/2016/11/I/M/VC62HMUIM.jpg";

    Toolbar mToolbar;
    private IjkPlayerView mPlayerView;
    private View mEtLayout;
    private EditText mEditText;
    private Button mIvSend;
    private boolean mIsFocus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ijk_player);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mPlayerView = (IjkPlayerView) findViewById(R.id.player_view);
        mEtLayout = findViewById(R.id.ll_layout);
        mEditText = (EditText) findViewById(R.id.et_content);
        mIvSend = (Button) findViewById(R.id.btn_send);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle("Video Player");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        InputStream stream = null;
        try {
            stream = getAssets().open("custom.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Glide.with(this).load(IMAGE_URL).fitCenter().into(mPlayerView.mPlayerThumb);
        mPlayerView.init()
                .setTitle("??????????????????TextView?????????????????????????????????-(??? -???)?????? ??????~")
                .enableDanmaku()
//                .setDanmakuCustomParser(new AcFunDanmakuParser(), AcFunDanmakuLoader.instance(), null)
                // ?????? setDanmakuCustomParser() ?????? setDanmakuSource() ?????????
                .setDanmakuCustomParser(new DanmakuParser(), DanmakuLoader.instance(), DanmakuConverter.instance())
                .setDanmakuSource(stream)
                .setVideoPath(VIDEO_URL)
                .setDanmakuListener(new OnDanmakuListener<DanmakuData>() {
                    @Override
                    public boolean isValid() {
                        // TODO: ????????????????????????????????????????????????????????????????????? true ??????????????????????????????????????????
                        Log.w("CustomDanmakuActivity", "??????????????????");
                        return true;
                    }

                    @Override
                    public void onDataObtain(DanmakuData data) {
                        // ??????????????????
                        data.userName = "LONG";
                        data.userLevel = 2;
                        // ??????????????????????????? DanmakuData ???????????? DanmakuConverter ?????????????????????????????????????????????????????? BaseDanmaku
                        Log.e("CustomDanmakuActivity", data.toString());
                        // GsonHelper.object2JsonStr(data)?????????Json????????????????????????????????????????????????????????????{assets/custom.json}??????
                        Log.e("CustomDanmakuActivity", GsonHelper.object2JsonStr(data));
                    }
                });

        mIvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayerView.sendDanmaku(mEditText.getText().toString(), false);
                mEditText.setText("");
                _closeSoftInput();
            }
        });
        mEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean isFocus) {
                if (isFocus) {
                    mPlayerView.editVideo();
                }
                mIsFocus = isFocus;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPlayerView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPlayerView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPlayerView.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mPlayerView.configurationChanged(newConfig);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mPlayerView.handleVolumeKey(keyCode)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        if (mPlayerView.onBackPressed()) {
            return;
        }
        super.onBackPressed();
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View view = getCurrentFocus();
        if (_isHideSoftInput(view, (int) ev.getX(), (int) ev.getY())) {
            _closeSoftInput();
            return true;
        }
        return super.dispatchTouchEvent(ev);
    }

    private void _closeSoftInput() {
        SoftInputUtils.closeSoftInput(this);
        mEditText.clearFocus();
        mPlayerView.recoverFromEditVideo();
    }

    private boolean _isHideSoftInput(View view, int x, int y) {
        if (view == null || !(view instanceof EditText) || !mIsFocus) {
            return false;
        }
        return x < mEtLayout.getLeft() ||
                x > mEtLayout.getRight() ||
                y < mEtLayout.getTop();
    }

}
