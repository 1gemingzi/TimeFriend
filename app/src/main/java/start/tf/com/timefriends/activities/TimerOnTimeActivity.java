/*
 * © 2016 咖枯. All Rights Reserved.
 */
package start.tf.com.timefriends.activities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import start.tf.com.timefriends.R;
import start.tf.com.timefriends.constant.Constants;
import start.tf.com.timefriends.util.AudioPlayer;

/**
 * 计时时间到activity
 *
 * @author 咖枯
 * @version 1.0 2015/12/31
 */
public class TimerOnTimeActivity extends Activity implements View.OnClickListener {

    private AudioManager mAudioManager;
    private int mCurrentVolume;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_on_time);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                        | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                        | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        playRing();
        initViews();

//        OttoAppConfig.getInstance().post(new TimerOnTimeEvent());
    }

    private void playRing() {
        mAudioManager = (AudioManager) getSystemService(
                Context.AUDIO_SERVICE);

        mCurrentVolume = mAudioManager
                .getStreamVolume(AudioManager.STREAM_MUSIC);
        // 设置铃声音量
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                6, AudioManager.ADJUST_SAME);

        SharedPreferences shares = getSharedPreferences(
                Constants.EXTRA_WEAC_SHARE, Activity.MODE_PRIVATE);
        String ringUrl = shares.getString(Constants.RING_URL_TIMER, Constants.DEFAULT_RING_URL);

        // 默认铃声
        switch (ringUrl) {
            case Constants.DEFAULT_RING_URL:
                AudioPlayer.getInstance(this).playRaw(R.raw.ring_alarm_clock_default, true, false);
                break;
            // 无铃声
            case Constants.NO_RING_URL:
                AudioPlayer.getInstance(this).stop();
                break;
            default:
                AudioPlayer.getInstance(this).play(ringUrl, true, false);
                break;
        }
    }

    private void initViews() {
        Button rogerBtn = (Button) findViewById(R.id.roger_btn);
        rogerBtn.setOnClickListener(this);

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        // 停止播放
        AudioPlayer.getInstance(this).stop();
        // 设置铃声音量
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                mCurrentVolume, AudioManager.ADJUST_SAME);
        finish();
        overridePendingTransition(0, R.anim.zoomout);
    }
}
