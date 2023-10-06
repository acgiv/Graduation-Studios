package com.laureapp.ui.card.Task;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.firestore.FirebaseFirestore;
import com.laureapp.R;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DettagliTesistaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DettagliTesistaFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    String ruolo;
    Context context;
    Bundle args;
    private NavController mNav;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DettagliTesistaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DettagliTesista.
     */
    // TODO: Rename and change types and number of parameters
    public static DettagliTesistaFragment newInstance(String param1, String param2) {
        DettagliTesistaFragment fragment = new DettagliTesistaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dettagli_tesista, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
        mNav = Navigation.findNavController(view);
        Button taskButton = view.findViewById(R.id.button_visualizza_task_tesista);
        mNav = Navigation.findNavController(view);
        Button deleteButton = view.findViewById(R.id.button_elimina_tesista);


        taskButton.setOnClickListener(view1 -> {
            mNav.navigate(R.id.action_dettagli_tesista_to_task);
        });

        deleteButton.setOnClickListener(view1 ->{
        });
    }
}