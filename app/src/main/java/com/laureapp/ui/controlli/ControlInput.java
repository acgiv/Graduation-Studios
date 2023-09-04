    package com.laureapp.ui.controlli;

    import static java.security.AccessController.getContext;

    import android.content.Context;
    import android.content.res.ColorStateList;
    import android.content.res.Resources;
    import android.net.ConnectivityManager;
    import android.net.NetworkInfo;
    import android.util.Log;
    import android.view.ViewGroup;

    import androidx.core.content.ContextCompat;

    import com.google.android.material.textfield.TextInputLayout;
    import com.laureapp.R;

    import org.apache.commons.lang3.StringUtils;

    import java.nio.charset.StandardCharsets;
    import java.security.MessageDigest;
    import java.security.NoSuchAlgorithmException;
    import java.util.Base64;
    import java.util.regex.Matcher;
    import java.util.regex.Pattern;

    public class ControlInput {
        //controllo del formato dell'email
        public static boolean  isValidEmailFormat(String email) {
            String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                    + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
            return Pattern.compile(regexPattern).matcher(email).matches();
        }
        //metodo per eliminare gli spazi
        public static String deleteBlank(String stringa){
            stringa = stringa.replaceAll("\t", "");
            stringa = stringa.replaceAll("\n", "");
            stringa = stringa.replaceAll(" ", "");
            return stringa;
        }
        //metodo per eliminare i numeri
        public static String deleteNumber(String stringa){
            stringa = stringa.replaceAll("[0-9]", "");
            return stringa;
        }

        //metodo per eliminare la punteggiatura
        public static String deletePunctuation(String stringa){
            String regexPattern = "[!\\\"#$%&'()*+,-./:;<=>?@\\\\[\\\\]^_`{|}~]";
            stringa =  stringa.replaceAll(regexPattern, "");
            return stringa;
        }

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

            if(containsNumber && containsSpecial && charsNumber >= 8) return true;
            else return false;
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
         * @param resources Le risorse dell'applicazione.
         */
        public static void set_error(TextInputLayout inputLayout, boolean value_error, String error, int color, Context context, int dimension, Resources resources) {
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
            change_width(inputLayout, dimension_text(dimension, resources), resources);
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
         * @param resources Le risorse dell'applicazione.
         * @return La dimensione convertita in unità dipendenti dalla densità (dp).
         */
        private static int dimension_text(int id_dimension, Resources resources) {
            // Ottieni la dimensione delle risorse in pixel (px)
            int pixelSize = resources.getDimensionPixelSize(id_dimension);

            // Converti la dimensione da pixel a unità dipendenti dalla densità (dp)
            return (int) (pixelSize / resources.getDisplayMetrics().density);
        }

        /**
         * Modifica la larghezza di un layout di input.
         *
         * @param input_layout Il layout di input da modificare.
         * @param dimension La dimensione desiderata in unità dipendenti dalla densità (dp).
         * @param resources Le risorse dell'applicazione.
         */
        private static void change_width(TextInputLayout input_layout, int dimension, Resources resources){
            // Ottieni la densità dello schermo
            float density = resources.getDisplayMetrics().density;

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
         * @param error_color Il colore da utilizzare per evidenziare l'errore.
         * @param text_element1 Il testo del primo elemento.
         * @param text_element2 Il testo del secondo elemento.
         * @param layout_error Il layout in cui visualizzare l'errore.
         * @param error_message Il messaggio di errore da visualizzare.
         * @param context Il contesto dell'applicazione.
         * @param resources Le risorse dell'applicazione.
         * @return true se i due elementi di testo sono uguali, false altrimenti.
         */
        public static boolean is_equal_password(int error_color, String text_element1, String text_element2, TextInputLayout layout_error, String error_message, Context context, Resources resources){
            boolean result = true;

            if (!StringUtils.equals(text_element1,text_element2)) {
                ControlInput.set_error(layout_error, true, error_message, error_color, context, R.dimen.input_text_layout_height_error, resources);
                result = false;
                Log.d("is_equal", String.valueOf(StringUtils.equals(text_element1,text_element2)));
            }
            return result;
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
         *
         * @return un booleano che indica se la connessione è presente: true se c'è connessione altrimenti false
         */
        public static boolean isConnected(ConnectivityManager cm) {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
            if (isConnected) {
                return true;
            } else {
                return false;
                // show an error message or do something else
            }
        }

    }
