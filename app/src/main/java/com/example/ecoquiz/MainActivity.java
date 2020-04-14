package com.example.ecoquiz;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainScreen";
    public static ArrayList<Question> questionList = new ArrayList<>();
    private static boolean devMode = false;
    private Button btLearn;
    private Button btSubmit;
    private Button btAbout;
    private static MediaPlayer mediaPlayer = new MediaPlayer();
    private int current_index = 0;
    private List<Integer> musicList;
    private TypedArray sounds;

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



        // TODO: 06.04.20  doom guy bloody face marathon mode
        // TODO: 06.04.20 Scoreboard database raspbi marathon mode
        // TODO: 06.04.20 Submit questions



        if (!questionList.isEmpty()) questionList.clear();
            createQuestions();
            Collections.shuffle(questionList);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(getSharedPreferences(Commons.SHAREDPREFERENCES, Context.MODE_PRIVATE).contains(Commons.MOD)){
            if(!mediaPlayer.isPlaying()) {
                Resources res = getResources();
                String painType = getSharedPreferences(Commons.SHAREDPREFERENCES, Context.MODE_PRIVATE).getString(Commons.MOD, "");
                if(painType.equals(Commons.ANNOY)) {
                    sounds = res.obtainTypedArray(R.array.a_array);
                }
                else if(painType.equals(Commons.BOOTCAMP)){
                    sounds = res.obtainTypedArray(R.array.b_array);
                }
                else if(painType.equals(Commons.BONFIRE)){
                    sounds = res.obtainTypedArray(R.array.c_array);
                }
                else if(painType.equals(Commons.NUCLEAR)){
                    sounds = res.obtainTypedArray(R.array.d_array);
                }

                musicList = new ArrayList<>();
                for (int i = 0; i < sounds.length(); i++) {
                    musicList.add(sounds.getResourceId(i, -1));
                }
                sounds.recycle();
                Collections.shuffle(musicList);

                mediaPlayer = MediaPlayer.create(this, musicList.get(0));
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        play();
                    }
                });
                mediaPlayer.start();
            }
        }
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
    private void play()
    {
        if(current_index == musicList.size()-1){
            current_index=0;
            Collections.shuffle(musicList);
        }
        else {
            current_index+=1;
        }
        AssetFileDescriptor afd = this.getResources().openRawResourceFd(musicList.get(current_index));

        try
        {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getDeclaredLength());
            mediaPlayer.prepare();
            mediaPlayer.start();
            afd.close();
        }
        catch (IllegalArgumentException e)
        {
            Log.e(TAG, "Unable to play audio queue do to exception: " + e.getMessage(), e);
        }
        catch (IllegalStateException e)
        {
            Log.e(TAG, "Unable to play audio queue do to exception: " + e.getMessage(), e);
        }
        catch (IOException e)
        {
            Log.e(TAG, "Unable to play audio queue do to exception: " + e.getMessage(), e);
        }
    }

    public static MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public static void setDevMode(boolean devMode) {
        MainActivity.devMode = devMode;
    }
}

