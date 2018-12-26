package com.example.android.programmertest;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import java.io.IOException;

public class MainActivity extends FragmentActivity {

    private static MainActivity instance;

    public DBHelper mDBHelper;

    public static MainActivity instance() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        if (findViewById(R.id.fragment_container) != null) {
//            // However, if we're being restored from a previous state,
//            // then we don't need to do anything and should return or else
//            // we could end up with overlapping fragments.
//            if (savedInstanceState != null) {
//                return;
//            }
//            // Create a new Fragment to be placed in the activity layout
//            LoginFragment loginFragment = new LoginFragment();
//            // In case this activity was started with special instructions from an
//            // Intent, pass the Intent's extras to the fragment as arguments
//            loginFragment.setArguments(getIntent().getExtras());
//            // Add the fragment to the 'fragment_container' FrameLayout
//            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, loginFragment).commit();
//        }
        changeView(IFragments.MENU);


        instance = this;

    }

    @Override
    protected void onResume() {
        if (mDBHelper == null) {
            mDBHelper = new DBHelper(this);
        }
        try {
//            deleteDatabase(DBHelper.DB_NAME);
            mDBHelper.createDataBase();
            mDBHelper.openDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }

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
            case IFragments.LOGIN:
                neededFragment=new LoginFragment();
                break;
            case IFragments.MENU:
                neededFragment = new MenuFragment();
                break;
            case IFragments.SETTINGS:
                neededFragment = new SettingsFragment();
                break;
            case IFragments.UPDATE:
                neededFragment = new UpdateFragment();
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
