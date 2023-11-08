package com.laureapp.ui.controlli;

import android.net.ConnectivityManager;

import android.content.Context;
import android.content.res.ColorStateList;

import android.net.Network;
import android.net.NetworkCapabilities;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.laureapp.R;
import com.laureapp.ui.roomdb.viewModel.ProfessoreModelView;
import com.laureapp.ui.roomdb.viewModel.StudenteModelView;
import com.laureapp.ui.roomdb.viewModel.UtenteModelView;

import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Questa classe fornisce metodi utili per il controllo e la validazione dell'input dell'utente.
 */
public class ControlInput {

    private static final int error_color = com.google.android.material.R.color.design_default_color_error;

    /**
     * Verifica se una stringa rappresenta un indirizzo email valido secondo un modello specifico.
     *
     * @param email la stringa da verificare come indirizzo email.
     * @return true se la stringa è un indirizzo email valido, false altrimenti.
     */
    public static boolean  isValidEmailFormat(String email) {
        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        return Pattern.compile(regexPattern).matcher(email).matches();
    }

    /**
     * Rimuove gli spazi bianchi, le tabulazioni e le interruzioni di riga da una stringa data.
     *
     * @param stringa la stringa dalla quale rimuovere gli spazi bianchi, le tabulazioni e le interruzioni di riga.
     * @return la stringa risultante dopo la rimozione degli spazi bianchi, delle tabulazioni e delle interruzioni di riga.
     */
    public static String deleteBlank(String stringa){
        stringa = stringa.replaceAll("\t", "");
        stringa = stringa.replaceAll("\n", "");
        stringa = stringa.replaceAll(" ", "");
        return stringa;
    }

    /**
     * Rimuove i caratteri numerici da una stringa data.
     *
     * @param stringa la stringa dalla quale rimuovere i caratteri numerici.
     * @return la stringa risultante dopo la rimozione dei caratteri numerici.
     */
    public static String deleteNumber(String stringa){
        stringa = stringa.replaceAll("[0-9]", "");
        return stringa;
    }

    /**
     * Rimuove i caratteri di punteggiatura da una stringa data.
     *
     * @param stringa la stringa dalla quale rimuovere i caratteri di punteggiatura.
     * @return la stringa risultante dopo la rimozione dei caratteri di punteggiatura.
     */
    public static String deletePunctuation(String stringa){
        String regexPattern = "[!\\\"#$%&'()*+,-./:;<=>?@\\\\[\\\\]^_`{|}~]";
        stringa =  stringa.replaceAll(regexPattern, "");
        return stringa;
    }

    /**
     * Verifica se una password soddisfa i requisiti di sicurezza.
     *
     * @param password la password da verificare.
     * @return true se la password soddisfa i requisiti di sicurezza (8 caratteri, almeno un numero e almeno un carattere speciale), altrimenti false.
     */
    public static boolean isPasswordSafe(String password) {
        password = deleteBlank(password);

        //controllo i requisiti di sicurezza della password
        //8 caratteri, un numero e un carattere speciale
        Pattern special = Pattern.compile("[^a-z0-9]", Pattern.CASE_INSENSITIVE);
        Pattern number = Pattern.compile("[0-9]", Pattern.CASE_INSENSITIVE);
        Pattern chars = Pattern.compile("[a-z]", Pattern.CASE_INSENSITIVE);

        Matcher matcherSpecial = special.matcher(password);
        Matcher matcherNumber = number.matcher(password);
        Matcher matcherChars = chars.matcher(password);

        int charsNumber = 0;
        boolean containsNumber = false;
        boolean containsSpecial = false;

        if(matcherSpecial.find()){
            containsSpecial = true;
            charsNumber++;
        }

        if(matcherNumber.find()){
            containsNumber = true;
            charsNumber++;
        }

        while (matcherChars.find() || matcherNumber.find() || matcherSpecial.find()) {
            charsNumber++;
        }

        return containsNumber && containsSpecial && charsNumber >= 8;
    }

    /**
     * Imposta lo stato di errore, il testo dell'errore, il colore del testo dell'errore e la larghezza di un TextInputLayout.
     *
     * @param inputLayout Il TextInputLayout per il quale impostare lo stato di errore e altre proprietà.
     * @param value_error True se lo stato di errore deve essere abilitato, False altrimenti.
     * @param error Il testo dell'errore da visualizzare, o null se nessun errore è da visualizzare.
     * @param color L'ID del colore delle risorse da utilizzare per il testo dell'errore.
     * @param context Il contesto dell'applicazione.
     * @param dimension L'ID della dimensione delle risorse da utilizzare per la larghezza.
     */
    public static void set_error(TextInputLayout inputLayout, boolean value_error, String error, int color, Context context, int dimension) {
        // Imposta lo stato di errore del TextInputLayout
        inputLayout.setErrorEnabled(value_error);

        // Imposta il testo dell'errore, se presente
        if (StringUtils.isEmpty(error))
            inputLayout.setError(null);
        else

            inputLayout.setError(error);
        // Imposta il colore del testo dell'errore e del suggerimento
        set_color_error(inputLayout, color, context);

        // Imposta la larghezza del TextInputLayout
        change_width(inputLayout, dimension_text(dimension, context), context);
    }

    /**
     * Imposta il colore del testo degli errori e del suggerimento per un TextInputLayout.
     *
     * @param inputLayout Il TextInputLayout per il quale impostare i colori.
     * @param id_color L'ID del colore delle risorse da utilizzare.
     * @param context Il contesto dell'applicazione.
     */
    private static void set_color_error(TextInputLayout inputLayout, int id_color, Context context) {
        // Ottieni il colore delle risorse dall'ID fornito
        int errorTextColor = ContextCompat.getColor(context, id_color);

        // Crea uno stato di elenco dei colori con il colore dell'errore
        ColorStateList errorColorStateList = ColorStateList.valueOf(errorTextColor);

        // Imposta il colore del testo degli errori e del suggerimento
        inputLayout.setErrorTextColor(errorColorStateList);
        inputLayout.setHintTextColor(errorColorStateList);
    }

    /**
     * Restituisce la dimensione di un testo convertita da pixel (px) a unità dipendenti dalla densità (dp).
     *
     * @param id_dimension L'ID della dimensione delle risorse in pixel (px) da convertire.
     * @return La dimensione convertita in unità dipendenti dalla densità (dp).
     */
    private static int dimension_text(int id_dimension, Context context) {
        // Ottieni la dimensione delle risorse in pixel (px)
        int pixelSize = context.getResources().getDimensionPixelSize(id_dimension);

        // Converti la dimensione da pixel a unità dipendenti dalla densità (dp)
        return (int) (pixelSize / context.getResources().getDisplayMetrics().density);
    }

    /**
     * Modifica la larghezza di un layout di input.
     *
     * @param input_layout Il layout di input da modificare.
     * @param dimension La dimensione desiderata in unità dipendenti dalla densità (dp).
     */
    private static void change_width(TextInputLayout input_layout, int dimension, Context context){
        // Ottieni la densità dello schermo
        float density = context.getResources().getDisplayMetrics().density;

        // Calcola l'altezza desiderata in pixel basata sulla densità
        int desiredHeightInPixels = (int) (dimension * density);

        // Ottieni i parametri di layout attuali e modifica l'altezza
        ViewGroup.LayoutParams layoutParams = input_layout.getLayoutParams();
        layoutParams.height = desiredHeightInPixels;

        // Applica le nuove impostazioni di layout
        input_layout.setLayoutParams(layoutParams);
    }

    /**
     * Verifica se due elementi di testo sono uguali e gestisce la visualizzazione dell'errore.
     *
     * @param text_element1 Il testo del primo elemento.
     * @param text_element2 Il testo del secondo elemento.
     * @param layout_error Il layout in cui visualizzare l'errore.
     * @param context Il contesto dell'applicazione.
     * @return true se i due elementi di testo sono uguali, false altrimenti.
     */
    public static boolean is_equal_password(String text_element1, String text_element2, TextInputLayout layout_error, Context context){
        if (!StringUtils.equals(text_element1,text_element2)) {
            ControlInput.set_error(layout_error, true, context.getString(R.string.non_equivalent_passwords), error_color, context, R.dimen.input_text_layout_height_error);
            return false;
        }
        return true;
    }
    /**
     * Verifica se una matricola è corretta secondo il seguente modello:
     * inizia con un numero tra 1 e 9, seguito da esattamente 5 cifre numeriche comprese tra 0 e 9.
     *
     * @param matricola La matricola da verificare.
     * @return true se la matricola è corretta, false altrimenti.
     */
    public static boolean is_correct_matricola(String matricola){
        String regexPattern = "[1-9][0-9]{5}";
        return  Pattern.compile(regexPattern).matcher(matricola).matches();
    }

    /**
     * Calcola l'hash SHA-256 di una stringa di testo.
     *
     * @param textToHash La stringa di testo da cui calcolare l'hash.
     * @return Una stringa che rappresenta l'hash SHA-256 della stringa di testo, o una stringa vuota se si verifica un'eccezione.
     */
    public static String hashWith256(String textToHash) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] byteOfTextToHash = textToHash.getBytes(StandardCharsets.UTF_8);
            byte[] hashedByetArray = digest.digest(byteOfTextToHash);
            return Base64.getEncoder().encodeToString(hashedByetArray);
        }catch (NoSuchAlgorithmException e){
            Log.e("LaureApp", "Error reading file", e);
        }
        return "";
    }

    /**
     * Verifica se il dispositivo è connesso a Internet attraverso una connessione WiFi o cellulare.
     *
     * @param cm Il gestore di connettività (ConnectivityManager) utilizzato per controllare la connessione di rete.
     * @return true se il dispositivo è connesso a Internet, false altrimenti.
     */
    public static boolean isConnected(ConnectivityManager cm) {
        if (cm != null) {
            Network network = cm.getActiveNetwork();
            NetworkCapabilities capabilities = cm.getNetworkCapabilities(network);
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    return true;
                } else return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR);
            }
        }
        return false;

    }

    /**
     * Verifica se un indirizzo email esiste già nel database dell'applicazione.
     *
     * @param email L'indirizzo email da verificare.
     * @param context Il contesto dell'applicazione utilizzato per accedere al database.
     * @return true se l'indirizzo email esiste nel database, false altrimenti.
     */
    public static boolean isExistEmail(String email, Context context){
        boolean result = false;
        UtenteModelView ut_view = new UtenteModelView(context);
        Log.d("controllo_email", String.valueOf(ut_view.getAllUtente()));
        if (ut_view.getIdUtente(email) != null) {
            result =  true;
        }
        return result;
    }

    /**
     * Mostra un messaggio di notifica temporaneo (Toast) all'utente.
     *
     * @param context Il contesto dell'applicazione.
     * @param message Il messaggio da mostrare come notifica.
     */
    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Verifica se il valore della matricola in un campo di input è valido e se è associato a un ruolo specifico.
     *
     * @param inputEditText Il campo di input contenente la matricola da verificare.
     * @param inputLayout Il layout di input associato al campo di input.
     * @param context Il contesto dell'applicazione.
     * @param ruolo Il ruolo specifico a cui la matricola dovrebbe essere associata.
     * @return true se la matricola è valida e non è associata al ruolo specifico, altrimenti false.
     */
    public static boolean is_correct_matricola(TextInputEditText inputEditText, TextInputLayout inputLayout, Context context, String ruolo){
        StudenteModelView st = new StudenteModelView(context);
        ProfessoreModelView pr = new ProfessoreModelView(context);
        if (!ControlInput.is_correct_matricola(Objects.requireNonNull(inputEditText.getText()).toString())) {
            String error_message = context.getString(R.string.errore_matricola).replace("{campo}", "Matricola");
            ControlInput.set_error(inputLayout, true, error_message, error_color, context, R.dimen.input_text_layout_height_error);
            return false;
        } else {
            Long presentestud = st.findStudenteMatricola(Long.valueOf(Objects.requireNonNull(inputEditText.getText()).toString())) ;
            Long presenteprof = pr.findProfessoreMatricola(Long.valueOf(Objects.requireNonNull(inputEditText.getText()).toString())) ;
            if (presentestud == null && presenteprof == null) {
                ControlInput.set_error(inputLayout, false, "", R.color.color_primary, context, R.dimen.input_text_layout_height);
                return true;
            }else{
                String error_message = "Esiste uno {campo} assosciato a questa matricola".replace("{campo}", ruolo);
                ControlInput.set_error(inputLayout, true, error_message, error_color, context, R.dimen.input_text_layout_height_error);
                return false;
            }
        }
    }

    /**
     * Verifica se il valore nel campo di input della media è valido e rientra nell'intervallo accettabile.
     *
     * @param inputEditText Il campo di input contenente la media da verificare.
     * @param inputLayout Il layout di input associato al campo di input.
     * @param context Il contesto dell'applicazione.
     * @return true se il valore della media è valido e rientra nell'intervallo accettabile, altrimenti false.
     */
    public static boolean is_correct_media(TextInputEditText inputEditText, TextInputLayout inputLayout, Context context){
        String media = Objects.requireNonNull(inputEditText.getText()).toString();
        if (StringUtils.isNumeric(media) && (Integer.parseInt(media)) >= 18 && Integer.parseInt(media) <= 30) {
            ControlInput.set_error(inputLayout, false, "", R.color.color_primary, context, R.dimen.input_text_layout_height);
            return true;
        } else {
            String error_message = context.getString(R.string.errore_media).replace("{campo}", context.getString(R.string.media));
            ControlInput.set_error(inputLayout, true, error_message, error_color, context, R.dimen.input_text_layout_height_error_media);
            return false;
        }
    }

    /**
     * Verifica se il valore nel campo di input degli esami mancanti è valido e rientra nell'intervallo accettabile.
     *
     * @param inputEditText Il campo di input contenente il numero di esami mancanti da verificare.
     * @param inputLayout Il layout di input associato al campo di input.
     * @param context Il contesto dell'applicazione.
     * @return true se il valore degli esami mancanti è valido e rientra nell'intervallo accettabile, altrimenti false.
     */
    public static boolean is_correct_esami_mancanti(TextInputEditText inputEditText, TextInputLayout inputLayout, Context context){
        String esami = Objects.requireNonNull(inputEditText.getText()).toString();
        if (StringUtils.isNumeric(esami) && (Integer.parseInt(esami)) >= 0 && Integer.parseInt(esami) <= 40) {
            ControlInput.set_error(inputLayout, false, "", R.color.color_primary, context, R.dimen.input_text_layout_height);
            return true;
        } else {
            String error_message = context.getString(R.string.errore_media).replace("{campo}", context.getString(R.string.media)).replace("30", "40");
            ControlInput.set_error(inputLayout, true, error_message, error_color, context, R.dimen.input_text_layout_height_error_email);
            return false;
        }
    }

    /**
     * Verifica se il valore nel campo di input della facoltà è una delle opzioni valide.
     *
     * @param inputEditText Il campo di input contenente la facoltà da verificare.
     * @param inputLayout Il layout di input associato al campo di input.
     * @param context Il contesto dell'applicazione.
     * @param facolta Un array di stringhe che rappresentano le opzioni valide per la facoltà.
     * @return true se il valore della facoltà è una delle opzioni valide, altrimenti false.
     */
    public static boolean is_correct_facolta(MaterialAutoCompleteTextView inputEditText, TextInputLayout inputLayout, Context context, String[] facolta){
        String facolta_text = Objects.requireNonNull(inputEditText.getText()).toString();
        boolean isFacoltaTextEqual = Arrays.stream(facolta)
                .anyMatch(s -> StringUtils.equals(s, facolta_text));
        if (isFacoltaTextEqual) {
            ControlInput.set_error(inputLayout, false, "", R.color.color_primary, context, R.dimen.input_text_layout_height);
            return true;
        } else {
            String error_message = context.getString(R.string.errore_scelta);
            ControlInput.set_error(inputLayout, true, error_message, error_color, context, R.dimen.input_text_layout_height_error_email);
            return false;
        }
    }

    /**
     * Verifica se il valore nel campo di input del corso di laurea è una delle opzioni valide.
     *
     * @param inputEditText Il campo di input contenente il corso di laurea da verificare.
     * @param inputLayout Il layout di input associato al campo di input.
     * @param context Il contesto dell'applicazione.
     * @param corsi Un array di stringhe che rappresentano le opzioni valide per il corso di laurea.
     * @return true se il valore del corso di laurea è una delle opzioni valide, altrimenti false.
     */
    public static boolean is_correct_corso_di_laurea(MaterialAutoCompleteTextView inputEditText, TextInputLayout inputLayout, Context context, String[] corsi){
        String corso_text = Objects.requireNonNull(inputEditText.getText()).toString();
        boolean iscorsiTextEqual = Arrays.stream(corsi)
                .anyMatch(s -> StringUtils.equals(s, corso_text));
        if (iscorsiTextEqual) {
            ControlInput.set_error(inputLayout, false, "", R.color.color_primary, context, R.dimen.input_text_layout_height);
            return true;
        } else {
            String error_message = context.getString(R.string.errore_scelta);
            ControlInput.set_error(inputLayout, true, error_message, error_color, context, R.dimen.input_text_layout_height_error_email);
            return false;
        }
    }

    /**
     * Verifica se la conferma della password è corretta e gestisce eventuali errori.
     *
     * @param editTextpassword     Il campo di input per la password.
     * @param editTextConfpassword Il campo di input per la conferma della password.
     * @param inputLayout          Il layout di input associato al campo di conferma della password.
     * @param context              Il contesto dell'applicazione.
     * @return Restituisce true se la conferma della password è corretta, altrimenti restituisce false.
     */
    public static boolean is_correct_confirm_password(TextInputEditText editTextpassword, TextInputEditText editTextConfpassword,TextInputLayout inputLayout, Context context){
        boolean result_error = ControlInput.is_equal_password( Objects.requireNonNull(editTextConfpassword.getText()).toString(),
                Objects.requireNonNull(editTextpassword.getText()).toString(), inputLayout, context);
        if(result_error)
            ControlInput.set_error(inputLayout, false, "", R.color.color_primary, context, R.dimen.input_text_layout_height);
        return result_error;
    }

    /**
     * Verifica se il testo inserito in un campo di input contiene solo caratteri alfabetici e gestisce eventuali errori.
     *
     * @param inputEditText Il campo di input da validare.
     * @param inputLayout   Il layout di input associato al campo.
     * @param context       Il contesto dell'applicazione.
     * @return Restituisce true se il testo inserito è valido (contiene solo caratteri alfabetici), altrimenti restituisce false.
     */
    public static boolean is_correct_nome_cognome(TextInputEditText inputEditText, TextInputLayout inputLayout, Context context){
        if (!StringUtils.isAlpha(Objects.requireNonNull(inputEditText.getText()).toString())) {
            String error_message = context.getString(R.string.errore_stringa).replace("{campo}", Objects.requireNonNull(inputEditText.getHint()).toString());
            ControlInput.set_error(Objects.requireNonNull(inputLayout), true, error_message, error_color, context, R.dimen.input_text_layout_height_error);
            return false;
        } else {
            ControlInput.set_error(Objects.requireNonNull(inputLayout), false, "", R.color.color_primary, context, R.dimen.input_text_layout_height);
            return true;
        }
    }

    /**
     * Verifica se la password inserita è sicura e gestisce eventuali errori.
     *
     * @param inputEditText Il campo di input contenente la password da validare.
     * @param inputLayout   Il layout di input associato al campo della password.
     * @param context       Il contesto dell'applicazione.
     * @return Restituisce true se la password è considerata sicura, altrimenti restituisce false.
     */
    public static boolean is_correct_password(TextInputEditText inputEditText, TextInputLayout inputLayout, Context context){
        if (!ControlInput.isPasswordSafe(Objects.requireNonNull(inputEditText.getText()).toString())) {
            String error_message = context.getString(R.string.password_not_safe_error);
            ControlInput.set_error(inputLayout, true, error_message, error_color, context, R.dimen.input_text_layout_height_error);
            return false;
        } else {
            ControlInput.set_error(inputLayout, false, "", R.color.color_primary, context, R.dimen.input_text_layout_height);
            return true;
        }
    }

    /**
     * Verifica se una stringa inserita in un componente di input è vuota e gestisce eventuali errori.
     *
     * @param component     Il componente di input contenente la stringa da validare.
     * @param layout_input  Il layout di input associato al componente.
     * @param campo_error   Il nome del campo associato per la creazione del messaggio di errore.
     * @param context       Il contesto dell'applicazione.
     * @return Restituisce true se la stringa è vuota, altrimenti restituisce false.
     */
    public static Boolean is_empty_string(Object component, TextInputLayout layout_input, String campo_error, Context context){
        String text = null;
        String error_message;
        if (component instanceof TextInputEditText){
            text= Objects.requireNonNull(((TextInputEditText) component).getText()).toString();
        }else if (component instanceof MaterialAutoCompleteTextView) {
            text= Objects.requireNonNull(((MaterialAutoCompleteTextView) component).getText()).toString();
        }else if (component instanceof MultiAutoCompleteTextView) {
            text= Objects.requireNonNull(((MultiAutoCompleteTextView) component).getText()).toString();
        }
        if(StringUtils.isEmpty(text)){
            error_message = context.getString(R.string.errore_campo_vuoto).replace("{campo}", campo_error);
            ControlInput.set_error(layout_input, true, error_message, error_color, context, R.dimen.input_text_layout_height_error);
            if (component instanceof View) {
                ((View) component).requestFocus();
            }
            return true;
        }else{
            ControlInput.set_error(layout_input, false, "",  R.color.color_primary, context, R.dimen.input_text_layout_height);
        }
        return false;
    }

    /**
     * Verifica se l'indirizzo email inserito è in un formato valido e se non esiste già nel contesto dell'applicazione,
     * gestendo eventuali errori.
     *
     * @param inputEditText Il campo di input contenente l'indirizzo email da validare.
     * @param inputLayout   Il layout di input associato al campo dell'indirizzo email.
     * @param context       Il contesto dell'applicazione.
     * @return Restituisce true se l'indirizzo email è valido e non esiste già, altrimenti restituisce false.
     */
    public static boolean is_correct_email(TextInputEditText inputEditText, TextInputLayout inputLayout, Context context){
        String email = Objects.requireNonNull(inputEditText.getText()).toString();
        if (!ControlInput.isValidEmailFormat(email)) {
            String error_message = context.getString(R.string.errore_email);
            ControlInput.set_error(inputLayout, true, error_message, error_color, context, R.dimen.input_text_layout_height_error);
        } else if (ControlInput.isExistEmail(email, context)) {
            String error_message = context.getString(R.string.errore_email_esistente);
            ControlInput.set_error(inputLayout, true, error_message, error_color, context, R.dimen.input_text_layout_height_error);
        } else {
            ControlInput.set_error(inputLayout, false, "", R.color.color_primary, context, R.dimen.input_text_layout_height);
            return true;
        }
        return  false;
    }

}