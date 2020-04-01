package com.example.ecoquiz;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class QuestionNumberFragment extends Fragment {

    public QuestionNumberFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_question_number, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        View view = getView();
        Button button = view.getRootView().findViewById(R.id.btn_next);
        button.setEnabled(false);
        QuestionScreen questionScreen = (QuestionScreen) getActivity();
        int maxQuestions = questionScreen.getMaxQuestions();
        LinearLayout layout = (LinearLayout) view;
        for (int i = 0; i < maxQuestions; i++) {
            TextView text = new TextView(getActivity());
            text.setText(Integer.toString(i+1));
            text.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
          //  text.setBackgroundColor(Color.parseColor("#a4c639"));
            text.setBackgroundColor(Color.LTGRAY);
            text.setTextColor(Color.parseColor("#080808"));
            text.setPadding(5,5,5,5);
            layout.addView(text);
        }




    }
}
