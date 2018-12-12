package com.example.android.geektest2;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
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
    private ArrayList<String> mAnswers = new ArrayList<>();
    private String mUniverse;

    private int mScore = 0;
    private int mQuestionNumber = 0;
    private int mQuestionType = 1;

    String selectedChoice;
    Button submit;

    RadioGroup mRadioGroup;
    ArrayList<RadioButton> mRadioButtons = new ArrayList<>();
    ArrayList<CheckBox> mCheckBoxes;
    LinearLayout ContainerCheckBoxes;
    EditText mInputAnswer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question, container, false);
        mCheckBoxes = new ArrayList<>();
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
        ContainerCheckBoxes = view.findViewById(R.id.container_radio_checkbox);
        mInputAnswer = view.findViewById(R.id.answer_editText);

        mQuestionText = view.findViewById(R.id.question_text);
        try {
            mQuestionLibrary.initQuestions(QuizInfo.instance().Category, QuizInfo.instance().UniverseId);
        } catch (IOException e) {
            e.printStackTrace();
        }


        submit = view.findViewById(R.id.next_ques_btn);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int id = -1;
                if (mQuestionType == 2) {
                    ArrayList<String> selected = new ArrayList<>();

                    for (CheckBox ch : mCheckBoxes) {
                        if (ch.isChecked()) selected.add((String) ch.getText());
                    }
                    if (selected.size() > 0) {
                        int correctCount = 0;
                        for (String s : selected) {
                            if (mAnswers.contains(s)) correctCount++;
                        }
                        if (mAnswers.size() == correctCount && mAnswers.size() == selected.size()) {
                            mScore++;
                            Toast.makeText(getActivity(), "Correct!", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(getActivity(), "Wrong!", Toast.LENGTH_SHORT).show();

                        updateQuestion();
                    }
                } else if (mQuestionType==1){
                    id = mRadioGroup.getCheckedRadioButtonId();
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
                else if (mQuestionType==3){
                    String answer = mInputAnswer.getText().toString();
                    if (answer.isEmpty()) return;

                    if (answer.trim().equals(mAnswer)) {
                        mScore++;
                        Toast.makeText(getActivity(), "Correct!", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(getActivity(), "Wrong!", Toast.LENGTH_SHORT).show();

                    View view = getActivity().getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (imm != null) {
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }
                    }
//                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
//                    if (imm != null) {
//                        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
//                    }
//                    InputMethodManager imm = (InputMethodManager) getActivity()
//                            .getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.hideSoftInputFromWindow(getActivity().getWindow()
//                            .getCurrentFocus().getWindowToken(), 0);
                    updateQuestion();
                }


                //Если выбран хоть 1 RadioButton

            }
        });

        updateQuestion();

        return view;
    }


    private void updateQuestion() {

        if (mQuestionNumber < mQuestionLibrary.getLength()) {
            mRadioButtons.clear();
            ContainerCheckBoxes.removeAllViews();
            mRadioGroup.removeAllViews();
            mInputAnswer.setText("");

            mAnswer = mQuestionLibrary.getCorrectAnswer(mQuestionNumber);
            mAnswers = mQuestionLibrary.getCorrectAnswers(mQuestionNumber);


            mQuestionText.setText(mQuestionLibrary.getQuestion(mQuestionNumber));
            mQuestionType = mQuestionLibrary.getQuestionType(mQuestionNumber);
            //mQuestionType = mQuestionLibrary.getCorrectAnswers(mQuestionNumber).size()==1?1:2;
            if (mQuestionType == 2) {
                mRadioGroup.setVisibility(View.INVISIBLE);
                mInputAnswer.setVisibility(View.INVISIBLE);
                ContainerCheckBoxes.setVisibility(View.VISIBLE);
                for (int i = 0; i < mQuestionLibrary.getChoiceArrayOfQuestion(mQuestionNumber).size(); i++) {
                    CheckBox cb = new CheckBox(getActivity());
                    mCheckBoxes.add(cb);
                    ContainerCheckBoxes.addView(cb);
                }
            } else if (mQuestionType == 1) {
                mRadioGroup.setVisibility(View.VISIBLE);
                mInputAnswer.setVisibility(View.INVISIBLE);
                ContainerCheckBoxes.setVisibility(View.INVISIBLE);
                for (int i = 0; i < mQuestionLibrary.getChoiceArrayOfQuestion(mQuestionNumber).size(); i++) {
                    RadioButton rb = new RadioButton(getActivity());
                    mRadioGroup.addView(rb);
                    mRadioButtons.add(rb);
                }
            } else if (mQuestionType == 3) {
                mRadioGroup.setVisibility(View.INVISIBLE);
                mInputAnswer.setVisibility(View.VISIBLE);
                ContainerCheckBoxes.setVisibility(View.INVISIBLE);
                int maxLengthOfEditText = mAnswers.get(0).length()+2;
                mInputAnswer.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLengthOfEditText)});
            }

            if (mQuestionType == 3) {

            } else {
                int i = 0;
                ArrayList<Question> choices = mQuestionLibrary.getListQuestions();
                for (String s : choices.get(mQuestionNumber).getChoiceArray()) {
                    if (mQuestionType == 1)
                        mRadioButtons.get(i).setText(s);
                    else if (mQuestionType == 2) mCheckBoxes.get(i).setText(s);
                    i++;
                }
            }


            //mUniverse = mQuestionLibrary.getUniverse(mQuestionNumber);

            mRadioGroup.clearCheck();
            for (CheckBox c : mCheckBoxes) c.setChecked(false);

            mQuestionNumber++;
        } else {
            goToNextFragment();
        }


    }

    private void goToNextFragment() {
        QuizInfo.instance().QuestionsCount = mQuestionLibrary.getLength();
        QuizInfo.instance().Score = mScore;
        QuizInfo.instance().UniverseName = mUniverse;

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



