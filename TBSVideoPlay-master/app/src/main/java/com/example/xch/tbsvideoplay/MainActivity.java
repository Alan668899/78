package com.example.xch.tbsvideoplay;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.tencent.smtt.sdk.TbsVideo;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn_tbs_videoPlay;
    private String videoUrl="http://111.231.191.26/See%20You%20Again.mp4";//视频地址(运行时请确认此链接是否还可用，如不可用换一个就行)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_tbs_videoPlay = findViewById(R.id.btn_tbs_videoPlay);
        btn_tbs_videoPlay.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_tbs_videoPlay:
                VideoPlayActivity.actionStart(MainActivity.this,videoUrl);

//                startPlay(videoUrl);
                break;
            default:
                break;
        }
    }

    /**
     * 直接调用播放视频
     * @param videoUrl 视频地址
     */
    private void startPlay(String videoUrl){
        //判断当前是否可用
        if(TbsVideo.canUseTbsPlayer(getApplicationContext())){
            //播放视频
            TbsVideo.openVideo(getApplicationContext(), videoUrl);
        } else {
            Toast.makeText(this, "视频播放器未准备好", Toast.LENGTH_SHORT).show();
        }
    }
}
