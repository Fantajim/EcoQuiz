package com.example.ecoquiz;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class QuestionFragment extends Fragment {
    private static final String TAG = "QuestionFragment";
    private RadioGroup radioGroup;
    private RadioButton radioButton1;
    private RadioButton radioButton2;
    private RadioButton radioButton3;
    private RadioButton radioButton4;
    private ImageView questionImage;
    private TextView questionText;
    private TextView questionCursor;
    private Button questionNext;
    private QuestionListener listener;
    private boolean correct;
    private Question question;

    public QuestionFragment() {
        // Required empty public constructor
    }

    public void setListener(QuestionListener mListener) {
        listener = mListener;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_question, container, false);
        radioGroup = view.findViewById(R.id.radio_group);
        radioButton1 = view.findViewById(R.id.radiobutton1);
        radioButton2 = view.findViewById(R.id.radiobutton2);
        radioButton3 = view.findViewById(R.id.radiobutton3);
        radioButton4 = view.findViewById(R.id.radiobutton4);
        questionImage = view.findViewById(R.id.question_image);
        questionText = view.findViewById(R.id.question_text);
        questionCursor = view.findViewById(R.id.question_cursor);
        questionNext = view.findViewById(R.id.question_nextbutton);

        question = MainActivity.getQuestionList().get(QuestionActivity.getCurrentQuestion()-1);

        questionText.setText(question.getQuestionText());
        if(question.getQuestionImage() != null &&   !question.getQuestionImage().isEmpty() ) {
            String resource = "@drawable/"+question.getQuestionImage();
            int imageResource = getResources().getIdentifier(resource, null, null );
            Drawable res = getResources().getDrawable(imageResource);
            questionImage.setImageDrawable(res);
        }
        //
        ArrayList<String> answerList = new ArrayList<>(question.getAnswerList());
        radioButton1.setText(answerList.get(0));
        radioButton2.setText(answerList.get(1));

        if(answerList.size() > 2) {
            radioButton3.setText(answerList.get(2));
            radioButton3.setVisibility(View.VISIBLE);
            radioButton4.setText(answerList.get(3));
            radioButton4.setVisibility(View.VISIBLE);

        }
        else {
            radioButton3.setVisibility(View.INVISIBLE);
            radioButton4.setVisibility(View.INVISIBLE);
        }
        int currentQuestions = QuestionActivity.getCurrentQuestion();
        int maxQuestions = QuestionActivity.getMaxQuestions();
        questionCursor.setText(currentQuestions+"/"+maxQuestions);

        questionNext.setEnabled(false);


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton answer = radioGroup.findViewById(checkedId);
                correct = answer.getText().toString().equals(question.getSolution());
                if(correct)answer.setBackgroundColor(Color.GREEN);
                else {
                    answer.setBackgroundColor(Color.RED);
                    int radiobuttonCount = radioGroup.getChildCount();
                    for(int i = 0; i < radiobuttonCount; i++) {
                        RadioButton temp = (RadioButton)radioGroup.getChildAt(i);
                        if(temp.getText().toString().equals(question.getSolution())) {
                            temp.setBackgroundColor(Color.GREEN);
                        }
                    }
                }
                radioButton1.setEnabled(false);
                radioButton2.setEnabled(false);
                radioButton3.setEnabled(false);
                radioButton4.setEnabled(false);
                questionNext.setEnabled(true);
            }
        });

        questionNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onNextPress(correct);
            }
        });

        Log.d(TAG, "onCreateView: Question Fragment created with id: "+question.getId());
        return view;

    }
}
