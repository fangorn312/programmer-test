package com.example.android.programmingquiz;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ResultFragment extends Fragment implements View.OnClickListener{


    public ResultFragment() {
        // Required empty public constructor

    }

//    int receiveInfoScore;
//    int receiveInfoQuantity;
//    String receiveInfoUniverse;
//    String receiveInfoLevel;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_result, container, false);

        //TextView nameInfo = view.findViewById(R.id.res_name_text);
        //nameInfo.setText("");

        TextView universeInfo = view.findViewById(R.id.res_univ_text);
        universeInfo.setText(MainActivity.instance().SectionName);

        TextView levelInfo = view.findViewById(R.id.res_level_text);
        levelInfo.setText(MainActivity.instance().SubsectionName);

        TextView quantityInfo = view.findViewById(R.id.res_total_text);
        quantityInfo.setText(String.valueOf(MainActivity.instance().QuestionsCount));

        TextView correctInfo = view.findViewById(R.id.res_correct_text);
        correctInfo.setText(String.valueOf(MainActivity.instance().Score));

        Button okButton = view.findViewById(R.id.res_ok_btn);
        okButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        MainActivity.instance().changeView(IFragments.SECTION);
//        FragmentTransaction ft = getFragmentManager().beginTransaction();
//        ft.replace(R.id.fragment_container, new SectionFragment());
//        ft.commit();
    }
}
