package com.dhian.quiz;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class QuizResultActivity extends AppCompatActivity {

    TextView userScore;
    TextView textViewCongratulations;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_result);

        userScore = findViewById(R.id.user_score);
        textViewCongratulations = findViewById(R.id.title);


        String name = getSharedPreferences("name");
        textViewCongratulations.setText("Congratulations " + name + "!");



        userScore.setText(getIntent().getStringExtra("RIGHT")
                + "/"
                + getIntent().getStringExtra("QUESTIONS"));

    }
    private String getSharedPreferences(String key) {
        SharedPreferences prefs = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        return prefs.getString(key, "");
    }

    public void takeNewOnClick(View view) {
        startActivity(new Intent(QuizResultActivity.this,MainActivity.class));
        finish();
    }

    public void finishOnClick(View view) {
        finish();
    }
}