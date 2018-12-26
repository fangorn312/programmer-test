package com.example.android.programmertest;


import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class AboutAppFragment extends Fragment implements OnBackPressedListener {


    public AboutAppFragment() {
        // Required empty public constructor
    }
    @Override
    public void onBackPressed() {
        MainActivity.instance().changeView(IFragments.SETTINGS);
    }

    TextView dbTextView;
    TextView appTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about_app, container, false);

        dbTextView = view.findViewById(R.id.info_database);
        appTextView = view.findViewById(R.id.info_appVersion);

        dbTextView.setText(MainActivity.instance().mDBHelper.loadFromCache());

        try {
            PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            String version = pInfo.versionName;
            appTextView.setText(version);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return view;
    }

}
