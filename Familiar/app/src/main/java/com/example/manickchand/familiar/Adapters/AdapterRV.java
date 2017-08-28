package com.example.manickchand.familiar.Adapters;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.manickchand.familiar.Interfaces.ReciclerViewOnClickListenerHack;
import com.example.manickchand.familiar.MainActivity;
import com.example.manickchand.familiar.R;
import com.example.manickchand.familiar.model.Familiar;

import java.io.File;
import java.util.List;

/**
 * Created by Manickchand on 25/08/2017.
 */
public class AdapterRV extends RecyclerView.Adapter<AdapterRV.MyViewHolder> {


    private List<Familiar> mlist;
    private LayoutInflater mlayoutInflater;
    private ReciclerViewOnClickListenerHack mReciclerViewOnClickListenerHack;
    private Context mcontext;
    private RecyclerView rv;
    private View view;
    private Activity ac;

    public AdapterRV(Context c, List<Familiar> l,Activity ac) {
        this.mlist = l;
        this.mlayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mcontext = c;
        this.ac = ac;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = mlayoutInflater.inflate(R.layout.layout_lista, parent, false);
        MyViewHolder mvh = new MyViewHolder(view);
        return mvh;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.tv_nome.setText(mlist.get(position).getNome());
        holder.tv_parentesco.setText(mlist.get(position).getParentesco());
        holder.tv_nascimento.setText(mlist.get(position).getNascimento());

        Bitmap bt = null;
            if (ContextCompat.checkSelfPermission(mcontext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ac, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
            }
            else
            {
                bt = loadBitmap(mlist.get(position).getId());
            }

        if(bt==null){holder.iv_familiar.setImageResource(R.drawable.ic_face_black_48dp);}
        else holder.iv_familiar.setImageBitmap(bt);

    }

        private Bitmap loadBitmap(int id) {
            File imgFile = new File(Environment.getExternalStorageDirectory() + mcontext.getResources().getString(R.string.folder_package) + "/" + id + ".jpg");

            Bitmap myBitmap = null;
            if (imgFile.exists()) {
                myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            }
            return myBitmap;
        }

        @Override
        public int getItemCount() {
            return mlist.size();
        }

        public void setReciclerViewOnClickListenerHack(ReciclerViewOnClickListenerHack r) {
            mReciclerViewOnClickListenerHack = r;
        }

        public void addListItem(Familiar i, int position) {
            mlist.add(i);
            notifyItemInserted(position);
        }

        public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            public ImageView iv_familiar;
            public TextView tv_nome, tv_parentesco, tv_nascimento;

            public MyViewHolder(View itemView) {
                super(itemView);

                iv_familiar = (ImageView) itemView.findViewById(R.id.iv_fotoParente);

                tv_nome = (TextView) itemView.findViewById(R.id.tvNomeParente);
                tv_parentesco = (TextView) itemView.findViewById(R.id.tvParentesco);
                tv_nascimento = (TextView) itemView.findViewById(R.id.tvNascimentoParente);

                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                if (mReciclerViewOnClickListenerHack != null) {
                    mReciclerViewOnClickListenerHack.onClickListener(v, getPosition());
                }
            }
        }
    }
