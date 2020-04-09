package com.example.ecoquiz;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainScreen";
    public static ArrayList<Question> questionList = new ArrayList<>();
    private static boolean devMode = false;
    private Button btLearn;
    private Button btSubmit;
    private Button btAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btLearn = findViewById(R.id.btLearn);
        btSubmit = findViewById(R.id.btSumbit);
        btAbout = findViewById(R.id.btAbout);
        btLearn.setOnClickListener(this);
        btSubmit.setOnClickListener(this);
        btAbout.setOnClickListener(this);



        // TODO: 06.04.20 secret easter egg mode with annoying mp3 loops
        // TODO: 06.04.20  doom guy bloody face marathon mode
        // TODO: 06.04.20 Scoreboard database raspbi marathon mode
        // TODO: 06.04.20 Submit questions
        // TODO: 06.04.20 About

        if(getSharedPreferences(Commons.SHAREDPREFERENCES, Context.MODE_PRIVATE).contains(Commons.MOD)){
            MediaPlayer mediaPlayer = new MediaPlayer();
            Looper looper;
            AssetManager assetManager = getAssets();
            String[] files;
         //   try {
            //    files = assetManager.list("A");
           //     List<String> it = new LinkedList<String>(Arrays.asList(files));
//                AssetFileDescriptor afd = getAssets().openFd("/A"+it.get(0));
            // TODO: 09.04.2020 rename music to A B C D etc. put in array
                MediaPlayer mp = MediaPlayer.create(this, R.raw.darude);
                mp.start();
            //    mediaPlayer.set(it.get(0));
            //    mediaPlayer.prepare();
            //    mediaPlayer.start();
         //   } catch (IOException e) {
         //       e.printStackTrace();
           // }


        }

        if (!questionList.isEmpty()) questionList.clear();
            createQuestions();
            Collections.shuffle(questionList);
    }

    public static ArrayList<Question> getQuestionList() {
        return questionList;
    }

    private void createQuestions() {
        try {
            JSONObject file = new JSONObject(loadJSONFromAsset());
            JSONArray questionArray = file.getJSONArray("questions");
            for (int i = 0; i < questionArray.length(); i++) {
                JSONObject jsonQuestion = questionArray.getJSONObject(i);
                String a1 = jsonQuestion.getString("answer1");
                String a2 = jsonQuestion.getString("answer2");
                String a3 = jsonQuestion.getString("answer3");
                String a4 = jsonQuestion.getString("answer4");
                String q = jsonQuestion.getString("question");
                String image = jsonQuestion.getString("image");
                String solution = jsonQuestion.getString("solution");
                if (jsonQuestion.getString("answer3").isEmpty()) {
                    Question question = new Question(a1, a2, solution, q);
                    questionList.add(question);
                }
                else {
                    Question question = new Question(a1,a2,a3,a4,solution,image, q);
                    questionList.add(question);
                }

            }
        }
        catch(JSONException e) {
            e.printStackTrace();
        }

        for (Question q : questionList) {
            Log.d("test", q.toString());
        }

    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("economics_questions.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    @Override
    public void onClick(View v) {
    switch(v.getId()) {
        case R.id.btLearn: {
            Intent intent = new Intent(this, OptionActivity.class);
            startActivity(intent);
            break;
        }
        case R.id.btSumbit: {
            Intent intent = new Intent(this, SubmitActivity.class);
            startActivity(intent);
            break;
        }

        case R.id.btAbout: {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
            break;
        }
    }
    }

    public static void setDevMode(boolean devMode) {
        MainActivity.devMode = devMode;
    }
}

