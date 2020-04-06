package com.example.ecoquiz;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MemeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MemeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String MEMESOURCE = "param1";
    private QuestionListener listener;
    private Button btMemeContinue;
    private ImageView ivMeme;

    public void setQuestionListener(QuestionListener mListener) {
        listener = mListener;
    }

    // TODO: Rename and change types of parameters
    private String mParam1;

    public MemeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment MemeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MemeFragment newInstance(String param1) {
        MemeFragment fragment = new MemeFragment();
        Bundle args = new Bundle();
        args.putString(MEMESOURCE, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(MEMESOURCE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meme, container, false);
        btMemeContinue = view.findViewById(R.id.btMemeContinue);
        btMemeContinue.setEnabled(false);
        btMemeContinue.setText(getString(R.string.btMemeWait));
        ivMeme = view.findViewById(R.id.ivMeme);
        String url = "https://meme-api.herokuapp.com/gimme/"+mParam1;
        Uri uri = Uri.parse(url);
        new JsonTask().execute(url);

        btMemeContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onMemePress();
            }
        });
        // Inflate the layout for this fragment

        return view;


    }

    private class JsonTask extends AsyncTask<String, String, String> {


        protected String doInBackground(String... params) {


            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                    Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)

                }

                return buffer.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONObject json = new JSONObject(result);
                new DownloadImageTask(ivMeme)
                        .execute(json.getString("url"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
            btMemeContinue.setEnabled(true);
            btMemeContinue.setText(getString(R.string.btMemeContinue));
        }
    }
}
