package com.container.listaamiga.activitys;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.container.listaamiga.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class RedefinirSenha extends AppCompatActivity {

    private String email;
    private EditText txtEmail;
    private Button btnSolicitar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redefinir_senha);

        txtEmail = findViewById(R.id.edt_email_redef_senha);
        btnSolicitar = findViewById(R.id.btn_redef_senha);

        btnSolicitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = txtEmail.getText().toString();

                enviarEmailReset( email );

            }
        });

    }

    private void enviarEmailReset(String emailExterno){

        FirebaseAuth auth = FirebaseAuth.getInstance();

        String emailInterno = emailExterno;

        auth.sendPasswordResetEmail( emailInterno ).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if ( task.isSuccessful() ){

                    Toast.makeText(RedefinirSenha.this, "Foi enviado um e-mail para a redefinição da senha.", Toast.LENGTH_LONG).show();
                    Log.i("redefineSenha", "Email enviado");

                    Intent intentLogin = new Intent(RedefinirSenha.this, Login.class);
                    startActivity( intentLogin );

                } else {

                    Log.i("redefineSenha", "Erro: " + task.getException() );

                }

            }
        });

    }

}
