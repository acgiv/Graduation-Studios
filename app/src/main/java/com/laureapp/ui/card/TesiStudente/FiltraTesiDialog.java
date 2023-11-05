package com.laureapp.ui.card.TesiStudente;

import android.app.Dialog;
        import android.content.Context;
        import android.os.Bundle;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;

        import androidx.annotation.NonNull;
        import androidx.fragment.app.DialogFragment;
        import androidx.fragment.app.Fragment;
        import androidx.fragment.app.FragmentManager;

        import com.laureapp.R;

public class FiltraTesiDialog extends DialogFragment {

    public FiltraTesiDialog() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_popup_filtra_tesi, container, false);
        return view;

    }

}
