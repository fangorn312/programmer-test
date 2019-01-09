package com.example.android.programmingquiz;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import java.io.IOException;

public class MainActivity extends FragmentActivity {

    private static MainActivity instance;

    public int SectionId;
    public String SectionName;
    public String SubsectionName;
    public int SubsectionId;
    public int Score;
    public int QuestionsCount;
    public DBHelper mDBHelper;

    public static MainActivity instance() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        changeView(IFragments.MENU);

        SectionName ="";
        SectionId =0;
        SubsectionName ="";
        Score=0;
        SubsectionId =0;
        QuestionsCount=0;

        instance = this;

    }

    @Override
    protected void onResume() {
        if (mDBHelper == null) {
            mDBHelper = new DBHelper(this);
        }
        mDBHelper.createDataBase();
        mDBHelper.openDataBase();

        super.onResume();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        OnBackPressedListener backPressedListener = null;
        for (Fragment fragment : fm.getFragments()) {
            if (fragment instanceof OnBackPressedListener) {
                backPressedListener = (OnBackPressedListener) fragment;
                break;
            }
        }

        if (backPressedListener != null) {
            backPressedListener.onBackPressed();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStop() {
        mDBHelper.close();
        super.onStop();
    }

    public void changeView(String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment neededFragment = null;


        switch (tag) {
            case IFragments.SECTION:
                neededFragment = new SectionFragment();
                break;
            case IFragments.QUESTION:
                neededFragment = new QuestionFragment();
                break;
            case IFragments.SUBSECTION:
                neededFragment = new SubsectionFragment();
                break;
            case IFragments.RESULT:
                neededFragment = new ResultFragment();
                break;
            case IFragments.MENU:
                neededFragment = new MenuFragment();
                break;
            case IFragments.SETTINGS:
                neededFragment = new SettingsFragment();
                break;
            case IFragments.ABOUT:
                neededFragment=new AboutAppFragment();
                break;
            default:
                break;
        }
        fragmentTransaction.replace(R.id.fragment_container, neededFragment, tag);
        fragmentTransaction.commitAllowingStateLoss();
    }

}
