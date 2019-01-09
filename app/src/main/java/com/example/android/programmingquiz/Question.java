package com.example.android.programmingquiz;

import java.util.ArrayList;
import java.util.Collections;

// class Question to describe on questionText for test:
// questionText itself, multiple choices to answer, and correct answer
public class Question {

    private String questionText;
    private ArrayList<String> choice = new ArrayList<>();
    //private String answer;
    private ArrayList<String> answers = new ArrayList<>();
    private String universe;
    private int questionType;

    public int getLanguageId() {
        return languageId;
    }

    public void setLanguageId(int languageId) {
        this.languageId = languageId;
    }

    private int languageId;

    public int getQuestionType() {
        return questionType;
    }

    public void setQuestionType(int questionType) {
        this.questionType = questionType;
    }



    public Question() {

    }

//    public Question(String questionText, String[] choices, String universe, String answer) {
//        this.questionText = questionText;
//        this.choice[0] = choices[0];
//        this.choice[1] = choices[1];
//        this.choice[2] = choices[2];
//        this.choice[3] = choices[3];
//        this.universe = universe;
//        this.answer = answer;
//    }



    public String getQuestionText() {
        return questionText;
    }

    public String getChoice(int i) {
        return choice.get(i);
    }

    public ArrayList<String> getChoiceArray() {
        Collections.shuffle(choice);
        return choice;
    }

    public ArrayList<String> getAnswers() {
        return answers;
    }


    public void setAnswer(String answer) {
        answers.add(answer);
    }

    public void setChoice(String _choice) {
        choice.add(_choice);
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getUniverse() {
        return universe;
    }

    public void setUniverse(String universe) {
        this.universe = universe;
    }
}
