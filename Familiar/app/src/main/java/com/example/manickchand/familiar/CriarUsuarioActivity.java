package com.example.manickchand.familiar;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.manickchand.familiar.model.Base64Custom;
import com.example.manickchand.familiar.model.FirebaseConfig;
import com.example.manickchand.familiar.model.Preferencias;
import com.example.manickchand.familiar.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;

public class CriarUsuarioActivity extends AppCompatActivity {


    private EditText nome,email,senha;
    private Button btnovo;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    Usuario u =new Usuario();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_usuario);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Novo Usuário");

        nome = (EditText)findViewById(R.id.etnovonome);
        email = (EditText)findViewById(R.id.etnovoemail);
        senha = (EditText)findViewById(R.id.etnovasenha);
        progressBar= (ProgressBar) findViewById(R.id.progressBar2);

        btnovo = (Button) findViewById(R.id.button2);
        btnovo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressBar.setVisibility(View.VISIBLE);
                btnovo.setVisibility(View.GONE);

                criaUsuario();
            }
        });
    }



    public void criaUsuario(){

        u.setEmail(email.getText().toString());
        u.setNome(nome.getText().toString());
        u.setSenha(senha.getText().toString());


        auth = FirebaseConfig.getAuth();
        auth.createUserWithEmailAndPassword(u.getEmail(),u.getSenha()).addOnCompleteListener(CriarUsuarioActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){


                    String identificador = Base64Custom.codificarBase65(u.getEmail());
                    FirebaseUser user = task.getResult().getUser();
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(u.getNome()).build();
                    user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });

                    u.setId(task.getResult().getUser().getUid());
                    u.salvar();

                    Preferencias preferencias = new Preferencias(CriarUsuarioActivity.this);
                    preferencias.salvarPreferencias(identificador,u.getNome());

                    progressBar.setVisibility(View.GONE);
                    btnovo.setVisibility(View.VISIBLE);
                    Toast.makeText(CriarUsuarioActivity.this,"Usuráio criado com sucesso",Toast.LENGTH_LONG).show();

                    finish();
                }else{

                    progressBar.setVisibility(View.GONE);
                    btnovo.setVisibility(View.VISIBLE);

                    try{
                        throw task.getException();
                    }
                    catch (FirebaseAuthUserCollisionException e){
                        Toast.makeText(CriarUsuarioActivity.this,"Email ja cadastrato",Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(CriarUsuarioActivity.this,"Erro ao criar usuário",Toast.LENGTH_LONG).show();
                    }


                }
            }

        });


    }



}
