package com.example.ecoquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView tvResultPercent;
    private TextView tvResultTotal;
    private Button btFinish;
    private int correctAnswers;
    private double correctDouble;
    private double percentage;
    private int maxQuestions;
    private double maxDouble;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        Intent intent = getIntent();


        correctAnswers = intent.getIntExtra(Commons.RESULT, 0);
        maxQuestions = intent.getIntExtra(Commons.AMOUNTQUESTIONS, 0);
        correctDouble = correctAnswers;
        maxDouble = maxQuestions;

        btFinish = findViewById(R.id.btResultFinish);
        progressBar = findViewById(R.id.pbResult);
        tvResultPercent = findViewById(R.id.tvResultPercent);
        tvResultTotal = findViewById(R.id.tvResultTotal);


        progressBar.setMax(maxQuestions);

        ValueAnimator animator = ValueAnimator.ofInt(0, correctAnswers);
        animator.setDuration(1500);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation){
                progressBar.setProgress((Integer)animation.getAnimatedValue());;
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                percentage = (correctDouble/maxDouble)*100;
                tvResultPercent.setText(percentage +"% correct");
                tvResultTotal.setText(correctAnswers+" of "+maxQuestions+" questions are correct !\n\n--> ");
                int temp = (int) percentage;
                if(temp == 100) tvResultTotal.append("Perfection.");
                else if(temp >90)tvResultTotal.append("Disciple of economy");
                else if(temp > 70)tvResultTotal.append("Such impressive, much wow");
                else if(temp > 50) tvResultTotal.append("Not great, not terrible...");
                else if(temp > 30)tvResultTotal.append("MEDIOCRE");
                else tvResultTotal.append("That's a paddlin'");
            }
        });
        animator.start();

        btFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}
