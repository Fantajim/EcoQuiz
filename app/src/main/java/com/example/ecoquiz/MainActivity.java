package com.example.ecoquiz;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainScreen";
    public static ArrayList<Question> questionList = new ArrayList<>();
    private static ArrayList<Question> tempQuestionList = new ArrayList<>();
    private static boolean devMode = false;
    private Button btLearn;
    private Button btSubmit;
    private Button btAbout;
    private Button btUpdate;
    private TextView tvQuestionCount;
    public static MediaPlayer mediaPlayer = new MediaPlayer();
    private int current_index = 0;
    private List<Integer> musicList;
    private TypedArray sounds;
    private ProgressBar pbUpdate;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pbUpdate = findViewById(R.id.pbUpdate);
        pbUpdate.setVisibility(ProgressBar.INVISIBLE);
        tvQuestionCount = findViewById(R.id.tvQuestionCount);
        btLearn = findViewById(R.id.btLearn);
        btSubmit = findViewById(R.id.btSumbit);
        btAbout = findViewById(R.id.btAbout);
        btUpdate = findViewById(R.id.btUpdate);
        btLearn.setOnClickListener(this);
        btSubmit.setOnClickListener(this);
        btAbout.setOnClickListener(this);
        btUpdate.setOnClickListener(this);

        // TODO: 06.04.20  doom guy bloody face marathon mode
        // TODO: 06.04.20 Scoreboard database raspbi marathon mode
        // TODO: 20.04.2020 imgur, tinypic linkbuttons with submit
        // TODO: 20.04.2020 categories + courses


        if (!questionList.isEmpty()) questionList.clear();
        createQuestions();
        updateQuestionCount(questionList.size());
        Collections.shuffle(questionList);

        if(questionList.isEmpty())btLearn.setEnabled(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getSharedPreferences(Commons.SHAREDPREFERENCES, Context.MODE_PRIVATE).contains(Commons.MOD)) {
            if (!mediaPlayer.isPlaying()) {
                Resources res = getResources();
                String painType = getSharedPreferences(Commons.SHAREDPREFERENCES, Context.MODE_PRIVATE).getString(Commons.MOD, "");
                if (painType.equals(Commons.ANNOY)) {
                    sounds = res.obtainTypedArray(R.array.a_array);
                } else if (painType.equals(Commons.BOOTCAMP)) {
                    sounds = res.obtainTypedArray(R.array.b_array);
                } else if (painType.equals(Commons.BONFIRE)) {
                    sounds = res.obtainTypedArray(R.array.c_array);
                } else if (painType.equals(Commons.NUCLEAR)) {
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

        SharedPreferences sharedPreferences = getSharedPreferences(Commons.SHAREDPREFERENCES, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(Commons.JSONARRAY, "");
        if (!json.equals("")) {
            Question[] q = gson.fromJson(json, Question[].class);
            questionList.addAll(Arrays.asList(q));
            String temp ="";
        }
        else {
            Toast.makeText(this, "No questions found, press update to download from database", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btLearn: {
                if(questionList.isEmpty()) {
                    Toast.makeText(this, "No questions found, press update to download from database", Toast.LENGTH_SHORT).show();
                    break;
                }
                else {
                    Intent intent = new Intent(this, OptionActivity.class);
                    startActivity(intent);
                    break;
                }
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

            case R.id.btUpdate: {

                pbUpdate.setVisibility(ProgressBar.VISIBLE);
                Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
                Network network = new BasicNetwork(new HurlStack());
                requestQueue = new RequestQueue(cache, network);
                requestQueue.start();
                String url = "https://machutichu.duckdns.org:8443/api/getQuestions";
                JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        fromJsonToList(response, tempQuestionList);
                        if (tempQuestionList.size() > questionList.size() || questionList.isEmpty()) {
                            SharedPreferences sharedPreferences = getSharedPreferences(Commons.SHAREDPREFERENCES, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            Gson gson = new Gson();
                            String json = gson.toJson(tempQuestionList);
                            editor.putString(Commons.JSONARRAY, json);
                            editor.apply();
                            int difference = tempQuestionList.size() - questionList.size();
                            updateQuestionCount(tempQuestionList.size());
                            questionList = tempQuestionList;
                            Toast.makeText(MainActivity.this, "Added "+difference+" questions from database", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "No new questions in database, keep calm and learn on", Toast.LENGTH_SHORT).show();
                        }
                        if(!questionList.isEmpty())btLearn.setEnabled(true);
                        pbUpdate.setVisibility(ProgressBar.INVISIBLE);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "onErrorResponse: " + error.toString());
                    }
                });
                requestQueue.add(jsonArrayRequest);
                break;
            }
        }
    }

    private void play() {
        if (current_index == musicList.size() - 1) {
            current_index = 0;
            Collections.shuffle(musicList);
        } else {
            current_index += 1;
        }
        AssetFileDescriptor afd = this.getResources().openRawResourceFd(musicList.get(current_index));

        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getDeclaredLength());
            mediaPlayer.prepare();
            mediaPlayer.start();
            afd.close();
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "Unable to play audio queue do to exception: " + e.getMessage(), e);
        } catch (IllegalStateException e) {
            Log.e(TAG, "Unable to play audio queue do to exception: " + e.getMessage(), e);
        } catch (IOException e) {
            Log.e(TAG, "Unable to play audio queue do to exception: " + e.getMessage(), e);
        }
    }

    private static void fromJsonToList(JSONArray jsonArray, ArrayList<Question> arrayList) {
        arrayList.clear();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonQuestion = jsonArray.getJSONObject(i);
                String id = jsonQuestion.getString("questionId");
                String a1 = jsonQuestion.getString("answer1");
                String a2 = jsonQuestion.getString("answer2");
                String a3 = jsonQuestion.getString("answer3");
                String a4 = jsonQuestion.getString("answer4");
                String q = jsonQuestion.getString("questionText");
                String image = jsonQuestion.getString("questionImageURL");
                String solution = jsonQuestion.getString("solution");
                if (jsonQuestion.getString("answer3").isEmpty()) {
                    Question question = new Question(id, a1, a2, solution, q);
                    arrayList.add(question);
                } else {
                    Question question = new Question(id, a1, a2, a3, a4, solution, image, q);
                    arrayList.add(question);
                }
            }
        }
        catch(JSONException e){
            Log.d(TAG, "createQuestions: ERROR: " + e.toString());
            e.printStackTrace();
        }
    }

    public static MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    private void updateQuestionCount(int count) {
        tvQuestionCount.setText(getString(R.string.tvQuestionCount) + count);
    }

    public static void setDevMode(boolean devMode) {
        MainActivity.devMode = devMode;
    }
}

