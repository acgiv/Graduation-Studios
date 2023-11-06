package com.laureapp.ui.card.Adapter;

import static com.laureapp.ui.card.Task.TaskTesiFragment.deleteTask;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;

import com.laureapp.R;
import com.laureapp.ui.roomdb.entity.TaskTesi;

import java.util.List;

/**
 * Questo adapter fornisce l'accesso ai data Item. In questo caso,
 * ai ListItem della ListView e si occupare di settare graficamente
 * gli elementi della lista
 */
public class TaskTesiAdapter extends ArrayAdapter<TaskTesi> {
    private final LayoutInflater inflater;
    private final List<TaskTesi> taskList;
    private NavController mNav;
    private Context context;



    /**
     * Costruisce un nuovo adapter per la visualizzazione della lista di task associati a tesi.
     *
     * @param context  si riferisce al contesto in cui viene utilizzato
     * @param taskList corrisponde alla lista di task da passare
     */
    public TaskTesiAdapter(Context context, List<TaskTesi> taskList, NavController navController) {
        super(context, 0, taskList);
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.taskList = taskList;
        this.mNav = navController;  // Inizializza il NavController

    }

}



