package com.example.android.programmertest;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    Account mAccount = new Account();

    EditText editTextLogin, editTextPass;
    String login;
    String password;

    String login_db;
    String password_db;

    //DBHelper mDBHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        Button btnLogin = view.findViewById(R.id.login_button);
        TextView linkCreateAcc = view.findViewById(R.id.createAcc_link);

        editTextLogin = view.findViewById(R.id.login_edit_text);
        editTextPass = view.findViewById(R.id.password_edit_text);


        View.OnClickListener onClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                switch (view.getId()) {
                    case R.id.login_button:
                        ft.replace(R.id.fragment_container, new MenuFragment());
                        /*login = editTextLogin.getText().toString();
                        password = editTextPass.getText().toString();

                        initAccount(getContext(), login, password);
                        //openBase(getContext());
                        if(checkLogin()){
                            if(checkPassword()){
                                ft.replace(R.id.fragment_container, new MenuFragment());
                                Toast.makeText(getActivity(), "Welcome!", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(getActivity(), "Неверный пароль!", Toast.LENGTH_SHORT).show();
                            }
                        }*/

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


    public void initAccount(Context context, String login, String password){
        //mDBHelper = new DBHelper(context, login, password);
        //mAccount.setLogin(mDBHelper.getAccountInfo().getLogin());
        //mAccount.setPassword(mDBHelper.getAccountInfo().getPassword());
    }

    void openBase(Context context){
        //mDBHelper = new DBHelper(context, login, password);
        //mAccount = mDBHelper.getAccountInfo();
    }

    private boolean checkLogin(){
        return (login.equals(mAccount.getLogin()));
    }

    private boolean checkPassword(){
        return (password.equals(mAccount.getPassword()));
    }

}
