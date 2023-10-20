package com.laureapp.ui.roomdb.viewModel;

import android.content.Context;

import com.laureapp.ui.roomdb.entity.TaskTesi;
import com.laureapp.ui.roomdb.repository.TaskTesiRepository;

import java.util.List;

public class TaskTesiModelView {

    private final TaskTesiRepository taskTesiRepository;

    public TaskTesiModelView(Context context){
        taskTesiRepository = new TaskTesiRepository(context);
    }

    public void insertTaskTesi(TaskTesi taskTesi){
        taskTesiRepository.insertTaskTesi(taskTesi);
    }

    public void updateTaskTesi(TaskTesi taskTesi){
        taskTesiRepository.updateTaskTesi(taskTesi);
    }

    public boolean deleteTaskTesi(Long id){
        return taskTesiRepository.deleteTaskTesi(id);
    }

    public TaskTesi findAllById(Long id){
        return taskTesiRepository.findAllById(id);
    }

    public List<TaskTesi> getAllTaskTesi(){return taskTesiRepository.getAllTaskTesi();
    }
}
