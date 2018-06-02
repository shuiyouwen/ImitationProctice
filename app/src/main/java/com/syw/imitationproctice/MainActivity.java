package com.syw.imitationproctice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.syw.imitationproctice.wechat.ComponentAdapter;
import com.syw.imitationproctice.wechat.HeaderView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.header_customer);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        initRecyclerView(recyclerView);

    }

    private void initRecyclerView(RecyclerView recyclerView) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        ComponentAdapter componentAdapter = new ComponentAdapter(this);
        recyclerView.setAdapter(componentAdapter);
    }
}
