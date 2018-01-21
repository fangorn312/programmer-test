package com.example.android.geektest2;

// This class contains a list of questions

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class QuestionBank {


    //QuestionFragment mQuestionFragment = new QuestionFragment();


    // declare list of Question objects
    List<Question> list = new ArrayList<>();
    DBHelper myDBHelper;


    // method returns number of questions in list
    public int getLength() {
        return list.size();
    }

    // method returns question from list based on list index
    public String getQuestion(int a) {
        return list.get(a).getQuestion();
    }

    public String getUniverse(int a) {return  list.get(a).getUniverse();}

    // method return a single multiple choice item for question based on list index,
    // based on number of multiple choice item in the list - 1, 2, 3 or 4
    // as an argument
    public String getChoice(int index, int num) {
        return list.get(index).getChoice(num - 1);
    }

    //  method returns correct answer for the question based on list index
    public String getCorrectAnswer(int a) {
        return list.get(a).getAnswer();
    }



    public void initQuestions(Context context, int lvl, int univ) {


        myDBHelper = new DBHelper(context, lvl, univ);
        list = myDBHelper.getAllQuestionsList();//get questions/choices/answers from database

    }

}