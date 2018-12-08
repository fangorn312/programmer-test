package com.example.android.geektest2;

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
        changeView(VPIds.MENU);
        if (mDBHelper == null) {
            mDBHelper = new DBHelper(this);
            try {
                mDBHelper.createDataBase();
//                mDBHelper.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        instance = this;

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

    public void changeView(String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment neededFragment = null;

        switch (tag) {
            case VPIds.UNIVERSE:
                neededFragment = new UniversesFragment();
                break;
            case VPIds.QUESTION:
                neededFragment = new QuestionFragment();
                break;
            case VPIds.CATEGORY:
                neededFragment = new CategoryFragment();
                break;
            case VPIds.RESULT:
                neededFragment = new ResultFragment();
                break;
            case VPIds.LOGIN:
                neededFragment=new LoginFragment();
                break;
            case VPIds.MENU:
                neededFragment = new MenuFragment();
                break;
            default:
                break;
        }

        fragmentTransaction.replace(R.id.fragment_container, neededFragment, tag);
        fragmentTransaction.commitAllowingStateLoss();
    }

}
