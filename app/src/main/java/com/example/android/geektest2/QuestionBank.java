package com.example.android.geektest2;

// This class contains a listQuestions of questions

import android.content.Context;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class QuestionBank {


    //QuestionFragment mQuestionFragment = new QuestionFragment();


    // declare listQuestions of Question objects
    ArrayList<Question> listQuestions = new ArrayList<>();
    ArrayList<String[]> listDB = new ArrayList<>();
    DBHelper myDBHelper;

    public ArrayList<String> getChoiceArrayOfQuestion(int questionNumber){
        return listQuestions.get(questionNumber).getChoiceArray();
    }

    // method returns number of questions in listQuestions
    public int getLength() {
        return listQuestions.size();
    }

    // method returns question from listQuestions based on listQuestions index
    public String getQuestion(int a) {
        return listQuestions.get(a).getQuestion();
    }

    public String getUniverse(int a) {return  listQuestions.get(a).getUniverse();}

    // method return a single multiple choice item for question based on listQuestions index,
    // based on number of multiple choice item in the listQuestions - 1, 2, 3 or 4
    // as an argument
    public String getChoice(int index, int num) {
        return listQuestions.get(index).getChoice(num - 1);
    }

    public ArrayList<Question> getListQuestions() {
        return listQuestions;
    }

    //  method returns correct answer for the question based on listQuestions index
    public String getCorrectAnswer(int a) {
        return listQuestions.get(a).getAnswer();
    }



    public void initQuestions(Context context, String category, int univ) throws IOException {
        myDBHelper = new DBHelper(context, category, univ);
        myDBHelper.createDataBase();
        myDBHelper.openDataBase();
        listDB = myDBHelper.getDbTableDetails();
        listQuestions = myDBHelper.getAllQuestionsList();//get questions/choices/answers from database
        Collections.shuffle(listQuestions);
        myDBHelper.close();
    }

}