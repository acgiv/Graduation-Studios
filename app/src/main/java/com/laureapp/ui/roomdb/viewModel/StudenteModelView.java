package com.laureapp.ui.roomdb.viewModel;
import android.content.Context;
import com.laureapp.ui.roomdb.entity.Studente;
import com.laureapp.ui.roomdb.entity.StudenteWithUtente;
import com.laureapp.ui.roomdb.repository.StudenteRepository;
import java.util.List;

public class StudenteModelView {

    private final StudenteRepository studenteRepository;

    public StudenteModelView(Context context){
        studenteRepository = new StudenteRepository(context);
    }

    public void insertStudente(Studente studente){
        studenteRepository.insertStudente(studente);
    }

    public void updateStudente(Studente studente){
        studenteRepository.updateStudente(studente);
    }

    public boolean delateStudente(long id){
        return studenteRepository.delateStudente(id);
    }

    public Studente findAllById(Long id){
        return studenteRepository.findAllById(id);
    }

    public List<Studente> getAllStudente(){
        return studenteRepository.getAllStudente();
    }

    public Long findStudente(Long id_utente) {
        return studenteRepository.findStudente(id_utente);
    }

    public Long findStudenteMatricola(Long matricola){return studenteRepository.findStudenteMatricola(matricola);}

    public List <StudenteWithUtente> findStudenteIdByUtente(){return studenteRepository.findStudenteIdByUtenteId();};
}
