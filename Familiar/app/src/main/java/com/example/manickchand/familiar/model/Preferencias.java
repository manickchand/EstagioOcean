package com.example.manickchand.familiar.model;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ocean on 13/09/17.
 */

public class Preferencias {


    private Context mcontext;
    private SharedPreferences preferences;
    private String NOME_ARQUIVO = "familiar";
    private  int MODE =0;
    private SharedPreferences.Editor editor;

    private final String CHAVE_IDENTIFICADOR="identificadorusuario";
    private final String CHAVE_NOME="nomeusuario";


    public Preferencias(Context context){

        this.mcontext = context;
        preferences = mcontext.getSharedPreferences(NOME_ARQUIVO,MODE);
        editor = preferences.edit();
    }

    public void salvarPreferencias(String identificador,String nome){

        editor.putString(CHAVE_IDENTIFICADOR,identificador);
        editor.putString(CHAVE_NOME,nome);
    }

    public String getCHAVE_IDENTIFICADOR(){
        return preferences.getString(CHAVE_IDENTIFICADOR,null);
    }
    public String getCHAVE_NOME(){
        return preferences.getString(CHAVE_NOME,null);
    }

}
