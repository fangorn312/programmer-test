package com.example.android.programmingquiz;

// This class contains a listQuestions of questions

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class QuestionBank {


    // declare listQuestions of Question objects
    private ArrayList<Question> listQuestions = new ArrayList<>();

    public ArrayList<String> getChoiceArrayOfQuestion(int questionNumber){
        return listQuestions.get(questionNumber).getChoiceArray();
    }

    // method returns number of questions in listQuestions
    public int getLength() {
        return listQuestions.size();
    }

    // method returns question from listQuestions based on listQuestions index
    public String getQuestion(int a) {
        return listQuestions.get(a).getQuestionText();
    }


    public ArrayList<Question> getListQuestions() {
        return listQuestions;
    }

    //  method returns correct answer for the question based on listQuestions index
    public ArrayList<String> getCorrectAnswers(int a){
        return listQuestions.get(a).getAnswers();
    }

    public int getQuestionType(int a){
        return listQuestions.get(a).getQuestionType();
    }

    public int getQuestionLanguageId(int a){
        return listQuestions.get(a).getLanguageId();
    }


    public void initQuestions(int categoryId) {
        listQuestions = MainActivity.instance().mDBHelper.getAllQuestionsList(categoryId);//get questions/choices/answers from database
        Collections.shuffle(listQuestions);
    }

}