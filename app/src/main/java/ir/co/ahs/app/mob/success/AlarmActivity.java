package ir.co.ahs.app.mob.success;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

import java.io.IOException;

import io.fabric.sdk.android.Fabric;
import ir.co.ahs.app.mob.success.Helper.GPSTracker;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by imac on 2/25/18.
 */

public class AlarmActivity extends AppCompatActivity {
    private String oneSignalUserID;
    private String oneSignalRegistrationID;
    private GPSTracker gps;
    private double LATITUDE;
    private double LONGTITUDE;
    private Ringtone ringtoneSound;
    private MediaPlayer mMediaPlayer;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_alarm);

        TextView stop = (TextView) findViewById(R.id.stop);
        TextView eventName = (TextView) findViewById(R.id.eventName);

        try {
            String name = getIntent().getStringExtra("event");
            eventName.setText(name);
        } catch (Exception e) {
        }

        Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

        if (alert == null) {
            // alert is null, using backup
            alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            // I can't see this ever being null (as always have a default notification)
            // but just incase
            if (alert == null) {
                // alert backup is null, using 2nd backup
                alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            }
        }
        try {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setDataSource(this, alert);
            final AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            if (audioManager.getStreamVolume(AudioManager.STREAM_RING) != 0) {
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
                mMediaPlayer.setLooping(true);
                mMediaPlayer.prepare();
                mMediaPlayer.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMediaPlayer.stop();
                finish();
            }
        });
    }


    @Override
    protected void onStop() {
        super.onStop();
        mMediaPlayer.stop();
    }
}
