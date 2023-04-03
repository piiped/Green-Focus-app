package com.example.green_focus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HistoryActivity extends AppCompatActivity {

    TextView thTextView;
    ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        thTextView = findViewById(R.id.thTextView);
        displayTimeHistory();

        imageView = findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void  onClick(View view) {
                startActivity(new Intent(HistoryActivity.this, MainActivity.class));
            }
        });
    }


    private void displayTimeHistory() {
        SharedPreferences sharedPreferences = getSharedPreferences("timerData", MODE_PRIVATE);
        String timeHistory = sharedPreferences.getString("timeHistory", "");

        if (!timeHistory.isEmpty()) {
            StringBuilder timeHistoryText = new StringBuilder("Time History:\n");
            String[] timeDataPairs = timeHistory.split(";");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());

            for (String timeDataPair : timeDataPairs) {
                String[] timeData = timeDataPair.split(",");
                int minutes = Integer.parseInt(timeData[0]);
                int hours = minutes / 60;
                int remainingMinutes = minutes % 60;
                long timestamp = Long.parseLong(timeData[1]);
                String date = sdf.format(new Date(timestamp));

                timeHistoryText.append(String.format("%02d:%02d on %s\n", hours, remainingMinutes, date));
            }

            thTextView.setText(timeHistoryText.toString());
        } else {
            thTextView.setText("Time History: No data");
        }
    }

    private void ctHistory() {
        SharedPreferences sharedPreferences = getSharedPreferences("timeData",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("timeHistory", "");
        editor.apply();
        thTextView.setText("Time History: No data");
    }

}