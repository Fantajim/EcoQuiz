package com.example.ecoquiz;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.media.MediaPlayer;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
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
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainScreen";
    public static ArrayList<Question> questionList = new ArrayList<>();
    private static boolean devMode = false;
    private Button btLearn;
    private Button btSubmit;
    private Button btAbout;
    private Button btUpdate;
    private TextView tvQuestionCount;
    private static MediaPlayer mediaPlayer = new MediaPlayer();
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


        if (!questionList.isEmpty()) questionList.clear();
        createQuestions();
        tvQuestionCount.setText(getString(R.string.tvQuestionCount)+questionList.size());
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
            SharedPreferences sharedPreferences = getSharedPreferences(Commons.SHAREDPREFERENCES, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            Gson gson = new Gson();
            String json = sharedPreferences.getString(Commons.JSONARRAY, "");

            if(!json.equals("")) {
                JSONObject jsonObject = gson.fromJson(json, JSONObject.class);
               // JsonArray jsonArray = new JsonArray(json);
                //JsonObject jsonObject = new JsonObject(json);
                JSONArray questionArray = jsonObject.getJSONArray(Commons.JSONARRAY);
                for (int i = 0; i < questionArray.length(); i++) {
                    JSONObject jsonQuestion = questionArray.getJSONObject(i);
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
                        questionList.add(question);
                    } else {
                        Question question = new Question(id, a1, a2, a3, a4, solution, image, q);
                        questionList.add(question);
                    }

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
            InputStream is = getAssets().open(Commons.JSONFILE);
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

            case R.id.btUpdate: {

                pbUpdate.setVisibility(ProgressBar.VISIBLE);
                // Instantiate the cache
                Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap

// Set up the network to use HttpURLConnection as the HTTP client.
                Network network = new BasicNetwork(new HurlStack());
// Instantiate the RequestQueue with the cache and network.
                requestQueue = new RequestQueue(cache, network);
// Start the queue
                requestQueue.start();
                String url = "https://machutichu.duckdns.org:8443/api/getQuestions";
                JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        JSONObject file = null;
                        try {
                            if(questionList.isEmpty() ||   Integer.parseInt(response.getJSONObject(response.length()-1).optString("questionId")) > questionList.get(questionList.size()-1).getId()) {
                                SharedPreferences sharedPreferences = getSharedPreferences(Commons.SHAREDPREFERENCES, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put(Commons.JSONARRAY, response);
                                Gson gson = new Gson();
                                String json = gson.toJson(jsonObject);
                                editor.putString(Commons.JSONARRAY, json);
                                editor.apply();
                            }
                            else {
                                Toast.makeText(MainActivity.this, "No new questions in database, keep calm and learn on", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        String temp = "";
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

