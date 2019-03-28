package com.container.listaamiga.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.container.listaamiga.Classes.ListasCompras;
import com.container.listaamiga.R;
import com.container.listaamiga.adapter.ListasComprasPrincipalAdapter;
import com.container.listaamiga.config.ConfiguracaoFirebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListasComprasFragment extends Fragment {

    private ListView viewListCompras;
    private ArrayList<ListasCompras> listasComprasMain;
    private ListasComprasPrincipalAdapter listasComprasAdapter;
    private DatabaseReference dadosListasComprasFirebase;
    private ValueEventListener valueEventListenerCompras;

    /** BOTAO FLOAT **/
    private FloatingActionButton buttonFloatListas;

    public ListasComprasFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_listas_compras, container, false);


        /** ATIVA O BOTÃO FLOAT E REDIRECIONA PARA A LISTA DE CADASTRO DE LISTAS **/
        buttonFloatListas = (FloatingActionButton) view.findViewById(R.id.fab);

        buttonFloatListas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "CLICK ATIVADO!!", Toast.LENGTH_SHORT).show();
            }
        });


        /** ASSOCIANDO A LIST VIEW AO XML DO LAYOUT **/
        viewListCompras = view.findViewById(R.id.list_listas_compras);

        listasComprasMain = new ArrayList<>();

        /** CRIANDO A LISTA DE COMPRAS PRINCIPAL **/
//        listasComprasMain = new ArrayList<ListasCompras>();
        listasComprasAdapter = new ListasComprasPrincipalAdapter(getContext(), listasComprasMain);
        viewListCompras.setAdapter( listasComprasAdapter );

        dadosListasComprasFirebase = ConfiguracaoFirebase.obterFirebase()
                .child("idUsuario")
                .child("nome");

        valueEventListenerCompras = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ListasCompras listasCompras = new ListasCompras();

                listasComprasMain.clear();

                for (DataSnapshot dados: dataSnapshot.getChildren() ){
                    listasCompras = dados.getValue( ListasCompras.class );
                    listasComprasMain.add( listasCompras );

                }

                listasComprasAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        dadosListasComprasFirebase.addValueEventListener( valueEventListenerCompras );

        Toast.makeText(view.getContext(), "LISTA DE COMPRAS", Toast.LENGTH_SHORT).show();

        return view;

    }

    private void obterListasCadastradas(){


    }

}
