package com.example.android.geektest2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// class Question to describe on question for test:
// question itself, multiple choices to answer, and correct answer
public class Question {

    private String question;
    private ArrayList<String> choice = new ArrayList<>();
    private String answer;
    private String universe;

    public Question() {

    }

//    public Question(String question, String[] choices, String universe, String answer) {
//        this.question = question;
//        this.choice[0] = choices[0];
//        this.choice[1] = choices[1];
//        this.choice[2] = choices[2];
//        this.choice[3] = choices[3];
//        this.universe = universe;
//        this.answer = answer;
//    }



    public String getQuestion() {
        return question;
    }

    public String getChoice(int i) {
        return choice.get(i);
    }

    public ArrayList<String> getChoiceArray() {
        Collections.shuffle(choice);
        return choice;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setChoice(String _choice) {
        choice.add(_choice);
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getUniverse() {
        return universe;
    }

    public void setUniverse(String universe) {
        this.universe = universe;
    }
}
