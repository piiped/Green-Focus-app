package com.example.green_focus;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    TextView rdText, ttView;
    String[] tArray;
    Handler handler;
    Runnable runnable;
    EditText tedText;
    Button sTB;
    MediaPlayer mdPlayer;
    long tLeft;
    AnimationDrawable anmDrawable;
    ImageView anmImgView;
    CountDownTimer cdTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rdText = findViewById(R.id.randomText);
        tArray = getResources().getStringArray(R.array.random_texts);
        handler = new Handler();

        runnable = new Runnable() {
            @Override
            public void run() {
                int randomIndex = new Random().nextInt(tArray.length);
                rdText.setText(tArray[randomIndex]);
                handler.postDelayed(this, 15000);
            }
        };
        handler.postDelayed(runnable, 0);

        tedText = findViewById(R.id.timeEditText);
        ttView = findViewById(R.id.timerTextView);
        sTB = findViewById(R.id.startTimerButton);
        anmImgView = findViewById(R.id.animationImageView);

        ttView.setText("00:00");

        sTB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String timeText = tedText.getText().toString();
                if (!timeText.isEmpty()) {
                    int timeInMinutes = Integer.parseInt(timeText);
                    startCountdownTimer(timeInMinutes);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);

        if (mdPlayer != null) {
            mdPlayer.release();
            mdPlayer = null;
        }
    }
    private void startCountdownTimer(int timeInMinutes) {
        long timerDurationMillis = timeInMinutes * 60 * 1000;
        long intervalMillis = 1000;

        cdTimer = new CountDownTimer(timerDurationMillis, intervalMillis) {
            @Override
            public void onTick(long millisUntilFinished) {
                int secondsRemaining = (int) (millisUntilFinished / 1000);
                int minutesRemaining = secondsRemaining / 60;
                int secondsLeft = secondsRemaining % 60;
                ttView.setText(String.format("%02d:%02d ", minutesRemaining, secondsLeft));
            }

            @Override
            public void onFinish() {
                ttView.setText("Finished!");
                playAlarmSound();
            }
        }.start();

        anmImgView.setBackgroundResource(R.drawable.animation);
        AnimationDrawable anmDrawable = (AnimationDrawable) anmImgView.getBackground();
        anmDrawable.start();
    }
    private void playAlarmSound(){
        mdPlayer = MediaPlayer.create(this, R.raw.alarm);
        mdPlayer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (cdTimer != null) {
            cdTimer.cancel();
            AnimationDrawable anmDrawable = (AnimationDrawable) anmImgView.getDrawable();
            anmDrawable.stop();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (tLeft > 0) {
            startCountdownTimer((int) (tLeft / 1000 / 60));
        }
    }
}