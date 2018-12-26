package com.example.android.programmertest;

public class QuizInfo {

    private static QuizInfo instance;

    public static QuizInfo instance(){
        if (instance==null)
            instance = new QuizInfo();
        return instance;
    }

    private QuizInfo(){
        SectionName ="";
        SectionId =0;
        SubsectionName ="";
        Score=0;
        SubsectionId =0;
        QuestionsCount=0;
    }

    public int SectionId;
    public String SectionName;
    public String SubsectionName;
    public int SubsectionId;
    public int Score;
    public int QuestionsCount;
}
