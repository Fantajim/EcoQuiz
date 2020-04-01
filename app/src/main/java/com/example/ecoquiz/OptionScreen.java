package com.example.ecoquiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;

public class OptionScreen extends AppCompatActivity {
    private String[] pickerValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_learn);
        NumberPicker picker = findViewById(R.id.numberpicker_option);
        picker.setMinValue(1);
        picker.setMaxValue(11);
        pickerValues = new String[11];
        for (int i = 0; i< 10;i++) {
            pickerValues[i] = Integer.toString(i+1);
        }
        pickerValues[10] = "all";
        picker.setDisplayedValues(pickerValues);
    }

    public void onGoClick(View view) {
    Intent intent = new Intent (this, QuestionScreen.class);
    NumberPicker picker = findViewById(R.id.numberpicker_option);
    intent.putExtra("selection", picker.getValue());
    startActivity(intent);
    }

    public String[] getPickerValues() {
        return pickerValues;
    }
}
