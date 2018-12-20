package com.example.android.geektest2;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.protectsoft.webviewcode.Codeview;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
    private WebView mQuestionText;
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
    ArrayList<Button> mDragButtons;
    ArrayList<String> mAnswerOrder=new ArrayList<>();
    LinearLayout ContainerCheckBoxes;
    EditText mInputAnswer;
    DragLinearLayout mDragLinearLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question, container, false);
        mCheckBoxes = new ArrayList<>();
        mDragButtons = new ArrayList<>();
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
        mDragLinearLayout = view.findViewById(R.id.dragLinear_layout);

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
                    int id = mRadioGroup.getCheckedRadioButtonId();
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

                    hideKeyboard();
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
                else if (mQuestionType==4){
                    Collections.sort(mAnswerOrder, String.CASE_INSENSITIVE_ORDER);
                    int correctCount=0;
                    for(int i = 0; i < mDragButtons.size(); i++){
                        Button child = (Button) mDragLinearLayout.getChildAt(i+1);
                        String s = mAnswerOrder.get(i);
                        int index= s.indexOf(" ");
                        String text = s.substring(index+1);
                        if (text.equals(child.getText().toString())){
                            correctCount++;
                        }
                    }
                    if (correctCount==mAnswerOrder.size()){
                        mScore++;
                        Toast.makeText(getActivity(), "Correct!", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(getActivity(), "Wrong!", Toast.LENGTH_SHORT).show();
                    updateQuestion();
                }


                //Если выбран хоть 1 RadioButton

            }
        });

        updateQuestion();

        return view;
    }


    private void hideKeyboard(){
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    private void updateQuestion() {

        if (mQuestionNumber < mQuestionLibrary.getLength()) {
            mRadioButtons.clear();
            for (View v: mDragButtons) mDragLinearLayout.removeView(v);
            //mDragLinearLayout.removeAllViews();
            ContainerCheckBoxes.removeAllViews();
            mRadioGroup.removeAllViews();
            mInputAnswer.setText("");
            mCheckBoxes.clear();
            mDragButtons.clear();
            mRadioGroup.clearCheck();

            mAnswer = mQuestionLibrary.getCorrectAnswer(mQuestionNumber);
            mAnswers = mQuestionLibrary.getCorrectAnswers(mQuestionNumber);


            Codeview.with(getActivity())
                    .withCode(mQuestionLibrary.getQuestion(mQuestionNumber))
                    .setAutoWrap(true)
                    .into(mQuestionText);
//            mQuestionText.setText(mQuestionLibrary.getQuestion(mQuestionNumber));
            mQuestionType = mQuestionLibrary.getQuestionType(mQuestionNumber);
            //mQuestionType = mQuestionLibrary.getCorrectAnswers(mQuestionNumber).size()==1?1:2;
            if (mQuestionType == 2) {
                mRadioGroup.setVisibility(View.GONE);
                mInputAnswer.setVisibility(View.GONE);
                ContainerCheckBoxes.setVisibility(View.VISIBLE);
                for (int i = 0; i < mQuestionLibrary.getChoiceArrayOfQuestion(mQuestionNumber).size(); i++) {
                    CheckBox cb = new CheckBox(getActivity());
                    mCheckBoxes.add(cb);
                    ContainerCheckBoxes.addView(cb);
                }
                int i = 0;
                ArrayList<Question> choices = mQuestionLibrary.getListQuestions();
                for (String s : choices.get(mQuestionNumber).getChoiceArray()) {
                    mCheckBoxes.get(i).setText(s);
                    i++;
                }
            } else if (mQuestionType == 1) {
                mRadioGroup.setVisibility(View.VISIBLE);
                mInputAnswer.setVisibility(View.GONE);
                ContainerCheckBoxes.setVisibility(View.GONE);
                for (int i = 0; i < mQuestionLibrary.getChoiceArrayOfQuestion(mQuestionNumber).size(); i++) {
                    RadioButton rb = new RadioButton(getActivity());
                    mRadioGroup.addView(rb);
                    mRadioButtons.add(rb);
                }
                int i = 0;
                ArrayList<Question> choices = mQuestionLibrary.getListQuestions();
                for (String s : choices.get(mQuestionNumber).getChoiceArray()) {
                    mRadioButtons.get(i).setText(s);
                    i++;
                }
            } else if (mQuestionType == 3) {
                mRadioGroup.setVisibility(View.GONE);
                mInputAnswer.setVisibility(View.VISIBLE);
                ContainerCheckBoxes.setVisibility(View.GONE);
                int maxLengthOfEditText = mAnswers.get(0).length()+2;
                mInputAnswer.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLengthOfEditText)});
            } else if (mQuestionType==4){
                mRadioGroup.setVisibility(View.GONE);
                mInputAnswer.setVisibility(View.GONE);
                ContainerCheckBoxes.setVisibility(View.GONE);
//                for (int i = 0; i < mQuestionLibrary.getChoiceArrayOfQuestion(mQuestionNumber).size(); i++) {
//                    TextView tv = new TextView(getActivity());
//                    mDragButtons.add(tv);
//                    mDragLinearLayout.addView(tv);
//                }
                ArrayList<Question> choices = mQuestionLibrary.getListQuestions();
                for (String s : choices.get(mQuestionNumber).getChoiceArray()) {
                    @SuppressLint("RestrictedApi") Button btn = new Button(new ContextThemeWrapper(getActivity(), R.style.Widget_AppCompat_Button_Colored), null, 0);//new Button(getActivity());
                    btn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                    int index= s.indexOf(" ");
                    String text = s.substring(index+1);
                    mAnswerOrder.add(s);
                    btn.setText(text);
                    mDragButtons.add(btn);
                    mDragLinearLayout.addView(btn,1);

                }
                for(int i = 1; i <= mDragButtons.size(); i++){
                    View child = mDragLinearLayout.getChildAt(i);
                    mDragLinearLayout.setViewDraggable(child, child);
                    // the child will act as its own drag handle

                }
            }
            else if(mQuestionType==5){

            }

            //mUniverse = mQuestionLibrary.getUniverse(mQuestionNumber);



//            for (CheckBox c : mCheckBoxes) c.setChecked(false);

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



