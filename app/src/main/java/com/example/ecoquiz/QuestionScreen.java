package com.example.ecoquiz;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

public class QuestionScreen extends AppCompatActivity {

    private int maxQuestions;
    private int currentQuestion=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        maxQuestions = getIntent().getExtras().getInt("selection");
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radio_answers);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Button button = findViewById(R.id.btn_next);
                button.setEnabled(true);
                // checkedId is the RadioButton selected
            }
        });
    }

    public void onNextClick(View view) {
        currentQuestion++;
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        QuestionFragment question = new QuestionFragment();
        Fragment fragment = manager.getFragments().get(R.id.question_frag_question);
        transaction.remove(fragment);
        transaction.commit();
        manager.beginTransaction();
        transaction.add(R.id.fragmentcontainer, question);
        transaction.commit();
    }


    public int getMaxQuestions() {
        return maxQuestions;
    }

    public int getCurrentQuestion() {
        return currentQuestion;
    }
}
