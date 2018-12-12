package com.example.android.geektest2;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class UniversesFragment extends Fragment implements OnBackPressedListener {

    ArrayList<Universe> mUniverses = new ArrayList<>();
    ArrayList<Button> universeButtons=new ArrayList<>();
    //DBHelper mDBHelper;
    LinearLayout containerUniverse;

    public static final String TAG_SELECTED_UNIV = "TAG_SELECTED_UNIV";

    private static final String TAG="UniversesFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_universes, container, false);

        containerUniverse = view.findViewById(R.id.container_universe);

        //mDBHelper = new DBHelper(getActivity());
        //mDBHelper.createDataBase();
        //mDBHelper.openDataBase();
        mUniverses = MainActivity.instance().mDBHelper.getAllUniverseList();
        //mDBHelper.close();

        for(Universe u: mUniverses){
            Button btn = new Button(getActivity());
            btn.setText(u.getUniverse());
            btn.setId(u.getID());
            btn.setGravity(Gravity.CENTER);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            btn.setLayoutParams(lp);
            universeButtons.add(btn);
            containerUniverse.addView(btn);
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
                for(Universe u: mUniverses){
                    if (view.getId()==u.getID()){
                        onLevelsFragment(u.getID());
                        break;
                    }
                }
            }
        };

        for(Button u: universeButtons){
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

    private void onLevelsFragment(int univ_id){

        //Смена фрагмента один на другой

        CategoryFragment lvlFrag = new CategoryFragment();
        QuizInfo qi = QuizInfo.instance();
        qi.UniverseId = univ_id;

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_container,  lvlFrag).commit();
        /*
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, new CategoryFragment(), LEVELS);
        ft.addToBackStack(null);
        ft.commit();
*/
    }

    @Override
    public void onBackPressed() {
        MainActivity.instance().changeView(VPIds.MENU);
    }
}
