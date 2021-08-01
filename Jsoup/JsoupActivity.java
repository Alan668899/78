package com.handsome.boke2.Jsoup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.handsome.boke2.R;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class JsoupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jsoup);

        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    for (int k = 0; k < 5; k++) {
                        Document doc = Jsoup.connect("http://www.qiushibaike.com/8hr/page/" + k + "/").get();
                        Elements els = doc.select("a.contentHerf");
                        Log.e("一、內容", els.toString());

                        for (int i = 0; i < els.size(); i++) {
                            Element el = els.get(i);
                            Log.e("1.标题", el.text());

                            String href = el.attr("href");
                            Log.e("2.链接", href);

                            Document doc_detail = Jsoup.connect("http://www.qiushibaike.com" + href).get();
                            Elements els_detail = doc_detail.select(".content");
                            Log.e("3.內容", els_detail.text());

                            Elements els_pic = doc_detail.select(".thumb img[src$=jpg]");
                            if (!els_pic.isEmpty()) {
                                String pic = els_pic.attr("src");
                                Log.e("4.图片连接", "" + pic);
                            } else {
                                Log.e("4.图片连接", "无");
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }
}
