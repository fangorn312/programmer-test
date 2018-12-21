package com.example.android.geektest2;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;


public class UniversesFragment extends Fragment implements OnBackPressedListener {

    ArrayList<Universe> mUniverses = new ArrayList<>();
    ArrayList<Button> universeButtons = new ArrayList<>();
    //DBHelper mDBHelper;
    LinearLayout containerUniverse;

    public static final String TAG_SELECTED_UNIV = "TAG_SELECTED_UNIV";

    private static final String TAG = "UniversesFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_universes, container, false);

        containerUniverse = view.findViewById(R.id.container_universe);

        //mDBHelper = new DBHelper(getActivity());
        //mDBHelper.createDataBase();
        //mDBHelper.openDataBase();
        mUniverses = MainActivity.instance().mDBHelper.getAllUniverseList();
        //mDBHelper.close();
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(5,5,5,5);
        for (Universe u : mUniverses) {
            Button btn = new Button(getActivity());
            btn.setTextColor(Color.parseColor("#FFFFFF"));
            btn.setBackgroundResource( R.drawable.buttonstyle);


            btn.setText(u.getUniverse());
            btn.setId(u.getID());
            btn.setGravity(Gravity.CENTER);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            btn.setLayoutParams(lp);
            universeButtons.add(btn);
            containerUniverse.addView(btn, layoutParams);
        }

//        Button btnLOTR = (Button) view.findViewById(R.id.lotr_btn);
//        Button btnPotter =(Button) view.findViewById(R.id.potter_btn);
//        Button btnDota = (Button) view.findViewById(R.id.dota_btn);
//        Button btnMarvel=(Button) view.findViewById(R.id.marvel_btn);
//        Button btnDC = (Button) view.findViewById(R.id.dc_btn);
//        Button btnRandom = (Button) view.findViewById(R.id.random_btn);

        View.OnClickListener onClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                for (Universe u : mUniverses) {
                    if (view.getId() == u.getID()) {
                        QuizInfo.instance().UniverseName = u.getUniverse();
                        QuizInfo.instance().UniverseId = u.getID();
                        goToCategoryFragment();
                        break;
                    }
                }
            }
        };

        for (Button u : universeButtons) {
            u.setOnClickListener(onClickListener);
        }

        //назначить обработчик на кнопки
//        btnLOTR.setOnClickListener(onClickListener);
//        btnDC.setOnClickListener(onClickListener);
//        btnDota.setOnClickListener(onClickListener);
//        btnMarvel.setOnClickListener(onClickListener);
//        btnPotter.setOnClickListener(onClickListener);
//        btnRandom.setOnClickListener(onClickListener);

        return view;
    }

    private void goToCategoryFragment() {

        //Смена фрагмента один на другой
        MainActivity.instance().changeView(VPIds.CATEGORY);

//        CategoryFragment categoryFrag = new CategoryFragment();
//        qi.UniverseId = univ_id;
//
//        FragmentManager fragmentManager = getFragmentManager();
//        fragmentManager.beginTransaction().replace(R.id.fragment_container,  categoryFrag).commit();
    }

    @Override
    public void onBackPressed() {
        MainActivity.instance().changeView(VPIds.MENU);
    }
}
