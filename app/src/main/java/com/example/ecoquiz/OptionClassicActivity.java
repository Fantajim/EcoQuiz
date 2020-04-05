package com.example.ecoquiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
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
        picker.setMaxValue(11);
        String[] pickerValues = new String[11];
        for (int i = 0; i< 10;i++) {
            pickerValues[i] = Integer.toString(i+1);
        }
        pickerValues[10] = "all";
        picker.setDisplayedValues(pickerValues);
    }

    public void onClassicStartClick(View view) {
        Intent intent = new Intent(this, QuestionActivity.class);
        intent.putExtra(QuestionActivity.SPINNERVALUE, picker.getValue());
        startActivity(intent);
    }
}
