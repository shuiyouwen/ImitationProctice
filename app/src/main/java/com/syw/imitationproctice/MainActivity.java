package com.syw.imitationproctice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.syw.imitationproctice.wechat.HeaderView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final HeaderView headerView = findViewById(R.id.header_view);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams layoutParams = headerView.getLayoutParams();
                layoutParams.height = 0;
                headerView.setLayoutParams(layoutParams);

            }
        });
    }
}
