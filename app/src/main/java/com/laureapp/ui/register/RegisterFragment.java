package com.laureapp.ui.register;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.laureapp.R;

import java.util.Objects;

public class RegisterFragment extends Fragment {

    Button btnRegister;
    Button btnStudente;
    Button btnProfessore;
    Bundle bundle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnRegister = view.findViewById(R.id.button_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Registrazione
            }
        });

        btnStudente = view.findViewById(R.id.studente_register);
        bundle = new Bundle();
        btnStudente.setOnClickListener(view12 -> getChildFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.fragment_student_register, RegisterStudenteFragment.class, bundle)
                .commit());

        btnProfessore = view.findViewById(R.id.professore_register);
        btnProfessore.setOnClickListener(view1 -> {
            if( getChildFragmentManager().findFragmentById(R.id.fragment_student_register) != null) {
                getChildFragmentManager()
                        .beginTransaction()
                        .remove(Objects.requireNonNull(getChildFragmentManager()
                                .findFragmentById(R.id.fragment_student_register)))
                        .commit();
            }

        });



    }
}
