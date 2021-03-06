package com.container.listaamiga.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.container.listaamiga.Classes.Usuario;
import com.container.listaamiga.R;
import com.container.listaamiga.config.ConfiguracaoFirebase;
import com.container.listaamiga.fragments.ListasComprasFragment;
import com.facebook.CallbackManager;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private FirebaseAuth firebaseAuth;
    private GoogleSignInClient loginGoogle;

    private CircleImageView fotoPerfilActivity;
    private TextView nomePerfilActivity, emailPerfilActivity, tipoContaActivity;

    private CallbackManager callbackManager;
    private Usuario usuario = new Usuario();

    private DatabaseReference dadosFirebase;

    private FirebaseUser userFirebase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //OBTEM A INTANCIA DO USUARIO AUTENTICADO
        firebaseAuth = FirebaseAuth.getInstance();

        dadosFirebase = ConfiguracaoFirebase.obterFirebase().child("usuarios").child(firebaseAuth.getUid());

        //CARREGA PERFIL DO USUARIO
        carregarPerfil( dadosFirebase );

        //callBack do Facebook
        callbackManager = CallbackManager.Factory.create();

        /**------------------------ CARREGA FLOAT, TOOLBAR, DRAWER E NAVIGATION ----------------------------------**/
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

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
//        receberDadosDoUsuario( intentLogin );

        /**## OBTEM IMAGENS E ATIVA O CLICK ##**/
        findViewById(R.id.id_cad_listas).setOnClickListener(this);
        findViewById(R.id.id_cad_categ_prod).setOnClickListener(this);
        findViewById(R.id.id_estatisticas).setOnClickListener(this);
        findViewById(R.id.id_contatos).setOnClickListener(this);


    }

    /** METODOS DE CLICKS **/
    @Override
    public void onClick(View view) {

        switch (view.getId()){

            //ABRIR LISTAS CADASTRADAS
            case R.id.id_cad_listas:
                abrirCadastroLista();

                break;

            case R.id.id_cad_categ_prod:

                break;

            case R.id.id_estatisticas:

                break;

            case R.id.id_contatos:

                break;


        }



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

    /** RECEBE OS DADOS DO USUARIO E CONFIGURA O PERFIL **/

    private void carregarPerfil(DatabaseReference databaseReference){

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Usuario usuario = new Usuario();

                String id = (String) dataSnapshot.child("idUsuario").getValue();
                String nome = (String) dataSnapshot.child("nome").getValue();
                String email = (String) dataSnapshot.child("email").getValue();
                String foto = (String) dataSnapshot.child("fotoURL").getValue();
                String tipoPerfil = (String) dataSnapshot.child("tipoPerfil").getValue();


                Toast.makeText(getBaseContext(), "Nome: " + nome, Toast.LENGTH_SHORT).show();
                Log.i("testeFirebase","ID: " + id );
                Log.i("testeFirebase","Nome: " + nome );
                Log.i("testeFirebase","Email: " + email );
                Log.i("testeFirebase","Foto: " + foto );
                Log.i("testeFirebase","Perfil: " + tipoPerfil );


                //ATRIBUINDO OS DADOS AOS TEXT VIEWs
                nomePerfilActivity.setText( nome );
                emailPerfilActivity.setText( email );
                tipoContaActivity.setText( tipoPerfil );

                //TRABALHA A IMAGEM PARA EXIBIR A FOTO DO PERFIL
                RequestOptions requestOptions = new RequestOptions();
                requestOptions.dontAnimate();

                Glide.with( getBaseContext() ).load(foto).into( fotoPerfilActivity );


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onStart() {

        super.onStart();
    }

    /**####### MÉTODOS DE CLICK PARA ABRIR AS TELAS #######**/

    private void abrirCadastroLista(){

        ListasComprasFragment listasComprasFragment = new ListasComprasFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.conteudo_fragment, listasComprasFragment).addToBackStack(null).commit();
    }

}