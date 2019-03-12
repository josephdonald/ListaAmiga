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
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.internal.CallbackManagerImpl;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth firebaseAuth;
    private TextView mensagemInicial;
    private GoogleSignInClient loginGoogle;

    private CircleImageView fotoPerfilActivity;
    private TextView nomePerfilActivity, emailPerfilActivity;

    private CallbackManager callbackManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i("testeIniciar", "1");

        //OBTEM A INTANCIA DO USUARIO AUTENTICADO
        firebaseAuth = FirebaseAuth.getInstance();

        //callBack do Facebook
        callbackManager = CallbackManager.Factory.create();

        /**----- CARREGA FLOAT, TOOLBAR, DRAWER E NAVIGATION -----**/
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
        /**--------------------------------------------------------**/

        /**-------- INSTANCIA A VIEW DO HEADER DO NAVIGATION E CARREGA OS XMLs ----------**/
        View headerView = navigationView.getHeaderView(0);

        fotoPerfilActivity = (CircleImageView) headerView.findViewById(R.id.foto_perfil);
        nomePerfilActivity = headerView.findViewById(R.id.nome_perfil);
        emailPerfilActivity = headerView.findViewById(R.id.email_perfil);
        /**------------------------------------------------------------------------------**/

        Log.i("testeIniciar", "2");

        //METODO PARA PERSONALIZAR O APP DE ACORDO COM O USUARIO

        Intent intentLogin = getIntent();
        int perfilLogin = intentLogin.getIntExtra("perfil",0);
        Log.i("testeIniciar", String.valueOf(perfilLogin));
        personalizaPerfil( perfilLogin );

        verificaStatusLogin();



//        mensagemInicial = findViewById(R.id.txt_msg_inicial);



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

    /**
     * ############## MÉTODOS ###############
     * **/

    /** RECEBER DADOS DO USUÁRIO LOGADO **/
    private void personalizaPerfil(int perfilRecebido){

        switch ( perfilRecebido ){

            case 1:

            carregarPerfilFacebook();

                break;

            case 2:

                break;

            case 3:

                break;

            case 4:

                break;

        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("testeIniciar", "3");

        //CARREGA DADOS DO FACEBOOK
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    /**-------------- METODOS PARA CARREGAR INFORMACOES DO USUARIO DO FACEBOOK ----------------**/
    private void carregarPerfilFacebook(){

        AccessTokenTracker tokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {

                if (currentAccessToken == null){
                    nomePerfilActivity.setText("");
                    emailPerfilActivity.setText("");
                    fotoPerfilActivity.setImageResource(0);
                    Toast.makeText(MainActivity.this, "Perfil não foi carregado.", Toast.LENGTH_SHORT).show();
                } else {

                    obterDadosPerfilFacebook( currentAccessToken );

                }

            }
        };

    }

    private void obterDadosPerfilFacebook(AccessToken accessToken){

        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {

                try{

                    String primeiroNome = object.getString("first_name");
                    String ultimoNome = object.getString("last_name");
                    String email = object.getString("email");
                    String id = object.getString("id");

                    String imageURL = "https://graph.facebook.com/" + id + "/picture?type=normal";

                    emailPerfilActivity.setText(email);
                    nomePerfilActivity.setText(primeiroNome + " " + ultimoNome);

                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.dontAnimate();

                    Glide.with( getBaseContext() ).load(imageURL).into( fotoPerfilActivity );

                } catch (JSONException e){

                    Toast.makeText(MainActivity.this, "Erro ao carregar o perfil: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();

                }


            }
        });


        Bundle parametros = new Bundle();
        parametros.putString("fields", "first_name, last_name, email, id ");
        request.setParameters( parametros );
        request.executeAsync();
    }
    /**-------------------------------------------------------------------------------------------**/

    private void verificaStatusLogin(){

        if (AccessToken.getCurrentAccessToken() != null){

            obterDadosPerfilFacebook( AccessToken.getCurrentAccessToken() );

        }

    }

    /**---------------- LOGOUT DO USUÁRIO ----------------**/
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
    /**---------------------------------------------------**/


}
