package com.syw.imitationproctice.utils;

import android.annotation.SuppressLint;
import android.app.Service;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Vibrator;
import android.support.annotation.RawRes;
import android.util.SparseIntArray;

import com.syw.imitationproctice.App;
import com.syw.imitationproctice.R;

/**
 * @author: Shui
 * @data: 2018/6/2
 * @description:交互相关工具
 */

public class InteractionUtils {
    private static SoundPool sSoundPool;
    @SuppressLint("UseSparseArrays")
    private static SparseIntArray sSoundIdMap = new SparseIntArray();


    static {
        sSoundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 5);
        sSoundIdMap.append(R.raw.app_brand_pull_recent_vew_down_sound
                , sSoundPool.load(App.sContext, R.raw.app_brand_pull_recent_vew_down_sound, 1));
    }

    /**
     * 播放音效
     *
     * @param soundId
     */
    public static void playSound(@RawRes int soundId) {
        sSoundPool.play(sSoundIdMap.get(soundId), 1, 1, 0, 0, 1);
    }

    /**
     * 震动
     *
     * @param time
     */
    public static void vibration(long time) {
        Vibrator vibrator = (Vibrator) App.sContext.getSystemService(Service.VIBRATOR_SERVICE);
        if (vibrator != null) {
            vibrator.vibrate(time);
        }
    }

    /**
     * 短震动
     */
    public static void vibration() {
        Vibrator vibrator = (Vibrator) App.sContext.getSystemService(Service.VIBRATOR_SERVICE);
        if (vibrator != null) {
            vibrator.vibrate(10);
        }
    }
}
