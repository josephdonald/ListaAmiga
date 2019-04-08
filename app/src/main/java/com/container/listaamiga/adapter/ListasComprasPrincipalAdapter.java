package com.container.listaamiga.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.container.listaamiga.Classes.ListasCompras;
import com.container.listaamiga.R;

import java.util.ArrayList;
import java.util.List;

public class ListasComprasPrincipalAdapter extends BaseAdapter {

    private Context contextInterno;
    private List<ListasCompras> listasComprasInterno;


    public ListasComprasPrincipalAdapter( Context contextExterno, List<ListasCompras> listasComprasExterna) {
//        super(contextExterno, listasComprasExterna);

        this.contextInterno = contextExterno;
        this.listasComprasInterno = listasComprasExterna;
    }

    @Override
    public int getCount() {
        return listasComprasInterno.size();
    }

    @Override
    public ListasCompras getItem(int position) {
        return listasComprasInterno.get( position );
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    /** CRIA UMA VIEW CASO ELA SEJA NULA E ASSOCIA O NOME E RFID À LISTA DE "LISTAS" DO ADAPTADOR **/
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = null;

        if (convertView == null){

                    if (listasComprasInterno != null){

                        // inicializar objeto para montagem da view
                        LayoutInflater inflater = (LayoutInflater) contextInterno.getSystemService(contextInterno.LAYOUT_INFLATER_SERVICE);

                        // Monta view a partir do xml
                        view = inflater.inflate(R.layout.item_listas_compras,parent, false);

                        TextView itemId = view.findViewById(R.id.item_id_lista);
                        TextView itemNomeLista = view.findViewById(R.id.item_nome_lista);
                        TextView itemQuantItensTotalLista = view.findViewById(R.id.item_quant_itens_lista);

                        ListasCompras listasCompras = listasComprasInterno.get( position );
                        itemId.setText( listasCompras.getIdLista() );
                        itemNomeLista.setText( listasCompras.getNomeLista() );
                        itemQuantItensTotalLista.setText( String.valueOf(listasCompras.getQuantItensTotal() ));

                    }

                } else {
                view = convertView;
                }


        return view;
    }
}


////Verifica se a lista está vazia
//        if (convertView == null){
//
//                // inicializar objeto para montagem da view
//                LayoutInflater inflater = ((Activity) contextInterno).getLayoutInflater();
//
//                // Monta view a partir do xml
//                view = inflater.inflate(R.layout.item_listas_compras, null);
//                } else {
//                view = convertView;
//                }
//
//final ListasCompras listasCompra= getItem( position );
//
//        TextView itemId = view.findViewById(R.id.item_id_lista);
//        TextView itemNomeLista = view.findViewById(R.id.item_nome_lista);
//        TextView itemQuantItensLista = view.findViewById(R.id.item_quant_itens_lista);
//
//        itemId.setText( "ID LISTA ");
//        itemNomeLista.setText("NOME LISTA");
//        itemQuantItensLista.setText("QUANTIDADE DE ITENS");
