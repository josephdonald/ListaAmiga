package com.container.listaamiga;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Login extends AppCompatActivity {

    private TextView textCadastro;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        textCadastro = findViewById(R.id.txt_cadastro);

        textCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentCadastro = new Intent(Login.this, CadastroUsuario.class);

                startActivity( intentCadastro );

            }
        });

    }
}
