package com.example.ecoquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.DecimalFormat;

public class ResultActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView tvResultPercent;
    private TextView tvResultTotal;
    private Button btFinish;
    private ImageView ivResult;
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

        ivResult = findViewById(R.id.ivResult);
        btFinish = findViewById(R.id.btResultFinish);
        progressBar = findViewById(R.id.pbResult);
        tvResultPercent = findViewById(R.id.tvResultPercent);
        tvResultTotal = findViewById(R.id.tvResultTotal);

        progressBar.setMax(maxQuestions*100);

        ValueAnimator animator = ValueAnimator.ofInt(0, correctAnswers*100);
        animator.setDuration(1000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation){
                progressBar.setProgress((Integer)animation.getAnimatedValue());
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                percentage = (correctDouble/maxDouble)*100;
                DecimalFormat df = new DecimalFormat("0.##");
                tvResultPercent.setText((df.format(percentage)) +"% correct");
                tvResultTotal.setText(correctAnswers+" of "+maxQuestions+" questions are correct !\n\nVerdict:");
                int temp = (int) percentage;
                if(temp == 100) ivResult.setImageResource(R.drawable.perfection);
                else if(temp > 80)ivResult.setImageResource(R.drawable.culture);
                else if(temp > 60)ivResult.setImageResource(R.drawable.success);
                else if(temp > 50) ivResult.setImageResource(R.drawable.notgreat);
                else if(temp > 30)ivResult.setImageResource(R.drawable.mediocre);
                else if(temp > 10)ivResult.setImageResource(R.drawable.brrr);
                else ivResult.setImageResource(R.drawable.paddlin);
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

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }
}
