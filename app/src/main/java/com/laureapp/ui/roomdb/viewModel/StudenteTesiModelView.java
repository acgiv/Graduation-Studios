package com.laureapp.ui.roomdb.viewModel;

import android.content.Context;

import com.laureapp.ui.roomdb.RoomDbSqlLite;
import com.laureapp.ui.roomdb.entity.StudenteTesi;
import com.laureapp.ui.roomdb.repository.StudenteTesiRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Questa classe agisce come ViewModel per l'entità StudenteTesi
 * Gestisce tutte le operazioni di business logic per l'entità StudenteTesi,
 * comunicando con il repository associato per l'accesso al database
 */
public class StudenteTesiModelView {

    private final StudenteTesiRepository studenteTesiRepository;

    /**
     * Costruttore della classe StudentiModelView
     * @param context Il contesto dell'applicazione
     */
    public StudenteTesiModelView(Context context){
        studenteTesiRepository = new StudenteTesiRepository(context);
    }

    /**
     * Inserisce un oggetto StudenteTesi nel database
     * @param studenteTesi L'oggetto StudenteTesi da inserire
     */
    public void insertStudenteTesi(StudenteTesi studenteTesi){
        studenteTesiRepository.insertStudenteTesi(studenteTesi);
    }

    /**
     * Aggiorna un oggetto StudenteTesi nel database
     * @param studenteTesi L'oggetto StudenteTesi da aggiornare
     */
    public void updateStudenteTesi(StudenteTesi studenteTesi){
        studenteTesiRepository.updateStudenteTesi(studenteTesi);
    }

    /**
     * Elimina un oggetto FileTesi dal database in base all'ID specificato
     * @param id dell'oggetto FileTesi da eliminare
     * @return True se l'eliminazione ha avuto successo, altrimenti False
     */
    public boolean deleteStudenteTesi(long id){
        return studenteTesiRepository.delateStudenteTesi(id);
    }

    /**
     * Trova StudenteTesi in base all'ID specificato.
     * @param id l'ID specificato
     * @return l'istanza di StudenteTesi corrispondente all'ID specificato, un'istanza vuota se non viene trovata alcuna corrispondenza o se si verifica un'eccezione
     */
    public StudenteTesi findAllById(Long id){
        return studenteTesiRepository.findAllById(id);
    }

    /**
     * Ottiene tutti gli oggetti StudenteTesi dal database.
     * @return una lista di oggetti StudenteTesi, una lista vuota se si verifica un'eccezione
     */
    public List<StudenteTesi> getAllStudenteTesi(){
        return studenteTesiRepository.getAllStudenteTesi();
    }

    /**
     * Ottiene l'ID della tesi dal database in base all'ID dello studente associato.
     * @param id l'ID dello studente
     * @return l'ID della tesi
     */
    public Long getIdTesiFromIdStudente(Long id){
        return studenteTesiRepository.getIdTesiFromIdStudente(id);
    }

    /**
     * Ottiene l'ID della tesi dal database in base all'ID dello studente associato.
     * @param idStudente l'ID dello studente
     * @return l'ID della tesi
     */
    public Long findIdTesiByIdStudente(Long idStudente){
        return studenteTesiRepository.findIdTesiByIdStudente(idStudente);
    }



}
