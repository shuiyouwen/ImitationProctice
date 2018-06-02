package com.syw.imitationproctice.wechat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.syw.imitationproctice.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author: Shui
 * @data: 2018/6/1
 * @description: 微信下拉选择小组件
 */
// TODO: 2018/6/2 headerview 中recyclerview翻页功能
public class WechatHeaderViewActivity extends AppCompatActivity {
    private List<String> mData = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wechat_header_view);


        for (int i = 0; i < 20; i++) {
            mData.add("数据：" + i);
        }

        RecyclerView recyclerView = findViewById(R.id.rv_data);
        MyAdapter myAdapter = new MyAdapter(mData, this);
        recyclerView.setAdapter(myAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
    }
}
