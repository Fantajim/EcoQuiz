package com.example.ecoquiz;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class QuestionActivity extends AppCompatActivity {
    private static final String TAG = "QuestionScreen";
    private static int maxQuestions;
    private static int currentQuestion=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        maxQuestions = getIntent().getExtras().getInt("maxquestions");
        QuestionFragment question = new QuestionFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,question).commit();

    }

    public static int getMaxQuestions() {
        return maxQuestions;
    }
    public static void increaseCurrentQuestion() {
        currentQuestion++;
    }
    public static int getCurrentQuestion() {
        return currentQuestion;
    }
}
