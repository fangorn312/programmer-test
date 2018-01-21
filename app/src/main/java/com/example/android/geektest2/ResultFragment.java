package com.example.android.geektest2;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ResultFragment extends Fragment implements View.OnClickListener {


    public ResultFragment() {
        // Required empty public constructor
    }

    int receiveInfoScore;
    int receiveInfoQuantity;
    String receiveInfoUniverse;
    String receiveInfoLevel;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_result, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            receiveInfoScore = bundle.getInt(QuestionFragment.TAG_SCORE);
            receiveInfoQuantity = bundle.getInt(QuestionFragment.TAG_QUANTITY);

            int lvl = bundle.getInt(QuestionFragment.TAG_LEVEL);
            switch (lvl){
                case 1: receiveInfoLevel = "Easy"; break;
                case 2: receiveInfoLevel = "Medium"; break;
                case 3: receiveInfoLevel = "Hard"; break;
                default: receiveInfoLevel = "Error"; break;
            }

            receiveInfoUniverse = bundle.getString(QuestionFragment.TAG_UNIVERSE);
        }


        TextView nameInfo = view.findViewById(R.id.res_name_text);
        nameInfo.setText("");

        TextView universeInfo = view.findViewById(R.id.res_univ_text);
        universeInfo.setText(""+receiveInfoUniverse);

        TextView levelInfo = view.findViewById(R.id.res_level_text);
        levelInfo.setText(""+receiveInfoLevel);

        TextView quantityInfo = view.findViewById(R.id.res_total_text);
        quantityInfo.setText(""+receiveInfoQuantity);

        TextView correctInfo = view.findViewById(R.id.res_correct_text);
        correctInfo.setText(""+receiveInfoScore);


        Button okButton = view.findViewById(R.id.res_ok_btn);
        okButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, new UniversesFragment());
        ft.commit();
    }

}
