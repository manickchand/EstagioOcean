package com.example.manickchand.familiar;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.manickchand.familiar.model.BDcore;
import com.example.manickchand.familiar.model.Familiar;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AdicionarFamiliar extends AppCompatActivity {

    private EditText et_nomeAddFamiliar,et_telefoneAddFamiliar;
    private ImageView iv_addFamiliar;
    private TextView tv_nascimentoAddFamiliar;
    private Spinner spinnerParentesco;
    private BDcore bDcore = new BDcore(this);
    private Button bt_salvar;
    private Bitmap bitmap;
    private View view;
    private static final int qualidade_image_profile = 85;
    private Familiar familiarEditado = null;
    int id_Familiar=-1;
    boolean result = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_familiar);
        view = findViewById(R.id.containerAdicionar);
        setTitle("Adicionar Familiar");

        et_nomeAddFamiliar = (EditText) findViewById(R.id.et_nomeAddFamiliar);
        et_telefoneAddFamiliar = (EditText) findViewById(R.id.et_telefoneAddFamiliar);
        iv_addFamiliar = (ImageView) findViewById(R.id.iv_addFamiliar);
        tv_nascimentoAddFamiliar = (TextView) findViewById(R.id.tv_nascimentoAddFamiliar);
        tv_nascimentoAddFamiliar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

        spinnerParentesco = (Spinner) findViewById(R.id.spinnerParentesco);

        preencheSpinner();

        familiarEditado = (Familiar) getIntent().getSerializableExtra("f");

        id_Familiar = getIntent().getIntExtra("id",-1);

        if(id_Familiar==-1){id_Familiar = bDcore.getMaxId();}
        else{setdata();}

        bt_salvar = (Button) findViewById(R.id.bt_Salvar);
        bt_salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(verifica()){
                   adicionarFamiliar();
                }
                else{
                     Snackbar.make(view, "Verifique Nome e Parentesco", Snackbar.LENGTH_LONG)
                     .setAction("OK", null).show();
                }
            }
        });

        iv_addFamiliar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 0);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            result = true;
            Uri targetUri = data.getData();
            Log.i("result image", "target uri " + targetUri.toString());

            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                iv_addFamiliar.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                Snackbar.make(view, "Erro ao carregar imagem.", Snackbar.LENGTH_LONG)
                        .setAction("OK", null).show();
            }
        }
    }

   private void adicionarFamiliar(){
       Familiar newF = new Familiar();

       newF.setNome(et_nomeAddFamiliar.getText().toString());
       newF.setTelefone(et_telefoneAddFamiliar.getText().toString());
       newF.setParentesco(spinnerParentesco.getSelectedItem().toString());
       newF.setNascimento(tv_nascimentoAddFamiliar.getText().toString());

       if(result){
           if (ContextCompat.checkSelfPermission(AdicionarFamiliar.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
               ActivityCompat.requestPermissions(AdicionarFamiliar.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
           }
           else
           {
               salvaFoto();
           }
       }

       if(familiarEditado!=null){newF.setId(id_Familiar); bDcore.editarFamiliar(newF);}
       else{bDcore.inserirFamiliar(newF);}

       Snackbar.make(view, R.string.familiarSalvo, Snackbar.LENGTH_LONG)
               .setAction("", null).show();
       finish();

    }

    //verifica se nome e parentesco estao preenchidos
    private boolean verifica(){
        if(et_nomeAddFamiliar.getText().length()>3 && spinnerParentesco.getSelectedItemPosition()>0){
            return true;
        }
        else return false;

    }

    //salva a foto em uma pasta FamiliarApp
    private void salvaFoto(){

        try {

            File f = new File(Environment.getExternalStorageDirectory() +getResources().getString(R.string.folder_package)+ "/" +id_Familiar+ ".jpg");

            if(f.exists()){ boolean deleted = f.delete();}

            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, qualidade_image_profile, bytes);

            try {
                f.createNewFile();
                FileOutputStream fo = new FileOutputStream(f);
                fo.write(bytes.toByteArray());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        catch (Exception E){
            Log.e("error","Erro ao carregar imagem");
        }
    }

    //insere parentescos no spinner
    private void preencheSpinner(){
        List<String> listaParentes = new ArrayList<String>();

        listaParentes.add(getString(R.string.selecioneParentesco));

        listaParentes.add(getString(R.string.pai));
        listaParentes.add(getString(R.string.mae));
        listaParentes.add(getString(R.string.esposoa));
        listaParentes.add(getString(R.string.filhoa));

        listaParentes.add(getString(R.string.neto));
        listaParentes.add(getString(R.string.genro));
        listaParentes.add(getString(R.string.nora));
        listaParentes.add(getString(R.string.tioa));
        listaParentes.add(getString(R.string.sobrinhoa));
        listaParentes.add(getString(R.string.amigo));

        ArrayAdapter<String> adapteSpinner = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listaParentes);
        adapteSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerParentesco.setAdapter(adapteSpinner);
    }

    //se for edição seta os dados do familiar passado
    private void setdata(){
        et_telefoneAddFamiliar.setText(familiarEditado.getTelefone().toString());
        et_nomeAddFamiliar.setText(familiarEditado.getNome().toString());

        File imgFile = new  File(Environment.getExternalStorageDirectory() + getResources().getString(R.string.folder_package)+"/"+id_Familiar+".jpg");

        if(imgFile.exists()){

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            iv_addFamiliar.setImageBitmap(myBitmap);
        }
        else{
            //iv_user_pic.setImageResource(R.drawable.user);
        }

    }

    //dialog datepicker
    private void showDialog(){

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.calendario);

        final DatePicker datePicker = (DatePicker) dialog.findViewById(R.id.datePicker2);
        final Button confirmarData  = (Button) dialog.findViewById(R.id.bt_confirmatdata);
        final Button cancelarData = (Button) dialog.findViewById(R.id.bt_cancelardata);

        datePicker.setMaxDate(System.currentTimeMillis() - 1000);
        datePicker.setMinDate(1920);

        cancelarData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        confirmarData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int d = datePicker.getDayOfMonth();
                int m = datePicker.getMonth()+1;
                int a = datePicker.getYear();
                tv_nascimentoAddFamiliar.setText(d+"/"+m+"/"+a);
                dialog.dismiss();
            }
        });

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        dialog.getWindow().setAttributes(lp);
        dialog.show();

    }
}
