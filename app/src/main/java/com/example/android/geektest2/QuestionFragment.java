package com.example.android.geektest2;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 */
public class QuestionFragment extends Fragment implements OnBackPressedListener {


    public QuestionFragment() {
        // Required empty public constructor
    }

    //Tag for Bundle
    public static final String TAG_SCORE = "TAG_SCORE";
    public static final String TAG_QUANTITY = "TAG_QUANTITY";
    public static final String TAG_UNIVERSE = "TAG_UNIVERSE";
    public static final String TAG_CATEGORY = "TAG_CATEGORY";
    //values from Bundle
    int receiveInfoUniv;
    String category;

    private QuestionBank mQuestionLibrary = new QuestionBank();
    private TextView mQuestionText;
    private String mAnswer;
    private String mUniverse;

    private int mScore = 0;
    private int mQuestionNumber = 0;

    String selectedChoice;
    Button submit;

    RadioGroup mRadioGroup;
    ArrayList<RadioButton> mRadioButtons = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question, container, false);

        //Получить данные из других фрагментов

        switch (QuizInfo.instance().UniverseId) {
            case 1:
                mUniverse = getResources().getString(R.string.lotr_btn);
                break;
            case 2:
                mUniverse = getResources().getString(R.string.potter_btn);
                break;
            case 3:
                mUniverse = getResources().getString(R.string.dota_btn);
                break;
            case 4:
                mUniverse = getResources().getString(R.string.marvel_btn);
                break;
            case 5:
                mUniverse = getResources().getString(R.string.dc_btn);
                break;
            default:
                mUniverse = "Unknown Universe";
                break;
        }

        mRadioGroup = view.findViewById(R.id.radioGroup_ques);

        mQuestionText = view.findViewById(R.id.question_text);
        try {
            mQuestionLibrary.initQuestions(getActivity(), QuizInfo.instance().Category, QuizInfo.instance().UniverseId);
        } catch (IOException e) {
            e.printStackTrace();
        }


        submit = view.findViewById(R.id.next_ques_btn);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int id = mRadioGroup.getCheckedRadioButtonId();

                //Если выбран хоть 1 RadioButton
                if (id != -1) {

                    View radioButton = mRadioGroup.findViewById(id);
                    int radioId = mRadioGroup.indexOfChild(radioButton);
                    RadioButton btn = (RadioButton) mRadioGroup.getChildAt(radioId);
                    selectedChoice = (String) btn.getText();

                    if (selectedChoice.equals(mAnswer)) {
                        mScore++;
                        Toast.makeText(getActivity(), "Correct!", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(getActivity(), "Wrong!", Toast.LENGTH_SHORT).show();

                    updateQuestion();


                }
            }
        });

        updateQuestion();

        return view;
    }


    private void updateQuestion() {

        if (mQuestionNumber < mQuestionLibrary.getLength()) {
            mRadioButtons.clear();
            mRadioGroup.removeAllViews();
            for (int i = 0; i < mQuestionLibrary.getChoiceArrayOfQuestion(mQuestionNumber).size(); i++) {
                RadioButton rb = new RadioButton(getActivity());
                mRadioGroup.addView(rb);
                mRadioButtons.add(rb);
            }

            ArrayList<Question> choices = mQuestionLibrary.getListQuestions();

            mQuestionText.setText(mQuestionLibrary.getQuestion(mQuestionNumber));

            int i = 0;
            for (String s : choices.get(mQuestionNumber).getChoiceArray()) {
                mRadioButtons.get(i).setText(s);
                i++;
            }

            mAnswer = mQuestionLibrary.getCorrectAnswer(mQuestionNumber);
            //mUniverse = mQuestionLibrary.getUniverse(mQuestionNumber);

            mRadioGroup.clearCheck();

            mQuestionNumber++;
        } else {
            goToNextFragment();
        }


    }

    private void goToNextFragment() {
        QuizInfo.instance().QuestionsCount=mQuestionLibrary.getLength();
        QuizInfo.instance().Score=mScore;
        QuizInfo.instance().UniverseName=mUniverse;

        MainActivity.instance().changeView(VPIds.RESULT);
//        ResultFragment resultFragment = new ResultFragment();
//
//        FragmentManager fragmentManager = getFragmentManager();
//        fragmentManager.beginTransaction().replace(R.id.fragment_container, resultFragment).commit();
    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Предупреждение");
        builder.setMessage("Вы действительно хотите прервать тест?");
        //.setIcon(R.drawable.ic_android_cat)
        builder.setCancelable(false);
        builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                MainActivity.instance().changeView(VPIds.CATEGORY);
            }
        }).setNegativeButton("Нет", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        builder.show();
        // полезный код
    }

}



