package com.example.android.geektest2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DBHelper extends SQLiteOpenHelper {
    private static String DB_PATH;// = "/data/data/com.example.geektest2/databases/";
    private static String DB_NAME = "myDB.sqlite";
    private SQLiteDatabase dataBase;
    private final Context fContext;

    private int level, univ_id;

    @SuppressLint("SdCardPath")
    public DBHelper(Context context, int level, int univ_id) {
        //super(context, DB_NAME, null, 1);
        //this.fContext = context;
        super(context, DB_NAME, null, 1);

        this.level = level;
        this.univ_id = univ_id;

        fContext = context;
        DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";

    }


    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();
        if (dbExist) {
            //ничего не делаем – файл базы данных уже есть
        } else {
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
            //файл базы данных отсутствует
        }
        if (checkDB != null) {
            checkDB.close();
        }
        return checkDB != null;
    }

    private void copyDataBase() throws IOException {
        InputStream input = fContext.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream output = new FileOutputStream(outFileName);
        byte[] buffer = new byte[4096];
        int length;
        while ((length = input.read(buffer)) > 0) {
            output.write(buffer, 0, length);
        }
        output.flush();
        output.close();
        input.close();
    }

    public void openDataBase() throws SQLException {
        String path = DB_PATH + DB_NAME;
        dataBase = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
    }

    @Override
    public synchronized void close() {
        if (dataBase != null)
            dataBase.close();
        super.close();
    }


    public List<Question> getAllQuestionsList() {
        List<Question> questionArrayList = new ArrayList<>();
        String selectQueryQues = "SELECT  * FROM Questions " +
                "WHERE level = "+ level + " and univ_id = " + univ_id + ";" ;//+ TABLE_QUESTION;
        String selectQueryCh = "SELECT * FROM Answers, Questions " +
                "WHERE Questions.ques_id = Answers.ques_id " +
                "and level = "+ level + " and univ_id = " + univ_id + ";";
        String selectQueryUniv = "SELECT * FROM Universes WHERE univ_id = " + univ_id + ";";
        //String selectQueryAns = "SELECT answer FROM Answers WHERE correct = 1;";

        SQLiteDatabase db = this.getReadableDatabase();
        @SuppressLint("Recycle") Cursor curs_ques = db.rawQuery(selectQueryQues, null);
        @SuppressLint("Recycle") Cursor curs_ch = db.rawQuery(selectQueryCh, null);
        @SuppressLint("Recycle") Cursor curs_univ = db.rawQuery(selectQueryUniv, null);
        //@SuppressLint("Recycle") Cursor curs_ans = db.rawQuery(selectQueryAns, null);

        // looping through all records and adding to the list
        curs_ch.moveToFirst();
        curs_univ.moveToFirst();
        //curs_ans.moveToFirst();


        if (curs_ques.moveToFirst()) {
            do {
                Question question = new Question();

                //String univText = curs_univ.getString(curs_univ.getColumnIndex("name"));
                //question.setUniverse(univText);

                String questText= curs_ques.getString(curs_ques.getColumnIndex("name"));
                question.setQuestion(questText);

                for (int i=0; i<4; i++) {

                    String choiceText = curs_ch.getString(curs_ch.getColumnIndex("answer"));
                    question.setChoice(i, choiceText);

                    int answerCorrect = curs_ch.getInt(curs_ch.getColumnIndex("correct"));
                    if (answerCorrect == 1) question.setAnswer(choiceText);

                    curs_ch.moveToNext();
                }

                // adding to Questions list
                questionArrayList.add(question);
            } while (curs_ques.moveToNext());
            //Collections.shuffle(questionArrayList);
        }
        return questionArrayList;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }




}