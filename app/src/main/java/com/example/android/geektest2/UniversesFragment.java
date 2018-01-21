package com.example.android.geektest2;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class UniversesFragment extends Fragment implements OnBackPressedListener {

    /*private static Logic instance;

    public static Logic instance(){
        if(instance==null){
            instance=new Logic();
        }
        return instance;
    }*/

    public static final String TAG_SELECTED_UNIV = "TAG_SELECTED_UNIV";

    private static final String TAG="UniversesFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_universes, container, false);

        Button btnLOTR = (Button) view.findViewById(R.id.lotr_btn);
        Button btnPotter =(Button) view.findViewById(R.id.potter_btn);
        Button btnDota = (Button) view.findViewById(R.id.dota_btn);
        Button btnMarvel=(Button) view.findViewById(R.id.marvel_btn);
        Button btnDC = (Button) view.findViewById(R.id.dc_btn);
        Button btnRandom = (Button) view.findViewById(R.id.random_btn);

        View.OnClickListener onClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.lotr_btn:
                        //instance.showLevelsFragment();
                        onLevelsFragment(1);
                        break;
                    case R.id.potter_btn:
                        //instance.showLevelsFragment();
                        onLevelsFragment(2);
                        break;
                    case R.id.dota_btn:
                        //instance.showLevelsFragment();
                        onLevelsFragment(3);
                        break;
                    case R.id.marvel_btn:
                        //instance.showLevelsFragment();
                        onLevelsFragment(4);
                        break;
                    case R.id.dc_btn:
                        //instance.showLevelsFragment();
                        onLevelsFragment(5);
                        break;
                    case R.id.random_btn:
                        //instance.showLevelsFragment();
                        onLevelsFragment(6);
                        break;
                }
            }
        };

        //назначить обработчик на кнопки
        btnLOTR.setOnClickListener(onClickListener);
        btnDC.setOnClickListener(onClickListener);
        btnDota.setOnClickListener(onClickListener);
        btnMarvel.setOnClickListener(onClickListener);
        btnPotter.setOnClickListener(onClickListener);
        btnRandom.setOnClickListener(onClickListener);

        return view;
    }

    private void onLevelsFragment(int univ_id){

        //Смена фрагмента один на другой

        LevelsFragment lvlFrag = new LevelsFragment();
        //QuestionFragment quesFrag = new QuestionFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TAG_SELECTED_UNIV, univ_id);
        lvlFrag.setArguments(bundle);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_container,  lvlFrag).addToBackStack(null).commit();
        /*
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, new LevelsFragment(), LEVELS);
        ft.addToBackStack(null);
        ft.commit();
*/
    }

    @Override
    public void onBackPressed() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, new MenuFragment());
        ft.commit();
    }
}
