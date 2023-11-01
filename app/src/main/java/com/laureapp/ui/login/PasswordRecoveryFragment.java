package com.laureapp.ui.login;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.laureapp.R;
import com.laureapp.databinding.FragmentPasswordRecoveryBinding;
import com.laureapp.databinding.FragmentRegister2Binding;
import com.laureapp.ui.controlli.ControlInput;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

public class PasswordRecoveryFragment extends Fragment {

    private NavController mNav;
    private FirebaseAuth auth;
    private final int error_color = com.google.android.material.R.color.design_default_color_error;
    private Context context;
    FragmentPasswordRecoveryBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = requireContext();
        binding = FragmentPasswordRecoveryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        auth = FirebaseAuth.getInstance();
        mNav = Navigation.findNavController(view);
        binding.btnSendEmail.setOnClickListener(view1 -> {
            String email = StringUtils.trim(Objects.requireNonNull(binding.emailRegister.getText()).toString());
            if(!StringUtils.isEmpty(email)) {
                auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    // Email di recupero inviata con successo
                                    ControlInput.set_error(binding.emailInput, false, "", R.color.color_primary, context, R.dimen.input_text_layout_height);
                                    mNav.navigate(R.id.action_passwordRecoveryFragment_to_loginFragment);
                                } else {
                                    String error_message = "Errore nell'invio dell'email di recupero";
                                    ControlInput.set_error(binding.emailInput, true, error_message, error_color, context, R.dimen.input_text_layout_height_error_email);
                                }
                            }
                        });
            }else{
                String error_message = getString(R.string.errore_campo_vuoto).replace("{campo}", getString(R.string.email));
                ControlInput.set_error(binding.emailInput, true, error_message, error_color, context, R.dimen.input_text_layout_height_error_email);
            }
        });

    }
}