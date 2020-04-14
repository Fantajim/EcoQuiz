package com.example.ecoquiz;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class OptionMemeActivity extends AppCompatActivity {

    public static final String SPINNERVALUE = "SpinnerValue";
    public static final String MEMESOURCE = "MemeSource";
    private static final String TAG = "OptionMemeActivity";
    private NumberPicker picker;
    private Button go;
    private RadioGroup rg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_meme);
        go = findViewById(R.id.btn_meme_go);
        go.setEnabled(false);
        rg = findViewById(R.id.radio_group_meme);

        picker = findViewById(R.id.numberpicker_meme);
        picker.setMinValue(1);
        picker.setMaxValue(11);
        String[] pickerValues = new String[11];
        for (int i = 0; i< 10;i++) {
            pickerValues[i] = Integer.toString(i+1);
        }
        pickerValues[10] = "all";
        picker.setDisplayedValues(pickerValues);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                go.setEnabled(true);
            }
        });

    }

    public void onMemeStartClick(View view) {

        int selectedRadioButtonId = rg.getCheckedRadioButtonId();
        if(selectedRadioButtonId !=-1) {
            Intent intent = new Intent(this, QuestionActivity.class);
            RadioButton selectedButton = findViewById(selectedRadioButtonId);
            String memeSource = selectedButton.getText().toString();
            intent.putExtra(MEMESOURCE, memeSource);
            intent.putExtra(QuestionActivity.SPINNERVALUE, picker.getValue());
            startActivity(intent);
        }
        else {
            Toast.makeText(this, "Please select a meme source!", Toast.LENGTH_SHORT).show();
        }


    }

}
