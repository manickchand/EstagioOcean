package com.example.manickchand.familiar.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Manickchand on 24/08/2017.
 */
public class BDcore extends SQLiteOpenHelper {

    private static final String nomeBD = "Familiar";
    private static final String familiaresTabela = "Familiares";
    private static final int versaoBD = 5;

    // informacoes do bd
    public BDcore(Context context) {
        super(context, nomeBD, null, versaoBD);
    }

    @Override
    public void onCreate(SQLiteDatabase bd) {

        //tabela familiares
        String SQLCreateTableFamilia = "CREATE TABLE "+familiaresTabela+" ("
                +"id INTEGER PRIMARY KEY AUTOINCREMENT, "
                +"nome TEXT, "
                +"parentesco TEXT, " +
                "nascimento TEXT, " +
                "telefone TEXT" +
                " )";
        bd.execSQL(SQLCreateTableFamilia);
    }

    @Override
    public void onUpgrade(SQLiteDatabase bd, int i, int i1) {
        String drop = "DROP TABLE IF EXISTS "+familiaresTabela;
        bd.execSQL(drop);
        onCreate(bd);
    }

    //*
    // INSERIR FAMILIAR
    // */
    public void inserirFamiliar(Familiar f){

        SQLiteDatabase bd = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("nome", f.getNome());
        cv.put("parentesco", f.getParentesco() );
        cv.put("telefone",f.getTelefone());
        cv.put("nascimento",f.getNascimento());

        bd.insert(familiaresTabela, null, cv);
        bd.close();
    }

    //*
    // LISTAR FAMILIAR
    // */
    public List<Familiar> listarFamiiares(){

        List<Familiar> listaFamiares= new ArrayList<Familiar>();

        SQLiteDatabase bd = getReadableDatabase();

        String sqlListar = "SELECT * FROM "+familiaresTabela;

        Cursor c = bd.rawQuery(sqlListar, null);

        if(c.moveToFirst()){
            do{

                Familiar f = new Familiar()	;

                f.setId(c.getInt(0));
                f.setNome(c.getString(1));
                f.setParentesco(c.getString(2));
                f.setNascimento(c.getString(3));
                f.setTelefone(c.getString(4));

                listaFamiares.add(f);

            } while(c.moveToNext());
        }

        bd.close();
        return listaFamiares;
    }

    //*
    // EDITAR FAMILIAR
    // */
    public void editarFamiliar(Familiar f){

        Log.i("logBD"," editar familiar");
        Log.i("logBD"," nome familiar " + f.getNome() );
        Log.i("logBD"," pare familiar " + f.getParentesco());
        Log.i("logBD"," te familiar "+f.getTelefone());
        Log.i("logBD"," id familiar "+f.getId());


        // habilita bd pra escrita
        SQLiteDatabase bd = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("nome", f.getNome());
        cv.put("parentesco", f.getParentesco() );
        cv.put("telefone",f.getTelefone());
        cv.put("nascimento",f.getNascimento());

        bd.update(familiaresTabela, cv, "id = ?", new String[]{"" + f.getId()});
        bd.close();
    }

    //*
    // EXCLUIR FAMILIAR
    // */
    public void deletarFamiliar(int id){
        SQLiteDatabase bd = getReadableDatabase();

        String sqlDelete = "DELETE FROM "+familiaresTabela+" WHERE id = "+id;

        //deleta e fecha conexao
        bd.execSQL(sqlDelete);
        bd.close();
    }


    // retorna o maior id+1 para ser o nome da imagem do familiar
    public int getMaxId(){
        SQLiteDatabase bd = getReadableDatabase();
        String sqlListar = "SELECT MAX(id) FROM "+familiaresTabela;

        Cursor c = bd.rawQuery(sqlListar, null);

        if(c.moveToFirst()) {
            return c.getInt(0)+1;
        }
        else{return 1;}
    }

}
