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
    private static final String usuarioTabela = "Usuario";
    private static final String idTabela = "ID";
    private static final int versaoBD = 13;

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

        //tabela usuario
        String SQLCreateTableUsuario = "CREATE TABLE "+usuarioTabela+" ("
                +"id TEXT PRIMARY KEY, "
                +"nome TEXT, "
                +"email TEXT"
                + " )";
        bd.execSQL(SQLCreateTableUsuario);
        //tabela familiares
        String SQLCreateTablid = "CREATE TABLE "+idTabela+" (id TEXT PRIMARY KEY )";
        bd.execSQL(SQLCreateTablid);

    }

    @Override
    public void onUpgrade(SQLiteDatabase bd, int i, int i1) {
        String drop = "DROP TABLE IF EXISTS "+familiaresTabela;
        bd.execSQL(drop);
        String drop2 = "DROP TABLE IF EXISTS "+usuarioTabela;
        bd.execSQL(drop2);

        String drop3 = "DROP TABLE IF EXISTS "+idTabela;
        bd.execSQL(drop3);
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


    public void inserirUsuario(Usuario u){

        SQLiteDatabase bd = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("id", u.getId());
        cv.put("nome", u.getNome());
        cv.put("email",u.getEmail());


        Log.i("addu","id "+u.getId());
        Log.i("addu","nome "+u.getNome());
        Log.i("addu","email "+u.getEmail());

        bd.insert(usuarioTabela, null, cv);
       // bd.close();
    }

    public Usuario getUsuario(){

        SQLiteDatabase bd = getReadableDatabase();


        Log.i("listau","ta em listar ");

        String sqlListar = "SELECT * FROM "+ usuarioTabela;

        Cursor c = bd.rawQuery(sqlListar, null);


        if(c.moveToFirst()){
            Usuario  u = new Usuario();
                u.setId(c.getString(0));
                u.setNome(c.getString(1));
                u.setEmail(c.getString(2));
            Log.i("listau","id "+u.getId());
            Log.i("listau","nome "+u.getNome());
            Log.i("listau","email "+u.getEmail());

            return  u;
        }

        bd.close();
        return null;
    }


    public void inseririd(String id){

        SQLiteDatabase bd = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("id", id);


        bd.insert(idTabela, null, cv);
        bd.close();
    }

    public String getidbd(){

        SQLiteDatabase bd = getReadableDatabase();

        String sqlListar = "SELECT * FROM "+ idTabela;

        Cursor c = bd.rawQuery(sqlListar, null);

        if(c.moveToFirst()){

            String s = c.getString(0);

            return  s;
        }

        bd.close();
        return "";
    }

    public void logout(){
        SQLiteDatabase bd = getReadableDatabase();

        String sqlDelete = "DELETE FROM "+usuarioTabela;
        String sqlDelete2 = "DELETE FROM "+familiaresTabela;
        String sqlDelete3 = "DELETE FROM "+idTabela;

        //deleta e fecha conexao
        bd.execSQL(sqlDelete2);
        bd.execSQL(sqlDelete);
        bd.execSQL(sqlDelete3);

        bd.close();
    }

}
