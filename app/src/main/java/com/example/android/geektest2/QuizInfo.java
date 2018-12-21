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
        CategoryName ="";
        Score=0;
        CategoryId=0;
        QuestionsCount=0;
    }

    public int UniverseId;
    public String UniverseName;
    public String CategoryName;
    public int CategoryId;
    public int Score;
    public int QuestionsCount;
}
