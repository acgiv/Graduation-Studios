package com.laureapp.ui.roomdb.viewModel;

import android.content.Context;

import com.laureapp.ui.roomdb.entity.TaskStudente;
import com.laureapp.ui.roomdb.entity.TaskTesi;
import com.laureapp.ui.roomdb.repository.TaskStudenteRepository;
import com.laureapp.ui.roomdb.repository.TaskTesiRepository;

import java.util.List;

/**
 * Questa classe agisce come ViewModel per l'entità TaskStudente
 * Gestisce tutte le operazioni di business logic per l'entità TaskStudente,
 * comunicando con il repository associato per l'accesso al database
 */
public class TaskStudenteModelView {

    private final TaskStudenteRepository taskStudenteRepository;

    /**
     * Costruttore per TaskStudenteModelView che inizializza il repository.
     * @param context Il contesto corrente dell'applicazione.
     */
    public TaskStudenteModelView(Context context){
        taskStudenteRepository = new TaskStudenteRepository(context);
    }

    /**
     * Inserisce un'istanza di TaskStudente nel repository.
     * @param taskStudente L'istanza di TaskStudente da inserire nel repository.
     */
    public void insertTaskStudente(TaskStudente taskStudente){
        taskStudenteRepository.insertTaskStudente(taskStudente);
    }

    /**
     * Aggiorna un'istanza esistente di TaskStudente nel repository.
     * @param taskStudente L'istanza di TaskStudente da aggiornare nel repository.
     */
    public void updateTaskStudente(TaskStudente taskStudente){
        taskStudenteRepository.updateTaskStudente(taskStudente);
    }

    /**
     * Elimina un'istanza di TaskStudente dal repository tramite l'ID specificato.
     * @param id L'ID dell'istanza di TaskStudente da eliminare.
     * @return true se l'eliminazione ha avuto successo, false altrimenti.
     */
    public boolean deleteTaskStudente(Long id){
        return taskStudenteRepository.deleteTaskStudente(id);
    }

    /**
     * Trova un'istanza di TaskStudente nel repository tramite l'ID specificato.
     * @param id L'ID dell'istanza di TaskStudente da trovare.
     * @return L'istanza di TaskStudente corrispondente all'ID specificato, se presente nel repository.
     */
    public TaskStudente findAllById(Long id){
        return taskStudenteRepository.findAllById(id);
    }

    /**
     * Ottiene tutti gli elementi di TaskStudente dal repository.
     * @return Una lista di tutte le istanze di TaskStudente presenti nel repository.
     */
    public List<TaskStudente> getAllTaskStudente(){return taskStudenteRepository.getAllTaskStudente();
    }
}
