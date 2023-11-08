package com.laureapp.ui.roomdb.viewModel;

import android.content.Context;

import com.laureapp.ui.roomdb.entity.TaskTesi;
import com.laureapp.ui.roomdb.repository.TaskTesiRepository;

import java.util.List;

/**
 * Questa classe agisce come ViewModel per l'entità TaskTesi
 * Gestisce tutte le operazioni di business logic per l'entità TaskTesi,
 * comunicando con il repository associato per l'accesso al database
 */
public class TaskTesiModelView {

    private final TaskTesiRepository taskTesiRepository;

    /**
     * Costruttore per TaskTesiModelView che inizializza il repository.
     * @param context Il contesto corrente dell'applicazione.
     */
    public TaskTesiModelView(Context context){
        taskTesiRepository = new TaskTesiRepository(context);
    }

    /**
     * Inserisce un'istanza di TaskTesi nel repository.
     * @param taskTesi L'istanza di TaskTesi da inserire nel repository.
     */
    public void insertTaskTesi(TaskTesi taskTesi){
        taskTesiRepository.insertTaskTesi(taskTesi);
    }

    /**
     * Aggiorna un'istanza esistente di TaskTesi nel repository.
     * @param taskTesi L'istanza di TaskTesi da aggiornare nel repository.
     */
    public void updateTaskTesi(TaskTesi taskTesi){
        taskTesiRepository.updateTaskTesi(taskTesi);
    }

    /**
     * Elimina un'istanza di TaskTesi dal repository tramite l'ID specificato.
     * @param id L'ID dell'istanza di TaskTesi da eliminare.
     * @return true se l'eliminazione ha avuto successo, false altrimenti.
     */
    public boolean deleteTaskTesi(Long id){
        return taskTesiRepository.deleteTaskTesi(id);
    }

    /**
     * Trova un'istanza di TaskTesi nel repository tramite l'ID specificato.
     * @param id L'ID dell'istanza di TaskTesi da trovare.
     * @return L'istanza di TaskTesi corrispondente all'ID specificato, se presente nel repository.
     */
    public TaskTesi findAllById(Long id){
        return taskTesiRepository.findAllById(id);
    }

    /**
     * Ottiene tutti gli elementi di TaskTesi dal repository.
     * @return Una lista di tutte le istanze di TaskTesi presenti nel repository.
     */
    public List<TaskTesi> getAllTaskTesi(){return taskTesiRepository.getAllTaskTesi();
    }
}
