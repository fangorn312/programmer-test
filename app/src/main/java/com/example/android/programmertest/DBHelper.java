package com.example.android.programmertest;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DBHelper extends SQLiteOpenHelper {


    public static String DB_PATH;
    public static String DB_NAME = "myDB.sqlite";
    private SQLiteDatabase dataBase;
    private final Context fContext;


    //private long lastModified;
    public static final int DB_VERSION = 10;
    //public static final boolean isChanged = false;


    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        fContext = context;
        Log.d("DBHelper", context.getFilesDir().getAbsolutePath());
        Log.d("DBHelper", context.getFilesDir().getPath());
        Log.d("DBHelper", context.getFilesDir().getName());
        DB_PATH = "data/data/"+context.getPackageName()+"/databases/";
        //loadFromCache();

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

    public void createDataBaseForced() {
        this.getReadableDatabase();
        try {
            copyDataBase();
        } catch (IOException e) {
            throw new Error("Error copying database");
        }
    }

    public String dbLastModified(){
        try {
            String myPath = DB_PATH + DB_NAME;
            File dbFile = new File(myPath);
            if (!dbFile.exists()) return "База отсутствует";
            long lm = dbFile.lastModified();
            String pattern = "dd-MM-yyyy HH:mm:ss";
            @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

            return simpleDateFormat.format(new Date(lm));
//            lastModified = dbFile.lastModified();
        } catch (SQLiteException e) {
            Log.d("TEST_TAG", "База отсутствует");
            e.printStackTrace();
            return "База отсутствует";
            //файл базы данных отсутствует
        }
    }

    private boolean checkDataBase() {
        boolean checkDB = false;
        try {
            String myPath = DB_PATH + DB_NAME;
            File dbFile = new File(myPath);
            checkDB = dbFile.exists();
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
        if (dataBase == null)
            dataBase = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
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

    public ArrayList<Subsection> getAllSubsectionsList(int sectionId) {
        ArrayList<Subsection> subsectionArrayList = new ArrayList<>();

        String query = "SELECT DISTINCT * FROM Subsection WHERE SectionId=" + sectionId;
//                "SELECT DISTINCT category FROM Questions " +
//                "WHERE univ_id= "+univId+";";

        SQLiteDatabase dataBase = this.getReadableDatabase();
        @SuppressLint("Recycle") Cursor cursor = dataBase.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndex("Name"));
                int id = cursor.getInt(cursor.getColumnIndex("Id"));
                subsectionArrayList.add(new Subsection(id, name));
            } while (cursor.moveToNext());
        }
        return subsectionArrayList;
    }

    public ArrayList<Section> getAllSectionsList() {
        ArrayList<Section> sectionArrayList = new ArrayList<>();

        String query = "SELECT * FROM Section;";
        SQLiteDatabase dataBase = this.getReadableDatabase();
        @SuppressLint("Recycle") Cursor cursor = dataBase.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndex("Name"));

                int id = cursor.getInt(cursor.getColumnIndex("Id"));

                Section section = new Section(id, name);
                sectionArrayList.add(section);
            } while (cursor.moveToNext());
        }
        return sectionArrayList;
    }

    public ArrayList<Language> getAllLanguages() {
        String query = "SELECT * FROM Language";
        ArrayList<Language> languages = new ArrayList<>();

        SQLiteDatabase dataBase = this.getReadableDatabase();
        @SuppressLint("Recycle") Cursor cursor = dataBase.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                String Name = cursor.getString(cursor.getColumnIndex("Name"));

                int Id = cursor.getInt(cursor.getColumnIndex("Id"));

                languages.add(new Language(Id, Name));
            } while (cursor.moveToNext());
        }
        return languages;

    }

    public ArrayList<Question> getAllQuestionsList(int subsectionId) {
        ArrayList<Question> questionArrayList = new ArrayList<>();


        String selectQueryQues = "SELECT * FROM Question q WHERE " + subsectionId + "= q.SubsectionId order by q.Id;";
        String selectQueryCh = "SELECT a.* FROM Answer a, Question q " +
                "WHERE q.Id = a.QuestionId and q.SubsectionId = " + subsectionId + " order by a.QuestionId;";

//        String selectQueryUniv = "SELECT * FROM Section WHERE univ_id = " + univ_id + ";";
//        //String selectQueryAns = "SELECT answer FROM Answers WHERE correct = 1;";
//        String selectTest = "SELECT name FROM sqlite_master WHERE type='table';";

        //dataBase = this.getReadableDatabase();
        SQLiteDatabase dataBase = this.getReadableDatabase();
        Log.d("DB_TAG", "isOpen = " + dataBase.isOpen());


        @SuppressLint("Recycle") Cursor cursor_question = dataBase.rawQuery(selectQueryQues, null);
        @SuppressLint("Recycle") Cursor cursor_answer = dataBase.rawQuery(selectQueryCh, null);
//        @SuppressLint("Recycle") Cursor curs_univ = dataBase.rawQuery(selectQueryUniv, null);
        //@SuppressLint("Recycle") Cursor curs_ans = db.rawQuery(selectQueryAns, null);

        // looping through all records and adding to the listQuestions
        cursor_answer.moveToFirst();
//        curs_univ.moveToFirst();
        //curs_ans.moveToFirst();


        if (cursor_question.moveToFirst()) {
            do {
                Question question = new Question();

                //String univText = curs_univ.getString(curs_univ.getColumnIndex("name"));
                //question.setUniverse(univText);

                String questText = cursor_question.getString(cursor_question.getColumnIndex("Name"));
                int languageId = cursor_question.getInt(cursor_question.getColumnIndex("LanguageId"));
                question.setQuestionText(questText);
                question.setLanguageId(languageId);
                int questionType = cursor_question.getInt(cursor_question.getColumnIndex("Type"));
                question.setQuestionType(questionType);
                String quesId = cursor_answer.getString(cursor_answer.getColumnIndex("QuestionId"));
                while (true) {

                    String quesId2 = cursor_answer.getString(cursor_answer.getColumnIndex("QuestionId"));
                    if (!quesId2.equals(quesId)) break;

                    Integer order = cursor_answer.getInt(cursor_answer.getColumnIndex("OrderLevel"));
                    String choiceText = cursor_answer.getString(cursor_answer.getColumnIndex("Name"));
                    if (order != 0) {
                        question.setChoice("order:" + order + " " + choiceText);
                    } else {
                        question.setChoice(choiceText);
                    }
                    int answerCorrect = cursor_answer.getInt(cursor_answer.getColumnIndex("IsCorrect"));
                    if (answerCorrect == 1) question.setAnswer(choiceText);

                    if (cursor_answer.isLast()) break;
                    cursor_answer.moveToNext();
                }
                questionArrayList.add(question);
                // adding to Questions listQuestions

            } while (cursor_question.moveToNext());
            //Collections.shuffle(questionArrayList);
        }
        return questionArrayList;
    }


    //Создание аккаунта и статистики
    @Override
    public void onCreate(SQLiteDatabase db) {

//        createDataBaseForced();
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
//        createDataBaseForced();
    }


    public void putToCache(String s) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(fContext);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("db_info", s);
        editor.apply();
    }

    public String loadFromCache() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(fContext);
        return settings.getString("db_info", "1.0");
    }


}