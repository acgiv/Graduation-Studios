package com.laureapp.ui.register;

import static android.content.Intent.getIntent;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.laureapp.R;
import com.laureapp.databinding.FragmentRegister2Binding;
import com.laureapp.ui.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class Register2Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    AutoCompleteTextView autoCompleteTextView;
    AutoCompleteTextView autoCompleteTextViewcorso;
    FragmentRegister2Binding binding;
    Bundle bundle;
    private NavController mNav;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRegister2Binding.inflate(inflater, container, false);
        // Inizializza autoCompleteTextView utilizzando il binding
        autoCompleteTextView = binding.filledExposedDropdown;
        autoCompleteTextViewcorso = binding.dropdownCorso;

        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mNav = Navigation.findNavController(view);
        String[] dipartimento =  getResources().getStringArray(R.array.Dipartimento);
        String[] corsi =  getResources().getStringArray(R.array.Corsi);
        // create an array adapter and pass the required parameter
        // in our case pass the context, drop down layout , and array.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.dropdown_item, dipartimento);
        autoCompleteTextView.setAdapter(adapter);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(getContext(), R.layout.dropdown_item, corsi);
        autoCompleteTextViewcorso.setAdapter(adapter2);

        //to get selected value add item click listener
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(), "" + autoCompleteTextView.getText().toString(), Toast.LENGTH_SHORT).show();

            }
        });

        autoCompleteTextViewcorso.setOnItemClickListener((parent, view12, position, id) -> Toast.makeText(getContext(), "" + autoCompleteTextViewcorso.getText().toString(), Toast.LENGTH_SHORT).show());

        bundle = new Bundle();
        binding.buttonRegister.setOnClickListener(view1 -> {
            Intent HomeActivity = new Intent(requireActivity(), MainActivity.class);
            bundle.putString("ruolo", "Studente");
            HomeActivity.putExtras(bundle);
            startActivity(HomeActivity);
            requireActivity().finish();
        });
           /*case "Matricola":
        if (!ControlInput.is_correct_matricola(Objects.requireNonNull(binding.matricolaRegister.getText()).toString())) {
            String error_message = getString(R.string.errore_matricola).replace("{campo}", getString(R.string.matricola));
            ControlInput.set_error(binding.matricolaInput, true, error_message, error_color, context, R.dimen.input_text_layout_height_error, getResources());
        } else {
            ControlInput.set_error(binding.matricolaInput, false, "", R.color.color_primary, context, R.dimen.input_text_layout_height, getResources());
            return true;
        }
        break;*/
    }
}