package com.example.ecoquiz;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class QuestionFragment extends Fragment {

    public QuestionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_question, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        View view = getView();
        if (view != null) {
            TextView questionText = view.findViewById(R.id.question_text);
            ImageView questionImg = view.findViewById(R.id.question_image);
            RadioButton answer1 = view.findViewById(R.id.radiobutton1);
            RadioButton answer2 = view.findViewById(R.id.radiobutton2);
            RadioButton answer3 = view.findViewById(R.id.radiobutton3);
            RadioButton answer4 = view.findViewById(R.id.radiobutton4);
            QuestionScreen questionScreen = (QuestionScreen) getActivity();
            Question question = MainScreen.getQuestionList().get(questionScreen.getCurrentQuestion());

            questionText.setText(question.getQuestionText());
            if(question.getQuestionImage() != null &&   !question.getQuestionImage().isEmpty() ) {
                String resource = "@drawable/"+question.getQuestionImage();
                int imageResource = getResources().getIdentifier(resource, null, null );
                Drawable res = getResources().getDrawable(imageResource);
                questionImg.setImageDrawable(res);
            }
            answer1.setText(question.getAnswer1());
            answer2.setText(question.getAnswer2());
            if(question.getAnswer3() != null &&   !question.getAnswer3().isEmpty()) {
                answer3.setText(question.getAnswer3());
                answer3.setVisibility(View.VISIBLE);
                answer4.setText(question.getAnswer4());
                answer4.setVisibility(View.VISIBLE);

            }
            else {
                answer3.setVisibility(View.INVISIBLE);
                answer4.setVisibility(View.INVISIBLE);
            }

          //  LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
          //  view.setLayoutParams(params);

        }
    }
}
