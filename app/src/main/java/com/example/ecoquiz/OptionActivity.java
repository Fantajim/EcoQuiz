package com.example.ecoquiz;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

public class OptionActivity extends AppCompatActivity {

    public static boolean fastMode;
    private CheckBox checkBox;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);
        checkBox = findViewById(R.id.cbFastMode);
    }

    public void classicOnClick(View view) {
      Intent intent = new Intent(this, OptionClassicActivity.class);
      QuestionActivity.setCurrentQuestion(1);
      if(checkBox.isChecked())fastMode = true;
      else fastMode = false;
      startActivity(intent);
      finish();
    }

    public void memeOnClick(View view) {
        Intent intent = new Intent(this, OptionMemeActivity.class);
        QuestionActivity.setCurrentQuestion(1);
        if(checkBox.isChecked())fastMode = true;
        else fastMode = false;
        startActivity(intent);
        finish();
    }
}
