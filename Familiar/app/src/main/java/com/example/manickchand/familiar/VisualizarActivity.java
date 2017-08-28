package com.example.manickchand.familiar;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.manickchand.familiar.model.BDcore;
import com.example.manickchand.familiar.model.Familiar;

import java.io.File;

public class VisualizarActivity extends AppCompatActivity {

    private ImageView iv_detalhes;
    private TextView tv_nomedetalhes,tv_parentescodetalhes,tv_nacimentodetalhes;
    private BDcore bDcore = new BDcore(this);
    private Familiar familiar = new Familiar();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Detalhes");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        iv_detalhes = (ImageView) findViewById(R.id.iv_detalhes);
        tv_nomedetalhes = (TextView) findViewById(R.id.tv_nomedetalhes);
        tv_nacimentodetalhes = (TextView) findViewById(R.id.tv_nacimentodetalhes);
        tv_parentescodetalhes = (TextView) findViewById(R.id.tv_parentescodetalhes);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(familiar.getTelefone().length()>1){ligar();}
                else{Snackbar.make(view, "Esta pessoa não possui número cadastrado.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();}

            }
        });

        familiar = (Familiar) getIntent().getSerializableExtra("f");

        setdata();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_edit) {

            Intent intent = new Intent(this,AdicionarFamiliar.class);
            intent.putExtra("f", familiar);
            intent.putExtra("id", familiar.getId());

            Log.i("logid"," IDf "+familiar.getId());
            finish();
            startActivity(intent);

            return true;
        }

        if (id == R.id.action_remove) {

            bDcore.deletarFamiliar(familiar.getId());
            File f = new File(Environment.getExternalStorageDirectory() +getResources().getString(R.string.folder_package)+ "/" +familiar.getId()+ ".jpg");

            if(f.exists()){ boolean deleted = f.delete();}
            finish();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

//carrega imagem no imageView
    private Bitmap loadBitmap(int id) {
        File imgFile = new File(Environment.getExternalStorageDirectory() + this.getResources().getString(R.string.folder_package) + "/" + id + ".jpg");

        Bitmap myBitmap = null;
        if (imgFile.exists()) {
            myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        }

        return myBitmap;
    }

    //preenche TextViews
    private void setdata(){
        tv_parentescodetalhes.setText(familiar.getParentesco().toString());
        tv_nacimentodetalhes.setText(familiar.getNascimento().toString());
        tv_nomedetalhes.setText(familiar.getNome());


        if (ContextCompat.checkSelfPermission(VisualizarActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(VisualizarActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }
        else
        {
            if(loadBitmap(familiar.getId())!=null) iv_detalhes.setImageBitmap(loadBitmap(familiar.getId()));
        }

    }

    //realiza chamada
    private void ligar(){

        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+familiar.getTelefone().toString()));
        if (ContextCompat.checkSelfPermission(VisualizarActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(VisualizarActivity.this, new String[]{Manifest.permission.CALL_PHONE},1);
        }
        else{startActivity(intent);}
    }

}
