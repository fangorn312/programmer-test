package com.example.android.programmertest;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class SubsectionFragment extends Fragment implements OnBackPressedListener{

    private String selectedChoice;
    public static final String TAG_SELECTED_CATEGORY = "TAG_SELECTED_CATEGORY";
    public static final String TAG_SELECTED_UNIV = "TAG_SELECTED_UNIV";

    public SubsectionFragment() {
        // Required empty public constructor
    }

    private class CategoryInfo{

    }

    ArrayList<Subsection> mCategories=new ArrayList<>();
    ArrayList<Button> categoryButtons = new ArrayList<>();
    LinearLayout containerCategories;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_subsections, container, false);

        containerCategories = view.findViewById(R.id.container_category);

        //MainActivity.instance().mDBHelper.openDataBase();
        mCategories = MainActivity.instance().mDBHelper.getAllSubsectionsList(QuizInfo.instance().SectionId);
        MainActivity.instance().mDBHelper.close();
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(5,5,5,5);
        for(Subsection c: mCategories){
            Button btn = new Button(getActivity());
            btn.setTextColor(Color.parseColor("#FFFFFF"));
            btn.setBackgroundResource( R.drawable.buttonstyle);
            btn.setText(c.getName());
            btn.setGravity(Gravity.CENTER);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            btn.setLayoutParams(lp);
            containerCategories.addView(btn,layoutParams);
            categoryButtons.add(btn);
        }

        for(final Button b: categoryButtons)
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedChoice = b.getText().toString();
                for (Subsection c : mCategories){
                    if (selectedChoice.equals(c.getName())) QuizInfo.instance().SubsectionId = c.getId();
                }
                goToNextFragment();
            }
        });

        return view;
    }


    private void goToNextFragment() {
//        QuestionFragment quesFrag = new QuestionFragment();
        QuizInfo.instance().SubsectionName =selectedChoice;

        MainActivity.instance().changeView(IFragments.QUESTION);
//        FragmentManager fragmentManager = getFragmentManager();
//        fragmentManager.beginTransaction().replace(R.id.fragment_container, quesFrag).addToBackStack(null).commit();
    }

    @Override
    public void onBackPressed() {
        MainActivity.instance().changeView(IFragments.SECTION);
    }
}
