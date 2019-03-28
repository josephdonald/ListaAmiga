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

    private Context context;
    private List<ListasCompras> listasCompras;


    public ListasComprasPrincipalAdapter( Context contextExterno, List<ListasCompras> listasComprasExterna) {
//        super(contextExterno, listasComprasExterna);

        this.listasCompras = listasComprasExterna;
        this.context = contextExterno;
    }

    @Override
    public int getCount() {
        return listasCompras.size();
    }

    @Override
    public ListasCompras getItem(int position) {
        return listasCompras.get( position );
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    /** CRIA UMA VIEW CASO ELA SEJA NULA E ASSOCIA O NOME E RFID À LISTA DE "LISTAS" DO ADAPTADOR **/
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = null;

        //Verifica se a lista está vazia
        if (convertView == null){

            // inicializar objeto para montagem da view
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();

            // Monta view a partir do xml
            view = inflater.inflate(R.layout.item_listas_compras, null);
        } else {
            view = convertView;
        }

        ListasCompras listasCompra= getItem( position );

        TextView itemId = view.findViewById(R.id.item_id_lista);
        TextView itemNomeLista = view.findViewById(R.id.item_nome_lista);
        TextView itemQuantItensLista = view.findViewById(R.id.item_quant_itens_lista);

        itemId.setText( "ID LISTA ");
        itemNomeLista.setText("NOME LISTA");
        itemQuantItensLista.setText("QUANTIDADE DE ITENS");


        return view;
    }
}
