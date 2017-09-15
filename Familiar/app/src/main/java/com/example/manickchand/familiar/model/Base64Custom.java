package com.example.manickchand.familiar.model;

import android.util.Base64;

/**
 * Created by ocean on 13/09/17.
 */

public class Base64Custom {

    public static String codificarBase65(String texto){

            return Base64.encodeToString(texto.getBytes(),Base64.DEFAULT);

    }

    public static String decodificarBase65(String texto){

        return new String(Base64.decode(texto,Base64.DEFAULT));

    }

}
