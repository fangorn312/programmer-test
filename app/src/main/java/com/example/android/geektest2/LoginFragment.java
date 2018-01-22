package com.example.android.geektest2;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {


    EditText etName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        Button btnLogin = view.findViewById(R.id.login_button);
        TextView linkCreateAcc = view.findViewById(R.id.createAcc_link);

        View.OnClickListener onClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                switch (view.getId()) {
                    case R.id.login_button:
                        ft.replace(R.id.fragment_container, new MenuFragment());
                        break;
                    case R.id.createAcc_link:
                        ft.replace(R.id.fragment_container, new CreateAccountFragment());
                        ft.addToBackStack(null);
                        break;
                }
                ft.commit();
            }
        };

        linkCreateAcc.setOnClickListener(onClickListener);
        btnLogin.setOnClickListener(onClickListener);

        return view;
    }


}
