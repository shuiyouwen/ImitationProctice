package com.syw.imitationproctice.xiami;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.syw.imitationproctice.MyAdapter;
import com.syw.imitationproctice.R;
import com.syw.imitationproctice.utils.StatusBarUtil;
import com.syw.imitationproctice.utils.WidgetUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Shui
 * @data: 2018/6/14
 * @description:
 */

public class XiamiActivity extends AppCompatActivity {

    private FrameLayout mFlHeaderShrink;
    private View mClHeaderExpand;
    private ImageView mIvAlbum;
    private RecyclerView mRvMusicList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xiami);
        mFlHeaderShrink = findViewById(R.id.fl_header_shrink);
        mClHeaderExpand = findViewById(R.id.cl_header_expand);
        mIvAlbum = findViewById(R.id.iv_album);
        mRvMusicList = findViewById(R.id.rv_music_list);

        setStatusBar();
        initView();
    }

    private void initView() {
        WidgetUtils.setRoundedImageView(this, mIvAlbum, R.mipmap.album, 50);

        MyAdapter adapter = new MyAdapter(getData(), this);
        mRvMusicList.setAdapter(adapter);
        mRvMusicList.setLayoutManager(new LinearLayoutManager(this));
    }

    private List<String> getData() {
        List<String> data = new ArrayList<>();
        for (int i = 0; i < 40; i++) {
            data.add("测试数据：" + i);
        }
        return data;
    }

    private void setStatusBar() {
        StatusBarUtil.setTranslucentForImageView(this, 0, mClHeaderExpand);
    }
}
