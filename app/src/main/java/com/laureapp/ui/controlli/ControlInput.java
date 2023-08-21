package com.laureapp.ui.controlli;

import com.laureapp.R;

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
}
