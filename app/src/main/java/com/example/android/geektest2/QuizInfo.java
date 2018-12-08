package com.example.android.geektest2;

public class QuizInfo {

    private static QuizInfo instance;

    public static QuizInfo instance(){
        if (instance==null)
            instance = new QuizInfo();
        return instance;
    }

    private QuizInfo(){
        UniverseName="";
        UniverseId=0;
        Category="";
        Score=0;
        QuestionsCount=0;
    }

    public int UniverseId;
    public String UniverseName;
    public String Category;
    public int Score;
    public int QuestionsCount;
}
