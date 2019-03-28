package com.container.listaamiga.activitys;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.container.listaamiga.Classes.Usuario;
import com.container.listaamiga.R;
import com.container.listaamiga.config.ConfiguracaoFirebase;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.internal.api.FirebaseNoSignedInUserException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import static com.google.android.gms.auth.api.credentials.CredentialPickerConfig.Prompt.SIGN_IN;

public class Login extends AppCompatActivity implements View.OnClickListener{

    private EditText edtLogin;
    private EditText edtSenha;

    private GoogleSignInClient loginGoogle;

    private GoogleApiClient mGoogleApiClient;

    private GoogleSignInAccount contaGoogle;

//    private LoginButton btnLoginFacebook;
    private CallbackManager  callbackManager;

    private FirebaseAuth firebaseAuth;

    private DatabaseReference referenciaFirebase;

    private ValueEventListener valueEventListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        /** VERIFICA SE HÁ USUÁRIO LOGADO **/
        verificaUsuarioLogado();

        /** INICIALIZA O SERVIÇO DE CONTA DO GOOGLE **/
        servicosGoogle();

        /** INICIALIZA O SERVICO DE CONTA DO FACEBOOK **/
        servicosFacebook();

        /** OBTENDO A INSTANCIA DO FIREBASE **/
        firebaseAuth = FirebaseAuth.getInstance();

        /** EDIT TEXT DE LOGIN E SENHA **/
        edtLogin = findViewById(R.id.edt_login_tela);

        edtSenha = findViewById(R.id.edt_senha_tela);


        /**
         * ATIVA CLICK DOS BUTTONS E TEXTS
         * **/
        //BOTAO PARA LOGAR NO APP POR EMAIL
        findViewById(R.id.btn_logar_tela).setOnClickListener(this);

        //BOTAO DE LOGIN DO GOOGLE
        findViewById(R.id.id_login_google).setOnClickListener(this);

        //BOTAO DE LOGIN PELO FACEBOOK
        findViewById(R.id.btn_login_facebook).setOnClickListener(this);

        //BOTAO PARA PULAR O LOGIN (ANONIMO)
        findViewById(R.id.btn_pular_login).setOnClickListener(this);

        //LINK PARA ABRIR TELA DE CADASTRO
        findViewById(R.id.txt_cadastro_tela).setOnClickListener(this);

        //LINK PARA REDEFINIÇÃO DE SENHA
        findViewById(R.id.txt_redef_senha).setOnClickListener(this);


    }

    /** OPÇÕES DE CLICKS DA TELA **/
    @Override
    public void onClick (View view){

        switch (view.getId()){
            //LOGA POR E-MAIL
            case R.id.btn_logar_tela:

                logarContaEmail();

                break;

            //LOGA PELO GOOGLE
            case R.id.id_login_google:

                logarContaGoogle();

                break;
            //LOGA PELO FACEBOOK
            case R.id.btn_login_facebook:

                logarContaFacebook();

                break;

            //ABRE A "CadastroUsuario.java"
            case R.id.txt_cadastro_tela:

                startActivity( new Intent(getBaseContext(), CadastroUsuario.class) );

                break;

            //ABRE A "RedefinirSenha.java"
            case R.id.txt_redef_senha:

                startActivity(new Intent(getBaseContext(), RedefinirSenha.class));

                break;

            //PULA O CADASTRO E VAI para "MainActivity.java"
            case R.id.btn_pular_login:

                loginAnonimo();

                break;

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        //METODO DE CALLBACK DO FACEBOOK
        callbackManager.onActivityResult(requestCode, resultCode, data);

        super.onActivityResult(requestCode, resultCode, data);
        Log.i("loginGoogle", "Request: " + resultCode);
        //METODO DE CALLBACK DO GOOGLE
        if (requestCode == SIGN_IN){

            GoogleSignInResult resultadoAutenticacao = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            try{

                if ( resultadoAutenticacao.isSuccess() ){
                Log.i("loginGoogle", "Google OK");
                    GoogleSignInAccount contaGoogle = resultadoAutenticacao.getSignInAccount();
                    autenticarFirebaseComGoogle( contaGoogle );

                }

            } catch (Exception e){

                Toast.makeText(this, "Erro no sign: " + e.getMessage(), Toast.LENGTH_LONG).show();
                Log.i("loginGoogle", "Erro: " + e.getMessage());

            }

        } else {
            Toast.makeText(this, "Erro no login do Google", Toast.LENGTH_LONG).show();
            Log.i("loginGoogle", "Request: " + resultCode);
        }

    }

    /** METODO PARA INICIALIZAR OS SERVICOS PELO FACEBOOK **/
    private void servicosFacebook(){

        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                //CASO HAJA SUCESSO NO LOGIN, ADICIONA OS DADOS DO FACEBOOK AO FIREBASE
                adicionarContaFacebookFirebase( loginResult.getAccessToken() );

            }

            @Override
            public void onCancel() {

                Toast.makeText(Login.this, "Login pela conta do Facebook cancelado.", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onError(FacebookException error) {

                Toast.makeText(Login.this, "Erro com o login do facebook: " + error.getMessage(), Toast.LENGTH_LONG).show();

            }
        });

    }

    /**---------------- METODO PARA INICIALIZAR OS SERVICOS PELO GOOGLE -------------------------**/
    private void servicosGoogle(){

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, null)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        loginGoogle = GoogleSignIn.getClient(this, gso);

    }


    /**----------------------------------- METODOS DE LOGIN -------------------------------------**/
    /** LOGAR CONTA EMAIL **/
    private void logarContaEmail(){

        Usuario usuarioEmail = new Usuario();

        try {
                    usuarioEmail.setEmail( edtLogin.getText().toString() );
                    usuarioEmail.setSenha( edtSenha.getText().toString() );
                    usuarioEmail.setTipoPerfil("email");

                    loginUsuario( usuarioEmail );

                } catch (Exception e){
                    Log.i("testeLogin", "Erro no login: " + e.getMessage() );
                }

    }

    //AUTENTICACAO DO USUARIO AO FIREBASE
    private void loginUsuario(final Usuario usuarioRecebidoEmail){

        String loginInterno = usuarioRecebidoEmail.getEmail();
        String senhaInterna = usuarioRecebidoEmail.getSenha();

        Log.i("testeLogin", "Usuario: " + loginInterno + " - Senha: " + senhaInterna);
//        String loginInterno = "teste@teste.com";
//        String senhaInterna = "123456";

        firebaseAuth.signInWithEmailAndPassword(loginInterno, senhaInterna).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if ( !task.isSuccessful() ){

                    try {

                        throw task.getException();

                    } catch(FirebaseAuthWeakPasswordException e) {
                        Toast.makeText(Login.this, "A senha inserida é muito curta. O mínimo são 6 caracteres.", Toast.LENGTH_LONG).show();
                        edtSenha.requestFocus();
                    } catch(FirebaseAuthInvalidCredentialsException e) {
                        Toast.makeText(Login.this, "O e-mail digitado é inválido.", Toast.LENGTH_LONG).show();
                        edtLogin.requestFocus();
                    } catch(FirebaseAuthUserCollisionException e) {
                        Toast.makeText(Login.this, "O e-mail inserido já está cadastrado.", Toast.LENGTH_LONG).show();
                        edtLogin.requestFocus();
                    }catch (FirebaseNoSignedInUserException e){
                        Toast.makeText(Login.this, "O e-mail inserido não pode ser vazio", Toast.LENGTH_LONG).show();
                        edtLogin.requestFocus();
                    }catch(Exception e) {
                        Log.i("testeLogin", "Erro TRY:" + e.getMessage());
                    }

                } else {

                    Toast.makeText(Login.this, "Usuário logado com sucesso.", Toast.LENGTH_LONG).show();
                    FirebaseUser usuarioFirebaseEmail = task.getResult().getUser();


                    usuarioRecebidoEmail.setIdUsuario( usuarioFirebaseEmail.getUid() );

                    /** SALVA OS DADOS DO USUÁRIO E ABRE A MAIN ACTIVITY **/
                    usuarioRecebidoEmail.salvarUsuario();
                    abrirMainActivity( );

                }

            }
        });

    }

    /** LOGAR CONTA GOOGLE **/
    private void logarContaGoogle(){

        contaGoogle = GoogleSignIn.getLastSignedInAccount(this);

        if ( contaGoogle == null ){

            Intent intentGoogle = loginGoogle.getSignInIntent();
            startActivityForResult(intentGoogle, SIGN_IN);
            Log.i("loginGoogle", "Request: " + SIGN_IN);


        } else {

            //CASO EXISTA OUTRA CONTA DO GOOGLE LOGADA
            Toast.makeText(this, "Já há um usuário logado.", Toast.LENGTH_LONG).show();

        }

    }

    /** ADICIONANDO CONTAS EXTERNAS AO FIREBASE VINCULANDO O USUÁRIO **/
    private void autenticarFirebaseComGoogle(final GoogleSignInAccount contaGoogleExterna){
        Log.i( "IDFirebase","firebaseAuthWithGoogle:" + contaGoogleExterna.getId());

        AuthCredential credenciais = GoogleAuthProvider.getCredential(contaGoogleExterna.getIdToken(), null);
        firebaseAuth.signInWithCredential(credenciais)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()){

                            FirebaseUser usuarioFirebaseGoogle = task.getResult().getUser();
                            Usuario usuarioGoogle = new Usuario();

                            usuarioGoogle.setIdUsuario( usuarioFirebaseGoogle.getUid() );
                            Log.i("testeDadosGoogle", "ID SALVO: "+ usuarioGoogle.getIdUsuario() );
//                            usuarioGoogle.salvarUsuario();

                            obterDadosContaGoogle( contaGoogleExterna, usuarioGoogle );

                        } else {
                            Toast.makeText(Login.this, "Erro ao logar o usuário pelo Google", Toast.LENGTH_LONG).show();
                        }

                    }
                });
    }

    private void obterDadosContaGoogle(GoogleSignInAccount contaGoogle, Usuario usuarioGoogleExterno){

        usuarioGoogleExterno.setNome( contaGoogle.getDisplayName() );
        usuarioGoogleExterno.setEmail( contaGoogle.getEmail() );
        usuarioGoogleExterno.setFotoURL( contaGoogle.getPhotoUrl().toString() );
        usuarioGoogleExterno.setTipoPerfil( "google" );

        usuarioGoogleExterno.salvarUsuario();
        abrirMainActivity(  );

    }


    /** LOGAR PELA CONTA DO FACEBOOK **/
    private void logarContaFacebook(){

        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));

    }


    private void adicionarContaFacebookFirebase(final AccessToken token) {

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser usuarioFirebaseFace = task.getResult().getUser();
                            Usuario usuarioFacebook = new Usuario();

                            usuarioFacebook.setIdUsuario( usuarioFirebaseFace.getUid() );
//                            usuarioFacebook.salvarUsuario();

                            obterDadosContaFacebook( token, usuarioFacebook);
                            Toast.makeText(Login.this, "Logado com sucesso pelo facebook.", Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(Login.this, "Erro no login do Facebook: " + task.getException(), Toast.LENGTH_LONG).show();
                            Log.i("testeFacebook", "Erro: " + task.getException().toString() );
                        }

                    }
                });
    }

    private void obterDadosContaFacebook(AccessToken accessToken, Usuario usuarioFaceExterno){

        final Usuario usuarioFacebook = usuarioFaceExterno;

        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {

                try{

                    String idPerfilFacebook = object.getString("id");
                    String primeiroNome = object.getString("first_name");
                    String ultimoNome = object.getString("last_name");
                    String email = object.getString("email");
                    String fotoURL = "https://graph.facebook.com/" + idPerfilFacebook + "/picture?type=normal";

//                    usuarioFacebook.setIdUsuario( id );
                    usuarioFacebook.setNome( primeiroNome + " "+ ultimoNome );
                    usuarioFacebook.setEmail( email );
                    usuarioFacebook.setFotoURL( fotoURL );
                    usuarioFacebook.setTipoPerfil("facebook");

                    usuarioFacebook.salvarUsuario();
                    abrirMainActivity( );

                } catch (JSONException e){

                    Toast.makeText(Login.this, "Erro ao carregar o perfil: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();

                }


            }
        });


        Bundle parametros = new Bundle();
        parametros.putString("fields", "first_name, last_name, email, id ");
        request.setParameters( parametros );
        request.executeAsync();

    }

    /** LOGIN ANONIMO **/
    private void loginAnonimo(){

        final Usuario usuarioAnonimo = new Usuario();

        firebaseAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {


                            FirebaseUser usuarioFirebaseFace = task.getResult().getUser();

                            usuarioAnonimo.setIdUsuario( usuarioFirebaseFace.getUid() );
//                            usuarioAnonimo.salvarUsuario();

                            usuarioAnonimo.setNome("");
                            usuarioAnonimo.setEmail("Usuário sem e-mail");
                            usuarioAnonimo.setTipoPerfil("Anônimo");

                            usuarioAnonimo.salvarUsuario();
                            abrirMainActivity( );

                        } else {

                            Toast.makeText(Login.this, "Falha ao entrar como anônimo.", Toast.LENGTH_SHORT).show();

                        }

                    }
                });


    }

    /** MÉTODO QUE ABRE A TELA PRINCIPAL **/
    private void abrirMainActivity(){

        startActivity( new Intent(getBaseContext(), MainActivity.class) );

        finish();
    }

    /** MÉTODO QUE VERIFICA SE HÁ USUÁRIOS LOGADOS **/
    private void verificaUsuarioLogado(){

        //OBTEM A COMUNICACAO DO APP COM O BD FIREBASE
        referenciaFirebase = ConfiguracaoFirebase.obterFirebase();

        firebaseAuth = FirebaseAuth.getInstance();

        if (( firebaseAuth.getCurrentUser() != null )){

            abrirMainActivity( );
        }
    }

    /** CONSTRUTOR **/
    public Login() {

    }

}