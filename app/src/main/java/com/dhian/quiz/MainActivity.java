package com.dhian.quiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    EditText editTextName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextName.setText(getSharedPreferences("name"));
    }

    public void startOnClick(View view) {
        setSharedPreferences("name", editTextName.getText().toString().trim());
        startActivity(new Intent(this, QuizActivity.class));
        finish();
    }
    private String getSharedPreferences(String key) {
        SharedPreferences prefs = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        return prefs.getString(key, "");
    }

    private void setSharedPreferences(String key, String value) {
        SharedPreferences prefs = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.commit();
    }

}