package com.example.android.geektest2;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends Fragment implements View.OnClickListener, OnBackPressedListener {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        Button btnChoiceUniv = (Button) view.findViewById(R.id.select_univ_btn_view);
        btnChoiceUniv.setOnClickListener(this);

        Button btnStats=(Button) view.findViewById(R.id.stats_btn);
        btnStats.setOnClickListener(this);

        return view;
    }


    @Override
    public void onClick(View view) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        switch (view.getId()) {
            case R.id.select_univ_btn_view:
                ft.replace(R.id.fragment_container, new UniversesFragment());
                break;
            case R.id.stats_btn:
                ft.replace(R.id.fragment_container, new StatisticsFragment());
                ft.addToBackStack(null);
                break;
        }
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Предупреждение");
        builder.setMessage("Вы действительно хотите выйти из приложения?");
        //.setIcon(R.drawable.ic_android_cat)
        builder.setCancelable(false);
        builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                getActivity().finish();
            }
        });
        builder.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}
