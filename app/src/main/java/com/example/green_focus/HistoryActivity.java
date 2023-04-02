package com.example.green_focus;

import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class HistoryActivity extends AppCompatActivity {

    TextView thTextView;
    Button vhButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        thTextView = findViewById(R.id.thTextView);
        displayTimeHistory();


    }

    private void displayTimeHistory() {
        SharedPreferences sharedPreferences = getSharedPreferences("timerData", MODE_PRIVATE);
        String timeHistory = sharedPreferences.getString("timeHistory", "");

        if (!timeHistory.isEmpty()) {
            thTextView.setText("Time History: " + timeHistory.replace(",", ", "));
        } else {
            thTextView.setText("Time History: No data");
        }
    }
}
