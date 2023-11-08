package com.laureapp.ui.roomdb.viewModel;
import android.content.Context;
import com.laureapp.ui.roomdb.entity.Studente;
import com.laureapp.ui.roomdb.entity.StudenteWithUtente;
import com.laureapp.ui.roomdb.repository.StudenteRepository;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Questa interfaccia fornisce metodi per eseguire operazioni CRUD (create, read, update, delete)
 * sull'entità Professore nel database Room
 */
public class StudenteModelView {

    private final StudenteRepository studenteRepository;

    /**
     * Costruttore della classe StudenteRepository
     * @param context Il contesto dell'applicazione
     */
    public StudenteModelView(Context context){
        studenteRepository = new StudenteRepository(context);
    }

    /**
     * Inserisce un oggetto Studente nel database
     * @param studente L'oggetto Profossore da inserire
     */
    public void insertStudente(Studente studente){
        studenteRepository.insertStudente(studente);
        studente.setId_studente(findStudenteMatricola(studente.getMatricola()));
    }
    /**
     * Aggiorna un oggetto Studente nel database
     * @param studente L'oggetto Studente da aggiornare
     */
    public void updateStudente(Studente studente){
        studenteRepository.updateStudente(studente);
    }

    /**
     * Elimina un oggetto Studente dal database in base all'ID specificato
     * @param id dell'oggetto Studente da eliminare
     * @return True se l'eliminazione ha avuto successo, altrimenti False
     */
    public boolean delateStudente(long id){
        return studenteRepository.deleteStudente(id);
    }

    /**
     * Trova un oggetto Studente nel database in base all'ID specificato
     * @param id L'ID dell'oggetto Studente da trovare
     * @return L'oggetto Studente corrispondente all'ID specificato, se presente, altrimenti un nuovo oggetto Professore vuoto
    */
    public Studente findAllById(Long id){
        return studenteRepository.findAllById(id);
    }

    /**
     * Ottiene tutti gli oggetti Studente presenti nel database
     * @return lista di tutti gli oggetti Studente presenti nel database
     */
    public List<Studente> getAllStudente(){
        return studenteRepository.getAllStudente();
    }

    /**
     * Trova un oggetto Studente nel database in base all'ID utente
     * @param id_utente L'ID dell'oggetto Studente da trovare
     * @return L'oggetto Studente corrispondente all'ID specificato, se presente, altrimenti un nuovo oggetto Professore vuoto
     */
    public Long findStudente(Long id_utente) {
        return studenteRepository.findStudente(id_utente);
    }

    /**
     * Trova un oggetto Studente nel database in base alla matricola specificato
     * @param matricola La matricola dell'oggetto Studente da trovare
     * @return L'oggetto Studente corrispondente alla matricola, se presente, altrimenti un nuovo oggetto Professore vuoto
     */
    public Long findStudenteMatricola(Long matricola){return studenteRepository.findStudenteMatricola(matricola);}

    /**
     * Recupera la matricola dello studente in base all'ID studente specificato nel database.
     * @param id_studente L'ID dello studente
     * @return La matricola dello studente associato all'ID specificato, -1L in caso di errore
     */
    public Long getMatricola(Long id_studente){return studenteRepository.getMatricola(id_studente);}

    /**
     * Recupera l'ID dello studente in base all'ID dell'utente associato nel database.
     * @return Una lista di oggetti StudenteWithUtente che rappresentano gli studenti con gli utenti associati nel database
     */
    public List <StudenteWithUtente> findStudenteIdByUtente(){return studenteRepository.findStudenteIdByUtenteId();};
}
