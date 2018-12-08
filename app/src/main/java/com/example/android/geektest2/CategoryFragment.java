package com.example.android.geektest2;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.io.IOException;
import java.sql.BatchUpdateException;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment implements OnBackPressedListener{

    private String selectedChoice;
    public static final String TAG_SELECTED_CATEGORY = "TAG_SELECTED_CATEGORY";
    public static final String TAG_SELECTED_UNIV = "TAG_SELECTED_UNIV";

    public CategoryFragment() {
        // Required empty public constructor
    }

    ArrayList<String> mCategories=new ArrayList<>();
    ArrayList<Button> categoryButtons = new ArrayList<>();
    LinearLayout containerCategories;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_categories, container, false);

        containerCategories = view.findViewById(R.id.container_category);

        try {
            MainActivity.instance().mDBHelper.openDataBase();
            mCategories = MainActivity.instance().mDBHelper.getAllCategoryList(QuizInfo.instance().UniverseId);
            MainActivity.instance().mDBHelper.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        for(String s: mCategories){
            Button btn = new Button(getActivity());
            btn.setText(s);
            btn.setGravity(Gravity.CENTER);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            btn.setLayoutParams(lp);
            containerCategories.addView(btn);
            categoryButtons.add(btn);
        }

        for(final Button b: categoryButtons)
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedChoice = b.getText().toString();
                goToNextFragment();
            }
        });

        return view;
    }


    private void goToNextFragment() {
//        QuestionFragment quesFrag = new QuestionFragment();
        QuizInfo.instance().Category=selectedChoice;

        MainActivity.instance().changeView(VPIds.QUESTION);
//        FragmentManager fragmentManager = getFragmentManager();
//        fragmentManager.beginTransaction().replace(R.id.fragment_container, quesFrag).addToBackStack(null).commit();
    }

    @Override
    public void onBackPressed() {
        MainActivity.instance().changeView(VPIds.UNIVERSE);
    }
}
