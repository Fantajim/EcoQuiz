package com.example.ecoquiz;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class SubmitActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "SubmitActivity";
    private ConstraintLayout clSubmit;
    private Button btSend;
    private Uri imageUri;
    private CheckedTextView ctvAnswer1;
    private CheckedTextView ctvAnswer2;
    private CheckedTextView ctvAnswer3;
    private CheckedTextView ctvAnswer4;
    private EditText etQuestion;
    private EditText etQuestionURL;
    private EditText etAnswer1;
    private EditText etAnswer2;
    private EditText etAnswer3;
    private EditText etAnswer4;
    private ArrayList<CheckedTextView> ctvArrayList = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit);
        clSubmit = findViewById(R.id.clSubmit);
        btSend = findViewById(R.id.btSend);
        ctvAnswer1 = findViewById(R.id.ctvAnswer1);
        ctvAnswer2 = findViewById(R.id.ctvAnswer2);
        ctvAnswer3 = findViewById(R.id.ctvAnswer3);
        ctvAnswer4 = findViewById(R.id.ctvAnswer4);
        etQuestion = findViewById(R.id.etQuestion);
        etQuestionURL = findViewById(R.id.etAnswerUrl);
        etAnswer1 = findViewById(R.id.etAnswer1);
        etAnswer2 = findViewById(R.id.etAnswer2);
        etAnswer3 = findViewById(R.id.etAnswer3);
        etAnswer4 = findViewById(R.id.etAnswer4);
        List<CheckedTextView> ctvList = Arrays.asList(ctvAnswer1, ctvAnswer2, ctvAnswer3, ctvAnswer4);
        ctvArrayList.addAll(ctvList);
        for(CheckedTextView ctv: ctvArrayList){
            ctv.setOnClickListener(this);
        }
        btSend.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Commons.PICK_IMAGE && resultCode==RESULT_OK) {
            imageUri = data.getData();
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btSend: {
                if(!ctvAnswer1.isChecked() && !ctvAnswer2.isChecked() && !ctvAnswer3.isChecked() && !ctvAnswer4.isChecked()){
                    Toast.makeText(this, "Please select an answer as solution", Toast.LENGTH_SHORT).show();
                    break;
                }
                else if(etQuestion.getText().toString().equals("")) {
                    Toast.makeText(this, "Question Text is empty!", Toast.LENGTH_SHORT).show();
                    break;
                }
                else if (etAnswer1.getText().toString().equals("")){
                    Toast.makeText(this, "Answer 1 Text cannot be empty", Toast.LENGTH_SHORT).show();
                    break;
                }
                else if(etAnswer2.getText().toString().equals("")) {
                    Toast.makeText(this, "Answer 2 Text cannot be empty", Toast.LENGTH_SHORT).show();
                    break;
                }
                else if((etAnswer3.getText().toString().equals("") && ctvAnswer3.isChecked()) | (etAnswer4.getText().toString().equals("") && ctvAnswer4.isChecked())) {
                    Toast.makeText(this, "Cheeky cheeky, select an answer that is not empty", Toast.LENGTH_SHORT).show();
                    break;
                }
                else {

                    JSONObject postData = new JSONObject();
                    try {
                        postData.put("submittedAnswer1", etAnswer1.getText().toString());
                        postData.put("submittedAnswer2", etAnswer2.getText().toString());
                        postData.put("submittedAnswer3", etAnswer3.getText().toString());
                        postData.put("submittedAnswer4", etAnswer4.getText().toString());
                        postData.put("submittedQuestionImageURL", etQuestionURL.getText().toString());
                        postData.put("submittedQuestionText", etQuestion.getText().toString());
                        String temp = "";
                        for(CheckedTextView ctv : ctvArrayList){
                            if(ctv.isChecked()) temp = ctv.getText().toString();
                        }
                        postData.put("submittedSolution", temp);
                        new JsonTask().execute("https://machutichu.duckdns.org:8443/api/createSubmittedQuestion", postData.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;
                }
            }
            case R.id.ctvAnswer1:
            case R.id.ctvAnswer2:
            case R.id.ctvAnswer3:
            case R.id.ctvAnswer4:
                CheckedTextView temp = (CheckedTextView) v;
                for(CheckedTextView ctv: ctvArrayList){
                    ctv.setChecked(false);
                }
                temp.setChecked(true);
                break;
        }
    }

    // TODO: 14.04.20 connect API
    private class JsonTask extends AsyncTask<String, Void, String> {
        private int responseCode = 0;
        private String response = "";

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection httpURLConnection = null;
            try {
                httpURLConnection = (HttpURLConnection) new URL(params[0]).openConnection();
                httpURLConnection.setConnectTimeout(15000);
                httpURLConnection.setReadTimeout(15000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                httpURLConnection.setRequestProperty("Accept", "application/json");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
                wr.writeBytes(params[1]);

                wr.flush();
                wr.close();
                Log.d(TAG, "doInBackground: CODE " + httpURLConnection.getResponseCode());
                Log.d(TAG, "doInBackground: MSG " + httpURLConnection.getResponseMessage());

                responseCode = httpURLConnection.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        response += line;
                    }
                } else {
                    response = "";

                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
            }
            return response;
        }

        @Override
        protected void onPostExecute(String string) {
            super.onPostExecute(string);
            if(responseCode == 201) {
                Toast.makeText(SubmitActivity.this, "Question submitted succesfully, thank you! It will be reviewed and added to the repository.", Toast.LENGTH_SHORT).show();
                etAnswer1.getText().clear();
                etAnswer2.getText().clear();
                etAnswer3.getText().clear();
                etAnswer4.getText().clear();
                etQuestionURL.getText().clear();
                etQuestion.getText().clear();
            }
            else if(responseCode == 406) {
                Toast.makeText(SubmitActivity.this, "There is already a question with the same text", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
