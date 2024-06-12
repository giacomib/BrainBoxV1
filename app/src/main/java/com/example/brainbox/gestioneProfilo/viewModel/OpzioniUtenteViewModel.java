package com.example.brainbox.gestioneProfilo.viewModel;

import androidx.lifecycle.ViewModel;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OpzioniUtenteViewModel extends ViewModel {
    public boolean emailValidation(String email){
        if(email.isEmpty())
            return false;
        if(!email.contains("@"))
            return false;
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$");
        Matcher matcher = pattern.matcher(email);
        boolean res = matcher.find();
        if(!res)
            return false;
        return true;
    }

    public boolean usernameValidation(String username){
        if(username.isEmpty())
            return false;
        if(username.contains(" "))
            return false;
        return true;
    }
}