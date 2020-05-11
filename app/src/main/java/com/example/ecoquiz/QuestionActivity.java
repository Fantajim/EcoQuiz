package com.example.ecoquiz;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

public class QuestionActivity extends AppCompatActivity implements QuestionListener{
    private static final String TAG = "QuestionScreen";
    public static int maxQuestions;
    public static int currentQuestion = 1;
    private int correctAnswers = 0;
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
        if(correctAnswer) correctAnswers++;
        if(meme)callMeme();
        else {
            if ( currentQuestion == maxQuestions) {
                endQuestions();
            }
            else {
                increaseCurrentQuestion();
                QuestionFragment question = new QuestionFragment();
                question.setListener(this);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, question, "questionFragment").commit();
            }
        }
    }

    private void endQuestions() {
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra(Commons.RESULT, correctAnswers);
        intent.putExtra(Commons.AMOUNTQUESTIONS, maxQuestions);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);

    }

    @Override
    public void onMemePress() {
        if(currentQuestion == maxQuestions) {
            endQuestions();
        }
        else {

        QuestionFragment question = new QuestionFragment();
        question.setListener(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, question).commit();
            increaseCurrentQuestion();
        }
    }

    public static void setCurrentQuestion(int currentQuestion) {
        QuestionActivity.currentQuestion = currentQuestion;
    }

    private void callMeme() {
        MemeFragment memeFragment = MemeFragment.newInstance(memeSource);
        memeFragment.setQuestionListener(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,memeFragment, "memeFragment").commit();
}

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, OptionActivity.class);
        startActivity(intent);
    }
}
