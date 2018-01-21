package com.example.android.geektest2;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {


    EditText etName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        Button btnLogin = (Button) view.findViewById(R.id.login_button);
        btnLogin.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {

        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, new MenuFragment());
        ft.commit();
    }


}
