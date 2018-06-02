package com.syw.imitationproctice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.syw.imitationproctice.utils.InteractionUtils;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button btn1 = findViewById(R.id.btn_1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSound();
            }
        });

        findViewById(R.id.btn_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibration();
            }
        });
    }

    private void vibration() {
        InteractionUtils.vibration();
    }

    private void playSound() {

        InteractionUtils.playSound(R.raw.app_brand_pull_recent_vew_down_sound);


    }
}
