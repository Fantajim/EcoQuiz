package com.example.ecoquiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class OptionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);
    }

    public void classicOnClick(View view) {
      Intent intent = new Intent(this, OptionClassicActivity.class);
      QuestionActivity.setCurrentQuestion(1);
      startActivity(intent);
      finish();
    }

    public void memeOnClick(View view) {
        Intent intent = new Intent(this, OptionMemeActivity.class);
        QuestionActivity.setCurrentQuestion(1);
        startActivity(intent);
        finish();
    }
}
