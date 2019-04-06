package com.container.listaamiga.Classes;

import java.util.Date;

public class ListasCompras {


    private int idLista;
    private String nomeLista;
    private int quantItensLista;
    private float totalItensLista;
    private float precoTotalLista;
    private Date dataCompra;
    private boolean finalizada;
    private float limiteValorTotal;


    public ListasCompras() {

    }

    public int getIdLista() {
        return idLista;
    }

    public void setIdLista(int idLista) {
        this.idLista = idLista;
    }

    public String getNomeLista() {
        return nomeLista;
    }

    public void setNomeLista(String nomeLista) {
        this.nomeLista = nomeLista;
    }

    public int getQuantItensLista() {
        return quantItensLista;
    }

    public void setQuantItensLista(int quantItensLista) {
        this.quantItensLista = quantItensLista;
    }

    public float getTotalItensLista() {
        return totalItensLista;
    }

    public void setTotalItensLista(float totalItensLista) {
        this.totalItensLista = totalItensLista;
    }

    public float getPrecoTotalLista() {
        return precoTotalLista;
    }

    public void setPrecoTotalLista(float precoTotalLista) {
        this.precoTotalLista = precoTotalLista;
    }

    public Date getDataCompra() {
        return dataCompra;
    }

    public void setDataCompra(Date dataCompra) {
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
