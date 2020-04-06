package com.example.ecoquiz;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class QuestionActivity extends AppCompatActivity implements QuestionListener{
    private static final String TAG = "QuestionScreen";
    private static int maxQuestions;
    private static int currentQuestion = 1;
    public static final String SPINNERVALUE = "SpinnerValue";
    private boolean meme = false;
    private String memeSource = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        maxQuestions = getIntent().getExtras().getInt(SPINNERVALUE);
        if (getIntent().getExtras().containsKey(OptionMemeActivity.MEMESOURCE)) {
            memeSource = getIntent().getExtras().getString(OptionMemeActivity.MEMESOURCE);
            meme = true;

        }

        QuestionFragment question = new QuestionFragment();
        question.setListener(this);
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,question).addToBackStack(null).commit();


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

    @Override
    public void onNextPress(boolean correctAnswer) {
        if ( currentQuestion == maxQuestions) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        else {
            increaseCurrentQuestion();
        }

        if(meme) {
           MemeFragment memeFragment = MemeFragment.newInstance(memeSource);
           memeFragment.setQuestionListener(this);
           getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,memeFragment).commit();
        }
        else {
            QuestionFragment question = new QuestionFragment();
            question.setListener(this);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, question).commit();
        }

    }


    @Override
    public void onMemePress() {
        QuestionFragment question = new QuestionFragment();
        question.setListener(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, question).commit();
    }

    public static void setCurrentQuestion(int currentQuestion) {
        QuestionActivity.currentQuestion = currentQuestion;
    }
}
