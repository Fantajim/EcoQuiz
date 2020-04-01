package com.example.ecoquiz;

import android.content.Intent;
import android.support.annotation.RawRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;

public class MainScreen extends AppCompatActivity {

    public static ArrayList<Question> questionList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!questionList.isEmpty()) questionList.clear();
            createQuestions();
            Collections.shuffle(questionList);

    }

    public void onClickLearn(View view) {
        Intent intent = new Intent(this, OptionScreen.class);
        startActivity(intent);
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
}

