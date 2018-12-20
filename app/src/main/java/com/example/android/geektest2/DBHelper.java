package com.example.android.geektest2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {


    private static String DB_PATH;
    private static String DB_NAME = "myDB.sqlite";
    private SQLiteDatabase dataBase;
    private final Context fContext;


    private long lastModified;
    public static final int DB_VERSION = 5;
    public static final boolean isChanged = false;

    private String category;
    private int univ_id;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        fContext = context;
        DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        loadFromCache();

    }

    @SuppressLint("SdCardPath")
    public DBHelper(Context context, String category, int univ_id) {
        super(context, DB_NAME, null, DB_VERSION);

        this.category = category;
        this.univ_id = univ_id;

        fContext = context;
        DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        loadFromCache();

    }


    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();
        if (dbExist)
            return;
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
    }

    private boolean checkDataBase() {
        boolean checkDB = false;
        try {
            String myPath = DB_PATH + DB_NAME;
            File dbFile = new File(myPath);
            checkDB = dbFile.exists();
            long lm = dbFile.lastModified();
            if (lm != lastModified) {
                lastModified = lm;
                checkDB = false;
                putToCache();
            }
//            lastModified = dbFile.lastModified();
        } catch (SQLiteException e) {
            Log.d("TEST_TAG", "База отсутствует");
            e.printStackTrace();
            //файл базы данных отсутствует
        }
        return checkDB;
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

    public void openDataBase() throws SQLException, IOException {
        String path = DB_PATH + DB_NAME;
        if (dataBase==null) dataBase = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
    }

    @Override
    public synchronized void close() {
        if (dataBase != null)
            dataBase.close();
        super.close();
    }

//    public ArrayList<String[]> getDbTableDetails() {
//        //SQLiteDatabase db = this.getReadableDatabase();
//        dataBase = this.getReadableDatabase();
//        Cursor c = dataBase.rawQuery(
//                "SELECT name FROM sqlite_master WHERE type='table'", null);
//        ArrayList<String[]> result = new ArrayList<String[]>();
//        int i = 0;
//        result.add(c.getColumnNames());
//        for (i = 0; i < c.getColumnNames().length; i++)
//            Log.d("TAG_COLUMNS", "" + c.getColumnNames()[i]);
//        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
//            String[] temp = new String[c.getColumnCount()];
//            for (i = 0; i < temp.length; i++) {
//                temp[i] = c.getString(i);
//                Log.d("TEST_TAG", "" + temp[i]);
//            }
//            result.add(temp);
//        }
//
//        return result;
//    }

    public ArrayList<String> getAllCategoryList(int univId){
        ArrayList<String> categoryArrayList = new ArrayList<>();

        String selectCategoryQuery = "SELECT DISTINCT c.Name FROM Category c, Universes u WHERE u.univ_id="+univId;
//                "SELECT DISTINCT category FROM Questions " +
//                "WHERE univ_id= "+univId+";";

        SQLiteDatabase dataBase = this.getReadableDatabase();
        @SuppressLint("Recycle") Cursor cursor = dataBase.rawQuery(selectCategoryQuery, null);

        if (cursor.moveToFirst()) {
            do {
                String categoryText = cursor.getString(cursor.getColumnIndex("Name"));
                categoryArrayList.add(categoryText);
            } while (cursor.moveToNext());
        }
        return categoryArrayList;
    }

    public ArrayList<Universe> getAllUniverseList() {
        ArrayList<Universe> universeArrayList = new ArrayList<>();

        String selectUniverseQuery = "SELECT * FROM Universes;";
        SQLiteDatabase dataBase = this.getReadableDatabase();
        @SuppressLint("Recycle") Cursor cursor = dataBase.rawQuery(selectUniverseQuery, null);

        if (cursor.moveToFirst()) {
            do {
                String univText = cursor.getString(cursor.getColumnIndex("name"));

                int univId = cursor.getInt(cursor.getColumnIndex("univ_id"));

                Universe universe = new Universe(univId, univText);
                universeArrayList.add(universe);
            } while (cursor.moveToNext());
        }
        return universeArrayList;
    }

    public ArrayList<Question> getAllQuestionsList(String category, int univ_id) {
        ArrayList<Question> questionArrayList = new ArrayList<>();

        String selectQueryQues = "SELECT  * FROM Questions " +
                "WHERE category = '" + category + "' and univ_id = " + univ_id + ";";//+ TABLE_QUESTION;

        String selectQueryCh = "SELECT * FROM Answers, Questions " +
                "WHERE Questions.ques_id = Answers.ques_id " +
                "and category = '" + category + "' and univ_id = " + univ_id + ";";

//        String selectQueryUniv = "SELECT * FROM Universe WHERE univ_id = " + univ_id + ";";
//        //String selectQueryAns = "SELECT answer FROM Answers WHERE correct = 1;";
//        String selectTest = "SELECT name FROM sqlite_master WHERE type='table';";

        //dataBase = this.getReadableDatabase();
        SQLiteDatabase dataBase = this.getReadableDatabase();
        Log.d("DB_TAG", "isOpen = " + dataBase.isOpen());


        @SuppressLint("Recycle") Cursor curs_ques = dataBase.rawQuery(selectQueryQues, null);
        @SuppressLint("Recycle") Cursor curs_ch = dataBase.rawQuery(selectQueryCh, null);
//        @SuppressLint("Recycle") Cursor curs_univ = dataBase.rawQuery(selectQueryUniv, null);
        //@SuppressLint("Recycle") Cursor curs_ans = db.rawQuery(selectQueryAns, null);

        // looping through all records and adding to the listQuestions
        curs_ch.moveToFirst();
//        curs_univ.moveToFirst();
        //curs_ans.moveToFirst();


        if (curs_ques.moveToFirst()) {
            do {
                Question question = new Question();

                //String univText = curs_univ.getString(curs_univ.getColumnIndex("name"));
                //question.setUniverse(univText);

                String questText = curs_ques.getString(curs_ques.getColumnIndex("name"));
                question.setQuestionText(questText);

                String quesId = curs_ch.getString(curs_ch.getColumnIndex("ques_id"));
                while (true) {

                    String quesId2 = curs_ch.getString(curs_ch.getColumnIndex("ques_id"));
                    if (!quesId2.equals(quesId)) break;

                    Integer order = curs_ch.getInt(curs_ch.getColumnIndex("order"));
                    String choiceText = curs_ch.getString(curs_ch.getColumnIndex("answer"));
                    if (order!=0) question.setChoice("order:"+order+" "+choiceText);
                    else question.setChoice(choiceText);
                    int answerCorrect = curs_ch.getInt(curs_ch.getColumnIndex("correct"));
                    if (answerCorrect == 1) question.setAnswer(choiceText);

                    int questionType =  curs_ch.getInt(curs_ch.getColumnIndex("type"));
                    question.setQuestionType(questionType);



                    if (curs_ch.isLast()) break;
                    curs_ch.moveToNext();
                }

                // adding to Questions listQuestions
                questionArrayList.add(question);
            } while (curs_ques.moveToNext());
            //Collections.shuffle(questionArrayList);
        }
        return questionArrayList;
    }


    //Создание аккаунта и статистики
    @Override
    public void onCreate(SQLiteDatabase db) {
//        db.execSQL("CREATE TABLE Accounts ("
//                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
//                + "name TEXT,"
//                + "password TEXT" + ");");
//
//        db.execSQL("CREATE TABLE Score ("
//                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
//                + "lotr TEXT,"
//                + "password TEXT" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }


    public void putToCache() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(fContext);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong("base_time", lastModified);
        editor.apply();
    }

    public void loadFromCache() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(fContext);
        lastModified = settings.getLong("base_time", 0);
    }


}