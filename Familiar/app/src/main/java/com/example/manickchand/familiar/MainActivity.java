package com.example.manickchand.familiar;

import android.Manifest;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.manickchand.familiar.Adapters.AdapterRV;
import com.example.manickchand.familiar.Interfaces.ReciclerViewOnClickListenerHack;
import com.example.manickchand.familiar.model.BDcore;
import com.example.manickchand.familiar.model.Familiar;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ReciclerViewOnClickListenerHack{

    private RecyclerView rv;
    private List<Familiar> lista = new ArrayList<Familiar>();
    private BDcore bDcore = new BDcore(this);
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }
        else
        {
           criarPasta();
        }

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv = (RecyclerView) findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(llm);

        tv = (TextView)findViewById(R.id.tv);

        aniversarios();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,AdicionarFamiliar.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        lista = bDcore.listarFamiiares();
        setAdapter();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_info) {
            showDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void criarPasta(){

        File folder = new File(Environment.getExternalStorageDirectory()+File.separator + getResources().getString(R.string.folder_package));
        if (!folder.exists()) {folder.mkdir();}
        else {

            File nomedia = new File(folder, ".nomedia");

            try {
                nomedia.createNewFile();
            } catch (Exception e) {
                Log.e("error", "Error create nomidea");
            }
        }

    }

    public void setAdapter(){
        if(lista.size()>0){
            AdapterRV adapter = new AdapterRV(this,lista, MainActivity.this);
            adapter.setReciclerViewOnClickListenerHack(this);
            rv.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            tv.setVisibility(View.GONE);
            rv.setVisibility(View.VISIBLE);
        }
        else{
            rv.setVisibility(View.GONE);
            tv.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClickListener(View v, int position) {

        Intent intent = new Intent(this,VisualizarActivity.class);
        intent.putExtra("f", lista.get(position));
        startActivity(intent);
    }

    public void Notificar(String nome){

        PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
        Resources r = getResources();
        Notification notification = new NotificationCompat.Builder(this)
                .setTicker(r.getString(R.string.notiTitle))
                .setSmallIcon(R.drawable.ic_logo)
                .setContentTitle(r.getString(R.string.notiTitle))
                .setContentText(" Dê os parabéns a "+nome)
                .setContentIntent(pi)
                .setAutoCancel(true)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }

    private  void aniversarios(){

        for(int i =0;i<lista.size();i++){

            String[] s = lista.get(i).getNascimento().toString().split("/");

            Calendar calendar = Calendar.getInstance();

            int d = calendar.get(Calendar.DAY_OF_MONTH);
            int m = calendar.get(Calendar.MONTH)+1;

            Log.i("logdm","Integer.parseInt(s[0])  "+Integer.parseInt(s[0]) +" d " +d +" Integer.parseInt(s[1]) "+ Integer.parseInt(s[1]) +" m "+ m);

            if(Integer.parseInt(s[0]) ==d && Integer.parseInt(s[1]) == m){ Notificar(lista.get(i).getNome());}

        }
    }


    //dialog info
    private void showDialog(){

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.layout_sobre);

        final Button ok  = (Button) dialog.findViewById(R.id.button);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
