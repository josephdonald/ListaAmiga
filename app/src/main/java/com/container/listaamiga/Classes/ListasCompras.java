package com.container.listaamiga.Classes;

public class ListasCompras {


    private String idLista;
    private String nomeLista;
    private int quantItensSelecionado;
    private float quantItensTotal;
    private float precoTotalLista;
    private String dataCompra;
    private boolean finalizada;
    private float limiteValorTotal;


    public ListasCompras() {

    }

    public String getIdLista() {
        return idLista;
    }

    public void setIdLista(String idLista) {
        this.idLista = idLista;
    }

    public String getNomeLista() {
        return nomeLista;
    }

    public void setNomeLista(String nomeLista) {
        this.nomeLista = nomeLista;
    }

    public int getQuantItensSelecionado() {
        return quantItensSelecionado;
    }

    public void setQuantItensSelecionado(int quantItensSelecionado) {
        this.quantItensSelecionado = quantItensSelecionado;
    }

    public float getQuantItensTotal() {
        return quantItensTotal;
    }

    public void setQuantItensTotal(float quantItensTotal) {
        this.quantItensTotal = quantItensTotal;
    }

    public float getPrecoTotalLista() {
        return precoTotalLista;
    }

    public void setPrecoTotalLista(float precoTotalLista) {
        this.precoTotalLista = precoTotalLista;
    }

    public String getDataCompra() {
        return dataCompra;
    }

    public void setDataCompra(String dataCompra) {
        this.dataCompra = dataCompra;
    }

    public boolean isFinalizada() {
        return finalizada;
    }

    public void setFinalizada(boolean finalizada) {
        this.finalizada = finalizada;
    }

    public float getLimiteValorTotal() {
        return limiteValorTotal;
    }

    public void setLimiteValorTotal(float limiteValorTotal) {
        this.limiteValorTotal = limiteValorTotal;
    }
}
