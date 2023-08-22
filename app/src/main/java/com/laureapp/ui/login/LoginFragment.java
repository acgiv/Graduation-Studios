package com.laureapp.ui.login;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import org.apache.commons.lang3.StringUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.room.Update;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.laureapp.R;
import com.laureapp.ui.MainActivity;
import com.laureapp.ui.controlli.ControlInput;
import com.laureapp.ui.roomdb.entity.Utente;
import com.laureapp.ui.roomdb.viewModel.UtenteModelView;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;

public class LoginFragment extends Fragment {

    Button btnLogin;
    Button btnHostLogin;
    MaterialTextView btnForgotPsw;
    MaterialTextView btnRegister;
    TextInputLayout password_layout;
    TextInputLayout email_layout;
    TextInputEditText password_text;
    TextInputEditText email_text;
    MaterialTextView error_text;
    Context context ;
    private NavController mNav;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        context = requireContext();
        return inflater.inflate(R.layout.fragment_login, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mNav = Navigation.findNavController(view);

        btnLogin = view.findViewById(R.id.button_login);
        email_layout  =  view.findViewById(R.id.email_input);
        password_layout = view.findViewById(R.id.password_input);
        email_text = view.findViewById(R.id.email_login);
        password_text = view.findViewById(R.id.password_login);
        error_text = view.findViewById(R.id.error_text);
        int error_color = com.google.android.material.R.color.design_default_color_error;
        Resources resources = getResources();
        UtenteModelView utenteView = new UtenteModelView(context);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view) {
                if(email_layout != null && password_layout != null){
                    HashMap<String, Boolean> result = is_correct_email_password(error_color);
                    if (Boolean.TRUE.equals(result.get("email")) && Boolean.TRUE.equals(result.get("password"))){
                        boolean result_query = utenteView.is_exist_email_password(String.valueOf(email_text.getText()),hashWith256(String.valueOf(password_text.getText())));
                        if (Boolean.FALSE.equals(result_query)){
                            error_text.setVisibility(View.VISIBLE);
                        }else{
                            error_text.setVisibility(View.GONE);
                            Intent HomeActivity = new Intent(requireActivity(), MainActivity.class);
                            startActivity(HomeActivity);
                            requireActivity().finish();
                        }
                    }
                }
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

    private HashMap<String,Boolean> is_correct_email_password(int error_color){
        HashMap<String,Boolean> result_error = new HashMap<String,Boolean>();
        if(StringUtils.isEmpty(String.valueOf(email_text.getText()))){
            String error_message = getString(R.string.errore_campo_vuoto).replace("{campo}", "Email");
            set_error(email_layout, true, error_message, error_color ,R.dimen.input_text_layout_height_error);
            email_text.requestFocus();
            result_error.put("Email", false);
        // controllo che il campo email non sia valido
        }else if (!ControlInput.isValidEmailFormat(String.valueOf(email_text.getText()))) {
            String error_message = getString(R.string.errore_email);
            set_error(email_layout, true, error_message, error_color ,R.dimen.input_text_layout_height_error);
            result_error.put("email", false);;
        }else{
            // cancello i messaggi di errore sul campo email
            set_error(email_layout, false, "", R.color.color_primary ,R.dimen.input_text_layout_height);
            result_error.put("email", true);
        }
        // controllo che il campo password non sia vuoto
        if( StringUtils.isEmpty(String.valueOf(password_text.getText()))){
            String error_message = getString(R.string.errore_campo_vuoto).replace("{campo}", "Password");
            set_error(password_layout, true, error_message, error_color ,R.dimen.input_text_layout_height_error);
            result_error.put("password", false);
            if (StringUtils.isNoneEmpty(String.valueOf(email_text.getText()))){
                password_text.requestFocus();
            }
        }else{
            // cancello i messaggi di errore sul campo password
            set_error(password_layout, false, "", R.color.color_primary ,R.dimen.input_text_layout_height);
            result_error.put("password", true);
        }

        return result_error;
    }

    private String hashWith256(String textToHash) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] byteOfTextToHash = textToHash.getBytes(StandardCharsets.UTF_8);
            byte[] hashedByetArray = digest.digest(byteOfTextToHash);
            return Base64.getEncoder().encodeToString(hashedByetArray);
        }catch (NoSuchAlgorithmException e){
            Log.e("LaureApp", "Error reading file", e);;
        }
        return "";
    }

    private void set_error(TextInputLayout inputLayout, boolean value_error, String error, int color, int dimension){
        inputLayout.setErrorEnabled(value_error);
        if (StringUtils.isEmpty(error))
            inputLayout.setError(null);
        else
            inputLayout.setError(error);
        set_color_error(inputLayout, color );
        change_width(inputLayout, dimension_text(dimension));
    }

    private void set_color_error(TextInputLayout inputLayout, int id_color){
        int errorTextColor = ContextCompat.getColor( context, id_color);
        ColorStateList errorColorStateList = ColorStateList.valueOf(errorTextColor);
        inputLayout.setErrorTextColor(errorColorStateList);
        inputLayout.setHintTextColor(errorColorStateList);
    }

    private int dimension_text(int id_dimension){
        Resources resources = getResources();
        return (int) (resources.getDimensionPixelSize(id_dimension)/ getResources().getDisplayMetrics().density);
    }

    private void change_width(TextInputLayout input_layout, int dimension){
        float density = getResources().getDisplayMetrics().density;
        int desiredHeightInPixels = (int) (dimension * density);
        ViewGroup.LayoutParams layoutParams = input_layout.getLayoutParams();
        layoutParams.height = desiredHeightInPixels;
        input_layout.setLayoutParams(layoutParams);
    }


}
