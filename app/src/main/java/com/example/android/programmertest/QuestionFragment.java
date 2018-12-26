package com.example.android.programmertest;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;


/**
 * A simple {@link Fragment} subclass.
 */
public class QuestionFragment extends Fragment implements OnBackPressedListener {

    public static final int TYPE_RADIO = 1;
    public static final int TYPE_CHECKBOX = 2;
    public static final int TYPE_INPUT = 3;
    public static final int TYPE_REPLACE = 4;

    public QuestionFragment() {
        // Required empty public constructor
    }

    //Tag for Bundle
    public static final String TAG_SCORE = "TAG_SCORE";
    public static final String TAG_QUANTITY = "TAG_QUANTITY";
    public static final String TAG_UNIVERSE = "TAG_UNIVERSE";
    public static final String TAG_CATEGORY = "TAG_CATEGORY";
    //values from Bundle

    private QuestionBank mQuestionLibrary = new QuestionBank();
    private TextView mQuestionText;
    //private String mAnswer;
    private ArrayList<String> mAnswers = new ArrayList<>();

    private int mScore = 0;
    private int mQuestionNumber = 0;
    private int mQuestionType = TYPE_RADIO;

    String selectedChoice;
    Button submit;

    RadioGroup mRadioGroup;
    ArrayList<RadioButton> mRadioButtons = new ArrayList<>();
    ArrayList<CheckBox> mCheckBoxes;
    ArrayList<Button> mDragButtons;
    ArrayList<String> mAnswerOrder = new ArrayList<>();
    LinearLayout ContainerCheckBoxes;
    EditText mInputAnswer;
    DragLinearLayout mDragLinearLayout;
    //CodeView mCodeView;
    WebView mWebView;
    String codeLanguage;
    ArrayList<Language> mLanguages;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question, container, false);
        mCheckBoxes = new ArrayList<>();
        mDragButtons = new ArrayList<>();
        codeLanguage = "";
        mLanguages = MainActivity.instance().mDBHelper.getAllLanguages();
        //Получить данные из других фрагментов


        mRadioGroup = view.findViewById(R.id.radioGroup_ques);
        ContainerCheckBoxes = view.findViewById(R.id.container_radio_checkbox);
        mInputAnswer = view.findViewById(R.id.answer_editText);
        mDragLinearLayout = view.findViewById(R.id.dragLinear_layout);
        mWebView = view.findViewById(R.id.web_view);
        //mCodeView = view.findViewById(R.id.code_view);

        mQuestionText = view.findViewById(R.id.question_text);
        try {
            mQuestionLibrary.initQuestions(QuizInfo.instance().SubsectionId);
        } catch (IOException e) {
            e.printStackTrace();
        }


        submit = view.findViewById(R.id.next_ques_btn);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (mQuestionType == TYPE_CHECKBOX) {
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
                } else if (mQuestionType == TYPE_RADIO) {
                    int id = mRadioGroup.getCheckedRadioButtonId();
                    if (id != -1) {

                        View radioButton = mRadioGroup.findViewById(id);
                        int radioId = mRadioGroup.indexOfChild(radioButton);
                        RadioButton btn = (RadioButton) mRadioGroup.getChildAt(radioId);
                        selectedChoice = (String) btn.getText();

                        if (selectedChoice.equals(mAnswers.get(0))) {
                            mScore++;
                            Toast.makeText(getActivity(), "Correct!", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(getActivity(), "Wrong!", Toast.LENGTH_SHORT).show();

                        updateQuestion();


                    }
                } else if (mQuestionType == TYPE_INPUT) {

                    String answer = mInputAnswer.getText().toString();
                    if (answer.isEmpty()) return;

                    if (answer.trim().equals(mAnswers.get(0))) {
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
                } else if (mQuestionType == TYPE_REPLACE) {
                    Collections.sort(mAnswerOrder, String.CASE_INSENSITIVE_ORDER);
                    int correctCount = 0;
                    for (int i = 0; i < mDragButtons.size(); i++) {
                        Button child = (Button) mDragLinearLayout.getChildAt(i + 1);
                        String s = mAnswerOrder.get(i);
                        int index = s.indexOf(" ");
                        String text = s.substring(index + 1);
                        if (text.equals(child.getText().toString())) {
                            correctCount++;
                        }
                    }
                    if (correctCount == mAnswerOrder.size()) {
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


    private void hideKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    private String findLanguageById(int id) {
        for (Language l : mLanguages) {
            if (l.getId() == id) return l.getName();
        }
        return "";
    }

    private void updateQuestion() {

        if (mQuestionNumber < mQuestionLibrary.getLength()) {
            mRadioButtons.clear();
            for (View v : mDragButtons) mDragLinearLayout.removeView(v);
            //mDragLinearLayout.removeAllViews();
            ContainerCheckBoxes.removeAllViews();
            mRadioGroup.removeAllViews();
            mInputAnswer.setText("");
            mCheckBoxes.clear();
            mDragButtons.clear();
            mRadioGroup.clearCheck();

            //mAnswer = mQuestionLibrary.getCorrectAnswer(mQuestionNumber);
            mAnswers = mQuestionLibrary.getCorrectAnswers(mQuestionNumber);


//            Codeview.with(getActivity())
//                    .withCode(mQuestionLibrary.getQuestion(mQuestionNumber))
//                    .setAutoWrap(true)
//                    .into(mQuestionText);
            int languageId = mQuestionLibrary.getQuestionLanguageId(mQuestionNumber);
            if (languageId != 0) {
                mWebView.setVisibility(View.VISIBLE);
                String code = mQuestionLibrary.getQuestion(mQuestionNumber);

                try {
                    String[] strings = code.split("!!code!!");
                    mQuestionText.setText(strings[0]);
                    code = strings[1];
                }
                catch (ArrayIndexOutOfBoundsException e){
                    e.printStackTrace();
                }
//                mQuestionText.setVisibility(View.GONE);
                Codeview.with(getActivity())
                        .withCode(code)
                        .setAutoWrap(true)
                        .setLang(findLanguageById(languageId))
                        .into(mWebView);
//                mCodeView.setCode(mQuestionLibrary.getQuestion(mQuestionNumber), findLanguageById(languageId));
            } else {
                mWebView.setVisibility(View.GONE);
//                mQuestionText.setVisibility(View.VISIBLE);
                mQuestionText.setText(mQuestionLibrary.getQuestion(mQuestionNumber));
            }
            mQuestionType = mQuestionLibrary.getQuestionType(mQuestionNumber);
            //mQuestionType = mQuestionLibrary.getCorrectAnswers(mQuestionNumber).size()==1?1:2;
            if (mQuestionType == TYPE_CHECKBOX) {
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
            } else if (mQuestionType == TYPE_RADIO) {
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
            } else if (mQuestionType == TYPE_INPUT) {
                mRadioGroup.setVisibility(View.GONE);
                mInputAnswer.setVisibility(View.VISIBLE);
                ContainerCheckBoxes.setVisibility(View.GONE);
                int maxLengthOfEditText = mAnswers.get(0).length() + 2;
                mInputAnswer.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLengthOfEditText)});
            } else if (mQuestionType == TYPE_REPLACE) {
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

                    int index = s.indexOf(" ");
                    String text = s.substring(index + 1);
                    mAnswerOrder.add(s);
                    btn.setText(text);
                    mDragButtons.add(btn);
                    mDragLinearLayout.addView(btn, 1);

                }
                for (int i = 1; i <= mDragButtons.size(); i++) {
                    View child = mDragLinearLayout.getChildAt(i);
                    mDragLinearLayout.setViewDraggable(child, child);
                    // the child will act as its own drag handle

                }
            } else if (mQuestionType == 5) {

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
        MainActivity.instance().changeView(IFragments.RESULT);
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
                MainActivity.instance().changeView(IFragments.SUBSECTION);
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



