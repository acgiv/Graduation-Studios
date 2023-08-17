package com.laureapp.ui.login;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.textview.MaterialTextView;
import com.laureapp.R;
import com.laureapp.ui.MainActivity;

public class LoginFragment extends Fragment {

    Button btnLogin;
    Button btnHostLogin;
    MaterialTextView btnForgotPsw;
    MaterialTextView btnRegister;
    private NavController mNav;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mNav = Navigation.findNavController(view);

        btnLogin = view.findViewById(R.id.button_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Login
            }
        });

        btnRegister = view.findViewById(R.id.registrati_login);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNav.navigate(R.id.action_loginFragment_to_registerFragment);
            }
        });

        btnForgotPsw = view.findViewById(R.id.recupero_password_login);
        btnForgotPsw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Navigate to forgot psw fragment
            }
        });

        btnHostLogin = view.findViewById(R.id.ospite_login);
        btnHostLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent HomeActivity = new Intent(requireActivity(), MainActivity.class);
                startActivity(HomeActivity);
                requireActivity().finish();
            }
        });

    }
}
