package com.example.ecoquiz;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


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

    public QuestionFragment() {
        // Required empty public constructor
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

        Question question = MainActivity.getQuestionList().get(QuestionActivity.getCurrentQuestion());

        questionText.setText(question.getQuestionText());
        if(question.getQuestionImage() != null &&   !question.getQuestionImage().isEmpty() ) {
            String resource = "@drawable/"+question.getQuestionImage();
            int imageResource = getResources().getIdentifier(resource, null, null );
            Drawable res = getResources().getDrawable(imageResource);
            questionImage.setImageDrawable(res);
        }
        radioButton1.setText(question.getAnswer1());
        radioButton2.setText(question.getAnswer2());
        if(question.getAnswer3() != null &&   !question.getAnswer3().isEmpty()) {
            radioButton3.setText(question.getAnswer3());
            radioButton3.setVisibility(View.VISIBLE);
            radioButton4.setText(question.getAnswer4());
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
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
               questionNext.setEnabled(true);
            }
        });

        questionNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QuestionActivity.increaseCurrentQuestion();
                QuestionFragment question = new QuestionFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, question).commit();
            //    getActivity().getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, question).addToBackStack(null).commit();
            }
        });

        return view;

    }

    public RadioGroup getRadioGroup() {
        return radioGroup;
    }

    public RadioButton getRadioButton1() {
        return radioButton1;
    }

    public RadioButton getRadioButton2() {
        return radioButton2;
    }

    public RadioButton getRadioButton3() {
        return radioButton3;
    }

    public RadioButton getRadioButton4() {
        return radioButton4;
    }

    public ImageView getQuestionImage() {
        return questionImage;
    }

    public TextView getQuestionText() {
        return questionText;
    }

    public TextView getQuestionCursor() {
        return questionCursor;
    }

    public Button getQuestionNext() {
        return questionNext;
    }
}
