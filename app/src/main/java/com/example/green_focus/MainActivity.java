package com.example.green_focus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    TextView rdText, ttView;
    String[] tArray;
    Handler handler, alarmHandler, aniHandler;
    Runnable runnable;
    EditText tedText;
    Button sTB, vhButton;
    MediaPlayer mdPlayer;
    long tLeft;
    ImageView anmImgView, btHome;
    CountDownTimer cdTimer;
    private  int[] imageArray = {
            R.drawable.tree1, R.drawable.tree2, R.drawable.tree3,
            R.drawable.tree4, R.drawable.tree5, R.drawable.tree6};
    private int imageIndex;
    boolean isReturning;
    private int[] selectedImagesArray = {
            R.drawable.tree8 };

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

        ttView.setText("00:00");

        sTB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String timeText = tedText.getText().toString();
                if (!timeText.isEmpty()) {
                    int timeInMinutes = Integer.parseInt(timeText);
                    startCountdownTimer(timeInMinutes);
                    startImageAnimation();
                }
            }
        });

        btHome = findViewById(R.id.imageView2);
         btHome.setOnClickListener(new View.OnClickListener(){
             @Override
             public  void  onClick(View view) {
                 startActivity(new Intent(MainActivity.this, MainActivity.class));
             }
         });
        alarmHandler = new Handler();

        vhButton = findViewById(R.id.vhButton);
        vhButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, HistoryActivity.class));
            }
        });

        anmImgView = findViewById(R.id.animationImageView);
        aniHandler = new Handler();
    }
    private void startImageAnimation() {
        imageIndex = 0;
        aniHandler.post(imageSwitcherRunnable);
    }
    private Runnable imageSwitcherRunnable = new Runnable() {
        @Override
        public void run() {
            if (imageIndex < imageArray.length){
                anmImgView.setImageResource(imageArray[imageIndex]);
                imageIndex++;
                aniHandler.postDelayed(this,15000);
            }
        }
    };
    private void startSelectedImagesAnimation() {
        imageIndex = 0;
        aniHandler.post(selectedImageSwitcherRunnable);
    }
    private Runnable selectedImageSwitcherRunnable = new Runnable() {
        @Override
        public void run() {
            if (imageIndex < selectedImagesArray.length) {
                anmImgView.setImageResource(selectedImagesArray[imageIndex]);
                imageIndex++;
                aniHandler.postDelayed(this, 10000);
            }
        }
    };
    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);

        if (mdPlayer != null) {
            mdPlayer.release();
            mdPlayer = null;
        }
        alarmHandler.removeCallbacksAndMessages(null);
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

                int totalTimeInMinutes = (int) (timerDurationMillis / 1000 / 60);
                saveTimeData(totalTimeInMinutes);

                stopImageAnimation();
            }

        }.start();
    }
    private void stopImageAnimation() {
        aniHandler.removeCallbacks(imageSwitcherRunnable);
    }
    private void playAlarmSound(){
        mdPlayer = MediaPlayer.create(this, R.raw.alarm);
        mdPlayer.start();

        alarmHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mdPlayer != null) {
                    mdPlayer.stop();
                    mdPlayer.release();
                    mdPlayer = null;
                }
            }
        }, 17000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (cdTimer != null) {
            cdTimer.cancel();
        }
        stopImageAnimation();
        startSelectedImagesAnimation();
        isReturning = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isReturning) {
            stopSelectedImagesAnimation();
            if (tLeft > 0) {
                startCountdownTimer((int) (tLeft / 1000 / 60));
                startImageAnimation();
            }
            isReturning = false;
        }
    }
    private void stopSelectedImagesAnimation() {
        aniHandler.removeCallbacks(selectedImageSwitcherRunnable);
    }

    private void saveTimeData(int totalMinutes) {
        SharedPreferences sharedPreferences = getSharedPreferences("timerData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        long currentTimeMillis = System.currentTimeMillis();
        String timeHistory = sharedPreferences.getString("timeHistory", "");
        timeHistory += totalMinutes + "," + currentTimeMillis + ";";
        editor.putString("timeHistory", timeHistory);
        editor.apply();
    }

}