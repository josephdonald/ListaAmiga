package com.container.listaamiga;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.container.listaamiga.Classes.Usuario;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.internal.CallbackManagerImpl;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.google.android.gms.auth.api.credentials.CredentialPickerConfig.Prompt.SIGN_IN;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth firebaseAuth;
    private GoogleSignInClient loginGoogle;

    private CircleImageView fotoPerfilActivity;
    private TextView nomePerfilActivity, emailPerfilActivity, tipoContaActivity;

    private CallbackManager callbackManager;
    private Usuario usuario = new Usuario();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //OBTEM A INTANCIA DO USUARIO AUTENTICADO
        firebaseAuth = FirebaseAuth.getInstance();

        //callBack do Facebook
        callbackManager = CallbackManager.Factory.create();

        /**------------------------ CARREGA FLOAT, TOOLBAR, DRAWER E NAVIGATION ----------------------------------**/
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        /**---------------------------------------------------------------------------------------------------------**/

        /**-------- INSTANCIA A VIEW DO HEADER DO NAVIGATION E CARREGA OS XMLs ----------**/
        View headerView = navigationView.getHeaderView(0);

        fotoPerfilActivity = (CircleImageView) headerView.findViewById(R.id.foto_perfil);
        nomePerfilActivity = headerView.findViewById(R.id.nome_perfil);
        emailPerfilActivity = headerView.findViewById(R.id.email_perfil);
        tipoContaActivity = headerView.findViewById(R.id.tipo_conta);
        /**------------------------------------------------------------------------------**/

        /**----------- RECEBENDO OS DADOS DO USUARIO QUE LOGOU NO APP ----------**/
        Intent intentLogin = getIntent();

        usuario.setId( intentLogin.getStringExtra("id") );
        usuario.setNome( intentLogin.getStringExtra("nome") );
        usuario.setEmail( intentLogin.getStringExtra("email") );
        usuario.setFotoURL( intentLogin.getStringExtra("fotoURL") );
        usuario.setTipoPerfil( intentLogin.getStringExtra("tipoPerfil") );

        if (usuario.getTipoPerfil().contentEquals("google") || usuario.getTipoPerfil().contentEquals("facebook") || usuario.getTipoPerfil().contentEquals("email")){

            //ATRIBUINDO OS DADOS AOS TEXT VIEWs
            nomePerfilActivity.setText( usuario.getNome() );
            emailPerfilActivity.setText( usuario.getEmail() );
            tipoContaActivity.setText( usuario.getTipoPerfil() );

            Log.i("testeDadosGoogle", "ID Google"+ usuario.getId() );

            //TRABALHA A IMAGEM PARA EXIBIR A FOTO DO PERFIL
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.dontAnimate();

            Glide.with( getBaseContext() ).load(usuario.getFotoURL()).into( fotoPerfilActivity );
        }

        /** LOG PARA CHECAGEM DE DADOS **/
        Log.i("dadosObtidos",
                "ID: " + usuario.getId() +
                        " Nome: " + usuario.getNome() +
                        " E-mail: " + usuario.getEmail() +
                        " Foto URL: " + usuario.getFotoURL() +
                        " Tipo de Perfil: " + usuario.getTipoPerfil()
        );



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()){

            case R.id.action_logout:
                //DESLOGAR USUARIO
                deslogarUsuario();
                return true;

            case R.id.action_configuracoes:
                //CADASTRAR FUNCIONARIO
//                cadastroFuncionario();
                return true;

        }

        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**----------------------------------- LOGOUT DO USUÁRIO -----------------------------------**/
    private void deslogarUsuario( ){

        //DESLOGA O USUARIO DO E-MAIL
        firebaseAuth.getInstance().signOut();

        //DESLOGAR O USUARIO DO FACEBOOK
        LoginManager.getInstance().logOut();


        //DESLOGA DA CONTA DO GOOGLE
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        loginGoogle = GoogleSignIn.getClient(this, gso);

        loginGoogle.signOut();

        Toast.makeText(this, "Logout feito com sucesso.", Toast.LENGTH_LONG).show();

        Intent intentLogout = new Intent(MainActivity.this, Login.class);
        startActivity( intentLogout );
        finish();

    }
    /**-----------------------------------------------------------------------------------------**/

}