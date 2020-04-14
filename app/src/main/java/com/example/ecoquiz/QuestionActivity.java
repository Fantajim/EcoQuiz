package com.example.ecoquiz;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

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

        if(savedInstanceState != null) {
            memeSource = savedInstanceState.getString("memeSource");
            meme = savedInstanceState.getBoolean("meme");
            if(getSupportFragmentManager().getFragments().get(0) instanceof  MemeFragment) {
                MemeFragment temp = (MemeFragment)getSupportFragmentManager().findFragmentByTag("memeFragment");
                temp.setQuestionListener(this);
            }
            else {
                QuestionFragment temp = (QuestionFragment) getSupportFragmentManager().findFragmentByTag("questionFragment");
                temp.setListener(this);
            }

            savedInstanceState.clear();

        }
        else {



        maxQuestions = getIntent().getExtras().getInt(SPINNERVALUE);
        if (getIntent().getExtras().containsKey(OptionMemeActivity.MEMESOURCE)) {
            memeSource = getIntent().getExtras().getString(OptionMemeActivity.MEMESOURCE);
            meme = true;
        }

        QuestionFragment question = new QuestionFragment();
        question.setListener(this);
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,question,"questionFragment").addToBackStack(null).commit();

        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("memeSource", memeSource);
        outState.putBoolean("meme", meme);

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


        if(meme) {
           MemeFragment memeFragment = MemeFragment.newInstance(memeSource);
           memeFragment.setQuestionListener(this);
           getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,memeFragment, "memeFragment").commit();
        }
        else {
            QuestionFragment question = new QuestionFragment();
            question.setListener(this);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, question, "questionFragment").commit();
        }
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
