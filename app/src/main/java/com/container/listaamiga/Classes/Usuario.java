package com.container.listaamiga.Classes;

import com.container.listaamiga.config.ConfiguracaoFirebase;
import com.google.firebase.database.DatabaseReference;

public class Usuario {

   private String idUsuario, nome, email, senha, fotoURL, tipoPerfil;


    public Usuario() {

    }

    /** METODO PARA ASSOCIAR O USUARIO NO FIREBASE**/
    public void salvarUsuario(){

        DatabaseReference referenciaFirebase = ConfiguracaoFirebase.obterFirebase();

        referenciaFirebase.child( "usuarios" ).child( getIdUsuario() ).setValue( this );

    }


    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getFotoURL() {
        return fotoURL;
    }

    public void setFotoURL(String fotoURL) {
        this.fotoURL = fotoURL;
    }

    public String getTipoPerfil() {
        return tipoPerfil;
    }

    public void setTipoPerfil(String tipoPerfil) {
        this.tipoPerfil = tipoPerfil;
    }


}