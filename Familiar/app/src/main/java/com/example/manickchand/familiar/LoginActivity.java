package com.example.manickchand.familiar;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.manickchand.familiar.model.BDcore;
import com.example.manickchand.familiar.model.Familiar;
import com.example.manickchand.familiar.model.FirebaseConfig;
import com.example.manickchand.familiar.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {


    private EditText emailET, senhaET;
    private Button btlogar,btnovaconta;
    private FirebaseAuth firebaseAutha;
    //private ValueEventListener valueEventListener;

    private BDcore bDcore = new BDcore(this);
    private DatabaseReference reference;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Familiar");

        emailET = (EditText) findViewById(R.id.etemail);
        senhaET = (EditText)findViewById(R.id.etsenha);

        btlogar= (Button) findViewById(R.id.btlogar);

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

       Usuario u = bDcore.getUsuario();


    /*  if(bDcore.getidbd().toString()!=""){
            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
        }*/

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
        } else {
            // No user is signed in
        }

        btnovaconta= (Button) findViewById(R.id.btnovaconta);
        btnovaconta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,CriarUsuarioActivity.class);
                startActivity(intent);
            }
        });

        btlogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(emailET.getText().toString() == "" && senhaET.getText().toString() == "" ){
                    Toast.makeText(LoginActivity.this,"Preencha os campos",Toast.LENGTH_LONG).show();}
                else{Logar();}
            }
        });
    }

    public void Logar(){

        firebaseAutha = FirebaseConfig.getAuth();
        firebaseAutha.signInWithEmailAndPassword(emailET.getText().toString(),senhaET.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    Log.i("usuario firebase ","task.getResult().getUser().getUid() "+task.getResult().getUser().getUid());
                    getUsuarioFirebase(task.getResult().getUser().getUid());

                    bDcore.inseririd(task.getResult().getUser().getUid());

                    Toast.makeText(LoginActivity.this,"Bem vindo",Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{ Toast.makeText(LoginActivity.this,"Não foi possível fazer login",Toast.LENGTH_LONG).show();}
            }
        });

    }

    private void getUsuarioFirebase(String id){

        Log.i("usuario firebase ","ta em get ");

        reference = FirebaseConfig.getFirebase().child("Usuarios").child(id);


        Log.i("usuario firebase ","ta em get2 ");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Usuario u = dataSnapshot.getValue(Usuario.class);
                Log.i("usuario firebase ","ta cok");

                Log.i("usuario firebase ","dataSnapshot.getValue() "+dataSnapshot.getValue());
                bDcore.inserirUsuario(u);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("usuario firebase ","ta cancelar ");
            }
        });

    }

}
