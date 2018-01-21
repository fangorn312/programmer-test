package com.example.android.geektest2;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;


/**
 * A simple {@link Fragment} subclass.
 */
public class LevelsFragment extends Fragment {

    RadioButton level1, level2, level3;
    private int selectedChoice;
    public static final String TAG_SELECTED_LVL = "TAG_SELECTED_LVL";
    public static final String TAG_SELECTED_UNIV = "TAG_SELECTED_UNIV";

    public LevelsFragment() {
        // Required empty public constructor
    }

    int receiveInfoUniv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_levels, container, false);

        level1 = view.findViewById(R.id.easy_radio_btn);
        level2 = view.findViewById(R.id.medium_radio_btn);
        level3 = view.findViewById(R.id.hard_radio_btn);
        //mAnswer = mQuestionLibrary.getCorrectAnswer(mQuestionNumber);


        Bundle bundle = getArguments();
        if (bundle != null) {
            //receiveInfoUniv = 1;
            receiveInfoUniv = bundle.getInt(UniversesFragment.TAG_SELECTED_UNIV);
        }
        
        Button btnNext = (Button) view.findViewById(R.id.start_test_button);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (level1.isChecked()) {
                    selectedChoice = 1;
                } else if (level2.isChecked()) {
                    selectedChoice = 2;
                } else if (level3.isChecked()) {
                    selectedChoice = 3;
                }

                if (isAllCheck()) {

                    QuestionFragment quesFrag = new QuestionFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt(TAG_SELECTED_LVL, selectedChoice);
                    bundle.putInt(TAG_SELECTED_UNIV, receiveInfoUniv);
                    quesFrag.setArguments(bundle);
                    //resultFragment.setArguments(bundle);
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.fragment_container, quesFrag).addToBackStack(null).commit();
                }
                //FragmentTransaction ft = getFragmentManager().beginTransaction();
                //ft.replace(R.id.fragment_container, new QuestionFragment(), QUESTIONS);
                //ft.addToBackStack(null);
                //ft.commit();
            }
        });

        return view;
    }

    private boolean isAllCheck(){
        return (level1.isChecked() || level2.isChecked() || level3.isChecked());
    }

}
