package com.example.manickchand.familiar.model;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ocean on 13/09/17.
 */

public class Usuario implements Serializable{

    private String nome,email,senha,id;


    public Usuario() {
    }

    public void salvar(){

        DatabaseReference reference =  FirebaseConfig.getFirebase();
        String idn = getId();
        tomap();
        //Log.i("log s","idn "+idn);
        //Log.i("log s","getId().toString() "+getId().toString());

        reference.child("Usuarios").child(getId().toString()).setValue(this);
    }

    public Map<String, Object> tomap(){
        HashMap<String,Object> hm = new HashMap<>();
        hm.put("id",getId());
        hm.put("nome",getNome());
        hm.put("email",getEmail());
        hm.put("senha",getSenha());

        return  hm;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
