package com.example.ecoquiz;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;

public class OptionClassicActivity extends AppCompatActivity {

    public static final String SPINNERVALUE = "SpinnerValue";
    private NumberPicker picker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_classic);

        picker = findViewById(R.id.numberpicker_classic);



        picker.setMinValue(1);
        picker.setMaxValue(MainActivity.questionList.size());
        String[] pickerValues = new String[MainActivity.questionList.size()];
        for (int i = 0; i< MainActivity.questionList.size()-1;i++) {
            pickerValues[i] = Integer.toString(i+1);
        }
        pickerValues[MainActivity.questionList.size()-1] = "all";
        picker.setDisplayedValues(pickerValues);
    }

    public void onClassicStartClick(View view) {
        Intent intent = new Intent(this, QuestionActivity.class);
        if(picker.getValue() == 11) {
            intent.putExtra(QuestionActivity.SPINNERVALUE, MainActivity.getQuestionList().size());
        }
        else {
            intent.putExtra(QuestionActivity.SPINNERVALUE, picker.getValue());

        }
        startActivity(intent);
    }
}
