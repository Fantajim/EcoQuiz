package com.example.ecoquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.DecimalFormat;

import pl.droidsonroids.gif.GifImageView;

public class ResultActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView tvResultPercent;
    private TextView tvResultTotal;
    private Button btFinish;
    private GifImageView givResult;
    private int correctAnswers;
    private double correctDouble;
    private double percentage;
    private int maxQuestions;
    private double maxDouble;
    private MediaPlayer resultMediaPlayer;


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

        givResult = findViewById(R.id.ivResult);
        btFinish = findViewById(R.id.btResultFinish);
        progressBar = findViewById(R.id.pbResult);
        tvResultPercent = findViewById(R.id.tvResultPercent);
        tvResultTotal = findViewById(R.id.tvResultTotal);

        progressBar.setMax(maxQuestions*100);
        btFinish.setEnabled(false);

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
                btFinish.setEnabled(true);
                percentage = (correctDouble/maxDouble)*100;
                DecimalFormat df = new DecimalFormat("0.##");
                tvResultPercent.setText((df.format(percentage)) +"% correct");
                tvResultTotal.setText(correctAnswers+" of "+maxQuestions+" questions are correct !\n\nVerdict:");
                int temp = (int) percentage;
                if(temp == 100) givResult.setImageResource(R.drawable.perfection);
                else if(temp > 80)givResult.setImageResource(R.drawable.success);
                else if(temp > 70)givResult.setImageResource(R.drawable.stonks);
                else if(temp > 60)givResult.setImageResource(R.drawable.culture);
                else if(temp > 50)givResult.setImageResource(R.drawable.mediocre);
                else if(temp > 40) givResult.setImageResource(R.drawable.gf);
                else if(temp > 30) givResult.setImageResource(R.drawable.pikachu);
                else if (temp > 20) givResult.setImageResource(R.drawable.minecraft);
                else if (temp > 10) givResult.setImageResource(R.drawable.fire);
                else if(temp > 0)givResult.setImageResource(R.drawable.brrr);
                else {
                    if(MainActivity.mediaPlayer.isPlaying()) {
                        MainActivity.mediaPlayer.pause();
                    }
                    if(hasWindowFocus()) {
                        resultMediaPlayer = MediaPlayer.create(ResultActivity.this, R.raw.coffin_short);
                        resultMediaPlayer.start();
                    }

                    givResult.setImageResource(R.drawable.coffindance);
                }
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
    protected void onStop() {
        super.onStop();
        if(resultMediaPlayer != null) {
            resultMediaPlayer.stop();
        }
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }
}
