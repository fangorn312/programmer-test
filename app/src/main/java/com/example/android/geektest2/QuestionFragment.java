package com.example.android.geektest2;


import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class QuestionFragment extends Fragment implements OnBackPressedListener {


    public QuestionFragment() {
        // Required empty public constructor
    }

    public static final String TAG_SCORE = "TAG_SCORE";
    public static final String TAG_QUANTITY = "TAG_QUANTITY";
    public static final String TAG_UNIVERSE = "TAG_UNIVERSE";
    public static final String TAG_LEVEL = "TAG_LEVEL";

    private QuestionBank mQuestionLibrary = new QuestionBank();
    private TextView mQuestionText;
    private String mAnswer;
    private String mUniverse;

    private int mScore = 0;
    private int mQuestionNumber = 0;

    String selectedChoice;
    Button submit;

    RadioGroup mRadioGroup;
    RadioButton choice1, choice2, choice3, choice4;
    List<Integer> choices;

    int receiveInfoUniv;
    int receiveInfoLvl;

    CountDownTimer start;

    ProgressBar progressBar;
    TextView progressTimer;

    //private Chronometer mChronometer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            receiveInfoUniv = bundle.getInt(LevelsFragment.TAG_SELECTED_UNIV);
            receiveInfoLvl = bundle.getInt(LevelsFragment.TAG_SELECTED_LVL);
            Log.d("MYTAG", "Accepted " + receiveInfoUniv + " " + receiveInfoLvl);
        }

        //mChronometer = view.findViewById(R.id.ques_chronometer);
        //mChronometer.start();
        progressBar = view.findViewById(R.id.progressBar);
        progressTimer = view.findViewById(R.id.timer_text_view);
        progressBar.setMax(8);

        switch (receiveInfoUniv){
            case 1: mUniverse = getResources().getString(R.string.lotr_btn); break;
            case 2: mUniverse = getResources().getString(R.string.potter_btn); break;
            case 3: mUniverse = getResources().getString(R.string.dota_btn); break;
            case 4: mUniverse = getResources().getString(R.string.marvel_btn); break;
            case 5: mUniverse = getResources().getString(R.string.dc_btn); break;
            default: mUniverse = "Unknown Universe"; break;
        }

        mRadioGroup = view.findViewById(R.id.radioGroup_ques);

        choice1 = view.findViewById(R.id.radioBtn1);
        choice2 = view.findViewById(R.id.radioBtn2);
        choice3 = view.findViewById(R.id.radioBtn3);
        choice4 = view.findViewById(R.id.radioBtn4);

        choices = new ArrayList<>();
        choices.add(1);
        choices.add(2);
        choices.add(3);
        choices.add(4);

        mQuestionText = view.findViewById(R.id.question_text);
        mQuestionLibrary.initQuestions(getContext(), receiveInfoLvl, receiveInfoUniv);

        submit = view.findViewById(R.id.next_ques_btn);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                int id= mRadioGroup.getCheckedRadioButtonId();

                if (id != -1) {

                    View radioButton = mRadioGroup.findViewById(id);
                    int radioId = mRadioGroup.indexOfChild(radioButton);
                    RadioButton btn = (RadioButton) mRadioGroup.getChildAt(radioId);
                    selectedChoice = (String) btn.getText();

                    if (selectedChoice.equals(mAnswer)) {
                        mScore = mScore + 1;
                        Toast.makeText(getActivity(), "Correct!", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(getActivity(), "Wrong!", Toast.LENGTH_SHORT).show();

                    stopTimer();
                    updateQuestion();


                }
            }
        });

        updateQuestion();

        return view;
    }


    private void startTimer(){
        ;
        start = new CountDownTimer(10000, 1000) {
            int i=0;
            @SuppressLint("SetTextI18n")
            public void onTick(long millisUntilFinished) {
                progressBar.setProgress(i++);
                progressTimer.setText(""+(millisUntilFinished / 1000));
                //progressBar.setProgress((int) (8-(millisUntilFinished / 1000)-1));
            }

            public void onFinish() {
                updateQuestion();
            }
        }.start();
    }

    private void stopTimer(){
        start.cancel();
        progressBar.setProgress(0);
    }


    private void updateQuestion() {

        startTimer();
        if (mQuestionNumber < mQuestionLibrary.getLength()) {

            //Случайный набор вариантов от 1 до 4
            Collections.shuffle(choices);

            mQuestionText.setText(mQuestionLibrary.getQuestion(mQuestionNumber));
            choice1.setText(mQuestionLibrary.getChoice(mQuestionNumber, choices.get(0)));
            choice2.setText(mQuestionLibrary.getChoice(mQuestionNumber, choices.get(1)));
            choice3.setText(mQuestionLibrary.getChoice(mQuestionNumber, choices.get(2)));
            choice4.setText(mQuestionLibrary.getChoice(mQuestionNumber, choices.get(3)));

            mAnswer = mQuestionLibrary.getCorrectAnswer(mQuestionNumber);
            //mUniverse = mQuestionLibrary.getUniverse(mQuestionNumber);

            mRadioGroup.clearCheck();

            mQuestionNumber++;
        } else {
            stopTimer();
            //mChronometer.stop();
            ResultFragment resultFragment = new ResultFragment();

            Bundle bundle = new Bundle();
            bundle.putInt(TAG_SCORE, mScore);
            bundle.putInt(TAG_QUANTITY, mQuestionNumber);
            bundle.putString(TAG_UNIVERSE, mUniverse);
            bundle.putInt(TAG_LEVEL, receiveInfoLvl);

            resultFragment.setArguments(bundle);
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment_container,  resultFragment).commit();

        }


    }

    private void restartProgress(){

        progressBar.setVisibility(View.VISIBLE);
        progressBar.setProgress(0);
        //mt.execute(10);
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
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container, new ResultFragment());
                ft.commit();
            }
        });
        builder.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        builder.show();
        // полезный код
    }

}



