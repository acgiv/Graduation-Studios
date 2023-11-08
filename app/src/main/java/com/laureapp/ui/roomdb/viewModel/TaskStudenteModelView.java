package com.laureapp.ui.roomdb.viewModel;

import android.content.Context;

import com.laureapp.ui.roomdb.entity.TaskStudente;
import com.laureapp.ui.roomdb.entity.TaskTesi;
import com.laureapp.ui.roomdb.repository.TaskStudenteRepository;
import com.laureapp.ui.roomdb.repository.TaskTesiRepository;

import java.util.List;

public class TaskStudenteModelView {

    private final TaskStudenteRepository taskStudenteRepository;

    public TaskStudenteModelView(Context context){
        taskStudenteRepository = new TaskStudenteRepository(context);
    }

    public void insertTaskStudente(TaskStudente taskStudente){
        taskStudenteRepository.insertTaskStudente(taskStudente);
    }

    public void updateTaskStudente(TaskStudente taskStudente){
        taskStudenteRepository.updateTaskStudente(taskStudente);
    }

    public boolean deleteTaskStudente(Long id){
        return taskStudenteRepository.deleteTaskStudente(id);
    }

    public TaskStudente findAllById(Long id){
        return taskStudenteRepository.findAllById(id);
    }

    public List<TaskStudente> getAllTaskStudente(){return taskStudenteRepository.getAllTaskStudente();
    }
}
